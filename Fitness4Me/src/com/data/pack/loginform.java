package com.data.pack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.data.fitness4me.R;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class loginform extends Activity{
	private  EditText loginUser;
	private EditText loginPass ;
	private Button loginButton;
	private TextView forgotText;
	private TextView lblPass;
	private TextView lblUser;
	private static PlaceDataSQL placeData;
	private String  userName;
	private String YesString="Yes";
	String loginError ="Einloggen Fehler";
	private  AdView adView ;
	private static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private Prefs prefs = new Prefs(this);
	private VOUserDetails obUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginform);
		
		createChildren();
		
		init();

	}
	private void init() {
		
		if(obUser.getSelectedLanguage().equals("1"))
		{
			forgotText.setText("\nForgot your password?Please visit\n http://fitness4.me/index/forgotlogin");
			YesString ="Yes";
			loginError ="Login Error";
		}
		else
		{
			lblPass.setText("Passwort");
			lblUser.setText("Benutzername"); 
			loginButton.setText("fortsetzen");
			forgotText.setText("\nPasswort vergessen?Bitte besuchen Sie\n http://fitness4.me/index/forgotlogin");
			YesString = "Ja";
			loginError ="Einloggen Fehler";
		}



		 
			(new Thread() {
				public void run() {
					adView.setVisibility(View.VISIBLE);
					//adView.loadAd(new AdRequest());
				}
			}).start();
 
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {

					loginTask loginTaskasync = new loginTask();
					loginTaskasync.execute(new String[] { "logindata"} );


				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	private void createChildren() {
		if(forgotText == null)
			forgotText = (TextView) findViewById(R.id.txtforgot);
		if(loginUser == null)
			loginUser = (EditText) findViewById(R.id.loginUser);
		if(loginPass == null)
			loginPass = (EditText) findViewById(R.id.loginPassword);
		if(loginButton == null)
			loginButton = (Button) findViewById(R.id.btnLogin);
		if(lblPass == null)
			lblPass = (TextView)findViewById(R.id.lblPass);
		if(lblUser == null)
			lblUser = (TextView)findViewById(R.id.lblUser);
		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/ARIAL.TTF");
		forgotText.setTypeface(tf,0);	 
		lblPass.setTypeface(tf,0);	 
		lblUser.setTypeface(tf,0);	 
		if(adView == null)
			adView = (AdView)this.findViewById(R.id.adViewLogin);
		if(obUser == null)
			obUser = new VOUserDetails(this);
	}
	
	/**
	 * Insert data into table
	 * 
	 * @param String , String , String , String , String ,String ,String 
	 * @return  
	 */
	private void insertData(String id, String username, String fname,String level ,String email) {
		try {
//			if(placeData == null)
//				placeData = new PlaceDataSQL(this);
//			SQLiteDatabase db = placeData.getWritableDatabase();
//			ContentValues values;
//			values = new ContentValues();
//			values.put("ID", id);
//			values.put("Username", username);
//			values.put("Fname", fname);
//			values.put("Level", level);
//			values.put("UserEmail", email); 
//			values.put("SelectedLang", obUser.getSelectedLanguage()); 
//			db.insert("FitnessUser", null, values);
			obUser.setSelectedUserName(username);
			obUser.setUserId(id);
			obUser.setSelectedFirstName(fname); 
			obUser.setLevel(level);
			obUser.setUseremail(email);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}

	
	class loginTask extends AsyncTask<String, Void, String>  {

		String errMessage =""; 
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS); 
		}

		@Override
		protected String doInBackground(String... aurl) {


			if(CheckNetworkAvailability.isNetworkAvailable(loginform.this))
			{ 
				String PassuserName = loginUser.getText().toString();
				String userPass = loginPass.getText().toString();
				
				//
				try{ 
					String url =	 getResources().getString(R.string.servername);
					JSONObject json = JSONfunctions.getJSONfromURL(url+"login=yes&username="+PassuserName+"&password="+userPass);
					if(json !=null )
					{
						JSONArray  userDetails = json.getJSONArray("items");

							for(int i=0;i<userDetails.length();i++){						
							//HashMap<String, String> map = new HashMap<String, String>();	
							JSONObject e = userDetails.getJSONObject(i);
							if(e.getString("id").equals("0"))
							{
								
								errMessage = e.getString("message");
								

							}
							else
							{
								insertData(e.getString("id"),e.getString("username"),e.getString("fname"),e.getString("level"),e.getString("email"));
								String userID = e.getString("id");

								GlobalData.allPurchased = Boolean.parseBoolean(e.getString("fullpurchase"));
								prefs.setPreference("allPurchased", ""+GlobalData.allPurchased);
								try {
									Intent intent = new Intent(loginform.this, HomeScreen.class);
									startActivity(intent);

								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}


						}		

					}
					 


					// setListAdapter(adapter);
				}catch(JSONException e)        {
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








			return null;
		}

		@Override
		protected void onProgressUpdate(Void... progress) {


		}

		@Override
		protected void onPostExecute(String unused) {
			super.onPostExecute(unused);

			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
 if(errMessage.length() > 5){
	 AlertDialog.Builder alert = new AlertDialog.Builder(loginform.this);
		alert.setTitle(loginError);
		alert.setMessage(""+errMessage);
		alert.setIcon(R.drawable.fitnessicon);
		alert.setPositiveButton(YesString,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				//  Toast.makeText(SqliteDBActivity.this, "Success", Toast.LENGTH_SHORT).show();
			}
		});

		alert.show();

 }
		}

	}
	private ProgressDialog mProgressDialog;
	private  String mesString="Please wait.."; 
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			mProgressDialog = new ProgressDialog(this);
			String st = "";
			if(obUser.getSelectedLanguage().equals("1"))
			{
				st="Please wait..";

			}
			else
			{
				st="Bitte warten ..";
			}
			mProgressDialog.setMessage(st);
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}
}
