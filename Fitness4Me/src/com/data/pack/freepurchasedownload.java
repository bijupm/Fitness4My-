package com.data.pack;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.data.fitness4me.R;
import com.data.pack.HomeScreen.DownloadImageFileAsync;
import com.data.pack.videostart.DownloadFile;
import com.google.ads.AdRequest;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class freepurchasedownload  extends Activity{
	private ArrayList<String> arrintrovideoUrl = new ArrayList<String>();
	private ArrayList<String> arrMainvideoUrl = new ArrayList<String>();
	private ArrayList<String> arrMainOthervideoUrl = new ArrayList<String>();
	private ArrayList<String> arrvideoUrl = new ArrayList<String>();
	private int valCount =0;
	private Prefs prefs = new Prefs(this);
	public ProgressDialog mProgressDialog;
	private static PlaceDataSQL placeData;
	private String builderStr="";
	private String  mess ="";
	private VOUserDetails obUser =  new VOUserDetails(this);
	private DownloadImageFileAsync dloadFAsync;
	private ArrayList<String> m_items;
	ProgressDialog dialog ;
	Cursor cursorList = null;
	// instantiate it within the onCreate method
	@Override
	protected void onDestroy(){

		//you may call the cancel() method but if it is not handled in doInBackground() method

		if (read != null && read.getStatus() != AsyncTask.Status.FINISHED)

			read.cancel(true);
		if (m_items != null)
			m_items = null;
		super.onDestroy();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 new insertFulldata().execute("insertFulldata");
		 if(!dialog.isShowing())
			dialog.show();
			 
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.freepurchasedownload);
		//Button imgdownloadButton = (Button) findViewById(R.id.imgdownloadButton);
		
		
		if(obUser.getSelectedLanguage().equals("1"))
		{
//			String displayLevel="";
//			                 
//			if(obUser.getLevel().equals("1"))
//			{
//				displayLevel="Beginner";
//			}
//			else if(obUser.getLevel().equals("2"))
//			{
//				displayLevel="Advanced";
//			}
//			else
//			{
//				displayLevel = "Expert";
//			}

			
			mess ="Please wait while I am downloading your 5 free workouts.\nDepending on your internet connection this may take several minutes.";
			builderStr = "Please wait..";
		}
		else
		{
//			String displayLevel="";
//			              
//			if(obUser.getLevel().equals("1"))
//			{
//				displayLevel="Anfänger";
//			}
//			else if(obUser.getLevel().equals("2"))
//			{
//				displayLevel="Fortgeschritten";
//			}
//			else
//			{
//				displayLevel = "Profi";
//			}

			
			mess="Deine kostenlosen Workouts werden heruntergeladen.\n"+
				"Je nach Qualität deiner Internetverbindung kann dies einige Minuten dauern";
			builderStr = "Bitte warten ..";
		}
		 
		dialog = ProgressDialog.show(freepurchasedownload.this, "",
				builderStr, true);
			 
		mProgressDialog = new ProgressDialog(freepurchasedownload.this);
		mProgressDialog.setMessage(mess);
		mProgressDialog.setTitle("www.fitness4.me");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(false);
		
		
		 arrintrovideoUrl = new ArrayList<String>();
		 arrMainvideoUrl = new ArrayList<String>();
		 arrMainOthervideoUrl = new ArrayList<String>();
		  arrvideoUrl = new ArrayList<String>();
		 arrvideoUrl = new ArrayList<String>();
		 placeData = new PlaceDataSQL(this);
		 
		 if (m_items == null)
				m_items = new ArrayList<String>();
		 
		//		imgdownloadButton.setOnClickListener(new OnClickListener(){
		//			
		//			@Override
		//			public void onClick(View v) {
		//				Intent intent = new Intent(fulldownload.this,FitnessforMeActivity.class);
		//				intent.putExtra("userID", GlobalData.selectedUserID);
		//				intent.putExtra("UserName", GlobalData.selectedUserName);
		//				startActivity(intent);
		//		}


		//});

		 try
			{

				WindowManager.LayoutParams params = getWindow().getAttributes();
				params.flags |= LayoutParams.FLAG_KEEP_SCREEN_ON;
				//	params.screenBrightness = 10;
				getWindow().setAttributes(params);

		 
			read.execute("freedownload");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	AsyncTask<String, Void, String> read = new AsyncTask<String, Void, String>() {

		@Override
		protected void onPreExecute() {


			super.onPreExecute();
			
			
			
		}

		@Override
		protected String doInBackground(String... params) {


				return "done";
		}
		public void removeDuplicates(ArrayList<String> list) {
			try
			{
				HashSet<String>  set = new HashSet<String>(list);
				list.clear();
				list.addAll(set);
			}
			catch (Exception e) {
				String st = e.toString();
				st= st+"";
			}
		}
		

		private int filedownloadedcount(ArrayList<String> list)
		{
			int count = 0;//arrNextvideoName.size()+arrCompletedvideoName.size()+arrMainvideoName.size()+arrPosterName.size()+arrRecoveryVideoName.size()+arrOtherSideVideoName.size()+arrOtherSidePosterVideoName.size()+arrStopVideoName.size();	 


			ArrayList<String> finalFilelist  = new ArrayList<String>();


			for(int i=0;i<arrvideoUrl.size();++i)
			{
				finalFilelist.add(list.get(i));
			}


			removeDuplicates(finalFilelist);

			for(int i=0;i<finalFilelist.size();++i)
			{

				File file = null;   
				String fname = finalFilelist.get(i);


				String name=null;
				int start,end;
				start=fname.lastIndexOf('/');
				end=fname.length();     //lastIndexOf('.');
				name=fname.substring((start+1),end);




				if(name.length() > 4)
				{
									File outputDir = new File(getCacheDir()+"");
									File new_dir = new File( outputDir + "" );
//					File outputDir = Environment.getExternalStorageDirectory();
//					File new_dir = new File( outputDir + "/fitness4vid" );
					File temp = new File(new_dir,name);

					if(!temp.exists() ) 
					{
						count++;
					}
				}
			}



			return count;	
		}





		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(CheckNetworkAvailability.isNetworkAvailable(getApplicationContext()) && obUser.getSelectedLanguage().length() > 0)
			{ 
				String stUrl = 	  getResources().getString(R.string.videourl);
				String urlString =  getResources().getString(R.string.servername);
				JSONObject json = JSONfunctions.getJSONfromURL(urlString+"freevideos=yes&duration=10&user_level="+obUser.getLevel()+"&lang="+obUser.getSelectedLanguage());


				if(json !=null )
				{
					JSONArray workoutList;
					try {
						workoutList = json.getJSONArray("video");



						for(int loop=0;loop<workoutList.length();loop++){						
							//	//HashMap<String, String> map = new HashMap<String, String>();	
							JSONObject json_data = workoutList.getJSONObject(loop);
							if(json_data.getString("intro").length() > 0)
							{
								arrintrovideoUrl.add(stUrl+json_data.getString("intro"));
							}
							if(json_data.getString("main").length() > 0)
							{
								arrMainvideoUrl.add(stUrl+json_data.getString("main"));
							}
							if(json_data.getString("main_other").length() > 0)
							{
								arrMainOthervideoUrl.add(stUrl+json_data.getString("main_other"));
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
								
								File outputDir = new File(getCacheDir()+"");
								File new_dir = new File( outputDir + "" );
//								File outputDir = Environment.getExternalStorageDirectory();
//								File new_dir = new File( outputDir + "/fitness4vid" );
								File temp = new File(new_dir,name);

								if(!temp.exists() ) 
									arrvideoUrl.add(arrintrovideoUrl.get(i));

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
								File outputDir = new File(getCacheDir()+"");
								File new_dir = new File( outputDir + "" );
//								File outputDir = Environment.getExternalStorageDirectory();
//								File new_dir = new File( outputDir + "/fitness4vid" );
								File temp = new File(new_dir,name);

								if(!temp.exists() ) 
									arrvideoUrl.add(arrMainvideoUrl.get(j));
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
								File outputDir = new File(getCacheDir()+"");
								File new_dir = new File( outputDir + "" );
//								File outputDir = Environment.getExternalStorageDirectory();
//								File new_dir = new File( outputDir + "/fitness4vid" );
								File temp = new File(new_dir,name);

								if(!temp.exists() ) 
									arrvideoUrl.add(arrMainOthervideoUrl.get(k));
							} 

						}


						removeDuplicates(arrvideoUrl);
						valCount = filedownloadedcount(arrvideoUrl);








						mProgressDialog.setMax(valCount);
						String[] url = new String[arrvideoUrl.size()];
						for (int index = 0; index < arrvideoUrl.size(); index++) {
							try
							{
								//Download myDownload =  new Download(Settings.this,arrvideoUrl.get(index));

								if(isCancelled()){
									break;
								}
								//  new Download(fulldownload.this,arrvideoUrl.get(index)).execute();

								//								 DownloadFile downloadFile = new DownloadFile();
								//								downloadFile.execute(arrvideoUrl.get(index)); 
								//								myDownload.execute();
								//	myDownload.cancel(true);


								url[index] = arrvideoUrl.get(index);

							}
							catch (Exception e) {
								String st = e.toString();
								st = st+"";
								read.cancel(true);
							}
						} 
						

						 
						
						 
							try {
								
								
								DownloadFile downloadFile = new DownloadFile(url);
								downloadFile.execute(url);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						 
						
						

					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						read.cancel(true);
					} 

				} 
			}

		}
	};




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
	class DownloadFile  extends AsyncTask<String, String, String> {
		// declare the dialog as a member field of your activity
		String[] paths;
		int current=0;
		public DownloadFile(String[] paths) {
			super();
			this.paths = paths;
//			for(int i=0; i<paths.length; i++)
//				System.out.println((i+1)+":  "+paths[i]);
		}
		@Override
		protected String doInBackground(String... sUrl) {
			int rows = sUrl.length;
			while(current < rows)
			{

				try {
					URL url = new URL(this.paths[current]);
					URLConnection connection = url.openConnection();
					connection.setDoOutput(true);
					connection.setReadTimeout(15000);
					connection.connect();
					// this will be useful so that you can show a typical 0-100% progress bar
					int fileLength = connection.getContentLength();
					 String PATH = getCacheDir()+"";
					//String PATH =  android.os.Environment.getExternalStorageDirectory().getPath() +"/"+ "fitness4vid";
					//  System.out.println("Current:  "+current+"\t\tRows: "+rows+"tot"+sUrl.length);
					File file = new File(PATH);
					String[] path = url.getPath().split("/");
					String mp3 = path[path.length - 1];

					String fileName = mp3;

					File outputFile = new File(file , fileName);
					FileOutputStream fos = new FileOutputStream(outputFile);
					// download the file
					//   InputStream input = new BufferedInputStream(url.openStream());
					outputFile.setReadable(true,false);
					InputStream input = connection.getInputStream();
					byte data[] = new byte[1024];
					long total = 0;
					int count;
					while ((count = input.read(data)) != -1) {
						total += count;
						// publishing the progress....
						publishProgress(""+(int) (total * 100 / fileLength));
						fos.write(data, 0, count);
					}
					fos.flush();
					fos.close();
					input.close();
					current++;
				} 
				catch (SocketTimeoutException e1) {
					
					 mProgressDialog.cancel();
					 read.cancel(true);
					 try {
						Intent intent = new Intent(freepurchasedownload.this,HomeScreen.class);
							startActivity(intent);
							GlobalData.freedownloadcomplete = true;
							prefs.setPreference("disconnectfreepurchase", "true");
							prefs.setPreference("freedownloadcomplete", ""+GlobalData.freedownloadcomplete);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 return  null;
					
				}
				catch (Exception e) {
					
					if( mProgressDialog.isShowing() )
					{
						current++;
						//mProgressDialog.cancel();
						//read.cancel(true);
					}
				}
			}
			return null;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//			mProgressDialog.setMessage(builderStr);
//			mProgressDialog.setTitle("");
		//	mProgressDialog.show();
			
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			 if( dialog.isShowing() )
				{
					dialog.cancel();
				}
			if( mProgressDialog.isShowing() )
			{
				mProgressDialog.cancel();
				read.cancel(true);
				try {
					Intent intent = new Intent(freepurchasedownload.this,HomeScreen.class);
					startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				GlobalData.freedownloadcomplete = true;
				prefs.setPreference("disconnectfreepurchase", "false");
				prefs.setPreference("freedownloadcomplete", ""+GlobalData.freedownloadcomplete);
				
				
				 
		             // My AsyncTask is done and onPostExecute was called
		          
		        
			}
		}
		@Override
		protected void onProgressUpdate(String... progress) {
			super.onProgressUpdate(progress);
//			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//			mProgressDialog.setMessage(mess);
			 
			 if( dialog.isShowing() )
			{
				dialog.cancel();
			}
			if(!mProgressDialog.isShowing())
				mProgressDialog.show();
			mProgressDialog.setProgress(current);
		}
	}

	class insertFulldata  extends AsyncTask<String, String, String> {
		// declare the dialog as a member field of your activity
		 
 
		@Override
		protected String doInBackground(String... sUrl) {
			if(CheckNetworkAvailability.isNetworkAvailable(getApplicationContext()))
			{ 
				String stUrl = 	  getResources().getString(R.string.videourl);
				String urlString =  getResources().getString(R.string.servername);
				JSONObject json = JSONfunctions.getJSONfromURL(urlString+"allworkouts=yes&duration=10&user_level="+obUser.getLevel()+"&lang="+obUser.getSelectedLanguage());


				if(json !=null )
				{
					JSONArray workoutList;
					try {
						workoutList = json.getJSONArray("workoutvideos");
						 
						if(placeData !=null)
						{
							try {
								SQLiteDatabase db = placeData.getWritableDatabase();


								db.delete("FitnessWorkoutVideos", null,
										null);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}


						}
						 
					        for(int i = 0; i < workoutList.length(); i++){
					        	JSONArray workoutLis   = workoutList.getJSONArray(i);
					        	  for(int j = 0; j < workoutLis.length(); j++){
					        		  
					        	  
					        	JSONObject e =workoutLis.getJSONObject(j);
					            String value =e.getString("workout_id");
					          //  System.out.println("workout_id"+value);
					            
					            insertWorkOutVideoData(value,e.getString("poster_name"),e.getString("poster_video"),e.getString("poster_rep"),e.getString("main_video"),e.getString("video_name"),e.getString("main_rep"),e.getString("recovery_video"),e.getString("recovery_video_name")
										,e.getString("stop_name"),e.getString("stop_video"),e.getString("stop_rep") ,
										e.getString("otherside_postername"),e.getString("otherside_poster"),e.getString("otherside_posterrep"),
										e.getString("otherside_name"),e.getString("otherside_video"),e.getString("otherside_rep"),
										e.getString("next_name"),e.getString("next_video"),e.getString("next_rep"),
										e.getString("completed_name"),e.getString("completed_video"),e.getString("completed_rep"),
										e.getString("poster_size"),e.getString("main_video_size"),	e.getString("otherside_size")
										
										
										
										
								);
					            
					            
					        }
					           
					        }
//						
//						 JSONArray names= json.names();
//						    JSONArray values = json.toJSONArray(names);
//						    for(int i = 0 ; i < values.length(); i++){
//						    	String st = values.getString(i);
//						    }   
//						
						
					//	int lenlist = workoutList.length();
//						for(int i = 0; i < len; ++i) {
//							 
//							JSONArray workoutListtest = workoutList.getJSONArray(0);
////							 JSONObject j_obj ;
////						     j_obj = workoutListtest.getJSONArray(i); 
////						    String location_id = j_obj.getString("workout_id");
////						   System.out.println("workout_id"+location_id);
//						}
//						 
						 
					}
					catch (Exception e) {
						String st = e.toString();
					st = st+"";	
					}
				}
			}	
			return null;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			//mProgressDialog.show();
			 
			
			
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

//			if( mProgressDialog.isShowing() )
//			{
//				mProgressDialog.cancel();
//			}
//			
//			
			if (CheckNetworkAvailability.isNetworkAvailable(freepurchasedownload.this)) {
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
			
		}
		@Override
		protected void onProgressUpdate(String... progress) {
		 
		}
		
		/**
		 * Insert data into table
		 * 
		 * @param String , String , String , String 
		 * @return  
		 */ 
		private void insertWorkOutVideoData(String id, String poster_name, String poster_video, String poster_rep,String main_video,String video_name,String main_rep,String RecoveryVideo,String RecoveryVideoName,String StopName , String StopVideo ,String StopRepeatCount ,String OtherSidePosterName , String OtherSidePosterVideo ,String OtherSidePosterRepeatCount
				,String OtherSideName , String OtherSideVideo ,String OtherSideRepeatCount
				,String NextVideoName , String NextVideo ,String NextRepeatCount
				,String CompletedVideoName , String CompletedVideo ,String CompletedRepeatCount,String posterSize,String mainSize,String OtherSideSize
		) {
			try {
				SQLiteDatabase db = placeData.getWritableDatabase();
				ContentValues values;
				values = new ContentValues();
				values.put("WorkoutID ", id);
				String videourl="";
				if(main_video.equals(""))
				{
					values.put("MainvideoUrl", main_video);
				}
				else
				{
					values.put("MainvideoUrl", videourl+main_video);
				}


				values.put("MainvideoName", video_name);
				values.put("MainvideoRepeatCount", main_rep);
				if(poster_video.equals(""))
				{
					values.put("PosterUrl",poster_video);	
				}
				else
				{

					values.put("PosterUrl", videourl+poster_video);	
				}

				values.put("PosterName", poster_name);
				values.put("PosterRepeatCount", poster_rep);


				if(RecoveryVideo.equals(""))
				{
					values.put("RecoveryVideo", RecoveryVideo);
				}
				else
				{
					values.put("RecoveryVideo", videourl+RecoveryVideo);
				}


				values.put("RecoveryVideoName", RecoveryVideoName);

				if(StopVideo.equals(""))
				{
					values.put("StopVideo", StopVideo);
				}
				else
				{
					values.put("StopVideo", videourl+StopVideo);
				}


				values.put("StopName", StopName);
				values.put("StopRepeatCount", StopRepeatCount);

				if(OtherSidePosterVideo.equals(""))
				{
					values.put("OtherSidePosterVideo", OtherSidePosterVideo);
				}
				else
				{
					values.put("OtherSidePosterVideo", videourl+OtherSidePosterVideo);
				}

				values.put("OtherSidePosterName", OtherSidePosterName);
				values.put("OtherSidePosterRepeatCount", OtherSidePosterRepeatCount);

				if(OtherSideVideo.equals(""))
				{
					values.put("OtherSideVideo", OtherSideVideo);
				}
				else
				{
					values.put("OtherSideVideo", videourl+OtherSideVideo);
				}


				values.put("OtherSideName", OtherSideName);
				values.put("OtherSideRepeatCount", OtherSideRepeatCount);

				if(NextVideo.equals(""))
				{
					values.put("NextVideo", NextVideo);
				}
				else
				{
					values.put("NextVideo", videourl+NextVideo);
				}


				values.put("NextVideoName", NextVideoName);
				values.put("NextRepeatCount",NextRepeatCount);
				if(CompletedVideo.equals(""))
				{
					values.put("CompletedVideo", CompletedVideo);
				}
				else
				{
					values.put("CompletedVideo", videourl+CompletedVideo);
				}


				values.put("CompletedVideoName", CompletedVideoName);
				values.put("CompletedRepeatCount",CompletedRepeatCount);
				values.put("MainVideoSize",mainSize);
				values.put("PosterSize",posterSize);
				values.put("OtherSideSize",OtherSideSize);
				db.insert("FitnessWorkoutVideos", null, values);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
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
					filecheck.setReadable(true, false);
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
