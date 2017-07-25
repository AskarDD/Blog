package ru.innopolis.askar.blog.models;

import java.io.Serializable;

/**
 * Created by admin on 14.07.2017.
 */

public class Message implements Serializable {
    private Long id;
    private String message;
    private int status;

    public Message(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int res = (id != null) ? 9 * id.hashCode() : 0;
        res += 9 * message.hashCode();
        res += 9 * status;
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Message message = (Message) obj;

        if (id != null ? !id.equals(message.id) : message.id != null) return false;
        if (!this.message.equals(message.message))
            return false;
        return (status == message.status);
    }
}
