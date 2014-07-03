package com.columbia.expander;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ChoicesActivity extends ListActivity {

	TextView textView;
	String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent i = getIntent();
		type = i.getStringExtra("type");
		
		List<String> labels = new ArrayList<String>();
		
		// populate list based on choice in previous screen
		if (type.equals(YelpVals.QUICK_EATS)) {
			labels.addAll(YelpVals.quickEatsMap.keySet());
		}
		else if (type.equals(YelpVals.RESTAURANTS)) {
			labels.addAll(YelpVals.restaurantMap.keySet());
		}
		
		// sort alphabetically
		Collections.sort((List<String>)labels, new Comparator<String>() {
			@Override
			public int compare(String lhs, String rhs) {
				if (lhs.compareTo(rhs) <= 0) return -1;
				else return 1;
			}
		});
		
		this.setListAdapter(new ArrayAdapter<Object>(this,
				android.R.layout.simple_list_item_1, labels.toArray()));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// open map based on choice
		TextView tv = (TextView) v;
		Intent i = new Intent(this, RestaurantMapActivity.class);
		i.putExtra("type", type);
		i.putExtra("genre", tv.getText());
		startActivity(i);
	}
}
