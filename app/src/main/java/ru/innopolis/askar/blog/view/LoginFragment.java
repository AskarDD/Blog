package ru.innopolis.askar.blog.view;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.innopolis.askar.blog.Manager.NetworkManager;
import ru.innopolis.askar.blog.R;
import ru.innopolis.askar.blog.RegistrationActivity;
import ru.innopolis.askar.blog.models.Account;
import ru.innopolis.askar.blog.models.User;
import ru.innopolis.askar.blog.presenters.ILoginPresenter;
import ru.innopolis.askar.blog.presenters.LoginPresentor;

/**
 * Created by admin on 09.07.2017.
 */

public class LoginFragment extends Fragment implements IViewLoginFragment {
    ILoginPresenter presenter;
    private EditText login;
    private EditText password;
    private Button btnLogin;
    private Button btnReg;
    String strLogin;
    String strPassword;
    private View.OnClickListener onLogin;
    private View.OnClickListener onReg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.bindView(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        login = getView().findViewById(R.id.etLoginFragment);
        password = getView().findViewById(R.id.etPasswordFragment);
        btnLogin = getView().findViewById(R.id.btnLoginFragment);
        btnReg = getView().findViewById(R.id.btnRegFragment);
        btnLogin.setOnClickListener(onLogin);
        btnReg.setOnClickListener(onReg);

        if (presenter == null)
            presenter = new LoginPresentor();
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        onLogin = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strLogin = login.getText().toString();
                strPassword = password.getText().toString();
                int type = password.getInputType();
                presenter.onLogin(strLogin, strPassword);
            }
        };

        onReg = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RegistrationActivity.class));
            }
        };
    }

    public String getStrLogin() {
        return strLogin;
    }

    public String getStrPassword() {
        return strPassword;
    }

    @Override
    public void onStartActivity(String key, Object obj, Class<?> classActivity) {
        User user = (User) obj;
        Intent intent = new Intent(getActivity(),classActivity);
        intent.putExtra(key, user);
        startActivity(intent);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
