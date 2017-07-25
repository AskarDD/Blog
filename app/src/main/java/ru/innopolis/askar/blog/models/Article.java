package ru.innopolis.askar.blog.models;

import java.io.Serializable;

public class Article implements Serializable {
    private long id;
    private String title;
    private String text;

    public Article(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int hashCode() {
        int res = 9 * title.hashCode() + 13 * text.hashCode();
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Article article = (Article) obj;
        if (!title.equals(article.title)) return false;
        return (text.equals(article.text));
    }
}
