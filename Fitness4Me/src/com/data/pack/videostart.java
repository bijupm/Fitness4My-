package com.data.pack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.data.fitness4me.R;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class videostart extends Activity implements OnClickListener {
	private   PlaceDataSQL placeData;
	private String workoutID;
	private String userID;
	private Cursor cursorImage;
	private Cursor cursorloacked;
	private int valCount = 0;
	private String UserName;
	private ImageView imgbtn;
	private int downLoadFileCount = 0;
	private int totalWokoutVideos = -1;
	public static int cancelflag = 0;
	private DownloadFile downloadFile;
	private VOUserDetails obUser;
	private String videourl = "";

	public static int totalPendingDownloadedvideoCount = 0;
	private ArrayList<String> arrImage;
	private String[] url;
	//private DownloadImageFileAsync dloadFAsync;
	private TextView lblProps;
	private Typeface tf;
	private AdView adView;
	private ArrayList<String> finaldownloadurls;
	private ProgressDialog mProgressDialogFile;
	private Bitmap resized;
	private VOWorkout obWorkout;
	private ArrayList<VOWorkoutVideos> arrVoVideoName;
@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	if (downloadFile != null)
		downloadFile.cancel(true);
}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		try {

			
			
			System.gc();
			finaldownloadurls = null;
			arrVoVideoName = null;
			if (downloadFile != null)
				downloadFile.cancel(true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.videostart);
		String builderStr = "";
		String builderStrWait = "";
		if(obUser == null)
			obUser = new VOUserDetails(this);
		if (arrVoVideoName == null)
			arrImage = new ArrayList<String>();
		if (arrVoVideoName == null)
			arrVoVideoName = new ArrayList<VOWorkoutVideos>();
		if (finaldownloadurls == null)
			finaldownloadurls = new ArrayList<String>();
		if (obUser.getSelectedLanguage().equals("1")) {
			builderStrWait = "Please wait..";
			builderStr = "I am downloading your workout and saving it to your device. \n"
					+ "Depending on your internet connection this may take several minutes. \n"
					+ "Don't worry, this happens only the first time you run the workout. \n"
					+ "In the future you will not have to wait. ";
		} else {
			builderStr = "Das Workout wird heruntergeladen und auf deinem Gerät gespeichert.\nJe nach Geschwindigkeit deiner Internetverbindung \nwird dies einige Minuten dauern. Keine Sorge,\ndas geschieht nur, wenn du das Workout zum ersten Mal durchführst.";
			builderStrWait = "Bitte warten ..";
		}
		
//		dialog = ProgressDialog.show(videostart.this, "",
//				builderStrWait, true);
		// mProgressDialog.show(videostart.this, "", "Loading...");
		mProgressDialogFile = new ProgressDialog(videostart.this);
		mProgressDialogFile.setMessage(builderStr);
		mProgressDialogFile.setTitle(null);
		mProgressDialogFile.setIndeterminate(false);
		mProgressDialogFile.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialogFile.setCancelable(true);
		mProgressDialogFile.setButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						if (downloadFile != null)
							downloadFile.cancel(true);
					}
				});
		cancelflag = 0;
		totalWokoutVideos = -1;
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		if (placeData == null)
			placeData = new PlaceDataSQL(this);
		workoutID = extras.getString("workoutID");
		userID = extras.getString("userID");
		UserName = extras.getString("UserName");
		imgbtn = (ImageView) findViewById(R.id.startimageButton);
		GlobalData.selectedWorkOutID = workoutID;
		imgbtn.setClickable(false);
		videourl = getResources().getString(R.string.videourl);
		lblProps = (TextView) findViewById(R.id.lblProps);

		if (adView == null)
			adView = (AdView) this.findViewById(R.id.adView);

		if (tf == null) {
			tf = Typeface.createFromAsset(getAssets(), "fonts/ARIAL.TTF");
			lblProps.setTypeface(tf, 0);
		}
		if (obUser.getSelectedLanguage().equals("1")) {
			lblProps.setText("Props :");
		} else {
			lblProps.setText("Hilfsmittel :");
		}
	//	checkAndCreateDirectory("/fitness4vid");
		// Add the adView to it

		imgbtn.setOnClickListener(this);

	

		try {
			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.flags |= LayoutParams.FLAG_KEEP_SCREEN_ON;
			getWindow().setAttributes(params);
			// populate.setVisibility(View.GONE);
			getDataAndPopulate();
			
		} catch (Exception ex) {
			String st = ex.toString();
			st = st + "";
		}

		// }.start();

	 

		// getDataAndDownload() ;

	}
	private Drawable grabImageFromUrl(String url) throws Exception {
		 
		
		
		
		return Drawable.createFromStream(
				(InputStream) new URL(url).getContent(), "src");

	}
@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	
	 new TheTask().execute();
//	 if(!dialog.isShowing())
//			dialog.show();
	getDataAndDownload();
	
	
	
}
	private int getCountMainVideo()
	{
		VOWorkoutVideos obvideos;
		int count=0;
		if(arrVoVideoName !=null)
		{
		for (int i = 0; i < arrVoVideoName.size(); ++i) {

			obvideos = arrVoVideoName.get(i);
			String fileName = obvideos.getMainvideoName();
 
	
	 File outputDir = new File(getCacheDir()+"");
	  File new_dir = new File( outputDir + "" );
//	File outputDir = Environment.getExternalStorageDirectory();
	//File new_dir = new File(outputDir + "/fitness4vid");
	  File temp = new File(new_dir, fileName);

		if (temp.exists()) {
			
			count++;
		}
	 
		}
		}
		if(count == 0)
			return -1;
		return count;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.startimageButton:

			try {
				
				Cursor cursor = getRawcursorsExcersiceEvents("FitnessWorkoutVideos");
				totalWokoutVideos = cursor.getCount();
				int k = getCountMainVideo();
				if ( totalWokoutVideos == getCountMainVideo()) {
					try {
						Intent intent = new Intent(videostart.this,
								ViewVideo.class);
						intent.putExtra("workoutname", obWorkout.getName());
						intent.putExtra("workoutID", workoutID);
						intent.putExtra("UserName",
								obUser.getSelectedUserName());
						intent.putExtra("userID", obUser.getUserId());
						startActivity(intent);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					CharSequence text = "No internet connection!";
					if (obUser.getSelectedLanguage().equals("1")) {
						text = "Please download files";
					} else {
						text = "Bitte  Herunterladen von Dateien";
					}
					Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG)
							.show();
				}
				break;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private Bitmap getResizedBitmap(Bitmap bm, int newWidth) {

		int width = bm.getWidth();

		int height = bm.getHeight();
		Bitmap resizedBitmap = null;
		float aspect = (float) width / height;

		float scaleWidth = newWidth;

		float scaleHeight = scaleWidth / aspect; // yeah!

		// create a matrix for the manipulation

		Matrix matrix = new Matrix();

		// resize the bit map

		matrix.postScale(scaleWidth / width, scaleHeight / height);

		// recreate the new Bitmap

		resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);

		bm.recycle();

		return resizedBitmap;
	}

	/**
	 * Get and populate data
	 * 
	 * @return
	 */
	private void getDataAndDownload() {

		Cursor cursor = getRawcursorsExcersiceEvents("FitnessWorkoutVideos");

		try {
			
			
			VOWorkoutVideos obworkoutVideos;

			while (cursor.moveToNext()) {
				obworkoutVideos = new VOWorkoutVideos();
				obworkoutVideos.setMainvideoName(cursor.getString(cursor
						.getColumnIndex("MainvideoName")));
				obworkoutVideos.setMainvideoUrl(cursor.getString(cursor
						.getColumnIndex("MainvideoUrl")));
				obworkoutVideos.setMainvideoRepeatCount(cursor.getString(cursor
						.getColumnIndex("MainvideoRepeatCount")));
				obworkoutVideos.setPosterName(cursor.getString(cursor
						.getColumnIndex("PosterName")));
				obworkoutVideos.setPosterUrl(cursor.getString(cursor
						.getColumnIndex("PosterUrl")));
				obworkoutVideos.setPosterRepeatCount(cursor.getString(cursor
						.getColumnIndex("PosterRepeatCount")));
				obworkoutVideos.setRecoveryVideo(cursor.getString(cursor
						.getColumnIndex("RecoveryVideo")));
				obworkoutVideos.setRecoveryVideoName(cursor.getString(cursor
						.getColumnIndex("RecoveryVideoName")));
				obworkoutVideos.setOtherSideName(cursor.getString(cursor
						.getColumnIndex("OtherSideName")));
				obworkoutVideos.setOtherSideVideo(cursor.getString(cursor
						.getColumnIndex("OtherSideVideo")));
				obworkoutVideos.setOtherSideRepeatCount(cursor.getString(cursor
						.getColumnIndex("OtherSideRepeatCount")));
				obworkoutVideos.setOtherSidePosterVideo(cursor.getString(cursor
						.getColumnIndex("OtherSidePosterVideo")));
				obworkoutVideos.setOtherSidePosterName(cursor.getString(cursor
						.getColumnIndex("OtherSidePosterName")));
				obworkoutVideos.setStopName(cursor.getString(cursor
						.getColumnIndex("StopName")));
				obworkoutVideos.setStopVideo(cursor.getString(cursor
						.getColumnIndex("StopVideo")));
				obworkoutVideos.setNextVideo(cursor.getString(cursor
						.getColumnIndex("NextVideo")));
				obworkoutVideos.setNextVideoName(cursor.getString(cursor
						.getColumnIndex("NextVideoName")));
				obworkoutVideos.setCompletedVideoName(cursor.getString(cursor
						.getColumnIndex("CompletedVideoName")));
				obworkoutVideos.setCompletedVideo(cursor.getString(cursor
						.getColumnIndex("CompletedVideo")));
				
				obworkoutVideos.setMainVideoSize(cursor.getString(cursor
						.getColumnIndex("MainVideoSize")));
				obworkoutVideos.setPosterSize(cursor.getString(cursor
						.getColumnIndex("PosterSize")));
				obworkoutVideos.setOtherSideSize(cursor.getString(cursor
						.getColumnIndex("OtherSideSize")));
				arrVoVideoName.add(obworkoutVideos);

			}
		

			videostart.totalPendingDownloadedvideoCount = filedownloadedcount();
			VOWorkoutVideos obvideos;

			for (int i = 0; i < arrVoVideoName.size(); ++i) {

				obvideos = arrVoVideoName.get(i);
				getVideoSource(obvideos.getMainvideoUrl(),
						obvideos.getMainvideoName(),obvideos.getMainVideoSize());
				getVideoSource(obvideos.getPosterUrl(),
						obvideos.getPosterName(),obvideos.getPosterSize());
				getVideoSource(obvideos.getRecoveryVideo(),
						obvideos.getRecoveryVideoName(),"-11");
				getVideoSource(obvideos.getOtherSideVideo(),
						obvideos.getOtherSideName(),obvideos.getOtherSideSize());
				getVideoSource(obvideos.getOtherSidePosterVideo(),
						obvideos.getOtherSidePosterName(),obvideos.getOtherSideSize());
				getVideoSource(obvideos.getStopVideo(), obvideos.getStopName(),"-11");
				getVideoSource(obvideos.getNextVideo(),
						obvideos.getNextVideoName(),"-11");
				getVideoSource(obvideos.getCompletedVideo(),
						obvideos.getCompletedVideoName(),"-11");
			}

			finaldownloadurls = new ArrayList<String>();
			for (int i = 0; i < videoPathes.size(); ++i) {

				try {
					VOVideo FileObject = (VOVideo) videoPathes.get(i);
					finaldownloadurls.add(FileObject.videoUrl);

				} catch (Exception e) {
				}

			}
			removeDuplicates(finaldownloadurls);
			valCount = filedownloadedcount();
			mProgressDialogFile.setMax(valCount);
			url = new String[finaldownloadurls.size()];
			for (int index = 0; index < finaldownloadurls.size(); index++) {
				try {

					url[index] = finaldownloadurls.get(index);

				} catch (Exception e) {
					String st = e.toString();
					st = st + "";

				}
			}
			if (valCount > 0) {

				downloadFile = new DownloadFile(url);
				downloadFile.execute(url);

			}
		} catch (Exception e) {
			String st = e.toString();
			st = st+"";
		} finally {
			cursor.close();
		}

	}

	private ArrayList<Object> videoPathes = new ArrayList<Object>();

	private void getVideoSource(String path, String fname,String Size) throws IOException {
		if (fname.length() > 4) {

			if (URLUtil.isNetworkUrl(videourl + path)) {

				VOVideo VO = new VOVideo();
				VO.videoUrl = videourl+path;
				VO.filename = fname;
				VO.repeate = "1";
				VO.size = Size;
				if (VO.videoUrl != "") {
					 File outputDir = new File(getCacheDir()+"");
					  File new_dir = new File( outputDir + "" ); // getCacheDir() + "/" + name;
					//File outputDir = Environment.getExternalStorageDirectory();
					//File new_dir = new File(outputDir + "/fitness4vid");
					File temp = new File(new_dir, fname);

					if (!temp.exists()) {
						long fSize = temp.length();
						videoPathes.add(VO);
					}

				}

			}
		}
	}

	public void checkAndCreateDirectory(String dirName) {
		// File new_dir = new File( getCacheDir()+ dirName );
		File new_dir = new File(Environment.getExternalStorageDirectory()
				+ dirName);
		if (!new_dir.exists()) {
			new_dir.mkdirs();
		}
	}

	/**
	 * Get raw data
	 * 
	 * @param String
	 * @return Cursor
	 */
	private Cursor getRawcursorsExcersiceEvents(String table) {
		Cursor cursorWorkoutVideos = null;
		try {
			SQLiteDatabase db = (placeData).getReadableDatabase();
			cursorWorkoutVideos = db.query(table, new String[] { "WorkoutId",
					"MainvideoUrl", "MainvideoName", "MainvideoRepeatCount",
					"PosterUrl", "PosterName", "PosterRepeatCount",
					"RecoveryVideo", "RecoveryVideoName", "StopVideo",
					"StopName", "StopRepeatCount", "OtherSidePosterVideo",
					"OtherSidePosterName", "OtherSidePosterRepeatCount",
					"OtherSideVideo", "OtherSideName", "OtherSideRepeatCount",
					"NextVideo", "NextVideoName", "NextRepeatCount",
					"CompletedVideo", "CompletedVideoName",
					"CompletedRepeatCount","MainVideoSize","PosterSize","OtherSideSize" }, "WorkoutId = ?",
					new String[] { String.valueOf(workoutID) }, null, null,
					null);

			startManagingCursor(cursorWorkoutVideos);
		} catch (Exception e) {
			// TODO: handle exception
			cursorWorkoutVideos.close();
		}

		return cursorWorkoutVideos;

	}

	/**
	 * Insert data into table
	 * 
	 * @param String
	 *            , String , String , String
	 * @return
	 */
	private void insertWorkOutVideoData(String id, String poster_name,
			String poster_video, String poster_rep, String main_video,
			String video_name, String main_rep, String RecoveryVideo,
			String RecoveryVideoName, String StopName, String StopVideo,
			String StopRepeatCount, String OtherSidePosterName,
			String OtherSidePosterVideo, String OtherSidePosterRepeatCount,
			String OtherSideName, String OtherSideVideo,
			String OtherSideRepeatCount, String NextVideoName,
			String NextVideo, String NextRepeatCount,
			String CompletedVideoName, String CompletedVideo,
			String CompletedRepeatCount,String MainVideoSize ,String PosterSize,String OtherSideSize) {
		SQLiteDatabase db = placeData.getWritableDatabase();
		ContentValues values;
		values = new ContentValues();
		values.put("WorkoutID ", id);

		if (main_video.equals("")) {
			values.put("MainvideoUrl", main_video);
		} else {
			values.put("MainvideoUrl", videourl + main_video);
		}

		values.put("MainvideoName", video_name);
		values.put("MainvideoRepeatCount", main_rep);
		if (poster_video.equals("")) {
			values.put("PosterUrl", poster_video);
		} else {

			values.put("PosterUrl", videourl + poster_video);
		}

		values.put("PosterName", poster_name);
		values.put("PosterRepeatCount", poster_rep);

		if (RecoveryVideo.equals("")) {
			values.put("RecoveryVideo", RecoveryVideo);
		} else {
			values.put("RecoveryVideo", videourl + RecoveryVideo);
		}

		values.put("RecoveryVideoName", RecoveryVideoName);

		if (StopVideo.equals("")) {
			values.put("StopVideo", StopVideo);
		} else {
			values.put("StopVideo", videourl + StopVideo);
		}

		values.put("StopName", StopName);
		values.put("StopRepeatCount", StopRepeatCount);

		if (OtherSidePosterVideo.equals("")) {
			values.put("OtherSidePosterVideo", OtherSidePosterVideo);
		} else {
			values.put("OtherSidePosterVideo", videourl + OtherSidePosterVideo);
		}

		values.put("OtherSidePosterName", OtherSidePosterName);
		values.put("OtherSidePosterRepeatCount", OtherSidePosterRepeatCount);

		if (OtherSideVideo.equals("")) {
			values.put("OtherSideVideo", OtherSideVideo);
		} else {
			values.put("OtherSideVideo", videourl + OtherSideVideo);
		}

		values.put("OtherSideName", OtherSideName);
		values.put("OtherSideRepeatCount", OtherSideRepeatCount);

		if (NextVideo.equals("")) {
			values.put("NextVideo", NextVideo);
		} else {
			values.put("NextVideo", videourl + NextVideo);
		}

		values.put("NextVideoName", NextVideoName);
		values.put("NextRepeatCount", NextRepeatCount);
		if (CompletedVideo.equals("")) {
			values.put("CompletedVideo", CompletedVideo);
		} else {
			values.put("CompletedVideo", videourl + CompletedVideo);
		}

		values.put("CompletedVideoName", CompletedVideoName);
		values.put("CompletedRepeatCount", CompletedRepeatCount);
		values.put("MainVideoSize", MainVideoSize);
		values.put("PosterSize", PosterSize);
		values.put("OtherSideSize", OtherSideSize);
		db.insert("FitnessWorkoutVideos", null, values);
	}

	private Cursor getEvents(String table) {
		SQLiteDatabase db = (placeData).getReadableDatabase();
		// Cursor cursor = db.query(table, null, null, null, null, null, null);
		// cursor = db.query(table, new String[] {"id", "title",
		// "videoUrl","videoName","description","imageurl"},
		// "id =? " + "+videoid+", null, null, null, null);

		Cursor cursorWorkouts = null;
		try {
			cursorWorkouts = db.query(table, new String[] { "Id", "Name", "Rate",
					"Image", "ImageThumb", "Description", "DescriptionBig",
					"IsLocked", "Props", "ImageAndroid" }, "Id = ?",
					new String[] { String.valueOf(workoutID) }, null, null,
					null);

			startManagingCursor(cursorWorkouts);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			cursorWorkouts.close();
		}

		return cursorWorkouts;
	}

	/**
	 * db update status is locked
	 * 
	 * @return
	 */
	private void UpdateStatus(String status) {
		SQLiteDatabase db = placeData.getWritableDatabase();
		try {

			db.execSQL("update FitnessWorkouts set IsLocked ='" + status
					+ "' where Id = '" + workoutID + "'");
		} catch (Exception e) {
			// TODO: handle exception
			String st = e.toString();
		} finally {
			db.close();
		}

	}

	private ImageView img;
	private Cursor cursor;

	private void getDataAndPopulate() {

		TextView label;
		TextView desc;
		TextView Props;
		label = (TextView) findViewById(R.id.videostarttitle);
		desc = (TextView) findViewById(R.id.videostartdesc);
		img = (ImageView) findViewById(R.id.startvedioimgurl);
		Props = (TextView) findViewById(R.id.videostartProps);
		try {
			if (cursor == null)
				cursor = getEvents("FitnessWorkouts");
				obWorkout = new VOWorkout();
			while (cursor.moveToNext()) {

				obWorkout.setName(cursor.getString(cursor
						.getColumnIndex("Name")));
				obWorkout.setImageAndroid(cursor.getString(cursor
						.getColumnIndex("Image")));
				obWorkout.setDescription(cursor.getString(cursor
						.getColumnIndex("Description")));
				obWorkout.setDescriptionBig(cursor.getString(cursor
						.getColumnIndex("DescriptionBig")));
				obWorkout.setIsLocked(cursor.getString(cursor
						.getColumnIndex("IsLocked")));
				obWorkout.setProps(cursor.getString(cursor
						.getColumnIndex("Props")));

			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		} finally {
			cursor.close();
		}

		label.setTypeface(tf, 1);
		desc.setTypeface(tf);
		Props.setTypeface(tf, 1);

		String[] url = new String[1];
		label.setText("" + obWorkout.getName());
		desc.setText("" + obWorkout.getDescriptionBig());
		String props =obWorkout.getProps().trim();
		if(props.length() > 2)
		{
		String coma = props.substring(props.length()-1, props.length());
		if(coma.equals(","))
		{
			props =  props.substring(0, props.length()-1);
		}
		}
		Props.setText(props);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
//		String str_ScreenSize = "The Android Screen is: " + dm.widthPixels
//				+ " x " + dm.heightPixels;
		// Toast.makeText(videostart.this," "+str_ScreenSize, 1).show();
		File temp = new File(getFileName(obWorkout.getImageAndroid()));
		int maxWidth = dm.widthPixels / 2;
		int maxHight = dm.heightPixels / 3;
		img.setMaxWidth(maxWidth);
		img.setMaxHeight(maxHight);
		if (maxWidth == 640) {
			maxWidth = 500;
		}
		if (maxHight == 240) {
			maxHight = dm.heightPixels / 2;
		}
		if (maxWidth == 240) {
			maxWidth = 120;
		}
		if (maxHight == 106) {
			maxHight = dm.heightPixels / 3;
		}
		// Toast.makeText(getApplicationContext()," "+str_ScreenSize, 1).show();
		//img.setScaleType(ImageView.ScaleType.);
		img.setAdjustViewBounds(true);
		img.invalidate();

		if (temp.exists()) {

			img.setImageURI(Uri.parse(getFileName(obWorkout.getImageAndroid())));
			img.setMaxWidth(maxWidth);
			img.setMaxHeight(maxHight);
			img.getLayoutParams().height = maxHight;
			img.getLayoutParams().width = maxWidth;
			img.setScaleType(ImageView.ScaleType.FIT_XY);
			img.setAdjustViewBounds(true);
			img.invalidate();

		} else {

			try {
				 
    			img.setImageDrawable(grabImageFromUrl(obWorkout.getImageAndroid()));
    			img.getLayoutParams().height = maxHight;
    			img.getLayoutParams().width = maxWidth;
    			img.setScaleType(ImageView.ScaleType.FIT_XY);
    			img.setAdjustViewBounds(true);
    			img.invalidate();
			 
			} catch (Exception e) {

			 
			}

		}
		
		 

	}
 

	private Cursor getRawEvents(String sql) {
		Cursor cursor = null;
		;
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// Log.d("CDA", "onKeyDown Called");
			onBackPressed();
		}

		return super.onKeyDown(keyCode, event);
	}

	public void onBackPressed() {
		try {
			Intent intent = new Intent(videostart.this,
					FitnessforMeActivity.class);
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return;
	}

	public String getFileName(String wholePath) {
		String name = null;
		int start, end;
		start = wholePath.lastIndexOf('/');
		end = wholePath.length(); // lastIndexOf('.');
		name = wholePath.substring((start + 1), end);
		//name = "/sdcard/fitness4meimages/" + name;
		  name = getCacheDir()+"/"+name;
		return name;
	}

	public void getDownLoadedFileCount(ArrayList<String> arrCompletedvideoName) {
		ArrayList<String> arrRet = new ArrayList<String>();
		// Check the file length and downloaded files are same

		int len = arrCompletedvideoName.size();

		// String g = downLoadFileCount+"";
		// String g1 = totalWokoutVideos+"";
		// }
		for (int i = 0; i < len; ++i) {
			String name = arrCompletedvideoName.get(i);
			if (!name.equals("")) {
				arrRet.add(name);
			}

		}

		for (int j = 0; j < arrRet.size(); ++j) {
			String filename = arrRet.get(j);
			  File outputDir = new File(getCacheDir()+"");
			  File new_dir = new File( outputDir + "" );
			//File outputDir = Environment.getExternalStorageDirectory();
			//File new_dir = new File(outputDir + "/fitness4vid");

			File temp = new File(new_dir, filename);

			if (!temp.exists()) {

				downLoadFileCount++;
			} else {
				break;
			}

		}
	}

	public void removeDuplicates(ArrayList<String> list) {
		try {
			HashSet<String> set = new HashSet<String>(list);
			list.clear();
			list.addAll(set);
		} catch (Exception e) {
			String st = e.toString();
			st = st + "";
		}
	}

	private int filedownloadedcount() {
		int count = 0;// arrNextvideoName.size()+arrCompletedvideoName.size()+arrMainvideoName.size()+arrPosterName.size()+arrRecoveryVideoName.size()+arrOtherSideVideoName.size()+arrOtherSidePosterVideoName.size()+arrStopVideoName.size();

		ArrayList<String> finalFilelist = new ArrayList<String>();
		VOWorkoutVideos obvideos;

		for (int i = 0; i < arrVoVideoName.size(); ++i) {
			obvideos = arrVoVideoName.get(i);
			finalFilelist.add(obvideos.getMainvideoName());
			finalFilelist.add(obvideos.getPosterName());
			finalFilelist.add(obvideos.getRecoveryVideoName());
			finalFilelist.add(obvideos.getOtherSideName());
			finalFilelist.add(obvideos.getOtherSidePosterName());
			finalFilelist.add(obvideos.getStopName());
			finalFilelist.add(obvideos.getNextVideoName());
			finalFilelist.add(obvideos.getCompletedVideoName());
		}

		removeDuplicates(finalFilelist);

		for (int i = 0; i < finalFilelist.size(); ++i) {

			File file = null;
			String fname = finalFilelist.get(i);

			if (fname.length() > 4) {
				  File outputDir = new File(getCacheDir()+"");
				  File new_dir = new File( outputDir + "" );
				//File outputDir = Environment.getExternalStorageDirectory();
				//File new_dir = new File(outputDir + "/fitness4vid");
				File temp = new File(new_dir, fname);

				if (!temp.exists()) {
					count++;
				}
			}
		}

		return count;
	}


	class DownloadFile extends AsyncTask<String, String, String> {
		// declare the dialog as a member field of your activity
		String[] paths;
		int current = 0;

		public DownloadFile(String[] paths) {
			super();
			this.paths = paths;
//			  for(int i=0; i<paths.length; i++)
//			  System.out.println((i+1)+":  "+paths[i]);
		}

		@Override
		protected String doInBackground(String... sUrl) {
			int rows = sUrl.length;
			while (current < rows) {
				try {
					if (isCancelled()) {
						break;
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
				}
				try {
					URL url = new URL(this.paths[current]);
					URLConnection connection = url.openConnection();
					connection.setDoOutput(true);
					connection.connect();
					connection.setReadTimeout(15000);
					// this will be useful so that you can show a typical 0-100%
					// progress bar
					int fileLength = connection.getContentLength();

//					String PATH = android.os.Environment
//							.getExternalStorageDirectory().getPath()
//							+ "/"
//							+ "fitness4vid";
					
					 String PATH = getCacheDir()+"";
					File file = new File(PATH);
					// if(!filecheck.exists()) {

					// System.out.println("Current:  "+current+"\t\tRows: "+rows+"tot"+sUrl.length);
					// File file = new File(PATH);
					String[] path = url.getPath().split("/");
					String mp3 = path[path.length - 1];

					String fileName = mp3;

					File outputFile = new File(file, fileName);
					
					FileOutputStream fos = new FileOutputStream(outputFile);
					// download the file
					// InputStream input = new
					// BufferedInputStream(url.openStream());
					outputFile.setReadable(true, false);
					InputStream input = connection.getInputStream();
					byte data[] = new byte[1024];
					long total = 0;
					int count;
					
					while ((count = input.read(data)) != -1) {
						total += count;
						// publishing the progress....
						publishProgress("" + (int) (total * 100 / fileLength));
						fos.write(data, 0, count);
					}
					fos.flush();
					fos.close();
					input.close();
					current++;
				}

				catch (SocketTimeoutException e1) {

					mProgressDialogFile.cancel();
					downloadFile.cancel(true);

					return null;

				}

				catch (Exception e) {
					if (mProgressDialogFile.isShowing()) {
						current++;
						// mProgressDialog.cancel();
						// read.cancel(true);
					}
				}
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialogFile.show();

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (mProgressDialogFile.isShowing()) {
				mProgressDialogFile.cancel();

			}
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			super.onProgressUpdate(progress);
			mProgressDialogFile.setProgress(current);
		}
	}

	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	ProgressDialog dialog ;
	private class TheTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// mProgressDialogFile.show();

			try {
				if (GlobalData.allPurchased == true) {

					adView.setVisibility(View.GONE);
				} else {

					(new Thread() {
						public void run() {
							adView.setVisibility(View.VISIBLE);
							//adView.loadAd(new AdRequest());
						}
					}).start();

				}
			} catch (Exception e) {
				String st = e.toString();
				st = st + "";
			}
		}
		@Override
		protected Void doInBackground(Void... params) {
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// if(mProgressDialogFile.isShowing())
			// mProgressDialogFile.cancel();
//			if( dialog.isShowing() )
//			{
//				dialog.cancel();
//			}
		}
	}

}
