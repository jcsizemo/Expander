package com.columbia.expander;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenuActivity extends Activity implements SurfaceHolder.Callback {

	SurfaceView colorView;
	SurfaceHolder holder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		YelpVals.init();
		
//		File file = new File(YelpVals.path);
//		file.mkdirs();
		
		// enable location gathering stuff
		LocationManager locMgr = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		LocationProvider locProvider = locMgr
				.getProvider(LocationManager.NETWORK_PROVIDER);
		String provider = locProvider.getName();
		Location location = locMgr
				.getLastKnownLocation(provider);
		if (null == location) {
			Toast.makeText(
					this,
					"Cannot acquire location. Are your location settings enabled?",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		// create alarm that functions as location "heartbeat" and gathers nearby restaurant data
		Alarm alarm = new Alarm();
		alarm.setAlarm(this);
		setContentView(R.layout.main_menu_layout);
		
		// set up color panel
		colorView = (SurfaceView) findViewById(R.id.colorView);
		holder = colorView.getHolder();
		holder.addCallback(this);
		
		// set color and personality info
		TextView colorView = (TextView) findViewById(R.id.colorTextView);
		colorView.setText(Profile.getPersonalityText());
	}
	
	public void onClick(View v) {
		// based on choice, set either restaurants or "quick eats"
		Intent i = new Intent(this, ChoicesActivity.class);
		if (v.getId() == R.id.restaurantsButton) {
			i.putExtra("type", YelpVals.RESTAURANTS);
		}
		else if (v.getId() == R.id.quickEatsButton) {
			i.putExtra("type", YelpVals.QUICK_EATS);
		}
		startActivity(i);
	}
	
	// go to suggested or instant queue screen
	public void startSuggestedForYou(View v) {
		Intent i = new Intent(this, RestaurantMapActivity.class);
		i.putExtra("isSuggestedForYou", true);
		startActivity(i);
	}
	
	public void startInstantQueue(View v) {
		Intent i = new Intent(this, RestaurantMapActivity.class);
		i.putExtra("isInstantQueue", true);
		startActivity(i);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// set color, called when screen starts or if orientation changes
		Canvas canvas = holder.lockCanvas();
		Paint paint = new Paint();
		canvas.drawPaint(Profile.getPersonalityColor());
		holder.unlockCanvasAndPost(canvas);
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}
}
