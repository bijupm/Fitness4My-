package com.data.pack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.string;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import com.data.fitness4me.R;


public class SplashScreen extends Activity {

	/**
	 * The thread to process splash screen events
	 */
	private Thread mSplashThread;    
	private static PlaceDataSQL placeData;
	private  int userCount =0;
	private  String userID = "0";
	private  String UserName = "";
	private TextView txtsync ;
	private TextView description;
	private Prefs prefs = new Prefs(this);
	private VOUserDetails obUser =  new VOUserDetails(this); 
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:

			mProgressDialog = new ProgressDialog(this);
			
			String mes = "Synchronizing - just a moment..";
			if(obUser.getSelectedLanguage().equals("2"))
			{
				mes = "Synchronisierung- einen Moment bitte";
				//  txtwelcome.setText("Welcome to fitness4.me");  
			}
			 else if(obUser.getSelectedLanguage().equals("1"))
			{
				 mes = "Synchronizing - just a moment..";
			}
			else
			{
				String stLocale =	Locale.getDefault().getDisplayLanguage();
				if(stLocale.equals("Deutsch"))
				{
					
					mes = "Synchronisierung- einen Moment bitte";
				}
				else
				{
					mes = "Synchronizing - just a moment..";
				}
				
			}
			if(obUser.getUserId().equals("0") || obUser.getUserId().equals(""))
			{
				String stLocale =	Locale.getDefault().getDisplayLanguage();
				if(stLocale.equals("Deutsch"))
				{
					
					mes = "Synchronisierung- einen Moment bitte";
				}
				else
				{
					mes = "Synchronizing - just a moment..";
				}
			}
			//    mProgressDialog.show(videostart.this, "", "Loading...");
			//			 if(obUser.getSelectedLanguage().equals("1"))
			//				{
			//				 
			mProgressDialog.setMessage(mes);
			//				}
			//			 else
			//			 {
			//			mProgressDialog.setMessage("Synchronisieren - nur einen Augenblick");
			//			 }
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.getWindow().setGravity(Gravity.BOTTOM);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);
			//			 mProgressDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
			//			       public void onClick(DialogInterface dialog, int id) {
			//			            dialog.cancel();
			//			       }}); 
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try
		{mDecimalFormater=new DecimalFormat("##.##");
		requestWindowFeature(Window.FEATURE_PROGRESS);
		//DisplayWifiState();
		bindListeners();
		}
		catch (Exception e) {
			String st = e.toString();
			st = st+"";
			// TODO: handle exception
		}
		setContentView(R.layout.splash);
	
		final SplashScreen sPlashScreen = this;   
		//        txtsync =(TextView)findViewById(R.id.txtsync);
		//        txtsync.setText("Synchronizing \n- just a moment");
		placeData = new PlaceDataSQL(this);

		 
		
		
		description = (TextView)findViewById(R.id.description);
		
		String stLocale =	Locale.getDefault().getDisplayLanguage();
		if(obUser.getSelectedLanguage().equals("1"))
		{
			description.setText("Welcome to");
		}
		else if(obUser.getSelectedLanguage().equals("2"))
		{
			description.setText("Willkommen bei");
		}
		else
		{
			if(stLocale.equals("Deutsch"))
			{
				
				description.setText("Willkommen bei");
			}
			else
			{
				description.setText("Welcome to");
			}
		}
		 try
			{

				WindowManager.LayoutParams params = getWindow().getAttributes();
				params.flags |= LayoutParams.FLAG_KEEP_SCREEN_ON;
				//	params.screenBrightness = 10;
				getWindow().setAttributes(params);

		 
		 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//	if(CheckNetworkAvailability.isNetworkAvailable(SplashScreen.this))
		//			{
		//		 		if(userCount > 0)
		//		 		{
		//				Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
		//				registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));  
		//				registrationIntent.putExtra("sender","lekbin@gmail.com");
		//				startService(registrationIntent);
		//				
		//			}
		//		 		
		showDialog(DIALOG_DOWNLOAD_PROGRESS);
		//checkAndCreateDirectory("/fitness4vid");
		// The thread to wait for splash screen events
		mSplashThread =  new Thread(){
			@Override
			public void run(){
				try {

					synchronized(this){
						// Wait given period of time or exit on touch
						//String liveVersion = Util.readversion();
						Context context = getApplicationContext();
						//int appversion = Util.getVersionCode(context);
						// upgradeApp();
						copyFilesToSdCard();
						wait(3000);
					}
				}
				catch(InterruptedException ex){                    
				}

				finish();
				if( obUser.getUserId().length() ==0)
				{
					try {
						Intent intent = new Intent();
						intent.setClass(sPlashScreen, SignUP.class);
						startActivity(intent);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					// Run next activity
					try {
						Intent intent = new Intent();
						String st = prefs.getPreference("disconnectfreepurchase");
						if(st.equals("true"))
						{
							GlobalData.freedownloadcomplete = false;
							prefs.setPreference("freedownloadcomplete", "false");
						}
						intent.setClass(sPlashScreen, HomeScreen.class);
						startActivity(intent);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};

		try {
			mSplashThread.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   

	}
	public void checkAndCreateDirectory(String dirName){
		//	File new_dir = new File( getCacheDir()+ dirName );
		File new_dir = new File(Environment.getExternalStorageDirectory()+ dirName);
		if( !new_dir.exists() ){
			new_dir.mkdirs();
		}
	}

	final static String TARGET_BASE_PATH = "/sdcard/fitness4vid/";

	private void copyFilesToSdCard() {
 		copyFileOrDir("videos",""); // copy all files in assets folder in my project
		//CopyAssets();
	}

	
	private void upgradeApp()
	{
		Intent updateIntent = new Intent(Intent.ACTION_VIEW,
			       Uri.parse("market://details?id=com.data.fitnessforme"));
			startActivity(updateIntent);
	}
	private void copyFileOrDir(String path,String outfilename) {
		AssetManager assetManager = this.getAssets();
		String assets[] = null;
		try {
			assets = assetManager.list(path);
			if (assets.length == 0) {
				copyFile(path,outfilename);
			} else {
				String fullPath =  getCacheDir()+"/" + path;
				//	            File dir = new File(fullPath);
				//	            if (!dir.exists())
				//	                if (!dir.mkdirs());
				//	                    Log.i("tag", "could not create dir "+fullPath);
				for (int i = 0; i < assets.length; ++i) {
					String p;
					if (path.equals(""))
						p = "";
					else 
						p = path + "/";
					if(!assets[i].equals("videos"))
						copyFileOrDir( p + assets[i],assets[i]);
				}
			}
		} catch (IOException ex) {
		}
	}

	private void copyFile(String fileName,String outfilename) {
		AssetManager assetManager = this.getAssets();

		InputStream in = null;
		OutputStream out = null;
		String newFileName = null;
		
		try {
			
			 String PATH = getCacheDir()+"";
				//String PATH =  android.os.Environment.getExternalStorageDirectory().getPath() +"/"+ "fitness4vid";
				//  System.out.println("Current:  "+current+"\t\tRows: "+rows+"tot"+sUrl.length);
				File file = new File(PATH);
				 
			 

				 

				File outputFile = new File(file , outfilename);
				//FileOutputStream fos = new FileOutputStream(outputFile);
				// download the file
				//   InputStream input = new BufferedInputStream(url.openStream());
				outputFile.setReadable(true,false);
			in = assetManager.open(fileName);
			if (fileName.endsWith(".mp4")) // extension was added to avoid compression on APK file
				newFileName = getCacheDir()+"/" + outfilename;
			out = new FileOutputStream(outputFile);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {
			String st = e.toString();
			st = st+"";
		}

	}

	/**
	 * Get raw data
	 * 
	 * @param String 
	 * @return  Cursor
	 */
	private Cursor getRawEvents(String sql) {
		SQLiteDatabase db = (placeData).getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);

		startManagingCursor(cursor);
		return cursor;
	}   
	/**
	 * Processes splash screen touch events
	 */
	@Override
	public boolean onTouchEvent(MotionEvent evt)
	{
		if(evt.getAction() == MotionEvent.ACTION_DOWN)
		{
			synchronized(mSplashThread){
				mSplashThread.notifyAll();
			}
		}
		return true;
	}  


	/**
	 * Setup event handlers and bind variables to values from xml
	 */
	private void bindListeners() {

		if(CheckNetworkAvailability.isNetworkAvailable(SplashScreen.this))
		{
			new Thread(mWorker).start();		
		}

	}



	private final Handler mHandler=new Handler(){
		@Override
		public void handleMessage(final Message msg) {
			switch(msg.what){
			case MSG_UPDATE_STATUS:
				final SpeedInfo info1=(SpeedInfo) msg.obj;
				//	Toast.makeText(getApplicationContext(), " update_speed"+mDecimalFormater.format(info1.megabits), Toast.LENGTH_LONG).show();  
				if(GlobalData.netSpeed == 0)
					GlobalData.netSpeed = info1.downspeed; 
				// Title progress is in range 0..10000
				setProgress(100 * msg.arg1);
				//  Toast.makeText(getApplicationContext(), " speed"+GlobalData.netSpeed +"getDownLoadedFileCount"+filedownloadedcount(), Toast.LENGTH_LONG).show(); 
				break;
			case MSG_UPDATE_CONNECTION_TIME:
				// Toast.makeText(getApplicationContext(), " update_connectionspeed"+msg.arg1, Toast.LENGTH_LONG).show(); 
				break;				
			case MSG_COMPLETE_STATUS:
				final  SpeedInfo info2=(SpeedInfo) msg.obj;
				// Toast.makeText(getApplicationContext(), " update_connectionspeed"+msg.arg1, Toast.LENGTH_LONG).show(); 



				if(networkType(info2.kilobits)==1){
					//Toast.makeText(getApplicationContext(), " 3G", Toast.LENGTH_LONG).show(); 
				}else{
					//	Toast.makeText(getApplicationContext(), " edge", Toast.LENGTH_LONG).show(); 
				}
				break;	
			default:
				super.handleMessage(msg);		
			}
		}
	};

	/**
	 * Our Slave worker that does actually all the work
	 */
	private final Runnable mWorker=new Runnable(){

		@Override
		public void run() {
			InputStream stream=null;
			try {
				int bytesIn=0;
				String downloadFileUrl="http://fitness4.me/dummy.txt";	
				long startCon=System.currentTimeMillis(); 
				URL url=new URL(downloadFileUrl);
				URLConnection con=url.openConnection();
				con.setUseCaches(false);
				long connectionLatency=System.currentTimeMillis()- startCon;
				stream=con.getInputStream();

				Message msgUpdateConnection=Message.obtain(mHandler, MSG_UPDATE_CONNECTION_TIME);
				msgUpdateConnection.arg1=(int) connectionLatency;
				mHandler.sendMessage(msgUpdateConnection);

				long start=System.currentTimeMillis();
				int currentByte=0;
				long updateStart=System.currentTimeMillis();
				long updateDelta=0;
				int  bytesInThreshold=0;

				while((currentByte=stream.read())!=-1){	
					bytesIn++;
					bytesInThreshold++;
					if(updateDelta>=UPDATE_THRESHOLD){
						int progress=(int)((bytesIn/(double)EXPECTED_SIZE_IN_BYTES)*100);
						Message msg=Message.obtain(mHandler, MSG_UPDATE_STATUS, calculate(updateDelta, bytesInThreshold));
						msg.arg2=progress;
						msg.arg1=bytesIn;
						mHandler.sendMessage(msg);
						//Reset
						updateStart=System.currentTimeMillis();
						bytesInThreshold=0;
					}
					updateDelta = System.currentTimeMillis()- updateStart;
				}

				long downloadTime=(System.currentTimeMillis()-start);
				//Prevent AritchmeticException
				if(downloadTime==0){
					downloadTime=1;
				}

				Message msg=Message.obtain(mHandler, MSG_COMPLETE_STATUS, calculate(downloadTime, bytesIn));
				msg.arg1=bytesIn;
				mHandler.sendMessage(msg);
			} 
			catch (Exception e) {
				//Log.e(TAG, e.getMessage());

				String st = e.toString();
				st = st+"";
			}finally{
				try {
					if(stream!=null){
						stream.close();
					}
				} catch (IOException e) {
					//Suppressed
				}
			}

		}
	};

	 
	/**
	 * Get Network type from download rate
	 * @return 0 for Edge and 1 for 3G
	 */
	private int networkType(final double kbps){
		int type=1;//3G
		//Check if its EDGE
		if(kbps<EDGE_THRESHOLD){
			type=0;
		}
		return type;
	}

	/**
	 * 	
	 * 1 byte = 0.0078125 kilobits
	 * 1 kilobits = 0.0009765625 megabit
	 * 
	 * @param downloadTime in miliseconds
	 * @param bytesIn number of bytes downloaded
	 * @return SpeedInfo containing current speed
	 */
	private SpeedInfo calculate(final long downloadTime, final long bytesIn){
		SpeedInfo info=new SpeedInfo();
		//from mil to sec
		long bytespersecond   =(bytesIn / downloadTime) * 1000;
		double kilobits=bytespersecond * BYTE_TO_KILOBIT;
		double megabits=kilobits  * KILOBIT_TO_MEGABIT;
		info.downspeed=bytespersecond;
		info.kilobits=kilobits;
		info.megabits=megabits;

		return info;
	}

	/**
	 * Transfer Object
	 * @author devil
	 *
	 */
	private static class SpeedInfo{
		public double kilobits=0;
		public double megabits=0;
		public double downspeed=0;		
	}


	//Private fields	

	private static final int EXPECTED_SIZE_IN_BYTES = 1048576;//1MB 1024*1024

	private static final double EDGE_THRESHOLD = 176.0;
	private static final double BYTE_TO_KILOBIT = 0.0078125;
	private static final double KILOBIT_TO_MEGABIT = 0.0009765625;

	private Button mBtnStart;
	private TextView mTxtSpeed;
	private TextView mTxtConnectionSpeed;
	private TextView mTxtProgress;
	private TextView mTxtNetwork;

	private final int MSG_UPDATE_STATUS=0;
	private final int MSG_UPDATE_CONNECTION_TIME=1;
	private final int MSG_COMPLETE_STATUS=2;

	private final static int UPDATE_THRESHOLD=300;


	private DecimalFormat mDecimalFormater;


	class updateduration extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			 
//
//			String url =	 getResources().getString(R.string.servername);
//				 if(CheckNetworkAvailability.isNetworkAvailable(ViewVideo.this))
//								{
//									JSONObject json = JSONfunctions.getJSONfromURL(url+"stats=yes&userid="+userID+"&workoutid="+workoutID+"&duration="+starttime);
//									//   JSONObject json = JSONfunctions.getJSONfromURL("http://fitness.bridge-delivery.com/test_method.txt");
//									try{
//										if(json !=null )
//										{
//											JSONArray  workoutList = json.getJSONArray("items");
//										}
//			
//			
//			
//			
//										// setListAdapter(adapter);
//									}catch(JSONException e)        {
//										//Log.e("log_tag", "Error parsing data "+e.toString());
//									}
//			
//			
//								} 
//			
//								else
//								{
//			try
//			{	 cursorRepeat = getRawEvents("select Duration as cnt from FitnessWorkoutDuration where ID ="+workoutID);
//
//			if (cursorRepeat.moveToNext()) {
//
//				int c=	Integer.parseInt(cursorRepeat.getString(0));
//			} else {
//
//			}
//			}catch(Exception ex)
//			{
//	String st = ex.toString();
//	st = st+"";
//			}
//			 
					//			}		
							
		 
			return null; 
	}
	}

		  
 
}
