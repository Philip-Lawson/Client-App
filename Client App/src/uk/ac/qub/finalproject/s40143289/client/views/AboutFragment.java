/**
 * 
 */
package uk.ac.qub.finalproject.s40143289.client.views;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import uk.ac.qub.finalproject.client.implementations.Implementations;
import uk.ac.qub.finalproject.s40143289.client.views.R;

/**
 * This fragment creates the about page view within the main page. It creates
 * the on click listener that directs the user to the project website when they
 * click the donate button.
 * 
 * @author Phil
 *
 */
public class AboutFragment extends Fragment {

	private Button donateButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.about_page_fragment, container,
				false);
		donateButton = (Button) view
				.findViewById(R.string.about_page_donate_button_id);

		// if the implementer wants to have a donate page accessible
		// the donate button will be visible and will have an onClickListener
		// registered to it that will open a browser to their specified link
		if (Implementations.donateButtonEnabled()) {
			donateButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Uri donateURL = Uri.parse(Implementations
							.getProjectWebsite());
					Intent donateIntent = new Intent(Intent.ACTION_VIEW,
							donateURL);
					startActivity(donateIntent);
				}

			});

		} else {
			donateButton.setVisibility(View.GONE);
		}

		return view;
	}
}
