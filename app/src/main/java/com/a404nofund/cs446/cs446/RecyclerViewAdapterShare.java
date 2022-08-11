package com.a404nofund.cs446.cs446;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.a404nofund.cs446.cs446.dataType.ShareDetail;
import com.google.gson.Gson;

import java.util.ArrayList;

public class RecyclerViewAdapterShare extends RecyclerView.Adapter<RecyclerViewAdapterShare.ShareViewHolder> {

    ArrayList<ShareDetail> shares;
    ArrayList<Product> products;
    private final int IMAGE_MAX_WIDTH = 540;
    private final int IMAGE_MAX_HEIGHT = 960;

    RecyclerViewAdapterShare(ArrayList<ShareDetail> s, ArrayList<Product> p){
        this.shares = s;
        this.products = p;
    }
    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override @NonNull
    public ShareViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_cardshare, viewGroup, false);
        ShareViewHolder pvh = new ShareViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(@NonNull final ShareViewHolder shareViewHolder, int i) {
        int scale = 1;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(products.get(i).getImage(), options);
        while (options.outWidth / scale >= IMAGE_MAX_WIDTH
                || options.outHeight / scale >= IMAGE_MAX_HEIGHT) {
            scale *= 2;
        }
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        options.inSampleSize = scale;
        shareViewHolder.shareImage.setImageBitmap(BitmapFactory.decodeFile(products.get(i).getImage(),bitmapOptions));

        final int likes = products.get(i).getLike();
        final int positionC = i;

        ImageButton star = (ImageButton) shareViewHolder.itemView.findViewById(R.id.likeIcon);
        ImageButton comt = (ImageButton) shareViewHolder.itemView.findViewById(R.id.commentIcon);
        ImageButton share = (ImageButton) shareViewHolder.itemView.findViewById(R.id.shareIcon);

        if (likes == 1){
            star.setImageResource(android.R.drawable.btn_star_big_on);
            star.setTag("like");
        } else {
            star.setImageResource(android.R.drawable.btn_star_big_off);
            star.setTag("unlike");
        }
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageButton ibtn = (ImageButton) v;

                if(ibtn.getTag().equals("like")){
                    ibtn.setImageResource(android.R.drawable.btn_star_big_off);
                    ibtn.setTag("unlike");
                    products.get(positionC).setLike(0);
                    Gson gson = new Gson();
                    String json = gson.toJson(products);
                    SharedPreferences mPreference = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                    SharedPreferences.Editor mEditor = mPreference.edit();
                    mEditor.putString("data",json);
                    mEditor.apply();
                    Toast.makeText(v.getContext(),"You Unliked this item",Toast.LENGTH_LONG).show();
                } else {
                    ibtn.setImageResource(android.R.drawable.btn_star_big_on);
                    ibtn.setTag("like");
                    products.get(positionC).setLike(1);
                    Gson gson = new Gson();
                    String json = gson.toJson(products);
                    SharedPreferences mPreference = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                    SharedPreferences.Editor mEditor = mPreference.edit();
                    mEditor.putString("data",json);
                    mEditor.apply();
                    Toast.makeText(v.getContext(),"You Liked this item",Toast.LENGTH_LONG).show();

                }

                }
        });

        comt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),CommentActivity.class);
                intent.putExtra("Position",shareViewHolder.getAdapterPosition());
                int x = shareViewHolder.getAdapterPosition();
                ((Activity)v.getContext()).startActivityForResult((intent),1);

            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Activity a = (AppCompatActivity) v.getContext();
                    RelativeLayout relativeLayout =  shareViewHolder.rl;//a.findViewById(R.id.shareLayout);

                    Bitmap bitmap = getBitmapFromView(relativeLayout);
                    File file = new File(a.getExternalCacheDir(),"share.png");
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true,false);
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/png");
                    share.putExtra(Intent.EXTRA_SUBJECT,"MY_ITEM");
                    share.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(a,BuildConfig.APPLICATION_ID+".provider",file));
                    a.startActivity(Intent.createChooser(share,"Look at my items!"));

                } catch (Exception e) {
                    Toast.makeText(v.getContext(),"Failed to share",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    e.getMessage();
                }
            }
        });
    }


    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null){
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    public static class ShareViewHolder extends  RecyclerView.ViewHolder {
        CardView cv;
        ImageView shareImage;
        RelativeLayout rl;
        public View itemView;
        private ShareViewHolder(View itemView){
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cvshare);
            shareImage = (ImageView)itemView.findViewById(R.id.shareImage);
            rl = itemView.findViewById(R.id.shareLayout);
            this.itemView = itemView;
        }
    }
}
