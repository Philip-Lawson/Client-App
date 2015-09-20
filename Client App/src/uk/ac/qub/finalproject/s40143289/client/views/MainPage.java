/**
 * 
 */
package uk.ac.qub.finalproject.s40143289.client.views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import uk.ac.qub.finalproject.client.services.RunnableClientTemplate;
import uk.ac.qub.finalproject.s40143289.client.views.R;

/**
 * This is the main page of the app. It acts as a hub to show the various page
 * fragments that make up the UI.
 * 
 * @author Phil
 *
 */
@SuppressWarnings("deprecation")
public class MainPage extends ActionBarActivity {

	public static String DEREGISTRATION_SUCCESS = "Account deleted";
	private ProgressDialog deleteProgress;
	private ProgressDialog dataDeletionProgress;
	private Thread deleteAccountThread;

	/**
	 * This handler is registered with the deregistration networking thread. The
	 * thread calls handle message when deregistration process is complete.
	 */
	private final Handler accountDeletionHandler = new Handler(
			Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			unlockScreenOrientation();
			Bundle b = msg.getData();
			handleAccountDeletion(b.getBoolean(DEREGISTRATION_SUCCESS));
		}

	};

	/**
	 * The handler used to receive communication from the DeleteAccountRunnable.
	 */
	private final Handler dataDeletionHandler = new Handler(
			Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (dataDeletionProgress.isShowing()) {
				dataDeletionProgress.dismiss();
			}

			unlockScreenOrientation();
			showDeleteSuccessfulDialog();
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle(R.string.home_page_button_title);
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new HomePageFragment()).commit();
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
		case R.string.home_page_button_id:
			getSupportActionBar().setTitle(R.string.home_page_button_title);
			getFragmentManager().beginTransaction()
					.replace(android.R.id.content, new HomePageFragment())
					.commit();
			break;
		case R.string.delete_account_menu_button_id:
			showAreYouSureDialog();
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Makes a decision on which actions to take based on whether the account
	 * was deleted from the server or not.
	 * 
	 * @param success
	 */
	private void handleAccountDeletion(boolean success) {
		deleteProgress.dismiss();

		if (success) {
			showDataDeletionProgress();
			deleteAccountData();
		} else {
			showDeleteUnsuccessfulDialog();
		}

	}

	/**
	 * Shows a confirmation dialog to the user confirming that they wish to
	 * delete their account.
	 */
	private void showAreYouSureDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to delete your account?");
		builder.setPositiveButton("YES", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				startDelete();
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

	/**
	 * Shows a progress dialog informing the user that their account is being
	 * deleted from the server.
	 */
	private void showServerProgress() {
		deleteProgress = ProgressDialog.show(this, "Deleting Account",
				"Communicating with the server");
	}

	/**
	 * Shows a progress dialog informing the user that the system is deleting
	 * their app files.
	 */
	private void showDataDeletionProgress() {
		dataDeletionProgress = ProgressDialog.show(this, "Deleting App Files",
				"Deleting work packets");
	}

	/**
	 * Shows a dialog box informing the user that their account was successfully
	 * deleted.
	 */
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

	/**
	 * Shows a dialog box informing the user that their details were not deleted
	 * from the server.
	 */
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

	private void startDelete() {
		lockScreenOrientation();
		RunnableClientTemplate deregisterClient = new DeregisterUserThread(
				getBaseContext(), accountDeletionHandler);
		deleteAccountThread = new Thread(deregisterClient);
		deleteAccountThread.setDaemon(true);
		deleteAccountThread.start();
		showServerProgress();
	}

	/*
	 * Helper method to lock the screen orientation to avoid cancelling a thread
	 * triggered from the UI thread.
	 */
	private void lockScreenOrientation() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
	}

	/**
	 * Helper method to unlock the screen when a background thread has finished
	 * running.
	 */
	private void unlockScreenOrientation() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
	}

	/**
	 * Moves the user to the register page. This should only be called if the
	 * user has deleted their account.
	 */
	private void moveToRegisterPage() {
		Intent moveToRegisterPage = new Intent(this, RegisterPage.class);
		startActivity(moveToRegisterPage);
		finish();
	}

	/**
	 * starts the thread that deletes all of the user's account data.
	 */
	private void deleteAccountData() {
		lockScreenOrientation();
		DeleteAccountRunnable dataDeleterRunnable = new DeleteAccountRunnable(
				this, dataDeletionHandler);
		Thread dataDeleterThread = new Thread(dataDeleterRunnable);
		dataDeleterThread.setDaemon(true);
		dataDeleterThread.start();
	}

}
