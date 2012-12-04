package com.data.pack;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNetworkAvailability {
	public static boolean isNetworkAvailable(Context context) {

		try {

			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo i = cm.getActiveNetworkInfo();
			if (i == null)
				return false;
			boolean isConnected = (cm.getActiveNetworkInfo() != null && cm
					.getActiveNetworkInfo().isConnectedOrConnecting());
			// if (!i.isConnected())
			// return false;
			// if (!i.isAvailable())
			// return false;
			// long TotalRxAfterTest = TrafficStats.getTotalTxBytes();
			// long TotalTxAfterTest = TrafficStats.getTotalRxBytes();

			return isConnected;
		} catch (Exception e) {
			// TODO: handle exception
			String st = "";
			st = e.toString();
			// Toast.makeText(context, " Network error "+e.toString(),
			// Toast.LENGTH_LONG).show();
			return false;
		}

	}

}
