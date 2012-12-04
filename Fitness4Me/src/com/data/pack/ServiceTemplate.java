package com.data.pack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.data.fitness4me.R;

public class ServiceTemplate extends Service {

	public static final String BROADCAST_ACTION = "com.data.pack.HomeScreen";
	private static PlaceDataSQL placeData;
	private final Handler handler = new Handler();
	Intent intent;
	private VOUserDetails obUser;
	int counter = 0;
	private Prefs prefs ;
	@Override
	public void onCreate() {
		// Called on service created
		try {
			intent = new Intent(BROADCAST_ACTION);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (placeData == null)
			placeData = new PlaceDataSQL(this);
		if (obUser == null)
			obUser = new VOUserDetails(this);
		if(prefs == null)
		  prefs = new Prefs(getApplicationContext());
	}
	
	 
	@Override
	public void onDestroy() {
		 super.onDestroy();
	}

	
 
 
	@Override
	public void onStart(Intent intent, int startid) {

		int i = 0;
		while (i < 101) {
			if (i > 100) {
				this.onDestroy();
			} else {
				counter = i;
				i++;
				handler.removeCallbacks(sendUpdatesToUI);
				handler.postDelayed(sendUpdatesToUI, 1 * 1000); // 1 sec
			}

		}

	}

	private Runnable sendUpdatesToUI = new Runnable() {
		public void run() {

			try {
				new RetreiveFeedTask().execute("dataretrive");

				handler.postDelayed(this, 1 * 1000); // 1 sec
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	};

	private void DisplayLoggingInfo() {

			

			try {

				if (obUser.getUserId().length() > 0) {
					// mProgress.setVisibility(View.VISIBLE);
					String url = getResources().getString(R.string.servername);
					//
					try {
						String strdataAcceptFlag = prefs.getPreference("dataAcceptFlag");
						if (strdataAcceptFlag.length() == 0)
								prefs.setPreference("dataAcceptFlag", "0");
						
						if (prefs.getPreference("dataAcceptFlag").equals("0")) {

							if (CheckNetworkAvailability
									.isNetworkAvailable(getApplicationContext())
									&& obUser.getSelectedLanguage().length() > 0) {
								GlobalData.dataAcceptFlag = "1";
								prefs.setPreference("dataAcceptFlag", "1");
								url = url + "listapps=yes&duration=10&userid="
										+ obUser.getUserId() + "&lang="
										+ obUser.getSelectedLanguage();
								JSONObject json = JSONfunctions
										.getJSONfromURL(url);

								if (json != null) {
									JSONArray workoutList = json
											.getJSONArray("items");

									if (placeData != null) {
										SQLiteDatabase db = placeData
												.getWritableDatabase();
										if (workoutList.length() > 0) {
											db.delete("FitnessWorkouts", null,
													null);
											// db.delete("FitnessWorkoutVideos",
											// null,null);
										}
									}
									for (int loop = 0; loop < workoutList
											.length(); loop++) {
										// //HashMap<String, String> map = new
										// HashMap<String, String>();
										JSONObject e = workoutList
												.getJSONObject(loop);

										insertData(e.getString("id"),
												e.getString("name"),
												e.getString("rate"),
												e.getString("image_android"),
												e.getString("image_android"),
												e.getString("description"),
												e.getString("description_big"),
												e.getString("islocked"),
												e.getString("props"),
												e.getString("image_android"));

										// GlobalData.arrImagedownloadList.add(e.getString("image_android"));
									}

								}
							}
						}
						// setListAdapter(adapter);
					} catch (JSONException e) {
						Log.e("log_tag", "Error parsing data " + e.toString());
					}

				}
			} catch (Exception e) {
				Log.e("error", "Error parsing data " + e.toString());

			}

			if (GlobalData.notification.equals("1")) {

				GlobalData.notification = "0";
				try {
					intent = new Intent(BROADCAST_ACTION);
					sendBroadcast(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		 
	}

	public static boolean isRunning() {
		return true;
	}

	/**
	 * Insert data into table
	 * 
	 * @param String
	 *            , String , String , String , String ,String ,String
	 * @return
	 */
	private void insertData(String id, String name, String rate, String image,
			String imagethumb, String description, String descriptionbig,
			String islocked, String Props, String ImageAndroid) {
		try {
			SQLiteDatabase db = placeData.getWritableDatabase();
			ContentValues values;
			values = new ContentValues();
			values.put("Id", id);
			values.put("Name", name);
			values.put("Rate", rate);
			values.put("Image", image);
			values.put("ImageThumb", imagethumb);
			values.put("Description", description);
			values.put("DescriptionBig", descriptionbig);
			values.put("IsLocked", islocked);
			values.put("Props", Props);
			values.put("ImageAndroid", ImageAndroid);
			db.insert("FitnessWorkouts", null, values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	class RetreiveFeedTask extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... urls) {
			DisplayLoggingInfo();
			return null;
		}

		protected void onPostExecute(String feed) {
			// TODO: check this.exception
			// TODO: do something with the feed
		//	 handler.removeCallbacks(sendUpdatesToUI);
		}
	}

}