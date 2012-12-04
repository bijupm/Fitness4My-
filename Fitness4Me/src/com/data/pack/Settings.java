package com.data.pack;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.data.fitness4me.R;

public class Settings extends Activity {
	private EditText txtName  ;
	private TextView txtUser  ;
	private TextView lblSettingsname  ;
	private TextView lblemail  ;
	private TextView lbllevel  ;
	private TextView lblbeadex  ;
	private TextView lblusername  ;
	private TextView lblname  ;
	private EditText txtEmail  ;
	private String level = "1";;
	private RadioButton rbbeginner;
	private RadioButton rbAdv;
	private RadioButton rbExpert;
	private int checkedRadioButton; 
	private Boolean userExist = false;
	private Boolean emailExist = false;
	private RadioGroup mRadioGroup;
	private Button imgUserRating ;
	private Drawable error_indicator;
	private int mProgressStatus = 0;
	private int diaflag = 0;
	private Button imgdownload ;
	private Cursor cursorsCountList;
	private  int ListCount =0;
	private AlertDialog alertDialog ;
	private  Prefs prefs;
	private VOUserDetails obUser;
	private ProgressDialog mProgressDialog;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private  String mesString="Please wait.."; 
	
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		if(placeData == null)
		placeData = new PlaceDataSQL(this);
		if(obUser == null)
			obUser = new VOUserDetails(this);
		Button imgUserUpdate = (Button) findViewById(R.id.imgUserUpdate);
		imgdownload = (Button) findViewById(R.id.imgdownload);
		lblname = (TextView)findViewById(R.id.lblSettingsname);

		txtName = (EditText)findViewById(R.id.txtSettingsname);
		txtUser = (TextView)findViewById(R.id.txtSettingsUserName);
		txtEmail = (EditText)findViewById(R.id.txtSettingsemail);
		imgUserRating =(Button)findViewById(R.id.imgUserRating);
		rbbeginner = (RadioButton) findViewById(R.id.rbSettingsbeginner);
		rbAdv = (RadioButton) findViewById(R.id.rbSettingsAdvanced);
		rbExpert = (RadioButton) findViewById(R.id.rbSettingsExpert);
		mRadioGroup = (RadioGroup) findViewById(R.id.rbSettingsgroup);
		txtName.setText(obUser.getSelectedFirstName().replace("%20", " "));
		txtEmail.setText(obUser.getUseremail());
		txtUser.setText("   " +obUser.getSelectedUserName());
		level = obUser.getLevel();
		lblSettingsname = (TextView)findViewById(R.id.lblSettingsname);
		lblusername 	= (TextView)findViewById(R.id.lblusername);
		lbllevel = (TextView)findViewById(R.id.lbllevel);
		lblbeadex =   (TextView)findViewById(R.id.lblbeadex);
		lblemail =  (TextView)findViewById(R.id.lblemail);
		if(obUser == null)
		obUser = new VOUserDetails(this);
		if(prefs == null)
		prefs = new Prefs(this);
		Typeface tf = Typeface.createFromAsset(getAssets(),
		"fonts/ARIAL.TTF");
		lblemail.setTypeface(tf,0) ;
		lblSettingsname.setTypeface(tf,0) ;
		lblbeadex.setTypeface(tf,0) ;
		lbllevel.setTypeface(tf,0) ;
		lblusername.setTypeface(tf,0) ;
		if(level.equals("1"))
		{
			rbbeginner.setChecked(true);
		}
		else if(level.equals("2"))
		{
			rbAdv.setChecked(true);
		}

		else if(level.equals("3"))
		{
			rbExpert.setChecked(true);
		}
		//checkAndCreateDirectory("/fitness4vid");
		 try {
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		checkedRadioButton = mRadioGroup.getCheckedRadioButtonId();
		if(obUser.getSelectedLanguage().equals("1"))
		{
			lblbeadex.setText(" Beginner        Advanced         Expert"); 
			lblname.setText("First Name");
			lbllevel.setText("Level"); 
		}
		else
		{
			lblbeadex.setText(" Anfänger        Fortgeschritten      Profi"); 
			lblusername.setText("Benutzername"); 
			lbllevel.setText("Level"); 
			lblemail.setText("E-mail");
			imgdownload.setText(" Vollständige Videos\n herunterladen  ");
			imgUserRating.setText("Bewerte diese App");
			imgUserUpdate.setText(" Einstellungen aktualisieren ");
			lblname.setText("Vorname");


		}
		mRadioGroup.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener()
		{

			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub

				RadioButton rb=(RadioButton)findViewById(arg1);
				int strlevel =  mRadioGroup.getCheckedRadioButtonId();
				if(strlevel == R.id.rbSettingsbeginner)
				{
					level = "1";
				}
				else if(strlevel == R.id.rbSettingsAdvanced)
				{
					level = "2";
				}
				else if(strlevel == R.id.rbSettingsExpert)
				{
					level = "3";
				}

			}

		}
		); 

		error_indicator = getResources().getDrawable(R.drawable.icon );
		int left = 0;
		int top = 0;
	
		int right = error_indicator.getIntrinsicHeight();
		int bottom = error_indicator.getIntrinsicWidth();
		error_indicator.setBounds(new Rect(left, top, right, bottom));
		txtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {

				if (!hasFocus) {
					if(!isEmailValid(txtEmail.getText().toString()))
					{
						emailExist = true;
						if(obUser.getSelectedLanguage().equals("1"))
						{
							txtEmail.setError("Not a valid Email id");
						}
						else
						{
							txtEmail.setError("Nicht eine gültige E-Mail-ID");
						}

					}
					else
					{


						if (!hasFocus) {
							if(txtEmail.getText().length() > 0)
							{
								String strEmail = txtEmail.getText().toString();
								String url =	 getResources().getString(R.string.servername);
								JSONObject json = JSONfunctions.getJSONfromURL(url+"checkemail=yes&email="+strEmail);

								try{

									JSONArray  workoutList = json.getJSONArray("items");

									for(int i=0;i<workoutList.length();i++){						
										//HashMap<String, String> map = new HashMap<String, String>();	
										JSONObject e = workoutList.getJSONObject(i);


										String status =	e.getString("status") ;

										if(Boolean.parseBoolean(status))
										{


											if(obUser.getSelectedLanguage().equals("1"))
											{
												txtEmail.setError("Email Already Exists");
											}
											else
											{
												txtEmail.setError("E-Mail Adresse existiert bereits");
											}

											txtEmail.setFocusable(true);
											emailExist = true;

										}
										else
										{
											txtEmail.setError(null);  
											emailExist = false;


										}

									}		




								}
								catch(Exception ex)
								{

								}

							}
						}


					}

				}
			}
		});


		imgUserRating.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {
					Intent intent = new Intent(Settings.this, evalution.class);
					startActivity(intent);
					GlobalData.appcount = 0;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   

			}
		});

		imgUserUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 if(CheckNetworkAvailability.isNetworkAvailable(getApplicationContext()))
	         	    {
				try {
					
					updateProfile updateProfileasync = new updateProfile();
					updateProfileasync.execute(new String[] { "profileUpdate"} );
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	         	    }
				 else
				 {
					 
	         	     CharSequence text = "No internet connection!";
	         	     int duration = Toast.LENGTH_SHORT;
	         	     Context context = getApplicationContext();
	         	     Toast toast = Toast.makeText(context, text, duration);
	         	     toast.show();
				 }
			}
		});


		imgdownload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {
					
					Intent intent = new Intent(Settings.this, fulldownload.class);
					startActivity(intent);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				// setListAdapter(adapter);



			}
		});



		if(GlobalData.allPurchased == false)
		{
			imgdownload.setVisibility(View.GONE);
		}
		else
		{
			if(GlobalData.fulldownloadcomplete == false)
			{
				imgdownload.setVisibility(View.VISIBLE);
			}
			else
			{
				imgdownload.setVisibility(View.GONE);
			}
		}



	}
	/**
	 * method is used for checking valid email id format.
	 * 
	 * @param email
	 * @return boolean true for valid false for invalid
	 */
	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		 return isValid;
		 
	}
	public void checkAndCreateDirectory(String dirName){
		//	File new_dir = new File( getCacheDir()+ dirName );
		File new_dir = new File(Environment.getExternalStorageDirectory()+ dirName);
		if( !new_dir.exists() ){
			new_dir.mkdirs();
		}
	}

	public void submitValus()
	{

		String url =	 getResources().getString(R.string.servername);
		String sname = ""+txtName.getText();
			try {
				sname = sname.replace(" ", "%20");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String strUrl = url+"user_setting=yes&user_level="+level+"&user_id="+obUser.getUserId()+"&user_name="+obUser.getSelectedUserName()+"&user_email="+txtEmail.getText()+"&user_surname="+sname;

		JSONObject json = JSONfunctions.getJSONfromURL(strUrl);
		//showDialog(DIALOG_DOWNLOAD_PROGRESS);
		diaflag = 1;
		try{
			if(json !=null )
			{
				JSONArray  workoutList = json.getJSONArray("items");


				for(int i=0;i<workoutList.length();i++){						
					//HashMap<String, String> map = new HashMap<String, String>();	
					JSONObject e = workoutList.getJSONObject(i);
					String status =	e.getString("message") ;
					if(status.equals("success"))
					{
						GlobalData.useremail = txtEmail.getText().toString() ;
						if(!obUser.getLevel().equals(level) )
						{
							if(placeData !=null)
							{
								SQLiteDatabase db = placeData.getWritableDatabase();

								db.delete("FitnessWorkouts", null,null);
								db.delete("FitnessWorkoutVideos", null,null);
								GlobalData.dataAcceptFlag ="0"; 
								prefs.setPreference("dataAcceptFlag", "0");
							}
						}
						if(level.equals(obUser.getLevel())  )
						{
							obUser.setLevel(level);
						}
						else
						{

							try{
								cursorsCountList = getRawEvents("select * from FitnessWorkouts");
								ListCount = cursorsCountList.getCount();
								GlobalData.fulldownloadcomplete = false; 
								prefs.setPreference("fulldownloadcomplete", ""+GlobalData.fulldownloadcomplete);
								GlobalData.freedownloadcomplete = false;
								prefs.setPreference("freedownloadcomplete", ""+GlobalData.freedownloadcomplete);
								if(GlobalData.allPurchased)
								{
									imgdownload.setVisibility(View.VISIBLE);
								}
								if(ListCount == 0)
								{
									try{
										// just doing some long operation
										//	showDialog(DIALOG_DOWNLOAD_PROGRESS);
										try {Thread.sleep(700);
										} catch (InterruptedException exp) {
											// TODO Auto-generated catch block
											exp.printStackTrace();
										}
										if(placeData !=null)
										{
											SQLiteDatabase db = placeData.getWritableDatabase();

											db.delete("FitnessWorkouts", null,null);
											db.delete("FitnessWorkoutVideos", null,null);
											Util.trimCache(getApplicationContext());
										}

										String st =obUser.getUserId();
										// mProgress.setVisibility(View.VISIBLE);
										String urllist =	 getResources().getString(R.string.servername);



										//JSONObject json = JSONfunctions.getJSONfromURL("http://bridge-delivery.com/designportfolio/biju/movie.txt");

										//   JSONObject json = JSONfunctions.getJSONfromURL("http://fitness.bridge-delivery.com/test_method.txt");
										try{
											JSONObject jsonList = JSONfunctions.getJSONfromURL(urllist+"listapps=yes&duration=10&userid="+st+"&lang="+obUser.getSelectedLanguage());
											if(json !=null )
											{
												JSONArray  workoutValues = jsonList.getJSONArray("items");



												for(int loop=0;loop<workoutValues.length();loop++){						
													//	//HashMap<String, String> map = new HashMap<String, String>();	
													JSONObject elist = workoutValues.getJSONObject(loop);

													insertData(elist.getString("id"),elist.getString("name"),elist.getString("rate"),elist.getString("image_android"),elist.getString("image_android"),elist.getString("description"),elist.getString("description_big"),elist.getString("islocked"),elist.getString("props"),elist.getString("image_android"));

												}		

											} 
											else
											{
												
												String text="";
												if(obUser.getSelectedLanguage().equals("1"))
												{ 
													text="Service Unavailable.Please try after some time";
												}
												else
												{
													text ="Service Unavailable.Please nach einiger Zeit versuchen";
												}
												Context context = getApplicationContext();
												Toast toast = Toast.makeText(context,text, 100);
												toast.show();
											}
											// setListAdapter(adapter);
										}catch(JSONException ex1)        {

										}


									} catch (Exception ex) { 


									}
								}

							}catch(Exception ex)
							{
								String st = ex.toString();
								st = st+"";
							}
							finally
							{
								cursorsCountList.close();
							}
							GlobalData.level =  level;
							obUser.setLevel(level);
							GlobalData.freedownloadcomplete = false;
							prefs.setPreference("freedownloadcomplete", ""+GlobalData.freedownloadcomplete);
							 
						}



						GlobalData.selectedFirstName =   txtName.getText().toString(); 
						UpdateUser(GlobalData.selectedFirstName,  GlobalData.level, GlobalData.useremail);
					


					}
					else
					{

					}

				}		

			}
		}
		catch(Exception ex)
		{
			String st = ex.toString();
			if(diaflag ==1)
			{
				//	dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
				diaflag=0;
			}
		}

	}
	/**
	 * Insert data into table
	 * 
	 * @param String , String , String , String , String ,String ,String 
	 * @return  
	 */
	private void insertData(String id, String name, String rate, String image,String imagethumb,String description,String descriptionbig,String islocked,String Props,String ImageAndroid) {
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
	}
	/**
	 * Get raw data
	 * 
	 * @param String 
	 * @return  Cursor
	 */
	private Cursor getRawEvents(String sql) {
		SQLiteDatabase db = (placeData).getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);

		startManagingCursor(cursor);
		return cursor;
	}
	private class InputValidator implements TextWatcher {
		private EditText et;

		private InputValidator(EditText editText) {
			this.et = editText;
		}



		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}



		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (s.length() != 0) {
				switch (et.getId()) {
				case R.id.txtUserName: {
					txtUser.setError(null);
					if (!Pattern.matches("^[a-zA-Z0-9]{1,16}$", s)) {
						if(obUser.getSelectedLanguage().equals("1"))
						{
						txtUser.setError("Username must have alphanumeric");
						}
						else
						{
							txtUser.setError("Benutzername sollte Buchstaben und Zahlen beinhalten");
						}
					}




				}
				break;


				}

			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	}
	private static PlaceDataSQL placeData;
	/**
	 * db update status is locked
	 * 
	 * @return
	 */
	private void UpdateUser(String Fname,String Level,String UserEmail)
	{
		obUser.setSelectedFirstName(Fname); 
		obUser.setLevel(Level);
		obUser.setUseremail(UserEmail);
	}

	private class EmptyTextListener implements OnEditorActionListener {
		private EditText et;

		public EmptyTextListener(EditText editText) {
			this.et = editText;
		}

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

			if (actionId == EditorInfo.IME_ACTION_NEXT) {
				// Called when user press Next button on the soft keyboard

				if (et.getText().toString().equals(""))
					et.setError("Oops! empty.", error_indicator);
			}
			return false;
		}




	}








	 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5
				&& keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			onBackPressed();
		}

		return super.onKeyDown(keyCode, event);
	}

	public void onBackPressed() {
		try {
			Intent intent = new Intent(Settings.this, HomeScreen.class);
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return;
	} 
	
	
	class updateProfile extends AsyncTask<String, Void, String>  {
		 

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS); 
		}

		@Override
		protected String doInBackground(String... aurl) {
			
			
			String username = txtUser.getText().toString();
			String name = txtName.getText().toString();
			String email = txtEmail.getText().toString(); 


			if(username.equalsIgnoreCase("")||name.equalsIgnoreCase("")||email.equalsIgnoreCase("") 
			)
			{
				String mess ="All Fields Required.";

				if(obUser.getSelectedLanguage().equals("1"))
				{
					mess ="All Fields Required.";
				}
				else
				{
					mess ="Alle Felder sind Pflichtfelder.";
				}
				Toast.makeText(Settings.this, mess, Toast.LENGTH_SHORT).show();
			}
			else if(!isEmailValid(email))
			{
				String messvalid ="All Fields Required.";

				if(obUser.getSelectedLanguage().equals("1"))
				{
					messvalid = "Enter valid Email.";
				}
				else
				{
					messvalid ="Geben Sie eine gültige E-Mail.";
				}
				
				Toast.makeText(Settings.this, messvalid, Toast.LENGTH_SHORT).show();
			}

			else
			{


				if(!emailExist)
				{

					if(CheckNetworkAvailability.isNetworkAvailable(Settings.this))
					{


						submitValus();
					}
					else
					{

						String mes = "";
						if(obUser.getSelectedLanguage().equals("1"))
						{
							mes = "There is no internet connection - please try later.";
						}
						else
						{
							mes = "Es besteht keine Internetverbindung - bitte versuche es später.";	
						}
						CharSequence text = mes;
						int duration = Toast.LENGTH_SHORT;
						Context context = getApplicationContext();
						Toast toast = Toast.makeText(context, text, duration);
						toast.show();
					}
				}
				else
				{
					

					String mes = "";
					if(obUser.getSelectedLanguage().equals("1"))
					{
						mes = "email / username not valid.";
					}
					else
					{
						mes = "E-Mail / Benutzername nicht gültig.";	
					}
					
					Toast.makeText(Settings.this, mes, Toast.LENGTH_SHORT).show(); 
				}

			}






			
			
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... progress) {


			//			if(show) {
			//				File dir = Environment.getExternalStorageDirectory();
			//				File imgFile = new File(dir, getFileName(this.paths[(current-1)]));
			//				Bitmap bmp  = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			//				show = false;
			//			}
		}
		private int fladgcount =0;
		@Override
		protected void onPostExecute(String unused) {
			super.onPostExecute(unused);
			 			   
			 			 dismissDialog(DIALOG_DOWNLOAD_PROGRESS);

							String mess="Profile Updated successfully!";


							if(obUser.getSelectedLanguage().equals("1"))
							{

							}
							else
							{
								mess = "Profil wurde aktualisiert!";
							}
			 			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
			 					Settings.this);

						// set title
						alertDialogBuilder.setTitle("Fitness4.me");
						// set dialog message

						alertDialogBuilder
						.setMessage(mess)
						.setCancelable(false)
						.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, close
								// current activity

								//	dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
								
								
								
								dialog.cancel();
								if(!GlobalData.freedownloadcomplete)
								{
								try {
									
									Intent intent = new Intent(Settings.this,freepurchasedownload.class);
									startActivity(intent);
									GlobalData.freedownloadcomplete = true;
									prefs.setPreference("freedownloadcomplete", ""+GlobalData.freedownloadcomplete);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
								}
							}
						});

						 
						// create alert dialog
						alertDialog = alertDialogBuilder.create();

						// show it
						alertDialog.show();

		}

	}
	
	
	
}
