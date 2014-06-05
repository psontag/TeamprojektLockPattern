package com.teamprojekt.lockpattern;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.haibison.android.lockpattern.LockPatternActivity;
import com.haibison.android.lockpattern.util.Settings;
import com.haibison.android.lockpattern.widget.LockPatternUtils;
import com.haibison.android.lockpattern.widget.LockPatternView;
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
	    
	/**
	 * Menu method to change grid size of matrix.
	 * @param item
	 */
	public void changeGridSize(MenuItem item){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.choose_grid_size)
			   .setItems(R.array.grid_size, new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int which) {
					   // The 'which' argument contains the index position
					   // of the selected item
					   switch(which){
					   //set width to 3x3
					   case 0:
						   status_message.setText("Achtung! 3x3 bietet zu wenig Kombinationsmöglichkeiten, "
						   		+ "um ein sicheres Muster zu erstellen.");
						   LockPatternView.setMatrixWidth(3);
						   break;
					   //set width to 4x4
					   case 1:
						   status_message.setText("");
						   LockPatternView.setMatrixWidth(4);
						   break;
					   //set width to 5x5
					   case 2:
						   status_message.setText("");
						   LockPatternView.setMatrixWidth(5);
						   break;
					   }
	               	}
	        });
	        builder.create();
	        builder.show();
	    }
	
	/**
	 * Menu method to set stealth mode.
	 * @param item
	 */
    public void setStealthMode(MenuItem item) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(R.string.stealth_mode)
    		   .setItems(R.array.choose_stealth_mode, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
			    	switch (which) {
			    	case 0:
			    		setPatternStealthMode(true);
			    		break;
			    	case 1:
			    		setPatternStealthMode(false);
			    		break;
			    	}
				}

    		   });
    	builder.create();
    	builder.show();
    }

    /**
     * Wrapper method, to change the stealth mode property for LockPattern.
     * @param stealthMode - true for stealth.
     */
    protected void setPatternStealthMode(boolean stealthMode) {
    	Settings.Display.setStealthMode(this, stealthMode);
    }
    
    protected void setStatusMessage(String message) {
    	this.status_message.setText(message);
    }
    
	public void createPattern(View view) {
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
