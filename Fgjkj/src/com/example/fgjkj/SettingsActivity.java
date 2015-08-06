/**
 * 
 */
package com.example.fgjkj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * @author Phil
 *
 */
public class SettingsActivity extends ActionBarActivity {
	
	Button saveSettingsButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment()).commit();
		
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.		
		return true;
	}
		

}
