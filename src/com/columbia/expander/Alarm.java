package com.columbia.expander;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver {
	
//	private boolean resetWifi = false;

    // This method executes when the alarm is triggered
	@Override
	public void onReceive(Context context, Intent intent) {
		
		// keep the phone on
		// PowerManager pm = (PowerManager) context
		// .getSystemService(Context.POWER_SERVICE);
		// PowerManager.WakeLock wl = pm.newWakeLock(
		// PowerManager.PARTIAL_WAKE_LOCK, "");
		// wl.acquire();
		
		// set WIFI on. Can aid in location tracking
//		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//		if (!wifi.isWifiEnabled()) resetWifi = true;
//		
//		if (resetWifi) {
//			wifi.setWifiEnabled(true);
//		}

		LocationManager locMgr = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
        // pull location from nearby network sources. More accurate. Can also use the GPS_PROVIDER
		LocationProvider locProvider = locMgr
				.getProvider(LocationManager.NETWORK_PROVIDER);
		
		String provider = locProvider.getName();
        // get user's approx. location
		Location location = locMgr
				.getLastKnownLocation(provider);

		double lon = location.getLongitude();
		double lat = location.getLatitude();
		YelpPoint p = new YelpPoint(lat,lon);
		
		Intent i = new Intent(context,AlarmService.class);
		i.putExtra("point", p);
        // all locations from the LocationManager will have an accuracy
		i.putExtra("acc", location.getAccuracy());
		i.putExtra("time", location.getTime());
        // show location on screen, comment out to prevent this from happening
		Toast.makeText(context, p.getLat() + ", " + p.getLon(), Toast.LENGTH_SHORT).show();
		context.startService(i);
		
		// release wake lock, turn WIFI off
//		if (resetWifi) {
//			wifi.setWifiEnabled(false);
//			resetWifi = false;
//		}
		
		// wl.release();
	}

    // register alarm with the OS. As defined below, fires every hour
	public void setAlarm(Context context) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, Alarm.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
		am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(),
				1000 * 60 * 60, pi); // Millisec * Second * Minute * Hour
	}

	public void cancelAlarm(Context context) {
		Intent intent = new Intent(context, Alarm.class);
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}
}