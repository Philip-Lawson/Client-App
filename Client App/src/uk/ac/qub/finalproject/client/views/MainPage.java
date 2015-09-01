/**
 * 
 */
package uk.ac.qub.finalproject.client.views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import uk.ac.qub.finalproject.client.services.RunnableClientTemplate;
import uk.ac.qub.finalproject.client.views.R;

/**
 * @author Phil
 *
 */
@SuppressWarnings("deprecation")
public class MainPage extends ActionBarActivity {

	public static String DEREGISTRATION_SUCCESS = "Account deleted";
	private ProgressDialog deleteProgress;
	private ProgressDialog dataDeletionProgress;

	/**
	 * This handler is registered with the deregistration thread. The thread
	 * calls handle message when deregistration process is complete.
	 */
	private final Handler accountDeletionHandler = new Handler(
			Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle b = msg.getData();
			handleAccountDeletion(b.getBoolean(DEREGISTRATION_SUCCESS));
		}

	};

	private final Handler dataDeletionHandler = new Handler(
			Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (dataDeletionProgress.isShowing()) {
				dataDeletionProgress.dismiss();
			}
			
			showDeleteSuccessfulDialog();
		}

	};

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
			showAreYouSureDialog();
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	private void handleAccountDeletion(boolean success) {
		deleteProgress.dismiss();

		if (success) {
			showDataDeletionProgress();
			deleteAccountData();			
		} else {
			showDeleteUnsuccessfulDialog();
		}

	}

	private void showAreYouSureDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to delete your account?");
		builder.setPositiveButton("YES", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				RunnableClientTemplate deregisterClient = new DeregisterUserThread(
						getApplicationContext(), accountDeletionHandler);
				Thread deleteAccountThread = new Thread(deregisterClient);
				deleteAccountThread.setDaemon(true);
				deleteAccountThread.start();
				showServerProgress();
			}

		});

		builder.setNegativeButton("NO", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.show();

	}

	private void showServerProgress() {
		deleteProgress = ProgressDialog.show(this, "Deleting Account",
				"Communicating with the server");
	}

	private void showDataDeletionProgress() {
		dataDeletionProgress = ProgressDialog.show(this, "Deleting App Files",
				"Deleting work packets");
	}

	private void showDeleteSuccessfulDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Account Successfully Deleted");
		builder.setMessage("Thank you for contributing to our project!");
		builder.setNeutralButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				moveToRegisterPage();
			}

		});

		builder.show();
	}

	private void showDeleteUnsuccessfulDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Delete Unsuccessful");
		builder.setMessage("Please try again later");
		builder.setNeutralButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});

		builder.show();
	}

	private void moveToRegisterPage() {
		Intent moveToRegisterPage = new Intent(this, MainActivity.class);
		startActivity(moveToRegisterPage);
		finish();
	}

	private void deleteAccountData() {
		DeleteAccountRunnable dataDeleterRunnable = new DeleteAccountRunnable(
				this, dataDeletionHandler);
		Thread dataDeleterThread = new Thread(dataDeleterRunnable);
		dataDeleterThread.setDaemon(true);
		dataDeleterThread.start();
	}

}
