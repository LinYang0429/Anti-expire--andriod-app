package com.a404nofund.cs446.cs446.dataType;

import java.util.ArrayList;

public class Comments {
    private User user;
    private String commentId;
    private long time;
    private String comment;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Comments(User user, String commentId, long time, String comment) {

        this.user = user;
        this.commentId = commentId;
        this.time = time;
        this.comment = comment;
    }
}
