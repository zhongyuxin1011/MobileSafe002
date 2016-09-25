package com.zyx1011.mobilesafe002.service;

import com.zyx1011.mobilesafe002.constants.Constants;
import com.zyx1011.mobilesafe002.utils.PreferenceUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

public class GPSService extends Service {

	private LocationManager mLm;
	private MyLocationListener mLl;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mLm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mLl = new MyLocationListener();
		mLm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 5, 3, mLl);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mLm != null) {
			mLm.removeUpdates(mLl);
			mLl = null;
			mLm = null;
		}
	}

	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			double longitude = location.getLongitude();
			double latitude = location.getLatitude();
			String msg = "longitude: " + longitude + "\nlatitude: " + latitude;

			SmsManager.getDefault().sendTextMessage(
					PreferenceUtils.getString(getApplication(), Constants.PROTECT_PHONE.SAFE_PHONE), null, msg, null,
					null);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

	}

}
