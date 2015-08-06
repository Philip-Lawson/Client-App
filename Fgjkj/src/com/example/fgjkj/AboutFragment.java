/**
 * 
 */
package com.example.fgjkj;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;

/**
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
		donateButton = (Button) view.findViewById(R.string.about_page_donate_button_id);
		donateButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Uri donateURL = Uri.parse("http://oxfam.org.uk"); 
				Intent donateIntent = new Intent(Intent.ACTION_VIEW, donateURL);
				startActivity(donateIntent);				
			}
			
		});
		
		return view;
	}
}
