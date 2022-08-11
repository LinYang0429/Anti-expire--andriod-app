package com.a404nofund.cs446.cs446;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.vipulasri.timelineview.TimelineView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TimelineAdapter extends RecyclerView.Adapter<TimeLineViewHolder>
implements Visitor {
    Context c;
    ArrayList<Product> products;
    private File imageFile = null;
    String img;

    private final int IMAGE_MAX_WIDTH = 540;
    private final int IMAGE_MAX_HEIGHT = 960;

    public TimelineAdapter(Context c, ArrayList<Product> products) {
        this.c = c;
        Collections.sort(products);
        this.products = products;
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline, parent, false);
        TimeLineViewHolder viewHolder = new TimeLineViewHolder(v, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder timeLineViewHolder, int i) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        timeLineViewHolder.mDate.setText(df.format(products.get(i).getExpireDate()));


        imageFile = new File(products.get(i).getImage());
        int scale = 0;
        scale = getZoomScale(imageFile);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = scale;
        timeLineViewHolder.mImage.setImageBitmap(BitmapFactory.decodeFile(products.get(i).getImage(),options));

        final String name = products.get(i).getProductName();
        final String tag = products.get(i).getTags();
        final String open = df.format(products.get(i).getOpenDate());
        final String start = df.format(products.get(i).getOpenDate());
        final String end = df.format(products.get(i).getExpireDate());
        final String left = Integer.toString(products.get(i).getDaysLeft());
        final int opedState = products.get(i).getPAO();
        img = products.get(i).getImage();

        timeLineViewHolder.cv.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                Bundle b = new Bundle();//Create Bundle
                b.putString("NAME_KEY",name);
                b.putString("TAG_KEY",tag);
                b.putString("OPEN_KEY",open);
                b.putString("START_KEY",start);
                b.putString("END_KEY",end);
                b.putString("LEFT_KEY",left);
                b.putString("IMAGE_KEY",img);
                b.putInt("OPENED_KEY", opedState);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                visitDetail(b, activity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
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

}
