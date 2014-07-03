package com.columbia.expander;

import java.util.HashMap;
import java.util.Map;

import android.os.Environment;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// class containing a bunch of global or setup stuff
public class YelpVals {
	
	public static final String QUICK_EATS = "food";
	public static final String RESTAURANTS = "restaurants";
	public static final String path = Environment.getExternalStorageDirectory().getPath() + "/Expander/";
	
    // keys to permit use w/ yelp API
	public final static String CONSUMER_KEY = "7YYMHElxhXMahrh9OL6q8w";
	public final static String CONSUMER_SECRET = "t2T2cRESzXoJsLu7GEiZJphYSws";
	public final static String TOKEN = "eRoH9wtondp7k-nVfYs0Rb7iRe7sS8v0";
	public final static String TOKEN_SECRET = "F_kRoyZHs3cwtUSXEQ4LsU4odJQ";
	public static Gson gson = new GsonBuilder().setFieldNamingPolicy(
			FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
	
    // map to hold places to eat that will populate the NSEW map
	public static Map<String,String> quickEatsMap = new HashMap<String,String>();
	public static Map<String,String> restaurantMap = new HashMap<String,String>();
	
    // populate map structure w/ food types
	public static void init() {
		for (int i = 0; i < quickEatsTypes.length; i += 2) {
			quickEatsMap.put(quickEatsTypes[i], quickEatsTypes[i+1]);
		}
		for (int i = 0; i < restaurantTypes.length; i += 2) {
			restaurantMap.put(restaurantTypes[i], restaurantTypes[i+1]);
		}
	}
	
    // yelp groups food into the following two lists. The former is for "quick eats", the other is for more traditional restaurants. Depending on the user's choice, one of the following will populate the map
	public static String[] quickEatsTypes = {
		"Bagels", "bagels",
		"Bakeries", "bakeries",
		"Coffee & Tea", "coffee",
		"Desserts", "desserts",
		"Ice Cream & Frozen Yogurt", "icecream",
		"Juice Bars & Smoothies", "juicebars"
	};
	
	public static String[] restaurantTypes = {
		"American (Traditional)", "tradamerican",
		"Burgers", "burgers",
		"Cafes", "cafes",
		"Caribbean", "caribbean",
		"Chinese", "chinese",
		"Delis", "delis",
		"Diners", "diners",
		"Indian", "indpak",
		"Italian", "italian",
		"Japanese", "japanese",
		"Mediterranean", "mediterranean",
		"Mexican", "mexican",
		"Middle Eastern", "mideastern",
		"Pizza", "pizza",
		"Seafood", "seafood",
		"Sushi", "sushi",
		"Vegan", "vegan",
		"Vegetarian", "vegetarian"
	};

}
