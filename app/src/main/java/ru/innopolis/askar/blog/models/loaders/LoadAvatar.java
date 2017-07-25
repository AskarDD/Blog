package ru.innopolis.askar.blog.models.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;
import ru.innopolis.askar.blog.Manager.NetworkManager;
import ru.innopolis.askar.blog.view.IViewBlogFragment;

/**
 * Created by admin on 24.07.2017.
 */

public class LoadAvatar extends AsyncTaskLoader<byte[]> {
    IViewBlogFragment view;
    Context context;
    String login;

    public LoadAvatar(Context context, String login) {
        super(context);
        this.view = (IViewBlogFragment) context;
        this.login = login;
        this.context = context;
    }

    @Override
    public byte[] loadInBackground() {
        byte[] bytes = null;
        String uri = "/loadavatar?login=" + login;
        Response response = NetworkManager.connection(uri);
        if (response != null){
            if (response.isSuccessful()) {
                InputStream inputStream = response.body().byteStream();
                int length = 0;
                try {
                    length = (int) response.body().contentLength();
                    bytes = new byte[length];
                    inputStream.read(bytes);
                } catch (IOException e) {
                    Toast.makeText(context, "Картинка не загрузилась", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }else
                view.showMessage("response not Successful");
        }else{
            view.showMessage("Сервер по указанному адресу отсутствует.\nИзмените адрес запроса.");
        }
        return bytes;
    }
}
