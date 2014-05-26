package com.teamprojekt.lockpattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.haibison.android.lockpattern.LockPatternActivity;
import com.haibison.android.lockpattern.util.Settings;
public class MainActivity extends Activity {
	private static final int REQ_CREATE_PATTERN = 1;
	private final static int REQ_ENTER_PATTERN = 2;
	private char[] userPattern;
	private TextView status_message;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.status_message = (TextView) findViewById(R.id.status_message);
	}
	public void createPattern(View view){
			Intent intentCreatePattern = new Intent(LockPatternActivity.ACTION_CREATE_PATTERN, null, this, LockPatternActivity.class);
			startActivityForResult(intentCreatePattern, REQ_CREATE_PATTERN);
			Settings.Security.setAutoSavePattern(this, true);
	}
	
	public void enterPattern(View view){
		Intent intentEnterPattern = new Intent(LockPatternActivity.ACTION_COMPARE_PATTERN, null, this, LockPatternActivity.class);
		intentEnterPattern.putExtra(LockPatternActivity.EXTRA_PATTERN, userPattern);
		startActivityForResult(intentEnterPattern, REQ_ENTER_PATTERN);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQ_CREATE_PATTERN: {

			if (resultCode == RESULT_OK) {
				char[] pattern = data.getCharArrayExtra(LockPatternActivity.EXTRA_PATTERN);
				userPattern = pattern;
				this.status_message.setText("Pattern wurde gespeichert :)");
			}
			break;
		}
		case REQ_ENTER_PATTERN: {
		    /*
		     * NOTE that there are 4 possible result codes!!!
		     */
			switch (resultCode) {
		        case RESULT_OK:
		        	this.status_message.setText("Pattern wurde akzeptiert :)");
		            break;
		        case RESULT_CANCELED:
		        
		            break;
		        case LockPatternActivity.RESULT_FAILED:
		        	this.status_message.setText("Pattern wurde nicht akzeptiert :(");
		            break;
		        case LockPatternActivity.RESULT_FORGOT_PATTERN:

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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
