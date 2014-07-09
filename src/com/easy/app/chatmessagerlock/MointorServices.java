package com.easy.app.chatmessagerlock;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.Log;

public class MointorServices extends Service{
	
	private SQLite sqlManager;
	private static String perviousAct="";
	static long starttime = 0L;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate(){
		super.onCreate();
		
		sqlManager = new SQLite(this);
		
		new Thread(){
			
			@Override
			public void run(){
				
				while(true){
					
					try {
						Thread.sleep((long) (2* 1000));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String app = getUsingActivity();
					
					if(app.equals("com.easy.apps.privacylock"))
						continue;
					
					if(app.equals("com.android.launcher"))
						continue;
				
					if(app.equals("com.htc.launcher"))
						continue;
					
					if(getRecordListFromDB(app)){						
						
						if(chkInTime(starttime) || !EmptyActivity.bFlag){
							starttime = System.currentTimeMillis();
							Log.d("@@@", "lock it");
							
							Intent intent = new Intent();
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setClass(MointorServices.this, EmptyActivity.class);
							MointorServices.this.startActivity(intent);
							
						}else{
							
							Log.d("@@@", "dont lock still in time");
							continue;
						}

						
					}else{
						Log.d("@@@", "not in db");
						continue;
					}
				}
				
				
			}
		}.start();
	}
	
	private boolean chkInTime(long start){
		if(start == 0L)
			return true;
		long newer = System.currentTimeMillis();
		String time = this.getSharedPreferences("prefs", 0).getString("locktime", "5");
		Log.d("@@@",newer+","+start+","+(newer-start) );
		if(newer - start > (Integer.valueOf(time) * 1000 * 60))
			return true;
		else
			return false;
	}
	
	@Override
	public void onDestroy(){
		if(sqlManager!=null)
			sqlManager.close();
		
		super.onDestroy();
	}
	
	private String getUsingActivity(){
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ActivityManager.RunningTaskInfo runTask = am.getRunningTasks(1).get(0);
		ComponentName con = runTask.topActivity;
		return con.getPackageName();
		
	}
	
	private boolean getRecordListFromDB(String nowapp){
		boolean flag = false;
		try {
			Cursor c = sqlManager.getAll();
			int rows_num = c.getCount();
			
			Log.d("@@@", "lcount:"+ rows_num);
			
			c.moveToFirst();

			for (int i = 0; i < rows_num; i++) {

				ListItems items = new ListItems();
				String isOn = c.getString(2);
				String pkgName = c.getString(1);
				
				if (isOn.equals("TRUE")){
					if(nowapp.equals(pkgName)){
						flag = true;
						break;
					}
						
				}else
					continue;

				c.moveToNext();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return flag;
	}
}
