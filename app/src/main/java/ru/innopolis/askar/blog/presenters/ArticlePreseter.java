package ru.innopolis.askar.blog.presenters;

import ru.innopolis.askar.blog.models.ArticleDB;
import ru.innopolis.askar.blog.models.User;
import ru.innopolis.askar.blog.view.IViewArticleActivity;


public class ArticlePreseter implements IArticlePreseter {
    IViewArticleActivity view;
    User user = null;
    @Override
    public void bindView(IViewArticleActivity view, User user) {
        this.view = view;
        this.user = user;
    }

    @Override
    public void loadarticle(ArticleDB articleDB) {

    }
}
