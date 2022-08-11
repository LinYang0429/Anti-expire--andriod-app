package com.a404nofund.cs446.cs446.dataType;

import java.io.Serializable;

public class Post implements Serializable{
    private User user;
    private String content;
    private String url;
    private  String postId;
    private long likes;
    private long comments;
    private long time;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getComments() {
        return comments;
    }

    public void setComments(long comments) {
        this.comments = comments;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }



}
