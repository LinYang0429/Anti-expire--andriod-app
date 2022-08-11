package com.a404nofund.cs446.cs446.dataType;

import java.io.Serializable;

public class User implements Serializable {
    private String user;
    private String email;
    private String photo;
    private String uid;

    public User() {
    }

    public User(String user, String email, String photo, String uid) {
        this.user = user;
        this.email = email;
        this.photo = photo;
        this.uid = uid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
