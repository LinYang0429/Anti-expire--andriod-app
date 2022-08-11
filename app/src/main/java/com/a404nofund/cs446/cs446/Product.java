package com.a404nofund.cs446.cs446;


import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;
public class Product implements Comparable<Product>{
    protected int itemID;
    protected String brandName;
    protected String productName;
    protected int daysLeft;
    protected String tags;
    protected Date expireDate;
    protected Date openDate;
    private int PAO; //(period after open)
    private int like;

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    protected String image;
    protected String userEmail;
    protected int finish;

    @Override
    public int compareTo(@NonNull Product o) {
        if (getExpireDate() == null || o.getExpireDate() == null)
            return 0;
        return getExpireDate().compareTo(o.getExpireDate());
    }

    enum finishedStatus {
        EXPIRE, // 0
        USED, // 1
        THROW; // 2
    }
    Date notificationTime;
    public Product(int itemID, String brandName, String productName, int daysLeft, String tags,
                   Date expireDate, Date openDate, String image, Date notificationTime,int finish,int PAO) {
        this.itemID = itemID;
        this.brandName = brandName;
        this.productName = productName;
        this.daysLeft = daysLeft;
        this.tags = tags;
        this.expireDate = expireDate;
        this.openDate = openDate;
        this.image = image;
        this.notificationTime = notificationTime;
        this.finish = finish;
        this.PAO = PAO;
        this.like = 0;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public int getPAO() {
        return PAO;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public void setPAO(int PAO) {
        this.PAO = PAO;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(Date notificationTime) {
        this.notificationTime = notificationTime;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void send() {
    }

    public Product requestByID(int itemID) {
        return this;
    }
}