package com.a404nofund.cs446.cs446;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.view.LayoutInflater;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.a404nofund.cs446.cs446.dataType.ProductDetail;

import pub.devrel.easypermissions.EasyPermissions;


public class RecyclerViewAdapterProduct extends RecyclerView.Adapter<RecyclerViewAdapterProduct.ProductViewHolder>
implements Visitor {

    Context c;
    ArrayList<Product> products;
    private File imageFile = null;
    String img;

    private final int IMAGE_MAX_WIDTH = 540;
    private final int IMAGE_MAX_HEIGHT = 960;

    public RecyclerViewAdapterProduct(Context c, ArrayList<Product> products) {
        this.c = c;
        this.products = products;

    }



    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override @NonNull
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_cardproduct, viewGroup, false);
        ProductViewHolder pvh = new ProductViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder productViewHolder, int i) {
        productViewHolder.nameText.setText(products.get(i).getBrandName());
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        productViewHolder.endDateText.setText(products.get(i).getProductName());


        imageFile = new File(products.get(i).getImage());
        int scale = 0;
        scale = getZoomScale(imageFile);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = scale;
        productViewHolder.image.setImageBitmap(BitmapFactory.decodeFile(products.get(i).getImage(),options));


        final String name = products.get(i).getProductName();
        final String tag = products.get(i).getTags();
        final String open = df.format(products.get(i).getOpenDate());
        final String start = df.format(products.get(i).getOpenDate());
        final String end = df.format(products.get(i).getExpireDate());
        final String left = Integer.toString(products.get(i).getDaysLeft());
        final int id = products.get(i).getItemID();
        final int opened = products.get(i).getPAO();
        img = products.get(i).getImage();

                productViewHolder.cv.setOnClickListener(new View.OnClickListener(){
                    @Override public void onClick(View v){
                        Bundle b = new Bundle();//Create Bundle
                        b.putString("NAME_KEY",name);
                        b.putString("TAG_KEY",tag);
                        b.putString("OPEN_KEY",open);
                        b.putString("START_KEY",start);
                        b.putString("END_KEY",end);
                        b.putString("LEFT_KEY",left);
                        b.putString("IMAGE_KEY",img);
                        b.putInt("ID_KEY", id);
                        b.putInt("OPENED_KEY", opened);
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        visitDetail(b, activity);
                    }
                });
    }

    private int getZoomScale(File imageFile) {
        int scale = 1;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(img, options);
        while (options.outWidth / scale >= IMAGE_MAX_WIDTH
                || options.outHeight / scale >= IMAGE_MAX_HEIGHT) {
            scale *= 2;
        }
        return scale;
    }

    @Override
    public void visitDetail(Bundle b, AppCompatActivity activity) {
        Fragment fragment = ProductDetailFragment.newInstance();
        fragment.setArguments(b);

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public static class ProductViewHolder extends  RecyclerView.ViewHolder {
        CardView cv;
        TextView nameText;
        TextView tagText;
        TextView openDateText;
        TextView startDateText;
        TextView endDateText;
        TextView daysLeftText;
        ImageView image;
        public View itemview;

        public ProductViewHolder(final View itemView){
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            nameText = (TextView) cv.findViewById(R.id.product_name);
            //tagText = (TextView) rootView.findViewById(R.id.product_tag);
            //openDateText = (TextView) rootView.findViewById(R.id.product_date_open);
            //startDateText = (TextView) rootView.findViewById(R.id.product_date_start);
            endDateText = (TextView) cv.findViewById(R.id.product_date_end);
            image  = (ImageView) cv.findViewById(R.id.product_photo);
            this.itemview = itemView;//unused

        }


    } //view holder



}// view adapter
