package ru.innopolis.askar.blog.presenters;

import android.content.Context;

import ru.innopolis.askar.blog.models.ArticleDB;
import ru.innopolis.askar.blog.models.User;
import ru.innopolis.askar.blog.view.IViewArticleActivity;
import ru.innopolis.askar.blog.view.IViewLoginFragment;

/**
 * Created by admin on 18.07.2017.
 */

public interface IArticlePreseter {
    void bindView(IViewArticleActivity view, User user);
    void loadarticle(ArticleDB articleDB);
}
