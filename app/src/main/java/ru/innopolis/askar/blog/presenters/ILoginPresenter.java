package ru.innopolis.askar.blog.presenters;

import ru.innopolis.askar.blog.view.IViewLoginFragment;

/**
 * Created by admin on 12.07.2017.
 */

public interface ILoginPresenter {
    void onLogin(String login, String password);
    void bindView(IViewLoginFragment view);
}
