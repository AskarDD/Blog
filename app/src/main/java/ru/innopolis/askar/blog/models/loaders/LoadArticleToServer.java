package ru.innopolis.askar.blog.models.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Response;
import ru.innopolis.askar.blog.Manager.NetworkManager;
import ru.innopolis.askar.blog.models.Article;
import ru.innopolis.askar.blog.models.ArticleDB;

/**
 * Created by admin on 18.07.2017.
 */

public class LoadArticleToServer extends AsyncTaskLoader<Article> {
    public static final String LOAD_TARGET = "article";
    Context context;
    ArticleDB articleDB;
    public LoadArticleToServer(Context context, ArticleDB articleDB) {
        super(context);
        this.articleDB = articleDB;
        this.context = context;
    }

    @Override
    public Article loadInBackground() {
        Article article = null;
        Gson gson = new Gson();
        String json = gson.toJson(articleDB);
        Response response = NetworkManager.connection(json, "/loadarticle");
        try {
            article = gson.fromJson(response.body().string(), Article.class);
        } catch (IOException e) {
            Toast.makeText(context, "Возникла ошибка при загрузке статьи", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return article;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
    }

    @Override
    public void deliverResult(Article data) {
        super.deliverResult(data);
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }
}
