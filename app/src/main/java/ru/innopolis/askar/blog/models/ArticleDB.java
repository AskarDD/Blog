package ru.innopolis.askar.blog.models;

import java.io.Serializable;

public class ArticleDB extends Article implements Serializable {
    private Account account;
    public ArticleDB(String title, String text, Account account) {
        super(title, text);
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }
}
