package ru.innopolis.askar.blog.models;

import java.io.Serializable;

/**
 * Created by admin on 09.07.2017.
 */

public class Account implements Serializable {
    private Long id;
    private String login;
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Account(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public int hashCode() {
        int res = (this.id != null) ? 9 * this.id.hashCode() : 0;
        res += 9 * this.login.hashCode();
        res += 9 * this.password.hashCode();
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Account account = (Account) obj;

        if (id != null ? !id.equals(account.id) : account.id != null) return false;
        if (!login.equals(account.login))
            return false;
        return (!password.equals(account.password));
    }
}
