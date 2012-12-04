package com.data.pack;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.appwidget.AppWidgetManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.data.fitness4me.R;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class HomeScreen extends Activity implements OnClickListener {
	private PlaceDataSQL placeData;

	public static String userRegID = "0";

	private Prefs prefs;
	Cursor cursorList = null;
	private ArrayList<String> m_items;
	private VOUserDetails obUser;
	private AdView adView;
	private String YesString="Yes";
	private  String NoString="No";
	String mes = "You are about to Exit fitness4me. Are you sure?";
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		
		Util.unbindDrawables(findViewById(R.id.root));
		System.gc();
		 
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		if (placeData != null)
			placeData = null;
		if (m_items != null)
			m_items = null;
		if (obUser != null)
			obUser = null;
		 
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:

			mProgressDialog = new ProgressDialog(this);
			// mProgressDialog.show(videostart.this, "", "Loading...");

			if (obUser.getSelectedLanguage().equals("1")) {

				mProgressDialog.setMessage("Please wait..");
			} else {
				mProgressDialog.setMessage("Bitte warten ..");

			}
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);
			// mProgressDialog.setButton("Cancel",
			// (DialogInterface.OnClickListener) null);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
	private Button btnWorkout;
	String st;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.homescreen);
		if (btnWorkout == null)
			btnWorkout = (Button) findViewById(R.id.btnWorkouts);
		Button btnHints = (Button) findViewById(R.id.btnHints);
		Button btnAboutUs = (Button) findViewById(R.id.btnAboutUs);
		Button btnSettings = (Button) findViewById(R.id.btnSettings);
		Button btnExitHome = (Button) findViewById(R.id.btnExitHome);
		if (placeData == null)
			placeData = new PlaceDataSQL(this);
//		if (adView == null)
//			adView = (AdView) this.findViewById(R.id.adView);
		if (m_items == null)
			m_items = new ArrayList<String>();
		if (obUser == null)
			obUser = new VOUserDetails(this);
		//checkAndCreateDirectory("/fitness4meimages");
		try {
			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.flags |= LayoutParams.FLAG_KEEP_SCREEN_ON;
			// params.screenBrightness = 10;
			getWindow().setAttributes(params);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		btnWorkout.setClickable(true);

		// Locale local = new Locale("fr_FR");
		if (prefs == null)
			prefs = new Prefs(this);
		

		try {

			// cursors = getRawEvents("select * from FitnessUser");
			// userCount = cursors.getCount();

			HomeScreen.userRegID = obUser.getUserId();

			String strAllPurchasedfirstTime = prefs.getPreference("allPurchasedfirstTime");
			if (strAllPurchasedfirstTime.length() == 0)
					prefs.setPreference("allPurchasedfirstTime", "" + true);
			
			String st1 = prefs.getPreference("fulldownloadcomplete");
			if (st1.length() > 0)
				GlobalData.fulldownloadcomplete = Boolean.parseBoolean(prefs
						.getPreference("fulldownloadcomplete"));
			String st2 = prefs.getPreference("allPurchased");
			if (st2.length() > 0)
				GlobalData.allPurchased = Boolean.parseBoolean(prefs
						.getPreference("allPurchased"));
			String st3 = prefs.getPreference("freedownloadcomplete");
			if (st3.length() > 0)
				GlobalData.freedownloadcomplete = Boolean.parseBoolean(prefs
						.getPreference("freedownloadcomplete"));
			if (obUser.getSelectedLanguage().equals("1")) {
				mes = "You are about to Exit fitness4me. Are you sure?";
				YesString = "Yes";
				NoString="No";

			} else {
				btnAboutUs.setText("Über uns");
				btnSettings.setText("Einstellungen");
				btnHints.setText("Tipps und Ratschläge");
				mes = "Möchtest du wirklich fitness4.me verlassen?";
				YesString = "Ja";
				NoString="Nein";

			}
			
			
			
			 
			
			if (!GlobalData.allPurchased) {
				fullpurchase();
			}
			if (GlobalData.allPurchased == true) {

				//adView.setVisibility(View.GONE);
			} else {
				(new Thread() {
					public void run() {
						//adView.setVisibility(View.VISIBLE);
					//	adView.setVisibility(View.GONE);
					}
				}).start();

			}
			if (CheckNetworkAvailability.isNetworkAvailable(HomeScreen.this)) {
				try {
					Intent registrationIntent = new Intent(
							"com.google.android.c2dm.intent.REGISTER");
					registrationIntent.putExtra("app", PendingIntent
							.getBroadcast(this, 0, new Intent(), 0));
					registrationIntent.putExtra("sender", "lekbin@gmail.com");
					startService(registrationIntent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// this is starting the service from a BroadcastReceiver

			if (CheckIfServiceIsRunning()) {
				// Toast.makeText(getApplicationContext(),"Continuation of last started service",
				// 1).show();
			} else {
				// Toast.makeText(getApplicationContext(),"  starte service",
				// 1).show();
				try {
					startService(new Intent(this, ServiceTemplate.class));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (!GlobalData.freedownloadcomplete) {
				try {

					GlobalData.freedownloadcomplete = true;
					prefs.setPreference("freedownloadcomplete", ""
							+ GlobalData.freedownloadcomplete);
					
					
					
					Intent intent = new Intent(HomeScreen.this,
							freepurchasedownload.class);
					startActivity(intent);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (CheckNetworkAvailability.isNetworkAvailable(HomeScreen.this)) {
				try {
					if (cursorList == null) {
						cursorList = getRawEvents("select * from FitnessWorkouts order by  IsLocked ASC");
					}

					while (cursorList.moveToNext()) {
						String stUrl = cursorList.getString(cursorList
								.getColumnIndex("ImageAndroid"));
						if (stUrl.length() > 1)
							m_items.add(stUrl);

					}

				} catch (Exception e) {
					if (cursorList != null) {
						cursorList.close();
						cursorList = null;
					}

				} finally {
					if(cursorList !=null)
					cursorList.close();
				}

				String[] url = m_items.toArray(new String[0]);

				for (int i = 0; i < m_items.size(); ++i) {

					try {
						String str1 = m_items.get(i);

						url[i] = str1;

					} catch (Exception e) {

					}

				}
				if (url.length > 0) {

					dloadFAsync = new DownloadImageFileAsync(url);
					dloadFAsync.execute(url);
				}
			}

		} catch (Exception ex) {
			String st = ex.toString();
			st = st + "";
		} finally {
		}
		btnWorkout.setOnClickListener(this);
		btnHints.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					Intent intent = new Intent(HomeScreen.this, Hints.class);
					startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

		btnAboutUs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					Intent intent = new Intent(HomeScreen.this, AboutUs.class);
					startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

		btnSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					Intent intent = new Intent(HomeScreen.this, Settings.class);
					startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		btnExitHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog		  alertDialog = new AlertDialog.Builder(HomeScreen.this).create();  
				 
				
				alertDialog.setTitle("Fitness4Me");
				alertDialog.setMessage(mes);

				alertDialog.setButton(-1, YesString, new DialogInterface.OnClickListener()
				{  
					public void onClick(DialogInterface dialog, int which) {  
						try
						{
							prefs.setPreference("terminateID", "0");	
							 
							 Intent intent = new Intent(Intent.ACTION_MAIN);
								intent.addCategory(Intent.CATEGORY_HOME);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							  stopService(new Intent(HomeScreen.this,ServiceTemplate.class));

						}
						catch (Exception e) {
							// TODO: handle exception
						}
					} }); 
				alertDialog.setButton(-2, NoString, new DialogInterface.OnClickListener()
				{    
					public void onClick(DialogInterface dialog, int which) {  
					 
						dialog.cancel(); 
					} });
				try {
					alertDialog.show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		
		
		try {
			Intent unregIntent = new Intent(
					"com.google.android.c2dm.intent.UNREGISTER");
			unregIntent.putExtra("app",
					PendingIntent.getBroadcast(this, 0, new Intent(), 0));
			startService(unregIntent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// }
	public void checkAndCreateDirectory(String dirName) {
		// File new_dir = new File( getCacheDir()+ dirName );
		File new_dir = new File(Environment.getExternalStorageDirectory()
				+ dirName);
		if (!new_dir.exists()) {
			new_dir.mkdirs();
		}
	}

	public void onReceive(Context context, Intent intent) {

		try {
			if (intent.getAction().equals(
					"com.google.android.c2dm.intent.REGISTRATION")) {
				handleRegistration(context, intent);
			} else if (intent.getAction().equals(
					"com.google.android.c2dm.intent.RECEIVE")) {
				handleMessage(context, intent);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleRegistration(Context context, Intent intent) {

		String registration = intent.getStringExtra("registration_id");
		if (intent.getStringExtra("error") != null) {

			// Registration failed, should try again later.
		} else if (intent.getStringExtra("unregistered") != null) {
			// unregistration done, new messages from the authorized sender will
			// be rejected

		} else if (registration != null) {
			// Send the registration ID to the 3rd party site that is sending
			// the messages.
			// This should be done in a separate thread.
			// When done, remember that all registration is done.

		}
	}

	private void handleMessage(Context context, Intent intent) {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			onBackPressed();
		}

		return super.onKeyDown(keyCode, event);
	}

	public void onBackPressed() {
		try {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// this.finish();
		return;
	}

	private void fullpurchase() {

		if (CheckNetworkAvailability.isNetworkAvailable(HomeScreen.this)) {
			try {

				// mProgress.setVisibility(View.VISIBLE);
				String url = getResources().getString(R.string.servername);

				try {
					JSONObject json = JSONfunctions
							.getJSONfromURL(url + "user_purchase=yes&user_id="
									+ obUser.getUserId());
					JSONArray userDetails = json.getJSONArray("items");

					for (int i = 0; i < userDetails.length(); i++) {
						// HashMap<String, String> map = new HashMap<String,
						// String>();
						JSONObject e = userDetails.getJSONObject(i);

						GlobalData.allPurchased = Boolean.parseBoolean(e
								.getString("fullpurchase"));
						prefs.setPreference("allPurchased", ""
								+ GlobalData.allPurchased);

					}
					// setListAdapter(adapter);
				} catch (JSONException e) {
					// Log.e("log_tag", "Error parsing data "+e.toString());
				}

			} catch (Exception e) {
				// Log.e("error", "Error parsing data "+e.toString());

			}
		}
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			try {
				Intent intentNew = new Intent(HomeScreen.this, HomeScreen.class);
				// intent.putExtra("userID", userID);
				prefs.setPreference("terminateID", "0");	
				startActivity(intentNew);
//				// stop serviceGlobalData.terminateID = "0";
//			  stopService(new Intent(HomeScreen.this,ServiceTemplate.class));
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	private DownloadImageFileAsync dloadFAsync;

	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(broadcastReceiver, new IntentFilter(
				ServiceTemplate.BROADCAST_ACTION));
		
if (prefs.getPreference("terminateID").equalsIgnoreCase("1")) {
	prefs.setPreference("terminateID", "0");	
	Intent intent = new Intent(Intent.ACTION_MAIN);
	intent.addCategory(Intent.CATEGORY_HOME);
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		 stopService(new Intent(HomeScreen.this,ServiceTemplate.class));
//			AlertDialog		  alertDialog = new AlertDialog.Builder(HomeScreen.this).create();  
//			 
//			
//			alertDialog.setTitle("Fitness4Me");
//			alertDialog.setMessage(mes);
//
//			alertDialog.setButton(-1, YesString, new DialogInterface.OnClickListener()
//			{  
//				public void onClick(DialogInterface dialog, int which) {  
//					try
//					{
//			 
//						 GlobalData.terminateID = "0";
//						 Intent intent = new Intent(Intent.ACTION_MAIN);
//							intent.addCategory(Intent.CATEGORY_HOME);
//							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//							startActivity(intent);
//						  stopService(new Intent(HomeScreen.this,ServiceTemplate.class));
//
//					}
//					catch (Exception e) {
//						// TODO: handle exception
//					}
//				} }); 
//			alertDialog.setButton(-2, NoString, new DialogInterface.OnClickListener()
//			{    
//				public void onClick(DialogInterface dialog, int which) {  
//					Intent intent = new Intent(HomeScreen.this, FitnessforMeActivity.class);
//					startActivity(intent);
//					dialog.cancel(); 
//				} });
//			try {
//				alertDialog.show();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
			
			
//			GlobalData.terminateID = "0";
//			try {
//				Intent intent = new Intent(Intent.ACTION_MAIN);
//				intent.addCategory(Intent.CATEGORY_HOME);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
// 				startActivity(intent);
//			  stopService(new Intent(HomeScreen.this,ServiceTemplate.class));
//			  
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

		}
		
	}

	@Override
	public void onPause() {
		super.onPause();
		// unregisterReceiver(broadcastReceiver);
	}

	private boolean isMyServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {

			if ("com.data.pack.ServiceTemplate".equals(service.service
					.getClassName())) {
				return true;
			}
		}
		return false;
	}

	private boolean CheckIfServiceIsRunning() {
		// If the service is running when the activity starts, we want to
		// automatically bind to it.
		if (isMyServiceRunning()) {
			// doBindService();
			return true;
		}
		return false;
	}

	private long parserNumber(String line) throws Exception {
		long ret = 0;
		String[] delim = line.split(" ");
		if (delim.length >= 1) {
			ret = Long.parseLong(delim[0]);
		}
		return ret;
	}

	public long syncFetchReceivedBytes() {
		// TODO Auto-generated method stub
		ProcessBuilder cmd;
		long readBytes = 0;
		BufferedReader rd = null;
		try {
			String[] args = { "/system/bin/cat", "/proc/net/dev" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			rd = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line;
			int linecount = 0;
			while ((line = rd.readLine()) != null) {
				linecount++;
				if (line.contains("lan0") || line.contains("eth0")) {
					String[] delim = line.split(":");
					if (delim.length >= 2) {
						readBytes = parserNumber(delim[1].trim());
						break;
					}
				}
			}
			rd.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rd != null) {
				try {
					rd.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return readBytes;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnWorkouts:
			String mes = "";
			if(obUser !=null)
			{
			if (obUser.getSelectedLanguage().equals("1")) {

				mes = "Please wait..";
			} else {
				mes = "Bitte warten ..";

			}
			ProgressDialog dialog = ProgressDialog.show(HomeScreen.this, "",
					mes, true);
			dialog.show();
			}
			btnWorkout.setClickable(true);

			try {
				Intent intent = new Intent(HomeScreen.this,
						FitnessforMeActivity.class);
				startActivity(intent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private Cursor getRawEvents(String sql) {
		Cursor cursor = null;
		try {
			SQLiteDatabase db = (placeData).getReadableDatabase();
			cursor = db.rawQuery(sql, null);

			startManagingCursor(cursor);
		} catch (Exception e) {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return cursor;
	}

	class DownloadImageFileAsync extends AsyncTask<String, String, String> {
		int current = 0;
		String[] paths;
		String fpath;
		boolean show = false;
		public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
		ProgressBar prgBar1;

		public DownloadImageFileAsync(String[] paths) {
			super();
			try {
				this.paths = paths;
			} catch (Exception e) {
				// TODO: handle exception
				String st = e.toString();
				st = st + "";
			}
			// for(int i=0; i<paths.length; i++)
			// System.out.println((i+1)+":  "+paths[i]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (GlobalData.allPurchased) {

			//	adView.setVisibility(View.GONE);

			} else {
				(new Thread() {
					public void run() {
						//adView.setVisibility(View.VISIBLE);
						//adView.loadAd(new AdRequest());
					}
				}).start();

			}
		}

		@Override
		protected String doInBackground(String... aurl) {
			int rows = aurl.length;
			while (current < rows) {
				int count;
				try {
					fpath = getFileName(this.paths[current]);

					// String fileMainUrl =
					// android.os.Environment.getExternalStorageDirectory().getPath()
					// +"/"+ fpath;
					String fileMainUrl = fpath;
					File filecheck = new File(fileMainUrl);
					if (!filecheck.exists()) {
						URL url = new URL(this.paths[current]);
						URLConnection conexion = url.openConnection();
						conexion.connect();
						flagcount = flagcount + 1;
						InputStream input = new BufferedInputStream(
								url.openStream(), 512);
						OutputStream output = new FileOutputStream(fpath);
						byte data[] = new byte[1512];
						long total = 0;
						while ((count = input.read(data)) != -1) {
							total += count;
							output.write(data, 0, count);
						}

						show = true;
						output.flush();
						output.close();
						input.close();
						output = null;
						input = null;
					}
					current++;
				} catch (Exception e) {
					String st = e.toString();
					st = st + "";
				} finally {

				}
			} // while end
			return null;
		}

		@Override
		protected void onProgressUpdate(String... progress) {

		}

		private int flagcount = 0;

		@Override
		protected void onPostExecute(String unused) {
			super.onPostExecute(unused);

		}

		public String getFileName(String wholePath) {
			String name = null;
			int start, end;
			start = wholePath.lastIndexOf('/');
			end = wholePath.length(); // lastIndexOf('.');
			name = wholePath.substring((start + 1), end);
			name = getCacheDir() + "/" + name;
			return name;
		}

		
		 
	}

}
