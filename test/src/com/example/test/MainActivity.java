package com.example.test;

import com.haibison.android.lockpattern.LockPatternActivity;
import com.haibison.android.lockpattern.util.Settings;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private static final int REQ_CREATE_PATTERN = 1;
	private final static int REQ_ENTER_PATTERN = 2;
	private char[] userPattern;
	
	private TextView txvKonsoleTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.txvKonsoleTV = (TextView) findViewById(R.id.KonsoleTV);
//		try {
//			this.createPackageContext("myservice", REQ_CREATE_PATTERN);
//		} catch (NameNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		this.txvKonsoleTV.append("TEST1\n ");
		Intent intent = new Intent(LockPatternActivity.ACTION_CREATE_PATTERN, null, this, LockPatternActivity.class);
		startActivityForResult(intent, REQ_CREATE_PATTERN);
		Settings.Security.setAutoSavePattern(this, true);
		this.txvKonsoleTV.append("TEST2\n ");
		
//		onActivityResult(REQ_CREATE_PATTERN, 1, intent);
		
		char[] savedPattern = {' ',' ',' '};

		Intent intent2 = new Intent(LockPatternActivity.ACTION_COMPARE_PATTERN, null, this, LockPatternActivity.class);
		intent2.putExtra(LockPatternActivity.EXTRA_PATTERN, userPattern);//savedPattern);
		startActivityForResult(intent2, REQ_ENTER_PATTERN);
//		onActivityResult(REQ_ENTER_PATTERN, LockPatternActivity.RESULT_OK, intent2);//this.getApplicationContext();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		this.txvKonsoleTV.append("KKKKK");
		switch (requestCode) {
		case REQ_CREATE_PATTERN: {
			this.txvKonsoleTV.append("" + resultCode);
			if (resultCode == RESULT_OK) {
				char[] pattern = data.getCharArrayExtra(LockPatternActivity.EXTRA_PATTERN);
				System.out.println(pattern.toString());
				userPattern = pattern;
			}
			break;
		}
		 case REQ_ENTER_PATTERN: {
		        /*
		         * NOTE that there are 4 possible result codes!!!
		         */
		        switch (resultCode) {
		        case RESULT_OK:
		    		this.txvKonsoleTV.append("Geschafft!\n ");
		            break;
		        case RESULT_CANCELED:
		        	this.txvKonsoleTV.append("Abgebrochen\n ");
		            break;
		        case LockPatternActivity.RESULT_FAILED:
		        	this.txvKonsoleTV.append("Falsch!\n ");
		            break;
		        case LockPatternActivity.RESULT_FORGOT_PATTERN:
		        	this.txvKonsoleTV.append("Streng dich an und denk nochmal nach!\n ");
		            break;
		        }

		        /*
		         * In any case, there's always a key EXTRA_RETRY_COUNT, which holds
		         * the number of tries that the user did.
		         */
		        int retryCount = data.getIntExtra(
		                LockPatternActivity.EXTRA_RETRY_COUNT, 0);
		        this.txvKonsoleTV.append("#Versuche: " + retryCount + "\n ");
		        break;
		 }
		}
	}
	
	

}
