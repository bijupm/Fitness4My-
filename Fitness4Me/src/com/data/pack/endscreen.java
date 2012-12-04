package com.data.pack;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.data.fitness4me.R;
import com.data.pack.fulldownload.DownloadFile;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class endscreen extends Activity {
	
	private static PlaceDataSQL placeData;
	private String videoid;
	private String temp_id  ;
	private String temp_videourl;
	private String temp_videoname;
	private String temp_imgurl;
		private String UserName;
	
	private String workoutID;
	private String temp_rate ;
	private String temp_title;
	private String temp_img;
	private String temp_desc;
	private String temp_islocked;
	private Cursor cursors ;
	private String userID;
	private VOUserDetails obUser =  new VOUserDetails(this); 
	//private final ImageDownloader imageDownloader = new ImageDownloader();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.endscreen);
		GlobalData.viewvideochange = 1;
		Button imganother =(Button)findViewById(R.id.videoanother);
		Button imgquit =(Button)findViewById(R.id.videoquit);
		  Intent i = getIntent();
			 Bundle extras = i.getExtras();
			 placeData = new PlaceDataSQL(this);
		     workoutID = extras.getString("workoutID");
		     userID = extras.getString("userID");
		     UserName = extras.getString("UserName");
		     AdView adView = (AdView)this.findViewById(R.id.adView);
		     TextView txtdesc = (TextView)findViewById(R.id.description);
		       TextView liketodo = (TextView)findViewById(R.id.liketodo);
		     txtdesc.setText(Html.fromHtml(getString(R.string.congratulations)));
		     
		     
		     Typeface tf = Typeface.createFromAsset(getAssets(),
	         "fonts/ARIAL.TTF");
		     txtdesc.setTypeface(tf,1);
		     liketodo.setTypeface(tf);
			  //adView = new AdView(this, AdSize.BANNER, "a14dccd0fb24d45");
		     if(GlobalData.allPurchased == true)
			 	{
		 		   
			 			adView.setVisibility(View.GONE);
			 	}
		 		else
		 		{
		 			adView.setVisibility(View.VISIBLE);
		 		   adView.loadAd(new AdRequest());
		 		   
		 		  try {
		 			  
		 			 WindowManager.LayoutParams params = getWindow().getAttributes();
		 			params.flags |= LayoutParams.FLAG_KEEP_SCREEN_ON;
		 			getWindow().setAttributes(params);
		 			 Handler handler = new Handler(); 
		 		    handler.postDelayed(new Runnable() { 
		 		         public void run() { 
		 		        	  String st[] = new String[1];
		 			 		  backgroundtask downloadFile = new backgroundtask(st);
		 			 		  downloadFile.execute(st);
		 		         } 
		 		    }, 2300); 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		 		 
		 			
		 		}
		    
		     if(obUser.getSelectedLanguage().equals("1"))
				{
				 
				}
			  else
			  {
				  
				  txtdesc.setText("Herzlichen Glückwunsch! Gut gemacht!");
				  liketodo.setText("Was möchtest du jetzt tun?");
				  imgquit.setText("Beenden");
				  imganother.setText("Ein weiteres Workout durchführen");
			  }
		     
//		     try
//		 	{
//		 	   cursors = getEvents("FitnessWorkouts");
//		 	 if (cursors.moveToNext()) {
//		 			//populate.setVisibility(View.GONE);
//		 			getDataAndPopulate();
//		 		} else {
//		 			//populate.setVisibility(View.VISIBLE);
//		 		}
//		 	}catch(Exception ex)
//		 	{
//		 		
//		 	}
//		 	finally
//		 	{
//		 		cursors.close();
//		 	}
		     
		     
		imgquit.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				 try {
					 GlobalData.selectedWorkOutID = workoutID;
					  Intent intent = new Intent(endscreen.this, sharescreen.class);
					  
 
					 startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		imganother.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				 
				 try {
					Intent intent = new Intent(endscreen.this, FitnessforMeActivity.class);
					 startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
			}
		});
		
 
	}

	
 

	@Override
	protected void onStop() {
		 try {
			MemoryCache memoryCache=new MemoryCache();
	 		 
	 		   System.gc();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onStop();
		
	}

/**
 * Get data from table
 * @param  String table
 */

private Cursor getEvents(String table) {
	SQLiteDatabase db = (placeData).getReadableDatabase();
	//Cursor cursor = db.query(table, null, null, null, null, null, null);
	//cursor = db.query(table, new String[] {"id", "title", "videoUrl","videoName","description","imageurl"}, 
      //      "id =? " + "+videoid+", null, null, null, null);
	
	Cursor cursor = db.query(table,  new String[] {"Id", "Name", "Rate","Image","Description","IsLocked"}, 
	          "Id = ?", new String[] { String.valueOf(workoutID)}, 
	          null, null, null);
	
    
	startManagingCursor(cursor);
	return cursor;
}

/**
 * Cursor management
 * @param String sql
 */
private Cursor getRawEvents(String sql) {
	SQLiteDatabase db = (placeData).getReadableDatabase();
	Cursor cursor = db.rawQuery(sql, null);

	startManagingCursor(cursor);
	return cursor;
}
@Override
public boolean onKeyDown(int keyCode, KeyEvent event)  {
    if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5
            && keyCode == KeyEvent.KEYCODE_BACK
            && event.getRepeatCount() == 0) {
        Log.d("CDA", "onKeyDown Called");
       // onBackPressed();
    }

    return false;// super.onKeyDown(keyCode, event);
}

public void onBackPressed() {
    
	 try {
		Intent intent = new Intent(endscreen.this, FitnessforMeActivity.class);
		 startActivity(intent);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    return;
}  
public String getFileName(String wholePath)
{
    String name=null;
    int start,end;
    start=wholePath.lastIndexOf('/');
    end=wholePath.length();     //lastIndexOf('.');
    name=wholePath.substring((start+1),end);
    name = "/sdcard/fitness4meimages/"+name;
    return name;
}




class backgroundtask  extends AsyncTask<String, String, String> {
		// declare the dialog as a member field of your activity
		 
		  
		  public backgroundtask(String[] paths) {
	          super();
	         
	        
	      }
	    @Override
	    protected String doInBackground(String... sUrl) {
	    	  
	    	 
	             
	        try {
	        	 if(GlobalData.allPurchased == false)
					{
						if(GlobalData.upgradepopupshow == false)
						{
						    	 try {
									Intent intent = new Intent(endscreen.this, upgradepopup.class);
									 GlobalData.upgradepopupshow = true;
									startActivity(intent);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						}
									 
					}
	            
	        } catch (Exception e) {
	        	 
	        }
	         
	        return null;
	    }
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        
	    }
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		 
	}
	    @Override
	    protected void onProgressUpdate(String... progress) {
	        
	    }
	}


}
