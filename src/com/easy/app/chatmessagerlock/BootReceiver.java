package com.easy.app.chatmessagerlock;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		if(arg1==null || arg1.getAction()==null)
			return;
		if (arg1.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent startIntent = new Intent();
			startIntent.setClass(arg0, MointorServices.class);
			arg0.startService(startIntent);
		}
	}

}
