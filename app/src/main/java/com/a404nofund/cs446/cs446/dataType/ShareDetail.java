package com.a404nofund.cs446.cs446.dataType;

public class ShareDetail {
    String tag;
    String name;
    String userid;

    String itemLikes;
    String commentLikes;
    Comment comment;


    int img;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public ShareDetail() {
    }

    public String getTag() {

        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }



    public String getItemLikes() {
        return itemLikes;
    }

    public void setItemLikes(String itemLikes) {
        this.itemLikes = itemLikes;
    }

    public String getCommentLikes() {
        return commentLikes;
    }

    public void setCommentLikes(String commentLikes) {
        this.commentLikes = commentLikes;
    }
}
