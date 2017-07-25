package ru.innopolis.askar.blog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by admin on 17.07.2017.
 */

public class ConfirmActivity extends AppCompatActivity {
    private EditText etLogin;
    private EditText etPassword;
    private Context preContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        etLogin = (EditText) findViewById(R.id.etLoginConfirm);
        etPassword = (EditText) findViewById(R.id.etPasswordConfirm);


    }

    public void onCancel(View view) {
    }

    public void onLogin(View view) {

    }
}
