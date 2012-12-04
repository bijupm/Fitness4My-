/**
 * 
 */
package com.data.pack;

import java.io.File;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.data.fitness4me.R;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

/**
 * @author Biju.P.M
 *
 */
public class userinfo extends Activity  {
	static final int DATE_DIALOG_ID = 0;
	private EditText txtName  ;
	private EditText txtLastName  ;
	private EditText txtUser  ;
	private EditText txtEmail  ;
	private EditText txtPassword  ;
	private String name;
	private String lastname;
	private String username;
	private String password;
	private String email;
	private Drawable error_indicator;
	//private CheckBox checkBox;
	//private TextView termsText;
	private String level = "1";;
	private RadioButton rbbeginner;
	private RadioButton rbAdv;
	private RadioButton rbExpert;
	private int checkedRadioButton; 
	private Boolean userExist = false;
	private Boolean emailExist = false;
	private AdView adView;
	private TextView lblAbout;
	private TextView lblName;
	private TextView lblLastName;
	private TextView lblIam;
	private TextView lblBAE;
	private TextView lblEmail;
	private TextView lblUserName;
	private TextView lblPassword;
	private TextView lblAccept;
	private Button imgButton;
	//SeekBar mSeekBar;
	private RadioGroup mRadioGroup;;
	private   PlaceDataSQL placeData;
	private VOUserDetails obUser; 
private String sendmailString="";
	private int seekbarvalue = 1;
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	public void onStopTrackingTouch(SeekBar seekBar) {

	}
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {



		seekbarvalue = (progress+1);
	}


	private ProgressDialog mProgressDialog;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(placeData == null)
		placeData = new PlaceDataSQL(this);
		if(obUser == null)
			obUser =  new VOUserDetails(this); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo);
		txtName = (EditText)findViewById(R.id.txtname);
		txtLastName = (EditText)findViewById(R.id.txtLastname);
		// mSeekBar = (SeekBar)findViewById(R.id.seekBarIam);
		txtUser = (EditText)findViewById(R.id.txtUserName);
		txtEmail = (EditText)findViewById(R.id.txtemail);
		imgButton = (Button) findViewById(R.id.imgnextuserinfo);
		rbbeginner = (RadioButton) findViewById(R.id.rbbeginner);
		rbAdv = (RadioButton) findViewById(R.id.rbAdvanced);
		rbExpert = (RadioButton) findViewById(R.id.rbExpert);
		txtPassword = (EditText)findViewById(R.id.txtPassword);
		mRadioGroup = (RadioGroup) findViewById(R.id.rbgroup);
		lblAbout = (TextView)findViewById(R.id.lblAbout);
		lblName = (TextView)findViewById(R.id.lblName);
		lblLastName = (TextView)findViewById(R.id.lblLastName);
		lblIam = (TextView)findViewById(R.id.lblIam);
		lblBAE = (TextView)findViewById(R.id.lblBAE);
		lblEmail = (TextView)findViewById(R.id.lblEmail);
		lblUserName = (TextView)findViewById(R.id.lblUserName);
		lblPassword = (TextView)findViewById(R.id.lblPassword);
		lblAccept = (TextView)findViewById(R.id.lblAccept);
		if (adView == null)
			adView = (AdView) this.findViewById(R.id.adView);



		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/ARIAL.TTF");

		lblAbout.setTypeface(tf,1) ;
		lblName.setTypeface(tf,0) ;
		lblLastName.setTypeface(tf,0) ;
		lblIam.setTypeface(tf,0)  ;
		lblBAE.setTypeface(tf,0)  ;
		lblEmail.setTypeface(tf,0)  ;
		lblUserName.setTypeface(tf,0)  ;
		lblPassword.setTypeface(tf,0) ;
		lblAccept.setTypeface(tf,0)  ;
		lblName.setTypeface(tf) ;
		lblIam.setTypeface(tf)  ;
		lblBAE.setTypeface(tf)  ;
		lblEmail.setTypeface(tf)  ;
		lblUserName.setTypeface(tf)  ;
		lblPassword.setTypeface(tf) ;
		if(obUser.getSelectedLanguage().equals("1"))
		{
			lblBAE.setText(" Beginner        Advanced         Expert"); 
			lblIam.setText("Level");
			lblAccept.setText("Shortly you will receive an email with a link to your personal login. Don\'t worry, it\'s free we do not share your data with anyone");
			lblName.setText("First Name");
			lblLastName.setText("Last Name");
		}
		else
		{
			lblName.setText("Vorname");
			lblIam.setText("Level");
			lblLastName.setText("Nachname");
			lblAbout.setText("erzähle uns etwas über Dich");
			lblBAE.setText(" Anfänger        Fortgeschritten      Profi"); 
			lblUserName.setText("Benutzername"); 
			imgButton.setText("Registrieren");
			lblPassword.setText("Passwort");
			lblEmail.setText("E-mail");
			lblAccept.setText("Wir schicken Dir gleich eine e-mail mit Deinen persönlichen zugangsdaten für unsere website. Mach dir keine Sorgen, es ist kostenlos und  wir teilen Deine Daten mit niemandem.");
		}

		mProgressDialog = new ProgressDialog(this);
		String builderStr="";
		if(obUser.getSelectedLanguage().equals("1"))
		{

			builderStr = "Please wait..";
		}
		else
		{
			builderStr = "Bitte warten ..";
		}
	try {
			

			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
//			String str_ScreenSize = "The Android Screen is: " + dm.widthPixels
//					+ " x " + dm.heightPixels;
			// Toast.makeText(videostart.this," "+str_ScreenSize, 1).show();
			int maxWidth = dm.widthPixels / 2;
			 
			if (adView == null)
				adView = (AdView) this.findViewById(R.id.adViewUserInfo);
			
			if (maxWidth == 360) {
				(new Thread() {
					public void run() {
						if(adView !=null)
						{
						adView.setVisibility(View.VISIBLE);
						adView.loadAd(new AdRequest());
						}
					}
				}).start();
				
			} else {
				if(adView!=null)
				{
				adView.setVisibility(View.GONE);
				adView.loadAd(null);
				}
			}
		} catch (Exception e) {
			String st = e.toString();
			st = st + "";
		}
		
		//    mProgressDialog.show(videostart.this, "", "Loading...");
		mProgressDialog = new ProgressDialog(userinfo.this);
		mProgressDialog.setMessage(builderStr);
		mProgressDialog.setTitle(null);
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(true);
		txtPassword.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					// Toast.makeText(getApplicationContext(), "got the focus", 2000).show();
				}else{
					//Toast.makeText(getApplicationContext(), "lost the focus", 2000).show();
				}

			}
		});
		checkedRadioButton = mRadioGroup.getCheckedRadioButtonId();
		mRadioGroup.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener()
		{

			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub

				RadioButton rb=(RadioButton)findViewById(arg1);
				int strlevel =  mRadioGroup.getCheckedRadioButtonId();
				if(strlevel == R.id.rbbeginner)
				{
					level = "1";
				}
				else if(strlevel == R.id.rbAdvanced)
				{
					level = "2";
				}
				else if(strlevel == R.id.rbExpert)
				{
					level = "3";
				}

			}

		}
		);


		txtUser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {


				if (!hasFocus) {
					if(txtUser.getText().length() > 0)
					{
						String url =	 getResources().getString(R.string.servername);
						JSONObject json = JSONfunctions.getJSONfromURL(url+"checkusername=yes&username="+txtUser.getText());
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
										txtUser.setError("Username Already Exists");
									}
									else
									{
										txtUser.setError("Benutzername existiert bereits");
									}

									txtUser.setFocusable(true);
									userExist = true;
								}
								else
								{
									txtUser.setError(null);  
									userExist = false;

								}

							}		




						}
						catch(Exception ex)
						{
					String st = ex.toString();
					st = st+"";
						}

					}
				}
			}
		});



		txtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {

				if (!hasFocus) {
					if(!isEmailValid(txtEmail.getText().toString()))
					{
						emailExist = true;
						String messvalid ="";

						if(obUser.getSelectedLanguage().equals("1"))
						{
							messvalid = "Enter valid Email.";
						}
						else
						{
							messvalid ="Geben Sie eine gültige E-Mail.";
						}
						
						txtEmail.setError(messvalid);

					}
					else
					{

 

					}

				}
			}
		});


		error_indicator = getResources().getDrawable(R.drawable.icon );
		int left = 0;
		int top = 0;

		int right = error_indicator.getIntrinsicHeight();
		int bottom = error_indicator.getIntrinsicWidth();
		txtUser.addTextChangedListener(new InputValidator(txtUser));
		txtName.addTextChangedListener(new InputValidator(txtName));
		txtLastName.addTextChangedListener(new InputValidator(txtLastName));
		txtPassword.addTextChangedListener(new InputValidator(txtPassword));

		error_indicator.setBounds(new Rect(left, top, right, bottom));

		//		  mPickDate.setOnClickListener(new OnClickListener() {
		//			
		//			@Override
		//			public void onClick(View v) {
		//				// TODO Auto-generated method stub
		//				
		//			}
		//		});


		imgButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				username = txtUser.getText().toString();
				name = txtName.getText().toString();
				lastname = txtLastName.getText().toString();
				email = txtEmail.getText().toString(); 
				password = txtPassword.getText().toString();


				// find the radiobutton by returned id




				if(username.equalsIgnoreCase("")||name.equalsIgnoreCase("")||email.equalsIgnoreCase("")||
						password.equalsIgnoreCase("") ||  lastname.equalsIgnoreCase(""))
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
					Toast.makeText(userinfo.this, mess, Toast.LENGTH_SHORT).show();
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
					
					Toast.makeText(userinfo.this, messvalid, Toast.LENGTH_SHORT).show();
				}
				else
				{
					// biju want to uncomment ---- if(!userExist && !emailExist)
					if(!userExist)
					{
						submitValus();
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
						
						Toast.makeText(userinfo.this, mes, Toast.LENGTH_SHORT).show(); 
					}

				}






			}
		});


	}



	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putString("name", txtName.getText().toString());

		super.onSaveInstanceState(outState);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		txtName.setText(savedInstanceState.getString("name"));
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
		//return true;
	}
	private Button.OnClickListener datePickerButtonOnClickListener
	= new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final Calendar c = Calendar.getInstance();
			myYear = c.get(Calendar.YEAR);
			myMonth = c.get(Calendar.MONTH);
			myDay = c.get(Calendar.DAY_OF_MONTH);
			showDialog(ID_DATEPICKER);
		}
	};


	private int myYear, myMonth, myDay;
	static final int ID_DATEPICKER = 0;
	private DatePickerDialog.OnDateSetListener myDateSetListener
	= new DatePickerDialog.OnDateSetListener(){
		@Override
		public void onDateSet(DatePicker view, int year, 
				int monthOfYear, int dayOfMonth) {
			// TODO Auto-generated method stub
			String date =  String.valueOf(dayOfMonth)+":"+String.valueOf(monthOfYear)+":"+String.valueOf(year) ;


		} 
	};

	private class InputValidator implements TextWatcher {
		private EditText et;

		private InputValidator(EditText editText) {
			this.et = editText;
		}

		@Override
		public void afterTextChanged(Editable s) {

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
						txtUser.setError("Username must contain only alphanumeric characters ");
						}
						else
						{
						txtUser.setError("Benutzername sollte Buchstaben und Zahlen beinhalten ");
						}
						
					}
					break;
				}
				case R.id.txtname: {
					txtName.setError(null);
					if (!Pattern.matches("^[a-zA-Z0-9 ]{1,16}$", s)) {
						if(obUser.getSelectedLanguage().equals("1"))
						{
						txtName.setError("Name must contain only alphanumeric characters ");
						}
						else
						{
						txtName.setError("Vorname sollte Buchstaben und Zahlen beinhalten ");
						}
					}
					break;
				}
				case R.id.txtLastname: {
					txtLastName.setError(null);
					if (!Pattern.matches("^[a-zA-Z0-9 ]{1,16}$", s)) {

						if(obUser.getSelectedLanguage().equals("1"))
						{
						txtLastName.setError("Name must contain only alphanumeric characters ");
						}
						else
						{
							txtLastName.setError("Nachname sollte Buchstaben und Zahlen beinhalten ");
						}
					}
					break;
				}
				
				
				case R.id.txtPassword: {
					txtPassword.setError(null);
					if (!Pattern.matches("^[a-zA-Z0-9]{1,16}$", s)) {
						if(obUser.getSelectedLanguage().equals("1"))
						{
						txtPassword.setError("Password must contain only alphanumeric characters ");
						}
						else
						{
							txtPassword.setError("Passwort sollte Buchstaben und Zahlen beinhalten ");	
						}
					}


				}
				break;


				}

			}
		}
	}
	//	public boolean dispatchTouchEvent(MotionEvent event) {
	//	    View v = getCurrentFocus();
	//	    boolean ret = super.dispatchTouchEvent(event);
	//	    InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);;
	//	    View w = getCurrentFocus();
	//	    int scrcoords[] = new int[2];
	//	    w.getLocationOnScreen(scrcoords);
	//	    float x = event.getRawX() + w.getLeft() - scrcoords[0];
	//	    float y = event.getRawY() + w.getTop() - scrcoords[1];
	//
	//	    Log.d("Activity", "Touch event "+event.getRawX()+","+event.getRawY()+" "+x+","+y+" rect "+w.getLeft()+","+w.getTop()+","+w.getRight()+","+w.getBottom()+" coords "+scrcoords[0]+","+scrcoords[1]);
	//	    if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) { 
	//	        inputManager.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
	//	    }
	//	    return ret;
	//
	//	}
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


	public void onFocusChange(View v, boolean hasFocus) {


	}
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private int flag=0;
	private String userid;
	public void submitValus()
	{
		String[] url = new String[0];
		submitValuesAsync dloadFAsync = new submitValuesAsync(url);
		dloadFAsync.execute(url);


	}

	/**
	 * Insert data into table
	 * 
	 * @param String , String , String , String , String ,String ,String 
	 * @return  
	 */
	//	private void insertWorkoutData(String id, String name, String rate, String image,String description,String islocked) {
	//		SQLiteDatabase db = placeData.getWritableDatabase();
	//		ContentValues values;
	//		values = new ContentValues();
	//		values.put("Id", id);
	//		values.put("Rate", rate);
	//		values.put("Name", name);
	//		values.put("Description", description);
	//		values.put("Image", image);		 
	//		values.put("IsLocked", islocked);
	//		db.insert("FitnessWorkouts", null, values);
	//	}
	/**
	 * Insert data into table
	 * 
	 * @param String , String , String , String , String ,String ,String 
	 * @return  
	 */
	private void insertData(String id, String username, String fname,String level,String email) {
		try {
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
			e.printStackTrace();
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
			password = password.replace("\n", "");
			String url =	 getResources().getString(R.string.servername);
			try {
				name = name.replace(" ", "%20");
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				lastname = lastname.replace(" ", "%20");
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			String strUrl = url+"register=yes&fname="+name+"&sname="+lastname+"&email="+email+"&username="+username+"&password="+password+"&level="+level+"&device=2&lang="+obUser.getSelectedLanguage();
			obUser.setLevel(level);
			json = JSONfunctions.getJSONfromURL(strUrl);

			try{
				if(json !=null )
				{
					JSONArray  workoutList = json.getJSONArray("items");

					if(placeData !=null)
					{
						SQLiteDatabase db = placeData.getWritableDatabase();

						db.delete("FitnessWorkouts", null,null);
						db.delete("FitnessWorkoutVideos", null,null);
					}
					for(int i=0;i<workoutList.length();i++){						
						//HashMap<String, String> map = new HashMap<String, String>();	
						JSONObject e = workoutList.getJSONObject(i);
						 userid = e.getString("userid");
//						GlobalData.useremail =  email;
//						GlobalData.level =  level;
//						GlobalData.selectedFirstName =   name; 
//						GlobalData.selectedUserName =  username; 
						insertData(userid,username,name,level,email);

						if(flag == 1)
							dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
						flag= 0;
						HomeScreen.userRegID = userid;

						try {
							Intent intent = new Intent(userinfo.this, HomeScreen.class);
							// intent.putExtra("userID", userid);
							// intent.putExtra("UserName", username);
							startActivity(intent);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}	 
					}		

				}
			}
			catch(Exception ex)
			{
				String st = ex.toString();
			}
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
		}


	}



}
