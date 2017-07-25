package ru.innopolis.askar.blog.view;

/**
 * Created by admin on 10.07.2017.
 */

public interface IViewRegFragment {
    void onStartActivity(String key, Object obj, Class<?> classActivity);
    void showMessage(String message);
}
