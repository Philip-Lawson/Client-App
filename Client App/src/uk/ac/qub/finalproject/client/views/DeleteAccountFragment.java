/**
 * 
 */
package uk.ac.qub.finalproject.client.views;
import uk.ac.qub.finalproject.client.services.RunnableClientTemplate;
import uk.ac.qub.finalproject.client.views.R;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * @author Phil
 *
 */
public class DeleteAccountFragment extends Fragment {

	public static String DEREGISTRATION_SUCCESS = "Account deleted";	

	private Button deleteAccountButton;
	private ProgressDialog deleteProgress;	

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
	
	private final Handler dataDeletionHandler = new Handler(Looper.getMainLooper()){@Override
		public void handleMessage(Message msg) {
		super.handleMessage(msg);
		deleteProgress.dismiss();
		moveToRegisterPage();		
	}

};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.delete_account_fragment,
				container, false);
		setupDeleteAccountButton(view);
		return view;
	}

	private void setupDeleteAccountButton(View view) {
		deleteAccountButton = (Button) view.findViewById(R.string.delete_account_button_id);
		deleteAccountButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				showAreYouSureDialog();
			}
		});
	}

	private void handleAccountDeletion(boolean success) {
		if (deleteProgress.isShowing()) {
			deleteProgress.dismiss();
		}

		if (success) {
			showDeleteSuccessfulDialog();
		} else {
			showDeleteUnsuccessfulDialog();
		}

	}

	private void showAreYouSureDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Account Successfully Deleted");
		builder.setPositiveButton("YES", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				RunnableClientTemplate deregisterClient = new DeregisterUserThread(
						getActivity(), accountDeletionHandler);
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
		deleteProgress = ProgressDialog.show(getActivity(), "Deleting Account",
				"Communicating with the server");
	}

	private void showDeleteSuccessfulDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Account Successfully Deleted");
		builder.setNeutralButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				deleteAccountData();
				
			}

		});

		builder.show();
	}

	private void showDeleteUnsuccessfulDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
		Intent moveToRegisterPage = new Intent(getActivity(),
				MainActivity.class);
		getActivity().startActivity(moveToRegisterPage);
		getActivity().finish();
	}

	private void deleteAccountData() {
		Context context = getActivity();
		DeleteAccountRunnable dataDeleterRunnable = new DeleteAccountRunnable(context, dataDeletionHandler);
		Thread dataDeleterThread = new Thread(dataDeleterRunnable);
		dataDeleterThread.setDaemon(true);
		dataDeleterThread.start();		
		showDeletionProgress();
		
		
	}

	private void showDeletionProgress() {
		deleteProgress = ProgressDialog.show(getActivity(), "Deleting User Data",
				"Please wait while the app data is deleted");
	}

	

}
