/**
 * 
 */
package uk.ac.qub.finalproject.client.views;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import uk.ac.qub.finalproject.client.views.R;

/**
 * @author Phil
 *
 */
@SuppressWarnings("deprecation")
public class MainPage extends ActionBarActivity {
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		getSupportActionBar().setTitle(R.string.account_button_title);
		getFragmentManager().beginTransaction()
		.replace(android.R.id.content, new MyAccountFragment())
		.commit();	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.string.settings_button_id:			
			getFragmentManager().beginTransaction()
					.replace(android.R.id.content, new SettingsFragment())
					.commit();		
			getSupportActionBar().setTitle(R.string.settings_button_title);
			break;
		case R.string.about_button_id:
			getSupportActionBar().setTitle(R.string.about_button_title);
			getFragmentManager().beginTransaction()
			.replace(android.R.id.content, new AboutFragment())
			.commit();
			break;
		case R.string.account_button_id:
			getSupportActionBar().setTitle(R.string.account_button_title);
			getFragmentManager().beginTransaction()
			.replace(android.R.id.content, new MyAccountFragment())
			.commit();		
			break;
		case R.string.delete_account_menu_button_id:
			getSupportActionBar().setTitle(R.string.delete_account_button_title);
			getFragmentManager().beginTransaction()
			.replace(android.R.id.content, new DeleteAccountFragment())
			.commit();	
			break;
			
		}

		return super.onOptionsItemSelected(item);
	}

}
