package com.columbia.expander;

import com.columbia.expander.yelp.Business;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

// wrapper class for the food overlays. includes info about the business and location
public class FoodOverlay extends OverlayItem {
	
	private Business business;
	private YelpPoint user;

	public FoodOverlay(GeoPoint arg0, String arg1, String arg2, Business b, YelpPoint user) {
		super(arg0, arg1, arg2);
		this.business = b;
		this.user = user;
	}
	
	Business getBusiness() {
		return this.business;
	}
	
	YelpPoint getUserPoint() {
		return this.user;
	}

}
