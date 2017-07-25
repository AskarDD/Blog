package ru.innopolis.askar.blog.presenters;

import ru.innopolis.askar.blog.view.IViewRegFragment;

/**
 * Created by admin on 10.07.2017.
 */

public interface IRegPresenter {
    void onReg(String name, String lastname, String login, String password, String confirm);
    void bindView(IViewRegFragment view);
}
