package com.easy.app.chatmessagerlock;

import group.pals.android.lib.ui.lockpattern.LockPatternActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class EmptyActivity extends Activity {
	static  boolean bFlag = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bFlag = false;
		callLock();
	}
	
	private void callLock(){
		String pattern = this.getSharedPreferences("prefs", 0).getString("pattern", "no");
		Log.d("@@@", "old pattern:"+pattern);
		
		char[] c = new char[pattern.length()];
		for(int i=0;i<pattern.length();i++){
			c[i] = pattern.charAt(i);
		}
		
		Log.d("@@@", "chararray pattern:"+c);
		
		Intent intent = new Intent(LockPatternActivity.ACTION_COMPARE_PATTERN, null,
		        this, LockPatternActivity.class);
		intent.putExtra(LockPatternActivity.EXTRA_PATTERN, c);
		startActivityForResult(intent, 2);
	
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		
	
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	        Intent data) {
		
		Log.d("@@@", "onActivityResult:"+requestCode+","+resultCode);
	    switch (requestCode) {
	    case 2: {
	        /*
	         * NOTE that there are 4 possible result codes!!!
	         */
	        switch (resultCode) {
	        case RESULT_OK:
	            // The user passed
	        	bFlag = true;
	        	finish();
	            break;
	        case RESULT_CANCELED:
	            // The user cancelled the task
	        	//callLock();
	        	finish();
	            break;
	        case LockPatternActivity.RESULT_FAILED:
	            // The user failed to enter the pattern
	        	callLock();
	            break;
	        case LockPatternActivity.RESULT_FORGOT_PATTERN:
	            // The user forgot the pattern and invoked your recovery Activity.
	            break;
	        }

	        /*
	         * In any case, there's always a key EXTRA_RETRY_COUNT, which holds
	         * the number of tries that the user did.
	         */
	        int retryCount = data.getIntExtra(
	                LockPatternActivity.EXTRA_RETRY_COUNT, 0);

	        break;
	    }// REQ_ENTER_PATTERN
	    }
	}
}
