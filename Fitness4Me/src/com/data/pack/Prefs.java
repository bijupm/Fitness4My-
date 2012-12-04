package com.data.pack;
/*

Use in your Activity like so:
Prefs prefs = new Prefs(this);
prefs.setPreference("username", "rpeacock");
...
String temp = prefs.getPreference("username");
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.data.fitness4me.R;

public class Prefs {

	Context myContext;

	public Prefs(Context ctx) {
		myContext = ctx;
	}

	/*
	 * Store a preference via key -> value
	 */
	public void setPreference(String key, String value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(myContext);

		SharedPreferences.Editor editor = prefs.edit();

		editor.putString(key, value);
		editor.commit();  // important!  Don't forget!

	}


	public void clearAllPreferences() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(myContext);

		SharedPreferences.Editor editor = prefs.edit();
		editor.clear();
		editor.commit();  // important!  Don't forget!

	}

	public String getPreference(String key) {
		String val = "";
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(myContext);
		val = prefs.getString(key, "");

		return val;
	}


}