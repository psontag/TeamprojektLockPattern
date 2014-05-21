package com.example.test;

import com.haibison.android.lockpattern.LockPatternActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	
	private static final int REQ_CREATE_PATTERN = 1;
	private final static int REQ_ENTER_PATTERN = 2;
	private char[] userPattern;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		try {
//			this.createPackageContext("myservice", REQ_CREATE_PATTERN);
//		} catch (NameNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Intent intent = new Intent(LockPatternActivity.ACTION_CREATE_PATTERN, null, this.getBaseContext(), LockPatternActivity.class);
		startActivityForResult(intent, REQ_CREATE_PATTERN);
		
		char[] savedPattern = {' ',' '};

		Intent intent2 = new Intent(LockPatternActivity.ACTION_COMPARE_PATTERN, null, this.getBaseContext(), LockPatternActivity.class);
		intent2.putExtra(LockPatternActivity.EXTRA_PATTERN, userPattern);//savedPattern);
		startActivityForResult(intent2, REQ_ENTER_PATTERN);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("KKKKK");
		switch (requestCode) {
		case REQ_CREATE_PATTERN: {
			System.out.println(resultCode);
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
		            System.out.println("Geschafft!");
		            break;
		        case RESULT_CANCELED:
		            System.out.println("Abgebrochen");
		            break;
		        case LockPatternActivity.RESULT_FAILED:
		            System.out.println("Falsch!");
		            break;
		        case LockPatternActivity.RESULT_FORGOT_PATTERN:
		            System.out.println("Streng dich an und denk nochmal nach!");
		            break;
		        }

		        /*
		         * In any case, there's always a key EXTRA_RETRY_COUNT, which holds
		         * the number of tries that the user did.
		         */
		        int retryCount = data.getIntExtra(
		                LockPatternActivity.EXTRA_RETRY_COUNT, 0);

		        break;
		 }
		}
	}
	
	

}
