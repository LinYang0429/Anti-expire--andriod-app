package com.a404nofund.cs446.cs446.dataType;

import com.a404nofund.cs446.cs446.R;

import java.util.ArrayList;

public class FakeDatabase {



    public static ArrayList<ProductDetail> getProducts(){
        ArrayList<ProductDetail> products = new ArrayList<>();

        ProductDetail productDetail = null;

        productDetail = new ProductDetail();
        productDetail.setStart("2017-01-01");
        productDetail.setEnd("2019-12-20");
        productDetail.setOpen("2018-05-21");
        productDetail.setName("Coke");
        productDetail.setTag("DRINKS");
        productDetail.setLeft("356");
        productDetail.setImg(R.drawable.coke);

        products.add(productDetail);

        productDetail = new ProductDetail();
        productDetail.setStart("2017-01-01");
        productDetail.setEnd("2019-11-20");
        productDetail.setOpen("2018-05-23");
        productDetail.setName("Canada Dry");
        productDetail.setTag("DRINKS");
        productDetail.setLeft("500");
        productDetail.setImg(R.drawable.canadadry);

        products.add(productDetail);

        productDetail = new ProductDetail();
        productDetail.setStart("2017-01-01");
        productDetail.setEnd("2019-12-25");
        productDetail.setOpen("2018-05-22");
        productDetail.setName("Sprite");
        productDetail.setTag("DRINKS");
        productDetail.setLeft("1000");
        productDetail.setImg(R.drawable.sprite);

        products.add(productDetail);

        return products;

    }

    public static ArrayList<ShareDetail> getShares() {
        ArrayList<ShareDetail> shares = new ArrayList<>();

        Comment comment = null;

        ShareDetail shareDetail = null;

        shareDetail = new ShareDetail();

        comment = new Comment(null,"1a",12,"bad");


        shareDetail.setName("Coke");
        shareDetail.setTag("DRINKS");
        shareDetail.setUserid("Potato");
        shareDetail.setCommentLikes("UP");
        shareDetail.setComment(comment);
        shareDetail.setImg(R.drawable.coke);
        shareDetail.setItemLikes("Like");
        shares.add(shareDetail);

        shareDetail = new ShareDetail();

        comment = new Comment(null,"1b",1234,"okay");


        shareDetail.setName("Canada Dry");
        shareDetail.setTag("DRINKS");
        shareDetail.setUserid("Potato");
        shareDetail.setCommentLikes("UP");
        shareDetail.setComment(comment);
        shareDetail.setImg(R.drawable.canadadry);
        shareDetail.setItemLikes("Like");
//        shares.add(shareDetail);

        shareDetail = new ShareDetail();

        comment = new Comment(null,"1c",123,"reallynice");



        shareDetail.setName("Sprite");
        shareDetail.setTag("DRINKS");
        shareDetail.setUserid("Potato");
        shareDetail.setCommentLikes("UP");
        shareDetail.setComment(comment);
        shareDetail.setImg(R.drawable.sprite);
        shareDetail.setItemLikes("UnLike");
//        shares.add(shareDetail);

        return shares;

    }
}
