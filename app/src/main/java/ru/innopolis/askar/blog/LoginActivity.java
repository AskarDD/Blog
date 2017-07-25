package ru.innopolis.askar.blog;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ru.innopolis.askar.blog.models.Account;
import ru.innopolis.askar.blog.view.OpenConfirmDialog;

public class LoginActivity extends AppCompatActivity {
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fragment = getFragmentManager().findFragmentById(R.id.login_fragment);
    }

    @Override
    public void onAttachFragment(android.support.v4.app.Fragment fragment) {
        super.onAttachFragment(fragment);
    }
}
