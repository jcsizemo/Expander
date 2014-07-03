package com.columbia.expander;

import java.util.List;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.columbia.expander.yelp.Business;
import com.columbia.expander.yelp.Coordinate;
import com.columbia.expander.yelp.YelpApi2;
import com.columbia.expander.yelp.YelpLocation;
import com.columbia.expander.yelp.YelpSearchResult;
import com.columbia.expander.yelp.YelpService;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;
import com.google.gson.JsonSyntaxException;

public class RestaurantMapActivity extends MapActivity {

	MapView mapView;
	List<Overlay> mapOverlays;
	YelpPoint userPoint;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resturant_map_layout);

		mapView = (MapView) findViewById(R.id.mapView); // get Map view
		mapOverlays = mapView.getOverlays();
		MapController mapController = mapView.getController();
		Projection projection = mapView.getProjection();
		mapView.setBuiltInZoomControls(true); // zoomable
		mapController.setZoom(14);	// zoom in relatively close

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable dUser = this.getResources().getDrawable(R.drawable.user);

		AddItemizedOverlay userOverlay = new AddItemizedOverlay(dUser, this);
		userPoint = getLocation();
		GeoPoint user = new GeoPoint((int) (userPoint.getLat() * 1E6),
				(int) (userPoint.getLon() * 1E6));
		mapController.animateTo(user);	// add user to overlay list, move map focus to location
		OverlayItem o = new OverlayItem(user, "Your location", null);
		userOverlay.addOverlay(o);
		mapOverlays.add(userOverlay); // repeat for restaurant locations
		userOverlay.populateNow();

		Intent i = getIntent();
		String type = i.getStringExtra("type");
		String genre = i.getStringExtra("genre");
		boolean isInstantQueue = i.getBooleanExtra("isInstantQueue", false);
		boolean isSuggestedForYou = i.getBooleanExtra("isSuggestedForYou", false);

		if (isInstantQueue) {
			populateMap(Profile.getInstantQueue());
		}
		else if (isSuggestedForYou) {
			populateMap(Profile.getSuggestedQueue());
		}
		else {
			if (type.equals(YelpVals.QUICK_EATS)) {
				genre = YelpVals.quickEatsMap.get(genre);
			} else if (type.equals(YelpVals.RESTAURANTS)) {
				genre = YelpVals.restaurantMap.get(genre);
			}
			new Yelp(YelpVals.CONSUMER_KEY, YelpVals.CONSUMER_SECRET,
					YelpVals.TOKEN, YelpVals.TOKEN_SECRET).execute(type,
					userPoint.getLat(), userPoint.getLon(), genre, "20");
		}
	}

	void populateMap(List<Business> businesses) {
		if (businesses == null) {
			Toast.makeText(this,
					"No businesses found nearby matching your criteria.",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Drawable dBusiness = this.getResources().getDrawable(
				R.drawable.boundary);
		Drawable dVisited = this.getResources().getDrawable(R.drawable.center);
		// two overlays for unvisited and visited restaurants
		AddItemizedOverlay businessOverlay = new AddItemizedOverlay(dBusiness,
				this);
		AddItemizedOverlay visitedOverlay = new AddItemizedOverlay(dVisited, this);
		for (Business b : businesses) {
			// parse out relevant info from returned list of businesses
			YelpLocation loc = b.getLocation();
			Coordinate c = loc.getCoordinate();
			GeoPoint gp = new GeoPoint((int) (c.getLatitude() * 1E6),
					(int) (c.getLongitude() * 1E6));
			String info = "Yelp Rating: " + b.getRating() + "\n"
					+ "# Reviews: " + b.getReviewCount() + "\n" + "Phone: "
					+ b.getPhone();
			FoodOverlay o = new FoodOverlay(gp, b.getName(), info, b, userPoint);
			
			if (Profile.visitedRecently(b)) visitedOverlay.addOverlay(o);
			else businessOverlay.addOverlay(o);
		}
		mapOverlays.add(businessOverlay);
		mapOverlays.add(visitedOverlay);
		businessOverlay.populateNow();
		visitedOverlay.populateNow();
		// refresh map
		mapView.invalidate();
	}

	YelpSearchResult parseResults(String results) {
		YelpSearchResult places = null;
		try {
			places = YelpVals.gson.fromJson(results, YelpSearchResult.class);
		} catch (JsonSyntaxException ex) {
			Log.e(YelpService.class.getName(),
					ex.getCause() + " : " + ex.getLocalizedMessage());
		}
		return places;
	}

	YelpPoint getLocation() {
		LocationManager locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationProvider locProvider = locMgr
				.getProvider(LocationManager.NETWORK_PROVIDER);
		String provider = locProvider.getName();
		Location location = locMgr.getLastKnownLocation(provider);
		double lon = location.getLongitude();
		double lat = location.getLatitude();
		return new YelpPoint(lat, lon);
	}

	public class Yelp extends AsyncTask<Object, Object, String> {

		OAuthService service;
		Token accessToken;

		public Yelp(String consumerKey, String consumerSecret, String token,
				String tokenSecret) {
			this.service = new ServiceBuilder().provider(YelpApi2.class)
					.apiKey(consumerKey).apiSecret(consumerSecret).build();
			this.accessToken = new Token(token, tokenSecret);
		}

        // build Yelp query
		@Override
		protected String doInBackground(Object... params) {
			String term = (String) params[0];
			Double latitude = (Double) params[1];
			Double longitude = (Double) params[2];
			String filter = (String) params[3];
			String numResults = (String) params[4];
			OAuthRequest request = new OAuthRequest(Verb.GET,
					"http://api.yelp.com/v2/search");
			request.addQuerystringParameter("term", term);
			request.addQuerystringParameter("limit", numResults);
			request.addQuerystringParameter("ll", latitude + "," + longitude);
			request.addQuerystringParameter("sort", "1");
			request.addQuerystringParameter("radius_filter", "2500");
			if (!filter.equals("")) {
				request.addQuerystringParameter("category_filter", filter);
			}
			this.service.signRequest(this.accessToken, request);
			Response response = request.send();
			return response.getBody();
		}

        // do Yelp call in background, parse the returned data,
        // populate the map
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			YelpSearchResult results = parseResults(result);
			populateMap(results.getBusinesses());
		}
	}
	

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
