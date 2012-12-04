package com.data.pack;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.Toast;
import com.data.fitness4me.R;
import com.data.pack.freepurchasedownload.insertFulldata;

public class fulldownload  extends Activity{
	private ArrayList<String> arrintrovideoUrl = new ArrayList<String>();
	private ArrayList<String> arrMainvideoUrl = new ArrayList<String>();
	private ArrayList<String> arrMainOthervideoUrl = new ArrayList<String>();
	private ArrayList<String> arrvideoUrl = new ArrayList<String>();
	private int valCount =0;
	private String strAlert="";
	private String builderStr="";
	private Prefs prefs = new Prefs(this);
	public ProgressDialog mProgressDialog;
	private ProgressDialog dialog ;
	private  DownloadFile downloadFile = null;
	private VOUserDetails obUser =  new VOUserDetails(this);
	// instantiate it within the onCreate method
	  
	 @Override
	 protected void onStop() {
	 	 try {
	 		MemoryCache memoryCache=new MemoryCache();
	  		 
	          System.gc();
	          read.cancel(true);
	 	} catch (Exception e) {
	 		// TODO Auto-generated catch block
	 		e.printStackTrace();
	 	}
	 	super.onStop();
	 	
	 }
	 @Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			 if(!dialog.isShowing())
				dialog.show();
				 
		}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fulldownload);
 		//Button imgdownloadButton = (Button) findViewById(R.id.imgdownloadButton);
		 arrintrovideoUrl = new ArrayList<String>();
		 arrMainvideoUrl = new ArrayList<String>();
		 arrMainOthervideoUrl = new ArrayList<String>();
		  arrvideoUrl = new ArrayList<String>();
		 arrvideoUrl = new ArrayList<String>();
		 if(obUser.getSelectedLanguage().equals("1"))
		 {
				builderStr = "Please wait..";
			 strAlert = "I am downloading your workouts  and saving it to your device.\nDepending on your internet connection this may take several minutes.Don't worry, you can cancel the download in between \nand resume it a later time .If you wish to cancel the download push cancel";
		 }
		 else
		 {
			 builderStr = "Bitte warten ..";
			 strAlert ="Dein Workout wird heruntergeladen und auf deinem Gerät gespeichert.\nJe nach Qualität deiner Internetverbindung kann dies einige Minuten dauern.\nKeine Sorge, du kannst den Download zwischendurch abbrechen und später fortführen. Wenn du das Herunterladen stoppen möchtest, klicke auf  \"Abbrechen\""; 
		 }
		mProgressDialog = new ProgressDialog(fulldownload.this);
		mProgressDialog.setMessage(strAlert);
		mProgressDialog.setTitle("www.fitness4.me");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(false);
		dialog = ProgressDialog.show(fulldownload.this, "",
				builderStr, true);
		mProgressDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				try {
					dialog.cancel();
					if(read !=null)
					read.cancel(true);
					if(downloadFile !=null)
						downloadFile.cancel(true);
						
					Intent intent = new Intent(fulldownload.this,FitnessforMeActivity.class);
					startActivity(intent);
					
					GlobalData.fulldownloadcomplete = false;
					prefs.setPreference("fulldownloadcomplete", ""+false);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}}); 
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
			 
			read.execute("fulldownload");
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
				JSONObject json = JSONfunctions.getJSONfromURL(urlString+"allvideos=yes&duration=10&user_level="+obUser.getLevel()+"&lang="+obUser.getSelectedLanguage());
				 
				 
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
								File outputDir = Environment.getExternalStorageDirectory();
								File new_dir = new File( outputDir + "/fitness4vid" );
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
								File outputDir = Environment.getExternalStorageDirectory();
								File new_dir = new File( outputDir + "/fitness4vid" );
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
						   downloadFile = new DownloadFile(url);
						downloadFile.execute(url);
						
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						read.cancel(true);
					} 

				} 
		}
	        
	    }
	};
 
	
	
 

  class DownloadFile  extends AsyncTask<String, String, String> {
	// declare the dialog as a member field of your activity
	  String[] paths;
	  int current=0;
	  public DownloadFile(String[] paths) {
          super();
          this.paths = paths;
//          for(int i=0; i<paths.length; i++)
//              System.out.println((i+1)+":  "+paths[i]);
      }
    @Override
    protected String doInBackground(String... sUrl) {
    	 int rows = sUrl.length;
    	 while(current < rows)
         {
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
            GlobalData.arrvideoUrlCountincrement++;
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
        } catch (Exception e) {
        	GlobalData.arrvideoUrlCountincrement++;
			current++;
        }
         }
        return null;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog.show();
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
		  try {
			mProgressDialog.cancel();
				read.cancel(true);
				Intent intent = new Intent(fulldownload.this,FitnessforMeActivity.class);
				startActivity(intent);
				
				GlobalData.fulldownloadcomplete = true;
				prefs.setPreference("fulldownloadcomplete", ""+GlobalData.fulldownloadcomplete);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
    @Override
    protected void onProgressUpdate(String... progress) {
        super.onProgressUpdate(progress);
        if( dialog.isShowing() )
		{
			dialog.cancel();
		}
        if(current == valCount)
        {
        	if( mProgressDialog.isShowing() )
        	{
        		mProgressDialog.cancel();
				read.cancel(true);
				Intent intent = new Intent(fulldownload.this,FitnessforMeActivity.class);
				startActivity(intent);
				
				GlobalData.fulldownloadcomplete = true;
				prefs.setPreference("fulldownloadcomplete", ""+GlobalData.fulldownloadcomplete);
        	}
        }
        mProgressDialog.setProgress(current);
    }
}
   
}
