package ru.innopolis.askar.blog.models.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.innopolis.askar.blog.Manager.NetworkManager;
import ru.innopolis.askar.blog.view.IViewBlogFragment;

public class UpLoadAvatars extends AsyncTaskLoader<byte[]> {
    IViewBlogFragment view;
    String path;
    String login;

    public UpLoadAvatars(Context context, String path, String login) {
        super(context);
        this.view = (IViewBlogFragment) context;
        this.path = path;
        this.login = login;
    }

    @Override
    public byte[] loadInBackground() {
        byte[] bytesRes = null;
        File file = new File(path);
        if (file.getName().endsWith(".jpeg") || file.getName().endsWith(".JPEG") || file.getName().endsWith(".jpg")
                || file.getName().endsWith(".png") || file.getName().endsWith(".gif") || file.getName().endsWith(".JPG")) {
            try {
                FileInputStream fin = new FileInputStream(file);
                int size = fin.available();
                if (size < 1024 * 1024) {
                    byte[] bytes = new byte[fin.available()];
                    fin.read(bytes);
                    MediaType type = MediaType.parse("multipart/form-data");
                    String uri = "/repo?login=" + login;
                    Request request = NetworkManager.connection(type, bytes, uri);

                    OkHttpClient httpClient = new OkHttpClient();
                    Response response = httpClient.newCall(request).execute();

                    InputStream inputStream = response.body().byteStream();
                    int length = 0;
                    length = (int) response.body().contentLength();
                    bytesRes = new byte[length];
                    inputStream.read(bytesRes);

                    fin.close();
                } else {
                    view.showMessage("Выберите файл с размером не более 1 Мега байт");
                }
            } catch (FileNotFoundException e) {
                view.showMessage("Файл не найден");
                e.printStackTrace();
            } catch (IOException e) {
                view.showMessage("Ошибка при чтении/записи файла");
                e.printStackTrace();
            }
        }else{
            view.showMessage("Выбранный файл не соответствует\n разрешению: jpeg, JPEG, jpg, JPG, gif, png");
        }
        return bytesRes;
    }
}
