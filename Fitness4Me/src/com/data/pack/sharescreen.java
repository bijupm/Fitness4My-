package com.data.pack;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.data.fitness4me.R;
import com.data.pack.AsyncFacebookRunner.RequestListener;
import com.data.pack.Facebook.DialogListener;
import com.google.ads.AdRequest;

public class sharescreen extends Activity implements DialogListener{
	private Facebook facebook ;
	private String workoutID;
	private static PlaceDataSQL placeData;
	private TextView edText;
	private TextView lblhead;
	private String workoutTitle;
	private String temp_img;
	private String userName;
	private Cursor cursors ;
	private Intent intentResult;
	private Button btnexit;
	private Button imgFb ;
	private ImageView imgshare;
	private Button imgTwitter;
	private String YesString="Yes";
	private String NoString="No";
	private  Bundle valuesB;
	private String userID;
	private Context con;
	private final ImageDownloader imageDownloader = new ImageDownloader();
	private SharedPreferences prefs;
	private Prefs prefsdata;
	private VOUserDetails obUser =  new VOUserDetails(this); 
	private final Handler mTwitterHandler = new Handler();
	final Runnable mUpdateTwitterNotification = new Runnable() {
		public void run() {
			String st = "";
			if(obUser.getSelectedLanguage().equals("1"))
			{
				Toast.makeText(getBaseContext(), "Tweet sent !", Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(getBaseContext(), "Tweet versendet!", Toast.LENGTH_LONG).show();
			}
		}
	};


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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub







		super.onCreate(savedInstanceState);
		setContentView(R.layout.sharescreen);
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		imgshare = (ImageView)findViewById(R.id.imgshare);
		edText = (TextView)findViewById(R.id.Sharelabel);
		lblhead = (TextView)findViewById(R.id.lblhead);
		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/ARIAL.TTF");
		lblhead.setTypeface(tf,1);	 
		edText.setTypeface(tf);
		if(obUser.getSelectedLanguage().equals("1"))
		{

			YesString = "Yes";
			NoString="No";
		}
		else
		{
			YesString = "Ja";
			NoString="Nein";
			lblhead.setText("Erzähle deine Freunde über dein Workout");
		}
		placeData = new PlaceDataSQL(this);

		try
		{
			
			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.flags |= LayoutParams.FLAG_KEEP_SCREEN_ON;
			getWindow().setAttributes(params);
			userID= obUser.getUserId() ;
			userName= obUser.getSelectedUserName();
			workoutID= GlobalData.selectedWorkOutID ;
			if(prefsdata == null)
			{
				prefsdata = new Prefs(this);
			}

			//cursors = getEvents("FitnessWorkouts");

			//populate.setVisibility(View.GONE);
			getDataAndPopulate();

		}catch(Exception ex)
		{
			String st = ex.toString();

		}
		finally
		{
			//cursors.close();
		}



		if (btnexit == null) 
			btnexit = (Button)findViewById(R.id.btnexit);
		if (imgFb == null) 
			imgFb = (Button)findViewById(R.id.btnfb);
		if (imgTwitter == null) 
			imgTwitter = (Button)findViewById(R.id.btntwitter);





		btnexit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//					Intent intent = new Intent(sharescreen.this, HomeScreen.class);
				//					// intent.putExtra("userID", userID);
				//					 startActivity(intent);

				AlertDialog		  alertDialog = new AlertDialog.Builder(sharescreen.this).create();  

				String mes = "You are about to Exit fitness4me. Are you sure?";
				if(obUser.getSelectedLanguage().equals("1"))
				{
					mes = "You are about to Exit fitness4me. Are you sure?";
				}
				else
				{
					mes = "Möchtest du wirklich fitness4.me verlassen?";
				}
				alertDialog.setTitle("Fitness4Me");
				alertDialog.setMessage(mes);

				alertDialog.setButton(-1, YesString, new DialogInterface.OnClickListener()
				{  
					public void onClick(DialogInterface dialog, int which) {  
						try
						{
 
							Intent intent = new Intent(sharescreen.this, HomeScreen.class);
							// intent.putExtra("userID", userID);
							prefsdata.setPreference("terminateID", "1");	
							startActivity(intent);
							  
						}
						catch (Exception e) {
							// TODO: handle exception
						}
					} }); 
				alertDialog.setButton(-2, NoString, new DialogInterface.OnClickListener()
				{    
					public void onClick(DialogInterface dialog, int which) {  
						try {
							Intent intent = new Intent(sharescreen.this, FitnessforMeActivity.class);
							startActivity(intent);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} });
				try {
					alertDialog.show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});



		imgTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(CheckNetworkAvailability.isNetworkAvailable(sharescreen.this))
				{
					try
					{ 
						if (TwitterUtils.isAuthenticated(prefs)) {
							sendTweet();
						} else {

							try {
								Intent i = new Intent(getApplicationContext(), PrepareRequestTokenActivity.class);
								i.putExtra("tweet_msg",getTweetMsg());
								startActivity(i);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
					catch (Exception e) {
						// TODO: handle exception
						String st = e.toString();
					}
				}
				else
				{
					CharSequence text = "No internet connection!";
					if(obUser.getSelectedLanguage().equals("1"))
					 {
						text = "No internet connection!";
					 }
					else
					{
						text = "Es besteht keine Internetverbindung - bitte versuche es später.";
					}
					int duration = Toast.LENGTH_SHORT;
					Context context = getApplicationContext();
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
			}

		});




		imgFb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(CheckNetworkAvailability.isNetworkAvailable(sharescreen.this))
				{
					try {
						facebook = new Facebook("447892125262110");
						facebook.authorize(sharescreen.this, new String[] {"user_photos,photo_upload,publish_checkins,publish_actions,publish_stream,read_stream,offline_access"}, sharescreen.this);
						shareFacebook();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{

					CharSequence text = "No internet connection!";
					if(obUser.getSelectedLanguage().equals("1"))
					 {
						text = "No internet connection!";
					 }
					else
					{
						text = "Es besteht keine Internetverbindung - bitte versuche es später.";
					}
					int duration = Toast.LENGTH_SHORT;
					Context context = getApplicationContext();
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}

			}
		});




	}

	private String getTweetMsg() {
		return   edText.getText().toString()+"\n"+temp_img+"\nFiness4.Me\n\nFitness Program";
	}	

	@Override
	protected void onResume() {
		super.onResume();


	}


	void sendTweet() {
		Thread t = new Thread() {
			public void run() {

				try {




					//        		ntent sharingIntent = new Intent(Intent.ACTION_SEND);
					//        		Uri screenshotUri = Uri.parse("file:///sdcard/image.jpg");
					//        		sharingIntent.setType("image/*");
					//        		sharingIntent.putExtra(Intent.EXTRA_TEXT, "Body text of the new status");
					//        		sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
					//        		startActivity(Intent.createChooser(sharingIntent, "Share image using"));
					TwitterUtils.sendTweet(prefs,getTweetMsg());
					mTwitterHandler.post(mUpdateTwitterNotification);
				} catch (Exception ex) {

				}

			}

		};
		t.start();
	}



	Facebook mFacebook;
	AsyncFacebookRunner mAsyncRunner;


	public void shareFacebook() {




	}


	/**
	 * get Data And Populate data
	 */

	private void getDataAndPopulate() {



		// ImageView img;
		// img = (ImageView) findViewById(R.id.shareimage);

		Cursor cursor = null;
		try {
			//cursor = getEvents("FitnessWorkouts");
			cursor  = 	getRawEvents("Select Name, ImageAndroid from FitnessWorkouts where Id = "+GlobalData.selectedWorkOutID);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(cursor !=null)
		{
		while (cursor.moveToNext()) {
			workoutTitle = cursor.getString(cursor.getColumnIndex("Name"));
			temp_img = cursor.getString(cursor.getColumnIndex("ImageAndroid"));
			try {
				int start,end;
				start=temp_img.lastIndexOf('/');
				end=temp_img.length();     //lastIndexOf('.');
				String name=temp_img.substring((start+1),end);





				if(name.length() < 4)
				{
					temp_img = "http://fitness4.me/public/images/exercises/thumbs/Expert_Chair.jpg";

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

		}
		}
		try {

			
			File temp = new File(getFileName(temp_img));

			//			if(!temp.exists() ) 
			//			{
			//				if (temp_img.length() > 0) {	
			//					Drawable myIcon = getResources().getDrawable( R.drawable.chair );
			//					
			//					img.setImageDrawable(myIcon);
			//				}
			//			}
			//			else
			//			{

			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			String str_ScreenSize = "The Android Screen is: "
				+ dm.widthPixels
				+ " x "
				+ dm.heightPixels;
			int maxWidth = dm.widthPixels / 2;
			int maxHight = dm.heightPixels / 3;
			imgshare.setMaxWidth(maxWidth);
			imgshare.setMaxHeight(maxHight);
			if (maxWidth == 640) {
				maxWidth = 650;
			}
			if (maxHight == 240) {
				maxHight = dm.heightPixels / 3;
			}
			if(dm.widthPixels  == 480)
			{
				  maxWidth = 232 ;
				  maxHight = dm.heightPixels /3 ;
 			}
			if(dm.widthPixels > 1200)
			{
				  maxWidth = 612 ;
				  maxHight = dm.heightPixels /2 ;
			}
			if(temp.exists() ) 
			{

				//imgshare.setImageURI(Uri.parse(getFileName(temp_img)));
				imgshare.setImageURI(Uri.parse(getFileName(temp_img)));
				imgshare.setMaxWidth(maxWidth);
				imgshare.setMaxHeight(maxHight);
				
				imgshare.setScaleType(ImageView.ScaleType.FIT_XY);
				imgshare.setAdjustViewBounds(true);
				imgshare.invalidate();
				imgshare.setBackgroundDrawable(null);

			}
			if(obUser.getSelectedLanguage().equals("1"))
			{
				edText.setText( Html.fromHtml(obUser.getSelectedFirstName().replace("%20", " ")+" just completed the fitness4.me \n<b>"+workoutTitle+"</b>"));
			}
			else
			{
				edText.setText( Html.fromHtml(obUser.getSelectedFirstName().replace("%20", " ")+"  hat gerade das Trainingsprogramm \n<b>"+workoutTitle+" </b>beendet"));   


			}


		} catch (Exception e) {
			String st = e.toString();
			st = st+"";
		}  

	}

	private Bitmap decodedByte ;
	/**
	 * Get data from table
	 * @param  String table
	 */

	private Cursor getEvents(String table) {
		SQLiteDatabase db = (placeData).getReadableDatabase();
		//Cursor cursor = db.query(table, null, null, null, null, null, null);
		//cursor = db.query(table, new String[] {"id", "title", "videoUrl","videoName","description","imageurl"}, 
		//      "id =? " + "+videoid+", null, null, null, null);
		Cursor cursor = null;
		try {
			cursor = db.query(table,  new String[] {"Id", "Name", "Rate","ImageAndroid","ImageThumb","Description","DescriptionBig","IsLocked"}, 
					"Id = ?", new String[] { String.valueOf(GlobalData.selectedWorkOutID)}, 
					null, null, null);


			startManagingCursor(cursor);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cursor;
	}
	private Cursor getRawEvents(String sql) {
		SQLiteDatabase db = (placeData).getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);

		startManagingCursor(cursor);
		return cursor;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);


		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	public void onComplete(Bundle values) {
		// TODO Auto-generated method stub
		if (values.isEmpty())
		{
			//"skip" clicked ?
			return;
		}

		// if facebookClient.authorize(...) was successful, this runs
		// this also runs after successful post
		// after posting, "post_id" is added to the values bundle
		// I use that to differentiate between a call from
		// faceBook.authorize(...) and a call from a successful post
		// is there a better way of doing this?
		if (!values.containsKey("post_id"))
		{
			try
			{
				//updateStatus(values.getString(Facebook.TOKEN));
				valuesB = values;

				imgFb.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						updateStatus(valuesB.getString(Facebook.TOKEN));
					}
				});
				//Bundle parameters = new Bundle();
				//parameters.putString("message", "this is a test");// the message to post to the wall
				//facebook.dialog(FacebookActivity.this, "stream.publish", parameters, FacebookActivity.this);// "stream.publish" is an API call
			}
			catch (Exception e)
			{
				// TODO: handle exception
				System.out.println(e.getMessage());
			}
		}
	}

	public void onFacebookError(FacebookError e) {
		// TODO Auto-generated method stub
		System.out.println("Error: " + e.getMessage());
	}

	public void onError(DialogError e) {
		// TODO Auto-generated method stub
		System.out.println("Error: " + e.getMessage());
	}

	public void onCancel() {
		// TODO Auto-generated method stub

	}



	public void updateStatus(String accessToken){
		try {

			// byte[] data = null;


			byte[] data = null;

			int start,end;
			start=temp_img.lastIndexOf('/');
			end=temp_img.length();     //lastIndexOf('.');
			String name=temp_img.substring((start+1),end);





			if(name.length() < 4)
			{
				temp_img = "http://fitness4.me/public/images/exercises/thumbs/Expert_Chair.jpg";

			}
			String DIRECTORY_PATH = getFileName(temp_img);
			Bitmap bitmap = BitmapFactory.decodeFile(DIRECTORY_PATH);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
			data = baos.toByteArray();
			bitmap.recycle();
			Bundle bundle = new Bundle();
			bundle.putString(Facebook.TOKEN,accessToken);
			bundle.putString("method", "photos.upload");
			bundle.putString("message", edText.getText().toString());
			bundle.putString("name", "Name");
			bundle.putString("link", "https://www.facebook.com/Fitness4.me");

			bundle.putByteArray("picture", data);

			bundle.putString("caption", edText.getText().toString()+"\nFiness4.Me\nhttps://www.facebook.com/Fitness4.me\nFitness Program");
			bundle.putString("description", "Fitness Program");

			baos.close();
			baos = null;
			String response = facebook.request("/me/feed",bundle,"POST");
			//  facebook.request(bundle);

			AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
			// mAsyncRunner.request(null, bundle, "POST", new RequestListener() {
			mAsyncRunner.request(null, bundle, "POST", new RequestListener() {
				public void onMalformedURLException(MalformedURLException e, Object state) {
				}


				public void onIOException(IOException e, Object state) {
				}

				public void onFileNotFoundException(FileNotFoundException e, Object state) {
				}

				public void onFacebookError(FacebookError e, Object state) {
				}

				public void onComplete(String response, Object state) {
				}
			}, null);
		} catch (Exception e) {
			String st =e.toString();
			st = st+"";
		} 
	}
	public String getFileName(String wholePath)
	{
		String name=null;
		int start,end;
		start=wholePath.lastIndexOf('/');
		end=wholePath.length();     //lastIndexOf('.');
		name=wholePath.substring((start+1),end);
		 name = getCacheDir()+"/"+name;
		//name = "/sdcard/fitness4meimages/"+name;
		return name;
	}


	 
	// do your stuff here
}
