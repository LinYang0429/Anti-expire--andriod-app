package com.a404nofund.cs446.cs446.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.a404nofund.cs446.cs446.Utils;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
		/*Intent service1 = new Intent(context, MyAlarmService.class);
	     context.startService(service1);*/
        Log.i("App", "called receiver method");
        try{
            Utils.generateNotification(context);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
