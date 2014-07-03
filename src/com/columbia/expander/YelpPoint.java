package com.columbia.expander;

import java.io.Serializable;

// location class, can be passed between activities. unnecessary but looks better in some areas.
// in future versions will probably get rid of this and just pass around two ints
public class YelpPoint implements Serializable {
	
	private double lat;
	private double lon;
	
	public YelpPoint(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}
	
	public double getLon() {
		return this.lon;
	}
	
	public double getLat() {
		return this.lat;
	}

	@Override
	public String toString() {
		return this.lat + "," + this.lon;
	}

}
