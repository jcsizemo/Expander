package com.columbia.expander;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.MotionEvent;
import android.widget.Toast;

import com.columbia.expander.yelp.Business;
import com.columbia.expander.yelp.Coordinate;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

/**
 * Class used to place marker or any overlay items on Map
 * */
public class AddItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();

	private Context context;

	public AddItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public AddItemizedOverlay(Drawable defaultMarker, Context context) {
		this(defaultMarker);
		this.context = context;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {

		if (event.getAction() == 1) {
			GeoPoint geopoint = mapView.getProjection().fromPixels(
					(int) event.getX(), (int) event.getY());
			// latitude
			double lat = geopoint.getLatitudeE6() / 1E6;
			// longitude
			double lon = geopoint.getLongitudeE6() / 1E6;
			// uncomment to display coordinates
			// Toast.makeText(context, "Lat: " + lat + ", Lon: "+lon,
			// Toast.LENGTH_SHORT).show();
		}
		return false;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mapOverlays.get(i);
	}

	@Override
	public int size() {
		return mapOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = mapOverlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(this.context);
		// get business name and brief description
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
        
		dialog.setPositiveButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
            
				});
		if (!item.getTitle().endsWith("Your location")) {
			final FoodOverlay foodOverlay = (FoodOverlay) item;
            
            // fire up Google Maps to get directions to the restaurant
			dialog.setNeutralButton("Get Directions",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Business b = foodOverlay.getBusiness();
							Coordinate c = b.getLocation().getCoordinate();
							YelpPoint user = foodOverlay.getUserPoint();
							Intent i = new Intent(
									android.content.Intent.ACTION_VIEW,
									Uri.parse("http://maps.google.com/maps?saddr="
											+ user.getLat()
											+ ","
											+ user.getLon()
											+ "&daddr="
											+ c.getLatitude()
											+ ","
											+ c.getLongitude()));
							context.startActivity(i);
						}
					});
            
            // add/delete to instant queue
			
			boolean inInstantQueue = Profile.inInstantQueue(foodOverlay.getBusiness());
			String addOrRemove = inInstantQueue ? "Remove From" : "Add To";
			
			dialog.setNegativeButton(addOrRemove + " Instant Queue",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Business b = foodOverlay.getBusiness();
							if (Profile.inInstantQueue(foodOverlay.getBusiness())) {
								Profile.removeFromInstantQueue(b);
								Toast.makeText(context, "Removed from instant queue.", Toast.LENGTH_SHORT).show();
							}
							else {
								Profile.addToInstantQueue(b);
								Toast.makeText(context, "Added to instant queue.", Toast.LENGTH_SHORT).show();
							}
						}
					});
		}
		dialog.show();
		return true;
	}

	// add to map display
	public void addOverlay(OverlayItem overlay) {
		mapOverlays.add(overlay);
	}

	public void populateNow() {
		this.populate();
	}

}