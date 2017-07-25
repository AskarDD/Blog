package ru.innopolis.askar.blog.view;

import android.content.Context;
import android.net.Uri;

import java.util.List;

import ru.innopolis.askar.blog.models.Article;

/**
 * Created by admin on 14.07.2017.
 */

public interface IViewBlogFragment {
    void onStartActivity(String key, Object obj, Class<?> classActivity);
    void showMessage(String message);
    Context getContext();
    void inflateRecycler(List<Article> articles);
}
