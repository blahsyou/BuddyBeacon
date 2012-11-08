package com.buddy.beacon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setting the content view
		setContentView(R.layout.mainmenu);

		// Initializing my buttons and "registering" them
		Button StartButton = (Button) findViewById(R.id.main_start_button);
		Button FriendsButton = (Button) findViewById(R.id.main_friends_button);
		Button EventsButton = (Button) findViewById(R.id.main_events_button);
		Button SettingsButton = (Button) findViewById(R.id.main_settings_button);

		// Setting the listener to "this" (StartActivity)
		FriendsButton.setOnClickListener(this);
		StartButton.setOnClickListener(this);
		EventsButton.setOnClickListener(this);
		SettingsButton.setOnClickListener(this);
	}

	// When a button is clicked, do this.
	public void onClick(View v) {
		Context context = getApplicationContext();
		LocationManager locationManager;

		// At this point, we don't know which button was clicked, just that one was clicked.
		
		// check to see which one here
		if (v.getId() == R.id.main_start_button) {
			
			// ********* This checks to see if your phone will allow our app to use location data (GPS) *********
			locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				
				// John, here's some working alert dialog code
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Location Services Not Enabled."); // GPS not found
				
				// sets a little picture in the top left corner of the dialog
				builder.setIcon(R.drawable.ic_dialog_alert);
				
				// setting the message
				builder.setMessage("This app won't work without location services activated. Turn them on to continue."); // Want to enable?
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					// this is fired when the "Yes" button is hit
					public void onClick(DialogInterface dialogInterface, int i) {
						// This sends the user to the location settings page.
						startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					}

				});
				// User can't just cancel out of this dialog
				builder.setCancelable(false);
				
				builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					// this is fired when the "No" button is hit
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						finish();
					}
				});
				// Building the dialog and then showing it on the screen
				builder.create();
				builder.show();
				// end of dialog code
				// ********* End of Location Code *********	
			} else {
			startActivity(new Intent(context, Map.class));
			}

		// If it was the friends button that was clicked on the start page...
		} else if (v.getId() == R.id.main_friends_button) {
			// go to the friends page
			startActivity(new Intent(this, ManageFriends.class));
	    // etc....
		} else if (v.getId() == R.id.main_events_button) {
			
			startActivity(new Intent(context, Events.class));
	
		} else if (v.getId() == R.id.main_settings_button) {
			
			startActivity(new Intent(context, Settings.class));
		
		}
	}

}
