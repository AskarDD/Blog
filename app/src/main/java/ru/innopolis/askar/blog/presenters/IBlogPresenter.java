package ru.innopolis.askar.blog.presenters;

import ru.innopolis.askar.blog.models.ArticleDB;
import ru.innopolis.askar.blog.models.User;
import ru.innopolis.askar.blog.view.IViewBlogFragment;
import ru.innopolis.askar.blog.view.IViewRegFragment;

/**
 * Created by admin on 14.07.2017.
 */

public interface IBlogPresenter {
    void bindView(IViewBlogFragment view, User user);
    void deleteArticle(ArticleDB articleDB);
}
