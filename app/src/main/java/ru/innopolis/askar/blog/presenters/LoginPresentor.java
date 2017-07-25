package ru.innopolis.askar.blog.presenters;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.innopolis.askar.blog.BlogActivity;
import ru.innopolis.askar.blog.Manager.NetworkManager;
import ru.innopolis.askar.blog.models.Account;
import ru.innopolis.askar.blog.models.Article;
import ru.innopolis.askar.blog.models.User;
import ru.innopolis.askar.blog.view.IViewLoginFragment;

/**
 * Created by admin on 12.07.2017.
 */

public class LoginPresentor implements ILoginPresenter {
    IViewLoginFragment view;

    @Override
    public void onLogin(String login, String password) {
        if (login.length() == 0){
            view.showMessage("Поле \"Логин\" должно быть заполнено");
            return;
        }
        if (password.length() < 8 && !password.equals("admin")){
            view.showMessage("Поле \"Пароль\" должно содержать не менее 8-ми символов");
            return;
        }
        if (password.split("[a-zA-Z0-9_]").length > 0){
            view.showMessage("Поле \"Пароль\" должно содержать только буквы латинского алфавита и/или цифры");
            return;
        }
        authentication.execute(login, password);
    }

    AsyncTask<String, Void, Response> authentication = new AsyncTask<String, Void, Response>() {
        @Override
        protected Response doInBackground(String... strings) {
            Account account = new Account(strings[0], strings[1]);
            Gson gson = new Gson();
            String json = gson.toJson(account);
            Response response = NetworkManager.connection(json, "/login");
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response == null) {
                view.showMessage("Сервер по указанному адресу отсутствует.\nИзмените адрес запроса.");
            }else
            if (response.isSuccessful()) {
                User user = null;
                Gson gson = new Gson();
                try {
                    user = gson.fromJson(response.body().string(), User.class);
                } catch (IOException e) {
                    view.showMessage("Ошибка запроса к серверу");
                    e.printStackTrace();
                }
                if (user != null) {
                    if (user.getArticles() == null)
                        user.setArticles(new ArrayList<Article>());
                    view.onStartActivity("user", user, BlogActivity.class);
                }
            }else{
                int code = response.code();
                switch (code){
                    case 404: view.showMessage("Не удается получить доступ к серверу.");break;
                    default: view.showMessage("Проблемы на сервере.");break;
                }
            }
        }
    };

    @Override
    public void bindView(IViewLoginFragment view) {
        this.view = view;
    }
}
