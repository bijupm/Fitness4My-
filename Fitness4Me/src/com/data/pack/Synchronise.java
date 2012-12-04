package com.data.pack;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.TextView;
import com.data.fitness4me.R;

public class Synchronise extends Activity {
	/**
     * The thread to process splash screen events
     */
    private Thread mSplashThread;    
    private static PlaceDataSQL placeData;
	//private Cursor cursors;
	private  int userCount =0;
	private  String userID = "0";
	private  String UserName = "";
	private TextView txtsync ;

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:

			mProgressDialog = new ProgressDialog(this);
			//    mProgressDialog.show(videostart.this, "", "Loading...");
			mProgressDialog.setMessage("Synchronizing - just a moment..");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.getWindow().setGravity(Gravity.BOTTOM);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);
			//mProgressDialog.setButton("Cancel", (DialogInterface.OnClickListener) null); 
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
        showDialog(DIALOG_DOWNLOAD_PROGRESS);
        // Splash screen view
        setContentView(R.layout.splash);
        
        final Synchronise sSynchronise = this;   
//        txtsync =(TextView)findViewById(R.id.txtsync);
//        txtsync.setText("Synchronizing \n- just a moment");
        placeData = new PlaceDataSQL(this);

//		   try
//		 	{
//			  // cursors = getRawEvents("select * from FitnessUser");
//		 	  // userCount = cursors.getCount();
//		 	  
//		 	}catch(Exception ex)
//		 	{
//		 
//		 	}
//		 	finally
//		 	{
//		 		cursors.close();
//		 	}
		 	
		 	
        // The thread to wait for splash screen events
        mSplashThread =  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        // Wait given period of time or exit on touch
                        wait(3000);
                    }
                }
                catch(InterruptedException ex){                    
                }

                finish();
                if(userCount <=0)
 		 	   {
 		 		  try {
					Intent intent = new Intent();
					intent.setClass(sSynchronise, SignUP.class);
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
					intent.setClass(sSynchronise, HomeScreen.class);
					startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                stop();    
                }
            }
        };
        
        mSplashThread.start();   
        
        
        
        
        
        
        
		
        
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
}
