package ru.innopolis.askar.blog.presenters;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Response;
import ru.innopolis.askar.blog.BlogActivity;
import ru.innopolis.askar.blog.Manager.NetworkManager;
import ru.innopolis.askar.blog.models.Message;
import ru.innopolis.askar.blog.models.User;
import ru.innopolis.askar.blog.models.UserDB;
import ru.innopolis.askar.blog.view.IViewRegFragment;

/**
 * Created by admin on 10.07.2017.
 */

public class RegistratPresenter implements IRegPresenter {
    IViewRegFragment view;
    Message messageObj;

    @Override
    public void bindView(IViewRegFragment view){
        this.view = view;
    }

    @Override
    public void onReg(String name, String lastname, String login, String password, String confirm) {
        if (name.length() == 0) {
            view.showMessage("Поле \"Имя\" должно быть заполнено");
            return;
        }
        if (lastname.length() == 0){
            view.showMessage("Поле \"Фамилия\" должно быть заполнено");
            return;
        }
        if (login.length() == 0){
            view.showMessage("Поле \"Логин\" должно быть заполнено");
            return;
        }
        if (password.length() < 8){
            view.showMessage("Поле \"Пароль\" должно содержать не менее 8-ми символов");
            return;
        }
        if (password.split("[a-zA-Z0-9_]").length > 0){
            view.showMessage("Поле \"Пароль\" должно содержать только буквы латинского алфавита и/или цифры");
            return;
        }
        if (!password.equals(confirm)){
            view.showMessage("Поле \"Пароль\" не совпадает с полем \"Повтор пароля\"");
            return;
        }
        registration.execute(name, lastname, login, password);
    }

    AsyncTask<String, Void, User> registration = new AsyncTask<String, Void, User>() {
        @Override
        protected User doInBackground(String... strings) {
            Message message = null;
            User user = null;
            UserDB userDB = new UserDB(strings[0], strings[1], strings[2], strings[3], 2);
            Gson gson = new Gson();
            String json = gson.toJson(userDB);
            Response response = NetworkManager.connection(json, "/registration");
            try {
                message = gson.fromJson(response.body().string(), Message.class);
            } catch (IOException e) {
                view.showMessage("Ошибка запроса к серверу");
                e.printStackTrace();
            }
            if (response.isSuccessful() && message.getStatus() == 0)
            {
                user = (User) userDB;
            }
            messageObj = message;
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (user != null)
                view.onStartActivity("user", user, BlogActivity.class);
            else
                view.showMessage(messageObj.getMessage());
        }
    };
}


