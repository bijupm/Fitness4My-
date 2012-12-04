package com.data.pack;
import android.app.Application;


public class FitnessApplication extends Application {

@Override
public void onCreate() {
	// TODO Auto-generated method stub
	super.onCreate();
	
}
 

@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
	try {
		MemoryCache memoryCache=new MemoryCache();
		 
		   System.gc();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
		super.onLowMemory();
	}
@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		try {
			MemoryCache memoryCache=new MemoryCache();
			 
			   System.gc();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onTerminate();
	}
}
