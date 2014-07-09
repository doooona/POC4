package com.easy.app.chatmessagerlock;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import group.pals.android.lib.ui.lockpattern.LockPatternActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	private static final int REQ_CREATE_PATTERN = 1;
	private static final String TAG = MainActivity.class.getSimpleName();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		String pattern = this.getSharedPreferences("prefs", 0).getString("pattern", "no");
		if(pattern.equals("no")){
			showAlertDialog();
		}else{
			goSettingsPage();	
			//finish();
		}	
	}
	
	private void goSettingsPage(){
		new Thread(){
			@Override
			public void run(){
				
				/*try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, SettingsActivity.class);
				MainActivity.this.startActivity(intent);
				finish();
			}
		}.start();
		
	}
	
	private void createNewPattern(){
		
		Intent intent = new Intent(LockPatternActivity.ACTION_CREATE_PATTERN, null,
			       this, LockPatternActivity.class);
		startActivityForResult(intent, REQ_CREATE_PATTERN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	        Intent data) {
		switch (requestCode) {
		case REQ_CREATE_PATTERN: {
			Log.d(TAG, "onActivityResult: " + resultCode);
			if (resultCode == RESULT_OK) {
				char[] pattern = data
						.getCharArrayExtra(LockPatternActivity.EXTRA_PATTERN);
				
				String newPattern ="";
				for(char c:pattern){
					Log.d(TAG, "pattern:" + String.format("%c", c));
					newPattern += String.format("%c", c);
				}
				
				this.getSharedPreferences("prefs", 0).edit()
						.putString("pattern", newPattern).commit();
				
				Log.d("@@@", "new pattern:"+newPattern);
				Crouton.makeText(this,
						this.getString(R.string.create_pattern_success),
						Style.ALERT).show();
			}
			break;
		}// REQ_CREATE_PATTERN
		}
	}
	
	private void showAlertDialog(){
		
		new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_info)
        .setTitle(this.getString(R.string.pwd_alert_title))		
        .setMessage(this.getString(R.string.pwd_alert_message))
        .setCancelable(false)
        .setPositiveButton(this.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               
            	createNewPattern(); 
            	dialog.dismiss();	
            }
        })
        .show();
		
	}
}
