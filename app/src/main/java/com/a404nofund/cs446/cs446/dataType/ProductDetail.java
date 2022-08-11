package com.a404nofund.cs446.cs446.dataType;

import android.widget.ImageView;
import android.widget.TextView;

public class ProductDetail {
    String name;
    String tag;
    String open;
    String start;
    String end;
    String left;


    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    int img;

    public ProductDetail() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
