package com.easy.app.chatmessagerlock;

import java.util.ArrayList;
import java.util.List;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	private final static String TAG =  SettingsActivity.class.getSimpleName();
	private PullToRefreshListView mPullRefreshListView;
	private LockItemAdapter adapter;
	private final static String [] BLOCKLIST = {"com.sonyericsson.conversations","com.android.mms","com.futurebits.instamessage.free","com.instagram.android","com.yahoo.mobile.client.android.im","com.bbm","jp.naver.line.android","com.facebook.katana",
		"com.google.android.apps.plus","com.facebook.orca","com.whatsapp","com.tencent.mm",
		"com.google.android.talk","com.tencent.mn","com.skype.raider","com.viber.voip","com.twitter.android"
		,"com.android.mms","com.google.android.gm","com.google.android.gallery3d","com.htc.album","com.android.gallery3d"
		,"com.cooliris.media","com.kakao.talk","com.immomo.momo","com.sgiggle.production","com.skout.android","com.android.dialer" ,
				"com.android.htcdialer"};
	
	private AdView adView;
	
	SQLite sqlHelper;
	List<ListItems> dataList = new ArrayList<ListItems>();
	ListView actualListView; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.settings);
		
		Crouton.makeText(this,
				this.getString(R.string.toast_loading),
				Style.ALERT).show();
		
		Toast.makeText(this, this.getString(R.string.toast_loading), Toast.LENGTH_LONG).show();
		
		 adView = (AdView)findViewById(R.id.adView);
	     adView.loadAd(new AdRequest());
		
		sqlHelper = new SQLite(this);
		
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("Refresh");
				new UpdateDataTask().execute();
			}
					
		});
		
		// Add an end-of-list listener
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
						
			}
		});
		
		actualListView = mPullRefreshListView.getRefreshableView();
		//adapter = new LockItemAdapter(this, dataList, this);
		
		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(this);
		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
		soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
		soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
		mPullRefreshListView.setOnPullEventListener(soundListener);
		
		new UpdateDataTask().execute();
		//actualListView.setAdapter(adapter);
		
		Intent intent = new Intent();
		intent.setClass(this, MointorServices.class);
		this.startService(intent);
		
	}
	
	@Override
    protected void onDestroy(){
		if (sqlHelper != null)
			sqlHelper.close();
		
		if(adView!=null)
			adView.destroy();
		
    	super.onDestroy();
	}
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	  	 super.onCreateOptionsMenu(menu);
	  	 
	  	
	    MenuItem item4=menu.add(1,5,0,this.getString(R.string.menu_item4));
	       item4.setIcon(android.R.drawable.ic_menu_edit);
	  	 
	  	 
	  	 MenuItem item2=menu.add(1,3,0,this.getString(R.string.menu_item1));
	       item2.setIcon(android.R.drawable.ic_menu_more);
	  	 
	     MenuItem item1=menu.add(1,2,0,this.getString(R.string.menu_item2));
	       item1.setIcon(android.R.drawable.ic_menu_add);
	       
	  	
	       return true;
	  }
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	  	switch (item.getItemId()) {
	  		
	  		case 2:
	  			
	  			Intent intentPro = new Intent("android.intent.action.VIEW");
	  			intentPro.setData(Uri.parse("market://details?id=com.easy.apps.privacylock"));
	              startActivity(intentPro);
	  			
	  			break;
	  			
	  		case 3:

	              Intent intent = new Intent("android.intent.action.VIEW");
	              intent.setData(Uri.parse("market://search?q=carter.ten&c=apps"));
	              startActivity(intent);
	              
	  			break;
	  			
	  		case 4:
	  			break;
	  			
	  		case 5:
	  			createSettingDialog(2);
	  			break;
	  	}
	  	
	  	return true;
	  }
	
	private void createSettingDialog(int type){
		final String[] items = this.getResources().getStringArray(R.array.lock);
		 new AlertDialog.Builder(this).setTitle(R.string.setting_dialog_msg).setCancelable(true).setItems(items, new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				SettingsActivity.this.getSharedPreferences("prefs",0).edit().putString("locktime", items[which]).commit();
				
				Crouton.makeText(SettingsActivity.this,
						SettingsActivity.this.getString(R.string.add_ok),
						Style.ALERT).show();
				
				Toast.makeText(SettingsActivity.this, SettingsActivity.this.getString(R.string.add_ok), 
						Toast.LENGTH_LONG).show();
				
				dialog.dismiss();
				
				
			}
			 
		 }).show();
	}
	
	private class UpdateDataTask extends AsyncTask<Void, Void, List<ListItems>> {

		@Override
		protected List<ListItems> doInBackground(Void... arg0) {
			
			try{
				dataList.clear();
				List<ListItems> list = getAppInstalledList();
				dataList.addAll(list);
				adapter = new LockItemAdapter(SettingsActivity.this, dataList, SettingsActivity.this);
				
			}catch (Exception e){
			}

			return dataList;
		}
		
		@Override
		protected void onPostExecute(List<ListItems> list) {
			
			//adapter.notifyDataSetChanged();
			
			actualListView.setAdapter((LockItemAdapter)adapter);
			mPullRefreshListView.onRefreshComplete();
			
			//super.onPostExecute(list);
		}
		
		private List<ListItems> getAppInstalledList(){
			List<ListItems> installedList = new ArrayList<ListItems>();
			try {
				int record = sqlHelper.getCount();
				if (record <= 0) {
					Log.d(TAG, "init setting list");

					final Intent mainIntent = new Intent(Intent.ACTION_MAIN,
							null);
					mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
					final List pkgAppsList = getPackageManager()
							.queryIntentActivities(mainIntent, 0);
					

					for (int i = 0; i < pkgAppsList.size(); i++) {
						ResolveInfo info = (ResolveInfo) pkgAppsList.get(i);
						String pkgName = info.activityInfo.packageName;

						for (String app : BLOCKLIST) {
							if (pkgName.equals(app)) {
								Log.d(TAG, "add app:"+ pkgName);
								ListItems item = new ListItems();
								item.setIcon(info.loadIcon(getPackageManager()));
								item.setAppName(info.loadLabel(
										getPackageManager()).toString());
								
								item.setLock(false);
								item.setPkgName(pkgName);
								
								installedList.add(item);
								SettingsActivity.this.addRecordIntoDB(pkgName, "FALSE");
								break;
							}
						}
					}

				}else {
					Log.d(TAG, "get old record list");
					installedList = getRecordListFromDB();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return installedList;
		}
		
		private List<ListItems> getRecordListFromDB(){
			List<ListItems> installedList = new ArrayList<ListItems>();
			try {
				Cursor c = sqlHelper.getAll();
				int rows_num = c.getCount();
				
				Log.d(TAG, "lcount:"+ rows_num);
				
				c.moveToFirst();

				for (int i = 0; i < rows_num; i++) {
					
					ListItems items = new ListItems();

					String pkgName = c.getString(1);
					String isOn = c.getString(2);
					Log.d(TAG, "pkgname:"+pkgName);
					Drawable icon = null;
					try{
					icon = getPackageManager().getApplicationIcon(
							pkgName);
					}catch(Exception e){
						e.printStackTrace();
						c.moveToNext();
						continue; 
					}
					ApplicationInfo ai;
					ai = getPackageManager().getApplicationInfo(pkgName, 0);
					String lable = (String) getPackageManager()
							.getApplicationLabel(ai);
					
					items.setPkgName(pkgName);
					
					items.setAppName(lable);
					items.setIcon(icon);
					if (isOn.equals("TRUE"))
						items.setLock(true);
					else
						items.setLock(false);
					
					installedList.add(items);
					c.moveToNext();
					}
			}catch(Exception e){
				e.printStackTrace();
			}
			return installedList;
	}
	
	}
	
	public void addRecordIntoDB(String pkgName,String inOn){
		try{
			if(pkgName == null)
				return;
			
			if (sqlHelper != null)
				try {
					if(!chkDoneUrl(pkgName)){
						Log.d(TAG, pkgName + " not yet in DB");
						sqlHelper.create(pkgName, inOn);
					}
					else{
						Log.d(TAG, pkgName + " already in DB");
						deleteNotes(pkgName);
						sqlHelper.create(pkgName, inOn);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private boolean chkDoneUrl(String url){
		 Cursor c = null;
		 if (sqlHelper != null)
				try {
					c = sqlHelper.getIdDone(url);
					if(c.getCount() >= 1)
						return true;
					else
						return false;
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					if(c != null)
						c.close();
				}
		 
				return false;
	 }
	
	 private void deleteNotes(String purpose){
		 Log.d(TAG, "del:" + purpose);
	    	if (sqlHelper != null){
	    		try{
	    			sqlHelper.deleteData("msg = '"+ String.valueOf(purpose)+"'");
	    		}catch(Exception e){
	    			e.printStackTrace();
	    		}
	    	}
	    		
	    }
}
	