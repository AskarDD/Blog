package ru.innopolis.askar.blog.models;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 09.07.2017.
 */

public class User implements Serializable {
    private Long id;
    private String name;
    private String lastname;
    private String login;
    private int role;
    private String imageUri;
    private List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public void addArticle(Article article){
        this.articles.add(article);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getRole() {
        return role;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public boolean removeArticle(String title){
        for (Article article : articles){
            if (article.getTitle().equals(title))
                return articles.remove(article);
        }
        return false;
    }

    public User(String name, String lastname, String login) {
        this.name = name;
        this.lastname = lastname;
        this.login = login;
        this.articles = new ArrayList<>();
    }

    public User(String name, String lastname, String login, int role) {
        this.name = name;
        this.lastname = lastname;
        this.login = login;
        this.role = role;
        this.articles = new ArrayList<>();
    }

    @Override
    public int hashCode() {
        int res = (id != null) ? 9 * id.hashCode() : 0;
        res += 9 * name.hashCode();
        res += 9 * lastname.hashCode();
        res += 9 * login.hashCode();
        res += 9 * role;
        res += 9 * articles.size();
        res += 9 * imageUri.hashCode();
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        User user = (User) obj;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (!name.equals(user.name))
            return false;
        if (!lastname.equals(user.lastname))
            return false;
        if (!login.equals(user.login)) return false;
        if (articles.size() != user.articles.size()) return false;
        return (role != user.role);
    }
}
