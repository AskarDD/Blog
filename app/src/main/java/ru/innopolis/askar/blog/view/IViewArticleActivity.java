package ru.innopolis.askar.blog.view;

import android.content.Context;

/**
 * Created by admin on 18.07.2017.
 */

public interface IViewArticleActivity {
    void onStartActivity(String key, Object obj, Class<?> classActivity);
    void showMessage(String message);
    Context getContext();
}
