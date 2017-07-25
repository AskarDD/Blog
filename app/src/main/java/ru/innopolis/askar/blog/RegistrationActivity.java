package ru.innopolis.askar.blog;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ru.innopolis.askar.blog.presenters.IRegPresenter;
import ru.innopolis.askar.blog.view.IViewRegFragment;
import ru.innopolis.askar.blog.view.RegistratFragment;

/**
 * Created by admin on 10.07.2017.
 */

public class RegistrationActivity extends AppCompatActivity {
    private Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        fragment = getFragmentManager().findFragmentById(R.id.registration_fragment);
    }
}
