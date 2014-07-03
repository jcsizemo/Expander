package com.columbia.expander;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.columbia.expander.yelp.Business;
import com.columbia.expander.yelp.YelpApi2;
import com.columbia.expander.yelp.YelpSearchResult;
import com.columbia.expander.yelp.YelpService;
import com.google.gson.JsonSyntaxException;

public class AlarmService extends Service {
	
	YelpPoint point;
    float acc;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		YelpPoint newPoint = (YelpPoint) intent.getSerializableExtra("point");
        // all locations from LocationManager have an accuracy
        acc = intent.getFloatExtra("acc",100.0);
		updatePoint(newPoint);
		return Service.START_STICKY;
	}
	
	void updatePoint(YelpPoint p) {
        /* if the point is accurate. can tweak this to include values from the
        LocationManager. This problem is tricky given that most of the
        buildings in NYC are very close together. */
        
//      if ((null == point) || !inBounds(point,p)) point = p;
//      else {
//          // execute if the new gathered point is actually accurate
//			new Yelp(YelpVals.CONSUMER_KEY, YelpVals.CONSUMER_SECRET, YelpVals.TOKEN, YelpVals.TOKEN_SECRET).execute("food", p.getLat(), p.getLon());
//      }
        
        if ((null == point) || (acc > 50)) point = p;
        else {
            new Yelp(YelpVals.CONSUMER_KEY, YelpVals.CONSUMER_SECRET, YelpVals.TOKEN, YelpVals.TOKEN_SECRET).execute("food", p.getLat(),p.getLon());
        }
	}

	boolean inBounds(YelpPoint currentPoint, YelpPoint newPoint) {
		double curLat = currentPoint.getLat();
		double curLon = currentPoint.getLon();
		double newLat = newPoint.getLat();
		double newLon = newPoint.getLon();
		double dLat = Math.abs(curLat - newLat);
		double dLon = Math.abs(curLon - newLon);
		if ((dLat < 0.01) && (dLon < 0.01)) return true;
        return false;
	}

	YelpSearchResult parseResults(String results) {
		YelpSearchResult places = null;
		try {
			// gather info from JSON string returned from Yelp
			places = YelpVals.gson.fromJson(results, YelpSearchResult.class);
		} catch (JsonSyntaxException ex) {
			Log.e(YelpService.class.getName(),
					ex.getCause() + " : " + ex.getLocalizedMessage());
		}
		return places;
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

		@Override
		protected String doInBackground(Object... params) {
			// type of cuisine and location get passed in to Yelp call
			String term = (String) params[0];
			Double latitude = (Double) params[1];
			Double longitude = (Double) params[2];
			OAuthRequest request = new OAuthRequest(Verb.GET,
					"http://api.yelp.com/v2/search");
			// type of food, # to return, location, by nearest, within 25 miles
			request.addQuerystringParameter("term", term);
			request.addQuerystringParameter("limit", "15");
			request.addQuerystringParameter("ll", latitude + "," + longitude);
			request.addQuerystringParameter("sort", "1");
			request.addQuerystringParameter("radius_filter", "25");
			this.service.signRequest(this.accessToken, request);
			Response response = request.send();
			return response.getBody();
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			YelpSearchResult yelpResults = parseResults(result);
			int resultSize = yelpResults.getBusinesses().size();
			Business currentBusiness;
			if (resultSize > 0) {
				// get nearest, update profile history.
				// add the business to "recently visited" and update
				// hits on cuisine types for machine learning purposes
				currentBusiness = yelpResults.getBusinesses().get(0);
				Profile.updateHistory(currentBusiness);
			}
		}
	}
}
