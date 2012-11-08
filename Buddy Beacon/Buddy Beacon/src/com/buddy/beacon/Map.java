package com.buddy.beacon;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.buddy.beacon.MyLocation.LocationResult;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class Map extends MapActivity {

	private MapController control;
	public GeoPoint user_location;
	MapView view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		view = (MapView) findViewById(R.id.themap);

		view.setBuiltInZoomControls(true);
		
		control = view.getController();
		
		// this will create a LocationResult object, then pass it into the MyLocation.getLocation() method
		LocationResult locationResult = new LocationResult(){
		    @Override
		    public void gotLocation(Location location){
		        user_location = new GeoPoint((int)location.getLatitude(), (int)location.getLongitude());
		        Add_Overlay("me", user_location);
		        control.animateTo(user_location);
		    }
		};
		MyLocation myLocation = new MyLocation();
		myLocation.getLocation(this, locationResult);
	}

	public void Add_Overlay(String pin, GeoPoint location) {
		List<Overlay> mapOverlays = view.getOverlays();

		if (pin.contains("me")) {
			Drawable image = getResources().getDrawable(R.drawable.you_pin);
			MapOverlay overlay = new MapOverlay(image, this);

			OverlayItem overlayitem = new OverlayItem(location, "My Location", "This is my current position");

			overlay.addOverlay(overlayitem);
			mapOverlays.add(overlay);
			
		} else if (pin.contains("friend")) {
			Drawable image = getResources().getDrawable(R.drawable.friend_pin);
			MapOverlay overlay = new MapOverlay(image, this);
															// I'd like for it to show the friends name, pic, etc.
															// don't have that ready yet...
			OverlayItem overlayitem = new OverlayItem(location, "Friend's Location", "This is a location of a friend");

			overlay.addOverlay(overlayitem);
			mapOverlays.add(overlay);
			
		} else if (pin.contains("event")) {
			Drawable image = getResources().getDrawable(R.drawable.event_pin);
			MapOverlay overlay = new MapOverlay(image, this);

			OverlayItem overlayitem = new OverlayItem(location, "Event Location", "This is where an event will be held");

			overlay.addOverlay(overlayitem);
			mapOverlays.add(overlay);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_Manage_Friends) {
			Intent intent = new Intent(this, ManageFriends.class);
			startActivity(intent);

		} else if (item.getItemId() == R.id.menu_Manage_Events) {
			Intent intent = new Intent(this, Events.class);
			startActivity(intent);

		} else if (item.getItemId() == R.id.menu_view_satellite) {
			view.setSatellite(true);
		} else if (item.getItemId() == R.id.menu_view_street) {
			view.setSatellite(false);
		}

		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_map, menu);
		return true;
	}
}
