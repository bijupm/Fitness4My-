package com.data.pack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.data.fitness4me.R;

public class MyC2dmReceiver extends BroadcastReceiver {

	private PlaceDataSQL placeData;
	private VOUserDetails obUser;
	private Context context;
	private Prefs prefs;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		if (placeData == null)
			placeData = new PlaceDataSQL(this.context);
		if (obUser == null)
			obUser = new VOUserDetails(this.context);

		if (prefs == null)
			prefs = new Prefs(this.context);

		if (intent.getAction().equals(
				"com.google.android.c2dm.intent.REGISTRATION")) {
			handleRegistration(context, intent);
		} else if (intent.getAction().equals(
				"com.google.android.c2dm.intent.RECEIVE")) {
			handleMessage(context, intent);
		}
	}

	/**
	 * db update User Level
	 * 
	 * @return
	 */
	private void UpdateLevel(String level) {
		String currentLevel = obUser.getLevel();
		if (!currentLevel.equals(level)) {
			obUser.setLevel(level);
			Util.trimCache(this.context);
			GlobalData.fulldownloadcomplete = false;
			prefs.setPreference("fulldownloadcomplete", ""
					+ GlobalData.fulldownloadcomplete);
			GlobalData.freedownloadcomplete = false;
			prefs.setPreference("freedownloadcomplete", ""
					+ GlobalData.freedownloadcomplete);
			if (CheckNetworkAvailability.isNetworkAvailable(this.context)) {

				try {

					if (obUser.getUserId().length() > 0) {
						// mProgress.setVisibility(View.VISIBLE);
						String url = this.context
								.getString(R.string.servername);
						//
						try {

							if (CheckNetworkAvailability
									.isNetworkAvailable(this.context)
									&& obUser.getSelectedLanguage().length() > 0) {
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
											db.delete("FitnessWorkoutVideos",
													null, null);
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

							// setListAdapter(adapter);
						} catch (JSONException e) {
							Log.e("log_tag",
									"Error parsing data " + e.toString());
						}

					}
				} catch (Exception e) {
					Log.e("error", "Error parsing data " + e.toString());

				}

			}
		}

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

	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is),
				8192);
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	private void handleRegistration(Context context, Intent intent) {
		String registration = intent.getStringExtra("registration_id");
		if (intent.getStringExtra("error") != null) {
			// Registration failed, should try again later.
			Log.d("c2dm", "registration failed");
			String error = intent.getStringExtra("error");
			if (error == "SERVICE_NOT_AVAILABLE") {
				Log.d("c2dm", "SERVICE_NOT_AVAILABLE");
			} else if (error == "ACCOUNT_MISSING") {
				Log.d("c2dm", "ACCOUNT_MISSING");
			} else if (error == "AUTHENTICATION_FAILED") {
				Log.d("c2dm", "AUTHENTICATION_FAILED");
			} else if (error == "TOO_MANY_REGISTRATIONS") {
				Log.d("c2dm", "TOO_MANY_REGISTRATIONS");
			} else if (error == "INVALID_SENDER") {
				Log.d("c2dm", "INVALID_SENDER");
			} else if (error == "PHONE_REGISTRATION_ERROR") {
				Log.d("c2dm", "PHONE_REGISTRATION_ERROR");
			}
		} else if (intent.getStringExtra("unregistered") != null) {
			// unregistration done, new messages from the authorized sender will
			// be rejected
			Log.d("c2dm", "unregistered");

		} else if (registration != null) {

			try {

				String url = "http://fitness4.me/mobile/testjson.php?android_key="
						+ registration + "&user_id=" + obUser.getUserId();
				HttpClient httpclient = new DefaultHttpClient();

				// Prepare a request object
				HttpGet httpget = new HttpGet(url);
				httpget.addHeader(new BasicHeader("Accept", "application/json"));
				// httpget.getParams().setParameter("format", "JSON");

				// Execute the request
				HttpResponse response;

				String result = null;
				response = httpclient.execute(httpget);

				// Get hold of the response entity
				HttpEntity entity = response.getEntity();
				// If the response does not enclose an entity, there is no need
				// to worry about connection release

				if (entity != null) {
					// A Simple Response Read
					InputStream instream = entity.getContent();
					result = convertStreamToString(instream);
					// Closing the input stream will trigger connection release
					instream.close();
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {

			}

		}
	}

	private static final String C2DM_DATA_ACTION = "action";

	private void handleMessage(Context context, Intent intent) {

		if (HomeScreen.userRegID != null) {
			if (Integer.parseInt(HomeScreen.userRegID) != 0) {

				String message = intent.getStringExtra("message");
				String badge = intent.getStringExtra("badge");

				Toast.makeText(context.getApplicationContext(),
						"\n message : " + message, 1).show();
				GlobalData.notification = "1";
				NotificationManager objNotfManager = (NotificationManager) context
						.getApplicationContext().getSystemService(
								Context.NOTIFICATION_SERVICE);
				int icon = R.drawable.fitnessicon;
				CharSequence tickerMessage = message;
				long when = System.currentTimeMillis();
				Notification objNotf = new Notification(icon, tickerMessage,
						when);
				objNotf.flags |= Notification.FLAG_AUTO_CANCEL;
				objNotf.defaults |= Notification.DEFAULT_SOUND;
				objNotf.defaults |= Notification.DEFAULT_LIGHTS;
				objNotf.defaults |= Notification.DEFAULT_VIBRATE;
				try {
					CharSequence title = "Fitness4Me";
					CharSequence mesage = "" + message;
					// Intent NotifIntent = new Intent(
					// context.getApplicationContext(),TabContainer.class);
					// NotifIntent.putExtra("message",message);
					PendingIntent contentIntent = PendingIntent.getActivity(
							context.getApplicationContext(), 0, intent, 0);
					objNotf.setLatestEventInfo(context.getApplicationContext(),
							title, mesage, contentIntent);
					objNotfManager.notify(1, objNotf);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (badge.equals("1") || badge.equals("2") || badge.equals("3")
						|| badge.equals("4") || badge.equals("5")) {
					try {
						GlobalData.notification = "1";

						if (badge.equals("1") || badge.equals("2")
								|| badge.equals("3")) {
							UpdateLevel(badge);
						} else {
							GlobalData.dataAcceptFlag = "0";
							prefs.setPreference("dataAcceptFlag", "0");
						}

					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(context.getApplicationContext(),
								"\n error while delete file ", 1).show();

					}
				}
			}
		}
		// Do whatever you want with the message
	}

	public static void CancelNotification(Context ctx, int notifyId) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager nMgr = (NotificationManager) ctx
				.getSystemService(ns);
		nMgr.cancel(notifyId);
	}
}