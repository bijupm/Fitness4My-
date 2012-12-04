package com.data.pack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.data.fitness4me.R;
import com.data.pack.freepurchasedownload.insertFulldata;
import com.data.pack.userinfo.submitValuesAsync;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
public class PaymanetPage extends Activity {
	private String userID="";
	private String UserName=""; 
	private String workoutID=""; 
	private String amout ="";
	private String workoutStatus ="";
	private WebView webView;
	private Button btnReturn;
	private int flag = 0;
	private int flaglock =0;
	private Prefs prefs = new Prefs(this);
	private submitValuesAsync dloadFAsync ;
	public ProgressDialog mProgressDialogLoaddata;
	
	private VOUserDetails obUser =  new VOUserDetails(this); 
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:

			mProgressDialog = new ProgressDialog(this);
			//    mProgressDialog.show(videostart.this, "", "Loading...");
			String st = "";
			if(obUser.getSelectedLanguage().equals("1"))
			{
			mProgressDialog.setMessage("Please wait..");
			}
			else
			{
			 mProgressDialog.setMessage("Bitte warten ..");
			 
			}
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paymentpage);
		webView = (WebView) findViewById(R.id.mywebview);
		btnReturn = (Button)findViewById(R.id.btnReturnHome);
		webView.setWebViewClient(new WebViewClient());
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different scales.
				showDialog(DIALOG_DOWNLOAD_PROGRESS); 

				if(progress >=100)
				{
					dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
				}
			}
		});


		Intent i = getIntent();
		Bundle extras = i.getExtras();
		placeData = new PlaceDataSQL(this);
		workoutID = extras.getString("workoutID");
		UserName = extras.getString("UserName");
		userID = extras.getString("userID");
		workoutStatus = extras.getString("workoutStatus");
		mProgressDialogLoaddata = new ProgressDialog(PaymanetPage.this);
		amout = extras.getString("amout");
		if(obUser.getSelectedLanguage().equals("1"))
		{
			mProgressDialogLoaddata.setMessage("Please wait..");
		}
		else
		{
			mProgressDialogLoaddata.setMessage("Bitte warten ..");
		 
		}
		mProgressDialogLoaddata.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialogLoaddata.setCancelable(false);
		if(workoutStatus.equals("all"))
		{
			workoutID = "";
		}
		else
		{
			workoutID = extras.getString("workoutID");
		}
		if(obUser.getSelectedLanguage().equals("1"))
		{ 
			btnReturn.setText("Home");
		}
		else
		{
			btnReturn.setText("Zurück");
		}
		String url =	 "http://fitness4.me/device/devicepayment/user_id/"+userID+"/workout_id/"+workoutID+"/amount/"+amout+"/type/"+workoutStatus+"/lang/"+obUser.getSelectedLanguage();
		webView.setWebViewClient(new myWebClient());  
		webView.loadUrl(url);
		Typeface tf = Typeface.createFromAsset(getAssets(),
		"fonts/ARIAL.TTF");
		btnReturn.setTypeface(tf,0) ;
		
		btnReturn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String[] url = new String[0];
				 dloadFAsync = new submitValuesAsync(url);
				dloadFAsync.execute(url);
				
//				Intent intent = new Intent(PaymanetPage.this, HomeScreen.class);
//				startActivity(intent);
				
			}
		});
	}
		/**
		 * Insert data into table
		 * 
		 * @param String , String , String , String , String ,String ,String 
		 * @return  
		 */
		private static PlaceDataSQL placeData;
		private void insertData(String id, String name, String rate, String image,String imagethumb,String description,String descriptionbig,String islocked,String Props,String ImageAndroid) {
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
	public class myWebClient extends WebViewClient  
	{  
		@Override  
		public void onPageStarted(WebView view, String url, Bitmap favicon) {  
			// TODO Auto-generated method stub  
			super.onPageStarted(view, url, favicon);  
		}  

		@Override  
		public boolean shouldOverrideUrlLoading(WebView view, String url) {  
			// TODO Auto-generated method stub  

			view.loadUrl(url);  
			return true;  

		}  
	}  
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5
				&& keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			Log.d("CDA", "onKeyDown Called");
			//onBackPressed();
		}

		return false;
	}

	public void onBackPressed() {
		try {
			fullpurchase();
			Intent intent = new Intent(PaymanetPage.this, HomeScreen.class);
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return;
	}  


	private void fullpurchase()
	{

		if(CheckNetworkAvailability.isNetworkAvailable(PaymanetPage.this))
		{
			try{


				String st =obUser.getUserId();
				// mProgress.setVisibility(View.VISIBLE);
				String url =	 getResources().getString(R.string.servername);




				try{
					JSONObject json = JSONfunctions.getJSONfromURL(url+"user_purchase=yes&user_id="+userID);
					JSONArray  userDetails = json.getJSONArray("items");

					for(int i=0;i<userDetails.length();i++){						
						//HashMap<String, String> map = new HashMap<String, String>();	
						JSONObject e = userDetails.getJSONObject(i);

						GlobalData.allPurchased = Boolean.parseBoolean(e.getString("fullpurchase"));
						prefs.setPreference("allPurchased", ""+GlobalData.allPurchased);

					}
					// setListAdapter(adapter);
				}catch(JSONException e)        {
				}


			} catch (Exception e) { 

			}
		}
	}
	class submitValuesAsync extends AsyncTask<String, String, String>  {
		JSONObject json = null;
		public submitValuesAsync(String[] paths) {
			super();

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();



		}

		@Override
		protected String doInBackground(String... aurl) {
			if(CheckNetworkAvailability.isNetworkAvailable(PaymanetPage.this))
			{
			
			try{
				
				String url =	 getResources().getString(R.string.servername);
				JSONObject json = JSONfunctions.getJSONfromURL(url+"listapps=yes&duration=10&userid="+obUser.getUserId()+"&lang="+obUser.getSelectedLanguage());
				if(json !=null )
				{
					JSONArray  workoutList = json.getJSONArray("items");

					if(placeData !=null)
					{
						SQLiteDatabase db = placeData.getWritableDatabase();

						db.delete("FitnessWorkouts", null,null);
					 
					}

					for(int loop=0;loop<workoutList.length();loop++){						
						//	//HashMap<String, String> map = new HashMap<String, String>();	
						JSONObject e = workoutList.getJSONObject(loop);

						insertData(e.getString("id"),e.getString("name"),e.getString("rate"),e.getString("image_android"),e.getString("image_android"),e.getString("description"),e.getString("description_big"),e.getString("islocked"),e.getString("props"),e.getString("image_android"));

					}		
				 
					 	
						
				  }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// }
			return null;
 	} 

		@Override
		protected void onProgressUpdate(String... progress) {



		}

		@Override
		protected void onPostExecute(String unused) {
			if(mProgressDialog.isShowing())
			{
				mProgressDialog.dismiss();
			}
	             // My AsyncTask is done and onPostExecute was called
	           new fullpurchasedata().execute("fullpurchase");
		}


	}
	
	
	class fullpurchasedata  extends AsyncTask<String, String, String> {
		// declare the dialog as a member field of your activity
		 
		 
		@Override
		protected String doInBackground(String... sUrl) {
			if(CheckNetworkAvailability.isNetworkAvailable(PaymanetPage.this))
			{
			
			try{
				fullpurchase();

				// setListAdapter(adapter);
			}catch(Exception e)        {
				//	Log.e("log_tag", "Error parsing data "+e.toString());
			}
			}
			return null;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			mProgressDialogLoaddata.show();
			 
			
			
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if( mProgressDialogLoaddata.isShowing() )
			{
				mProgressDialogLoaddata.cancel();
			}
			try {
				Intent intent = new Intent(PaymanetPage.this, FitnessforMeActivity.class);
				startActivity(intent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		protected void onProgressUpdate(String... progress) {
		 
		}
		
		 
		 

		
	}
	public void onDestroy(){
	    super.onDestroy();
	    webView.destroy();
	    webView = null;
	    finish();
	    System.gc();
	}
	
}
