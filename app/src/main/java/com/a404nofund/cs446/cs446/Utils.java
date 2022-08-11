package com.a404nofund.cs446.cs446;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;


public class Utils {

    public static NotificationManager mManager;


    @SuppressWarnings("static-access")
    public static void generateNotification(Context context){

        NotificationCompat.Builder nb= new NotificationCompat.Builder(context);
        nb.setSmallIcon(R.drawable.coke);
        nb.setContentTitle("Anti-Expir");
        nb.setContentText("Hi, this is a reminder of expiration foods/products");
        nb.setTicker("Take a look :)");

        nb.setAutoCancel(true);



        //get the bitmap to show in notification bar
        Bitmap bitmap_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.coke);
        NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(bitmap_image);
        s.setSummaryText("Hi, this is a reminder of expiration foods/products");
        nb.setStyle(s);

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder TSB = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            TSB = TaskStackBuilder.create(context);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            TSB.addParentStack(MainActivity.class);
        }
        // Adds the Intent that starts the Activity to the top of the stack
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            TSB.addNextIntent(resultIntent);
        }
        PendingIntent resultPendingIntent =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            resultPendingIntent = TSB.getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }

        nb.setContentIntent(resultPendingIntent);
        nb.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(11221, nb.build());


    }
}
