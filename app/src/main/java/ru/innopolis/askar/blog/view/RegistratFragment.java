package ru.innopolis.askar.blog.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.innopolis.askar.blog.LoginActivity;
import ru.innopolis.askar.blog.R;
import ru.innopolis.askar.blog.models.User;
import ru.innopolis.askar.blog.presenters.IRegPresenter;
import ru.innopolis.askar.blog.presenters.RegistratPresenter;

/**
 * Created by admin on 10.07.2017.
 */

public class RegistratFragment extends Fragment implements IViewRegFragment {
    private IRegPresenter presenter;
    private EditText etName;
    private EditText etLastname;
    private EditText etLogin;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnCansel;
    private Button btnReg;
    private View.OnClickListener onCancel;
    private View.OnClickListener onReg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.registration_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.bindView(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etName = getView().findViewById(R.id.etRegistratName);
        etLastname = getView().findViewById(R.id.etRegistratLastName);
        etLogin = getView().findViewById(R.id.etRegistratLogin);
        etPassword = getView().findViewById(R.id.etRegistratPassword);
        etConfirmPassword = getView().findViewById(R.id.etConfirmPassword);
        btnCansel = getView().findViewById(R.id.btnRegistratCancel);
        btnReg = getView().findViewById(R.id.btnRegistratReg);
        btnCansel.setOnClickListener(onCancel);
        btnReg.setOnClickListener(onReg);

        if (presenter == null)
            presenter = new RegistratPresenter();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onReg = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onReg(getName(), getLastname(), getLogin(), getPassword(), getConfirm());
            }
        };

        onCancel = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        };
    }

    public String getName(){
        return etName.getText().toString();
    }
    public String getLastname(){
        return etLastname.getText().toString();
    }
    public String getLogin(){
        return etLogin.getText().toString();
    }
    public String getPassword(){
        return etPassword.getText().toString();
    }
    public String getConfirm() { return etConfirmPassword.getText().toString(); }

    @Override
    public void onStartActivity(String key, Object obj, Class<?> classActivity) {
        User user = (User) obj;
        Intent intent = new Intent(getActivity(), classActivity);
        intent.putExtra(key, user);
        startActivity(intent);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
