package ru.innopolis.askar.blog.presenters;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.List;

import okhttp3.Response;
import ru.innopolis.askar.blog.Manager.NetworkManager;
import ru.innopolis.askar.blog.models.Article;
import ru.innopolis.askar.blog.models.ArticleDB;
import ru.innopolis.askar.blog.models.User;
import ru.innopolis.askar.blog.view.IViewBlogFragment;

/**
 * Created by admin on 14.07.2017.
 */

public class BlogPresenter implements IBlogPresenter {
    IViewBlogFragment view;
    User user = null;
    Article article;

    @Override
    public void deleteArticle(ArticleDB articleDB) {
        article = articleDB;
        asyncDeleteArticle.execute(articleDB);
    }

    @Override
    public void bindView(IViewBlogFragment view, User user) {
        this.view = view;
        this.user = user;
    }

    AsyncTask<ArticleDB, Void, Response> asyncDeleteArticle = new AsyncTask<ArticleDB, Void, Response>() {
        @Override
        protected Response doInBackground(ArticleDB... articleDBs) {
            Gson gson = new Gson();
            String json = gson.toJson(articleDBs[0]);
            Response response = NetworkManager.connection(json, "/deletearticle");
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            if (response == null){
                view.showMessage("Сервер не доступен");
                return;
            }
            if (!response.isSuccessful()){
                int code = response.code();
                switch (code){
                    case 404: view.showMessage("Не удается получить доступ к серверу.");break;
                    default: view.showMessage("Проблемы на сервере про попытке удаления Статьи.");break;
                }
                return;
            }
            user.removeArticle(article.getTitle());
            view.inflateRecycler(user.getArticles());
        }
    };

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
}
