package ru.innopolis.askar.blog;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.List;

import ru.innopolis.askar.blog.Manager.NetworkManager;
import ru.innopolis.askar.blog.models.Article;
import ru.innopolis.askar.blog.models.User;
import ru.innopolis.askar.blog.presenters.BlogPresenter;
import ru.innopolis.askar.blog.presenters.IBlogPresenter;
import ru.innopolis.askar.blog.models.loaders.LoadAvatar;
import ru.innopolis.askar.blog.models.loaders.UpLoadAvatars;
import ru.innopolis.askar.blog.view.IViewBlogFragment;
import ru.innopolis.askar.blog.view.OpenFileDialog;

public class BlogActivity extends AppCompatActivity implements IViewBlogFragment, LoaderManager.LoaderCallbacks<byte[]> {
    public static final int DOWNLOAD_AVATAR = 2;
    public static final int UPLOAD_AVATAR = 3;
    private static String PATH_RESOURCE = Environment.getExternalStorageDirectory().getAbsolutePath();
    Loader upLoader;
    Loader downLoader;
    private TextView name;
    private TextView surname;
    private TextView lastDate;
    private WebView avatar;
    private ImageView avatar_;
    private TextView tvChangeAvatar;
    private TextView tvExitBlog;
    private static final int PERMISSION_REQUEST_CODE = 123;
    private Fragment fragment;
    private Path checkFile;
    private User user;
    OpenFileDialog openDialog;
    Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        name = (TextView) findViewById(R.id.tvNameBlog);
        surname = (TextView) findViewById(R.id.tvSurnameBlog);
        lastDate = (TextView) findViewById(R.id.tvDateLastBlog);
        //avatar = findViewById(R.id.wvAvatarBlog);
        avatar_ = (ImageView) findViewById(R.id.ivAvatarBlog);
        tvChangeAvatar = (TextView) findViewById(R.id.tvClickableEditAvatar);
        tvExitBlog = (TextView) findViewById(R.id.tvExitBlog);

        fragment = getFragmentManager().findFragmentById(R.id.blogFragment);
        bundle = new Bundle();

        tvChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasPermissions())
                    openFileDialog();
                else
                    requestPermissionWidthRationale();
            }
        });
        this.PATH_RESOURCE = this.getCacheDir().getAbsolutePath();
    }

    @Override
    public void onStart() {
        super.onStart();
        user = (User) getIntent().getSerializableExtra("user");
        if (user == null)
            user = (User) bundle.get("user");
        name.setText(user.getName());
        surname.setText(user.getLastname());
        openDialog = new OpenFileDialog(this)
                .setFilter(".*\\.txt")
                .setOpenDialogListener(new OpenFileDialog.OpenDialogListener() {
                    @Override
                    public void OnSelectFile(String fileName) {
                        uploadImage(fileName);
                    }
                })
                .setFolderIcon(this.getResources().getDrawable(R.drawable.folder, null))
                .setFileIcon(this.getResources().getDrawable(R.drawable.image, null));
    }

    @Override
    public void onResume() {
        super.onResume();
        Uri uri = Uri.parse(NetworkManager.IP_ADDRESS + "/loadavatar?login=" + user.getLogin());
        Picasso.with(this).load(uri).placeholder(R.drawable.dog1).error(R.drawable.dog2).into(avatar_);
    }

    public void openFileDialog(){
        openDialog = new OpenFileDialog(this)
                .setFilter(".*\\.txt")
                .setOpenDialogListener(new OpenFileDialog.OpenDialogListener() {
                    @Override
                    public void OnSelectFile(String fileName) {
                        uploadImage(fileName);
                    }
                })
                .setFolderIcon(this.getResources().getDrawable(R.drawable.folder, null))
                .setFileIcon(this.getResources().getDrawable(R.drawable.image, null));
        openDialog.show();
    }

    private boolean hasPermissions() {
        int res = 0;
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        for (String perm : permissions) {
            res = checkCallingOrSelfPermission(perm);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }


    private void requestPermissionWidthRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            final String message = "Storage permission is needed to show files count";
            Snackbar.make(BlogActivity.this.findViewById(R.id.activity_blog_view), message, Snackbar.LENGTH_LONG)
                    .setAction("GRANT", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestPerms();
                        }
                    }).show();
        } else {
            requestPerms();
        }
    }

    private void requestPerms() {
        String[] permissions = new String[] {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch (requestCode){
            case PERMISSION_REQUEST_CODE:{
                for (int res : grantResults){ // if user granted all permissions.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
            }break;
            default:allowed = false;break; // if user not granted permissions
        }

        if (allowed){
            //user granted all permissions we can perform our task.
            openFileDialog();
        }else{
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Toast.makeText(this, "Storage permission denied.", Toast.LENGTH_SHORT).show();
                }else{
                    showNotStoragePermissionSnackBar();
                }
            }

        }
    }

    private void showNotStoragePermissionSnackBar() {
        Snackbar.make(BlogActivity.this.findViewById(R.id.activity_blog_view), "Storage permission isn't granted", Snackbar.LENGTH_LONG)
                .setAction("SETTINGS", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openApplicationSettings();
                        Toast.makeText(getApplicationContext(), "Open Permissins and grant the Storage permission", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    private void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_REQUEST_CODE){
            openFileDialog();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void uploadImage(String path) {
        bundle.putString("path", path);
        bundle.putString("login", user.getLogin());
        upLoader = getSupportLoaderManager().initLoader(UPLOAD_AVATAR, bundle, this);
        upLoader.forceLoad();
    }

    public void onExit(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        bundle.putSerializable("user", user);
    }

    public void addArticle(View view) {
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public Loader<byte[]> onCreateLoader(int id, Bundle args) {
        Loader loader = null;
        String login = args.getString("login");
        if (id == UPLOAD_AVATAR) {
            String path = args.getString("path");
            loader = new UpLoadAvatars(this, path, login);
        }
        if (id == DOWNLOAD_AVATAR){
            loader = new LoadAvatar(this, login);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<byte[]> loader, byte[] data) {
        if (data == null) {
            Toast.makeText(this, "Картинка не загружена", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(this.getCacheDir(), user.getLogin());
        try {
            boolean created = true;
            if (!file.exists())
                created = file.createNewFile();
            if (created) {
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(data);
                outputStream.close();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Файл не найден", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, "Ошибка при записи/чтении файла", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        Uri uri = Uri.parse(NetworkManager.IP_ADDRESS + "/loadavatar?login=" + user.getLogin());
        Picasso.with(this).invalidate(uri);
        Picasso.with(this).load(uri).placeholder(R.drawable.dog1).error(R.drawable.dog2).into(avatar_);
    }

    @Override
    public void onLoaderReset(Loader<byte[]> loader) {

    }

    public void deleteArticle(){

    }

    @Override
    public void onStartActivity(String key, Object obj, Class<?> classActivity) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void inflateRecycler(List<Article> articles) {

    }
}
