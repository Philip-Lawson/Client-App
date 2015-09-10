package uk.ac.qub.finalproject.client.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import uk.ac.qub.finalproject.client.views.R;

/**
 * This dialog fragment shows a message to the user indicating that the email
 * they have entered is invalid. The dialog is closed when the 'OK' button is
 * clicked. This should be used to maintain a consistent behaviour across the
 * app.
 * 
 * @author Phil
 *
 */
public class InvalidEmailDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.invalid_email_text);
		builder.setTitle(R.string.invalid_email_title);
		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		return builder.create();
	}
}
