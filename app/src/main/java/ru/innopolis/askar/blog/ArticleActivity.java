package ru.innopolis.askar.blog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import ru.innopolis.askar.blog.models.Account;
import ru.innopolis.askar.blog.models.Article;
import ru.innopolis.askar.blog.models.ArticleDB;
import ru.innopolis.askar.blog.models.User;
import ru.innopolis.askar.blog.presenters.ArticlePreseter;
import ru.innopolis.askar.blog.presenters.IArticlePreseter;
import ru.innopolis.askar.blog.models.loaders.LoadArticleToServer;
import ru.innopolis.askar.blog.view.IViewArticleActivity;
import ru.innopolis.askar.blog.view.OpenConfirmDialog;

public class ArticleActivity extends AppCompatActivity implements IViewArticleActivity, LoaderManager.LoaderCallbacks<Article> {
    public static final int LOAD_ARTICLE = 1;
    private OpenConfirmDialog confirmDialog;
    private EditText etTitle;
    private EditText etText;
    private IArticlePreseter presenter;
    ArticleDB articleDB = null;
    Loader<Article> loader;
    Bundle bundle = null;
    User user;
    int position;
    Article article;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        etTitle = (EditText) findViewById(R.id.etTitleArticle);
        etText = (EditText) findViewById(R.id.etTextArticle);
        user = (User) getIntent().getSerializableExtra("user");
        position = getIntent().getIntExtra("position", -1);
        if (position > -1)
            article = user.getArticles().get(position);
        bundle = new Bundle();
        if (presenter == null)
            presenter = new ArticlePreseter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (article != null){
            etText.setText(article.getText());
            etTitle.setText(article.getTitle());
        }
        presenter.bindView(this, user);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_cancel: cancel(); return true;
            case R.id.action_save: save(); return true;
            case R.id.action_delete: delete(); return true;
            default: return true;
        }
    }

    public void save(){
        confirmDialog = new OpenConfirmDialog(this, "  Введите логин и пароль для подтверждения сохранения", "Введите логин", "Введите пароль")
                .setOpenDialogListener(new OpenConfirmDialog.OpenDialogListener() {
                    @Override
                    public void OnAddAccount(Account account) {
                        sendToServer(account);
                    }
                });
        confirmDialog.show();
    }

    public void sendToServer(Account account){
        articleDB = new ArticleDB(etTitle.getText().toString(), etText.getText().toString(), account);
        bundle.putSerializable("article", articleDB);
        loader = getSupportLoaderManager().initLoader(LOAD_ARTICLE, bundle, this);
        loader.forceLoad();
    }

    public void cancel(){
        etTitle.setText("");
        etText.setText("");
        Intent intent = new Intent(this, BlogActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void delete(){
        etText.setText("");
    }

    @Override
    public void onStartActivity(String key, Object obj, Class<?> classActivity) {
        Intent intent = new Intent(this, classActivity);
        intent.putExtra(key, (Serializable) obj);
        startActivity(intent);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Loader<Article> loader = null;
        if (id == LOAD_ARTICLE)
            loader = new LoadArticleToServer(this, articleDB);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Article> loader, Article data) {
        List<Article> articles = user.getArticles();
        if (position > -1) {
            articles.remove(position);
            articles.add(position, data);
        }else{
            articles.add(data);
        }
        user.setArticles(articles);
        Intent intent = new Intent(this, BlogActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
