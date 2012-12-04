/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.data.pack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.data.fitness4me.R;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class ViewVideo extends Activity implements
		MediaController.MediaPlayerControl {
	private  PlaceDataSQL placeData;
	private VideoView mVideoView;
	private String current;
	private String filename;
	private String UserName;
	private String workoutID;
	private int count = 1;
	private int FileRepeateCountFinal = 0;
	private String userID = "0";
	private MediaController mycontroller;
	private int FileTempRep = 1;
	private ArrayList<Object> videoPathes;
	private Cursor cursorRepeat;
	private ArrayList<VOWorkoutVideos> arrVoVideoName;
	private Button btnQuit;
	private int checkfileflag = 1;
	private String YesString = "Yes";
	private String NoString = "No";
	View l;
	private long starttime = 0l;
	private String QuitString = "Quit";
	private String videourl;
	private String strLeaveVideo = "You are about to exit the Video. Are you sure ?";
	private VOUserDetails obUser = new VOUserDetails(this);
private Timer myTimer;
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		

	 
	}
			 /**
				 * Get raw data
				 * 
				 * @param String 
				 * @return  Cursor
				 */
				private Cursor getRawEvents(String sql) {
					SQLiteDatabase db = (placeData).getReadableDatabase();
					Cursor cursor = null;
					try {
						cursor = db.rawQuery(sql, null);

						startManagingCursor(cursor);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return cursor;
				}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		insertDurationData(workoutID, starttime + "");
		// }
		if (mVideoView != null) {
			mVideoView.pause();
			//mVideoView = null;
		}
		starttime = 0l;
		
		
		if (GlobalData.viewvideochange == 0) {
			try {
//				Intent intent = new Intent(ViewVideo.this,
//						FitnessforMeActivity.class);
//				startActivity(intent);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		try {
			
			
			new deleteFiles().execute("deleteFile");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	if(mVideoView !=null)
	mVideoView.start();
}
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.videoplay);
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		filename = extras.getString("workoutname");
		workoutID = extras.getString("workoutID");
		UserName = extras.getString("UserName");
		userID = extras.getString("userID");
		// l= (View)findViewById(R.id.btnnavigation);
		btnQuit = (Button) findViewById(R.id.headeQuitricon);
		// btnplay =(Button)findViewById(R.id.btnplay);
		placeData = new PlaceDataSQL(this);
		GlobalData.appcount++;
		if (arrVoVideoName == null)
			arrVoVideoName = new ArrayList<VOWorkoutVideos>();
		GlobalData.viewvideochange = 0;
		if (videoPathes == null)
			videoPathes = new ArrayList<Object>();
		
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.flags |= LayoutParams.FLAG_KEEP_SCREEN_ON;
		 
		// params.screenBrightness = 10;
		getWindow().setAttributes(params);
		if (obUser.getSelectedLanguage().equals("1")) {
			YesString = "Yes";
			strLeaveVideo = "You are about to exit the Video. Are you sure ?";
			NoString = "No";
		} else {
			YesString = "Ja";
			QuitString = "verlassen";
			strLeaveVideo = "Du bist dabei das Video zu beenden, bist Du sicher ?";
			NoString = "Nein";
		}
		count = 1;
		// Toast.makeText(getBaseContext(), "countcountcountcount"
		// +count,Toast.LENGTH_LONG).show();
		// btnQuit =(Button) findViewById(R.id.headeQuitricon);
		btnQuit.setText(QuitString);
		btnQuit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent intent = new Intent(sharescreen.this,
				// HomeScreen.class);
				// // intent.putExtra("userID", userID);
				// startActivity(intent);

				AlertDialog alertDialog = new AlertDialog.Builder(
						ViewVideo.this).create();
				if (mVideoView.isPlaying()) {
					mVideoView.pause();
				}

				alertDialog.setTitle("fitness4.me");
				alertDialog.setMessage(strLeaveVideo);

				alertDialog.setButton(-1, YesString,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									GlobalData.viewvideochange = 1;
									Intent intent = new Intent(ViewVideo.this,
											FitnessforMeActivity.class);
									startActivity(intent);
									if(mVideoView !=null)
									mVideoView.stopPlayback();

								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						});
				alertDialog.setButton(-2, NoString,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								GlobalData.viewvideochange = 0;

								mVideoView.start();

								dialog.cancel();
							}
						});
				try {
					alertDialog.show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		mycontroller = new MediaController(this);
		mVideoView = (VideoView) findViewById(R.id.surface_view);
		mVideoView.setMediaController(null);
		AdView adView = (AdView) this.findViewById(R.id.adView);
		if (GlobalData.allPurchased == true) {

			adView.setVisibility(View.GONE);
		} else {
			adView.setVisibility(View.VISIBLE);
			adView.loadAd(new AdRequest());

		}

		 try {
		 myTimer = new Timer();
		 myTimer.schedule(new TimerTask() {
			
			 
		 @Override
		 public void run() {
		 TimerMethod();
		 }
		
		 }, 1, 1000);
		 } catch (Exception e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }

		mVideoView.setOnCompletionListener(myVideoViewCompletionListener);
		getDataAndPopulate();

	}

	private void TimerMethod()
	{
	 
		this.runOnUiThread(Timer_Tick);
	}
	
	
	private Runnable Timer_Tick = new Runnable() {
		public void run() {

			//This method runs in the same thread as the UI.    	       

			//Do something to the UI thread here
			if(mVideoView!=null)
			{
				if(mVideoView.isPlaying())
				{
					starttime++;


				}
				else
				{
					
				 
					//Toast.makeText(getBaseContext(),"\n stop : "+prefs.getPreference("duration"),1).show();
					


					//FitnessWorkoutDuration
					
					//Toast.makeText(getBaseContext(),"\n stop : "+starttime,1).show();
				}
			}	 
		}
	};

 
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mycontroller == null) {
			mycontroller = new MediaController(this);
		}
		mVideoView.setMediaController(mycontroller);
		mycontroller.show();
		// }

		return true;

	}

	/**
	 * Get and populate data
	 * 
	 * @return
	 */
	private void getDataAndPopulate() {

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

			VOWorkoutVideos obvideos;
			if (videourl == null)
				videourl = getResources().getString(R.string.videourl);
			for (int i = 0; i < arrVoVideoName.size(); ++i) {

				obvideos = arrVoVideoName.get(i);

				getVideoSource(obvideos.getPosterUrl(),
						obvideos.getPosterName(),obvideos.getPosterSize());
				getVideoSource(obvideos.getMainvideoUrl(),
						obvideos.getMainvideoName(),obvideos.getMainVideoSize());
				getVideoSource(obvideos.getStopVideo(), obvideos.getStopName(),"-11");
				getVideoSource(obvideos.getOtherSidePosterVideo(),
						obvideos.getOtherSidePosterName(),obvideos.getOtherSideSize());

				getVideoSource(obvideos.getOtherSideVideo(),
						obvideos.getOtherSideName(),obvideos.getOtherSideSize());
				getVideoSource(obvideos.getRecoveryVideo(),
						obvideos.getRecoveryVideoName(),"-11");
				getVideoSource(obvideos.getNextVideo(),
						obvideos.getNextVideoName(),"-11");
				getVideoSource(obvideos.getCompletedVideo(),
						obvideos.getCompletedVideoName(),"-11");
			}
			int cnt = 1;
			if (cnt != -120) {

				try {

					// String filenameArray[] =
					// arrMainvideoName.get(i).split("\\.");
					// String extension = filenameArray[filenameArray.length-1];
					// String filename = filenameArray[0];

					if (videoPathes.size() > 0) {
						VOVideo FileObject = (VOVideo) videoPathes.get(0);
						String FileUrlName = FileObject.videoUrl;
						File filecheck = new File(FileUrlName);
						if (filecheck.exists()) {

							playVideo(filecheck.getPath(), filename);

						}
					} else {
						try {
							Intent intent = new Intent(ViewVideo.this,
									FitnessforMeActivity.class);
							startActivity(intent);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					// Log.i("progress",e.toString());
				}
			} else {
				String text = "";
				if (obUser.getSelectedLanguage().equals("1")) {
					text = "No internet connection! \nPlease connect internet to download files";
				} else {
					text = "Es besteht keine Internetverbindung - bitte versuche es später. \n Bitte verbinden Sie Internet zum Herunterladen von Dateien";
				}
				Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG)
						.show();

				try {
					Intent intent = new Intent(ViewVideo.this, videostart.class);
					intent.putExtra("workoutID", GlobalData.selectedWorkOutID);
					intent.putExtra("userID", obUser.getUserId());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		// }
		catch (Exception e) {
			String st = e.toString();
		} finally {
			cursor.close();
		}

	}

	public String getFileName(String wholePath) {
		String name = null;
		int start, end;
		start = wholePath.lastIndexOf('/');
		end = wholePath.length(); // lastIndexOf('.');
		name = wholePath.substring((start + 1), end);
		//name = Environment.getExternalStorageDirectory().getPath()+"/fitness4vid/" + name;
		  name = getCacheDir()+"/"+name;
		return name;
	}

	private void getVideoSource(String path, String fname ,String Size) throws IOException {
		// path = videourl+path;

			VOVideo VO = new VOVideo();
			VO.videoUrl = getFileName(path);
			VO.filename = fname;
			VO.repeate = "1";
			VO.size = Size;
			if (VO.videoUrl.length() > 4) {
				 File outputDir = new File(getCacheDir()+"");
				  File new_dir = new File( outputDir + "" );
			//	File outputDir = Environment.getExternalStorageDirectory();
				//File new_dir = new File(outputDir + "/fitness4vid");
				File temp = new File(new_dir, fname);

				if (temp.exists()) {
					
					long fSize = temp.length();
					
					if (fname.equalsIgnoreCase("completed_exercise_de.mp4")
							|| fname
							.equalsIgnoreCase("completed_exercise.mp4")

						||  fname.equalsIgnoreCase("next_exercise_de.mp4")
						||  fname.equalsIgnoreCase("next_exercise.mp4")
						||  fname
							.equalsIgnoreCase("otherside_exercise_de.mp4")
						||  fname
							.equalsIgnoreCase("otherside_exercise.mp4")
						||  fname.equalsIgnoreCase("recovery_15_de.mp4")
						||  fname.equalsIgnoreCase("recovery_15.mp4")
						||  fname.equalsIgnoreCase("recovery_30_de.mp4")
						||  fname.equalsIgnoreCase("recovery_30.mp4")
						||  fname.equalsIgnoreCase("stop_exercise_de.mp4")
						||  fname.equalsIgnoreCase("stop_exercise.mp4")

			)
					{
						videoPathes.add(VO);
					}
						
					else if(!Size.equals("-11") && VO.size.equals(fSize+""))
					{
						videoPathes.add(VO);
					}
					else
					{
						try {
							temp.delete();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}


		}
	}

	public void removeDuplicates(ArrayList<String> list) {
		HashSet<String> set = new HashSet<String>(list);
		list.clear();
		list.addAll(set);
	}

	/**
	 * Get raw data
	 * 
	 * @param String
	 * @return Cursor
	 */
	private Cursor getRawcursorsExcersiceEvents(String table) {
		SQLiteDatabase db = (placeData).getReadableDatabase();
		Cursor cursor = db.query(table,
				new String[] { "WorkoutId", "MainvideoUrl", "MainvideoName",
						"MainvideoRepeatCount", "PosterUrl", "PosterName",
						"PosterRepeatCount", "RecoveryVideo",
						"RecoveryVideoName", "StopVideo", "StopName",
						"StopRepeatCount", "OtherSidePosterVideo",
						"OtherSidePosterName", "OtherSidePosterRepeatCount",
						"OtherSideVideo", "OtherSideName",
						"OtherSideRepeatCount", "NextVideo", "NextVideoName",
						"NextRepeatCount", "CompletedVideo",
						"CompletedVideoName", "CompletedRepeatCount","MainVideoSize","PosterSize","OtherSideSize"},
				"WorkoutId = ?", new String[] { String.valueOf(workoutID) },
				null, null, null);

		startManagingCursor(cursor);
		return cursor;
	}


	MediaPlayer.OnCompletionListener myVideoViewCompletionListener = new MediaPlayer.OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer arg0) {

			int nextScreen = videoPathes.size();
			if (nextScreen == count) {
				GlobalData.upgradepopupshow = false;
				try {
					Intent intent = new Intent(ViewVideo.this, endscreen.class);
					intent.putExtra("userID", userID);
					intent.putExtra("workoutID", workoutID);
					intent.putExtra("UserName", UserName);
					startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {

				try {


					VOVideo FileTempObject = (VOVideo) videoPathes.get(count);
					String FileTempUrlName = FileTempObject.videoUrl;
					FileTempUrlName = FileTempObject.videoUrl;
					checkfileflag = 1;

					// }

					if (checkfileflag == 1) {
						File filecheck = new File(FileTempUrlName);
						filecheck.setReadable(true,false);
						if (filecheck.exists()) {
							// if(FileRepeateCountFinal==0)
							// {
							count++;

							FileTempRep = Integer
									.parseInt(FileTempObject.repeate);
							// count = (count + 1) % videoPathes.size();
							// mVideoView.setVideoPath(FileTempUrlName);
							mVideoView.setVideoURI(Uri.parse(FileTempUrlName));
							// }
							// Toast.makeText(getBaseContext(),"\n count : "+count+"int nextScreen"+nextScreen,1).show();
							// playVideo(filecheck.getPath(),filename);
							FileRepeateCountFinal++;
							mVideoView.start();
							mVideoView.requestFocus();

						}

						if (FileRepeateCountFinal == FileTempRep) {

							FileRepeateCountFinal = 0;
						}
					} else {
						// Toast.makeText(getBaseContext(),"\n count : "+count+"file "+FileTempUrlName,1).show();
						count++;

						// count = (count + 1) % videoPathes.size();
						mVideoView.setVideoURI(Uri.parse(FileTempUrlName));

						// playVideo(filecheck.getPath(),filename);
						FileRepeateCountFinal++;
						mVideoView.start();
						mVideoView.requestFocus();

						mVideoView.setMediaController(null);

					}

				} catch (Exception e) {
					// TODO: handle exception
					String st = e.toString();
					Toast.makeText(getBaseContext(), " sterror " + st,
							Toast.LENGTH_LONG).show();

				}

			}
		}
	};

	private void playVideo(String fileurl, String fname) {

		try {
			String path = fileurl;
			// Log.v(TAG, "path: " + path);
			if (path == null || path.length() == 0) {
				Toast.makeText(ViewVideo.this, "File URL/path is empty",
						Toast.LENGTH_LONG).show();

			} else {
				// If the path has not changed, just start the media player
				if (path.equals(current) && mVideoView != null) {
					mVideoView.start();
					mVideoView.requestFocus();
					return;
				}
				current = path;

				mVideoView.setVideoURI(Uri.parse(path));
				mVideoView.start();
				mVideoView.requestFocus();

			}
		} catch (Exception e) {
			// Log.e(TAG, "error: " + e.getMessage(), e);
			if (mVideoView != null) {
				mVideoView.stopPlayback();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// Log.d("CDA", "onKeyDown Called");
			 //  onBackPressed();
		}

		return false;// super.onKeyDown(keyCode, event);
	}

	public void onBackPressed() {
		// Log.d("CDA", "onBackPressed Called");
		try {

			Intent intent = new Intent(ViewVideo.this, endscreen.class);
			intent.putExtra("userID", obUser.getUserId());
			intent.putExtra("workoutID", workoutID);
			intent.putExtra("UserName", obUser.getSelectedUserName());
			startActivity(intent);

		} catch (Exception e) {
			// TODO: handle exception
		}

		return;
	}

	/**
	 * Insert data into table
	 * 
	 * @param String
	 *            , String , String , String , String ,String ,String
	 * @return
	 */
	private void insertDurationData(String id, String duration) {
		try {
			SQLiteDatabase db = placeData.getWritableDatabase();
			ContentValues values;
			values = new ContentValues();
			values.put("ID", id);
			values.put("Duration", duration);
			db.insert("FitnessWorkoutDuration", null, values);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	 
	@Override
	public boolean canPause() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canSeekBackward() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canSeekForward() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getBufferPercentage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCurrentPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void seekTo(int pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	

	class deleteFiles extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			

			 if(CheckNetworkAvailability.isNetworkAvailable(getApplicationContext()) && obUser.getSelectedLanguage().length() > 0)
				{ 
					String stUrl = 	  getResources().getString(R.string.videourl);
					String urlString =  getResources().getString(R.string.servername);
					JSONObject json = JSONfunctions.getJSONfromURL(urlString+"allvideos=yes&duration=10&user_level="+obUser.getLevel()+"&lang="+obUser.getSelectedLanguage());
					 
					  ArrayList<String> arrintrovideoUrl = new ArrayList<String>();
					  ArrayList<String> arrMainvideoUrl = new ArrayList<String>();
					  ArrayList<String> arrMainOthervideoUrl = new ArrayList<String>();
					    ArrayList<String> arrvideoUrl = new ArrayList<String>();
					if(json !=null )
					{
						JSONArray workoutList;
						try {
							workoutList = json.getJSONArray("video");
							 

							 
							for(int loop=0;loop<workoutList.length();loop++){						
								//	//HashMap<String, String> map = new HashMap<String, String>();	
								JSONObject json_data = workoutList.getJSONObject(loop);
								if(json_data.getString("intro_name").length() > 0)
								 {
								 arrintrovideoUrl.add(stUrl+json_data.getString("intro_name"));
								 }
								 if(json_data.getString("main_name").length() > 0)
								 {
								 arrMainvideoUrl.add(stUrl+json_data.getString("main_name"));
								 }
								 if(json_data.getString("main_other_name").length() > 0)
								 {
								 arrMainOthervideoUrl.add(stUrl+json_data.getString("main_other_name"));
								 }
							 
							}
							
							
							
							for (int i = 0; i < arrintrovideoUrl.size(); i++) {
								
								File file = null;   
								String fname = arrintrovideoUrl.get(i);


								String name=null;
								int start,end;
								start=fname.lastIndexOf('/');
								end=fname.length();     //lastIndexOf('.');
								name=fname.substring((start+1),end);
								 
								 


								if(name.length() > 4)
								{
									 
										arrvideoUrl.add(name);
									
								}
							    }
							for (int j = 0; j < arrMainvideoUrl.size(); j++) {
								File file = null;   
								String fname = arrMainvideoUrl.get(j);


								String name=null;
								int start,end;
								start=fname.lastIndexOf('/');
								end=fname.length();     //lastIndexOf('.');
								name=fname.substring((start+1),end);
								 

								if(name.length() > 4)
								{
									 
										arrvideoUrl.add(name);
									
								}
							}
							for (int k = 0; k < arrMainOthervideoUrl.size(); k++) {
								File file = null;   
								String fname = arrMainOthervideoUrl.get(k);


								String name=null;
								int start,end;
								start=fname.lastIndexOf('/');
								end=fname.length();     //lastIndexOf('.');
								name=fname.substring((start+1),end);
								 
								if(name.length() > 4)
								{
									 
										arrvideoUrl.add(name);
									
								}
							
							}
							 
							
							removeDuplicates(arrvideoUrl);
							 Util.clearCacheVideos(getApplicationContext(),arrvideoUrl);
							
							
							
		}catch (Exception e) {
			// TODO: handle exception
		}
					}
			
			
		 
		}
			return null; 
	}
	}

		  
		    
	
	
}
