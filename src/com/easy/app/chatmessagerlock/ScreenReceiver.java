package com.easy.app.chatmessagerlock;

import java.util.List;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.widget.Toast;
import android.provider.Settings;
import android.provider.Settings.System;

public class ScreenReceiver extends BroadcastReceiver {
	
	static public MainActivity act;
	
   // thanks Jason
	private boolean screenOff;
	 
   @Override
   public void onReceive(Context context, Intent intent) {
       if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
           // do whatever you need to do here
    	   Log.d("@@@","ACTION_SCREEN_OFF");
    	   screenOff  = false;
           ///killAllProcess(context);
    	   
    	
           
       } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
           // and do whatever you need to do here
    	   Log.d("@@@","ACTION_SCREEN_ON");
    	   screenOff  = true;
       }
       
       
   }
 
   
}
