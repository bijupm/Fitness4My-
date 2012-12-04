package com.data.pack;

import java.util.Locale;

import com.data.fitness4me.R;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SignUP extends Activity{

	public Button loginScreenButton;
	public Button signUpButton;
	private TextView lblhave;
	private TextView lblsinup;
	private int langPosi = 0;
	private String array_spinner[] ={"English","German"};
	AlertDialog.Builder builder;
	AlertDialog alertDialog;
	public Button btnLanguage ;
	private ArrayAdapter adapter;
	private Prefs prefs = new Prefs(this);
	private VOUserDetails obUser =  new VOUserDetails(this); 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		GlobalData.allPurchased = false;
		setContentView(R.layout.signup);
		loginScreenButton = (Button) findViewById(R.id.btnLoginScreen);
		signUpButton = (Button) findViewById(R.id.btnSignup);
		lblhave = (TextView)findViewById(R.id.lblhave);
		lblsinup = (TextView)findViewById(R.id.lblsinup);
		AdView adView = (AdView)this.findViewById(R.id.adViewSignUp);
		adView.loadAd(new AdRequest());
		 Typeface tf = Typeface.createFromAsset(getAssets(),
         "fonts/ARIAL.TTF");
		 lblhave.setTypeface(tf,0);	 
		 lblhave.setTypeface(tf);
		 lblsinup.setTypeface(tf,0);	 
		 signUpButton.setTypeface(tf,0);
		 loginScreenButton.setTypeface(tf,0);
		if(obUser.getSelectedLanguage().equals("1"))
		{
			lblsinup.setText("New to Fitness4.me ? \n");
			lblhave.setText("\nYou already have a \nfitness4.me account? \n");
			signUpButton.setText("Sign Up");
			loginScreenButton.setText("Sign In");
		}
		else  
		{
			lblsinup.setText("Du bist neu bei fitness4.me? \n");
			lblhave.setText("\nDu hast bereits einen fitness4.me Account? \n");
			signUpButton.setText("Registrieren");
			loginScreenButton.setText("Anmelden");
		}
		
		
		loginScreenButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(CheckNetworkAvailability.isNetworkAvailable(SignUP.this))
				{
					try {
						Intent intent = new Intent(SignUP.this, loginform.class);
						startActivity(intent);
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

		signUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(CheckNetworkAvailability.isNetworkAvailable(SignUP.this))
				{
					try {
						Intent intent = new Intent(SignUP.this, welcomepage.class);
						startActivity(intent);
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


				//	http://fitness.bridge-delivery.com/device/userlogin/username/leks/password/123456
				//Set the transition -> method available from Android 2.0 and beyond  
				// overridePendingTransition(R.anim.push_left_in,R.anim.push_left_in);  
			}
		});

		Context mContext = getApplicationContext();
		LayoutInflater inflater 
		= (LayoutInflater)getBaseContext()
		.getSystemService(LAYOUT_INFLATER_SERVICE);  
		
		
		View layout = inflater.inflate(R.layout.spinner,null);

		Display display = getWindowManager().getDefaultDisplay();
		
//		String array_spinner[];
//		array_spinner=new String[5];
//
//		array_spinner[0]="English";
//		array_spinner[1]="German";

		Spinner spinnerLang = (Spinner) layout.findViewById(R.id.Spinner01);
		  btnLanguage  = (Button)layout.findViewById(R.id.btnLanguage);
		  adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, array_spinner);
		  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerLang.setAdapter(adapter);
		try
		{
		//spinnerLang.setOnItemSelectedListener(new SpinnerListener(layout));
		spinnerLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		       
		    	Object item = parent.getItemAtPosition(pos);
		    	langPosi = pos+1;
		    	 
		    	 if(langPosi == 1)
		    	 {
		    		 btnLanguage.setText("Select Language");
		    		 obUser.setSelectedLanguage("1");
		    		 
		    	 }
		    	 else
		    	 {
		    		 btnLanguage.setText("Sprache auswählen");
		    		 obUser.setSelectedLanguage("2");
		    	 }
		    }
		    public void onNothingSelected(AdapterView<?> parent) {
		    	alertDialog.cancel();
		    }
		});
		}catch (Exception e) {
			 String st = e.toString();
			 alertDialog.cancel();
			 st = st+"";
		}
		
		btnLanguage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				obUser.setSelectedLanguage(langPosi+"");
				 alertDialog.cancel();
				 if(obUser.getSelectedLanguage().equals("1"))
					{
						lblsinup.setText("New to Fitness4.me ? \n");
						lblhave.setText("\nYou already have a \nfitness4.me account? \n");
						signUpButton.setText("Sign Up");
						loginScreenButton.setText("Sign In");
					}
					else  
					{
						lblsinup.setText("Du bist neu bei fitness4.me? \n");
						lblhave.setText("\nDu hast bereits einen fitness4.me Account? \n");
						signUpButton.setText("Registrieren");
						loginScreenButton.setText("Anmelden");
					}
						try {
							Intent intent = new Intent(SignUP.this, submitpage.class);
							startActivity(intent);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			}
		});
		
//		if(GlobalData.termsShow == 0 && obUser.getSelectedLanguage().length() > 0)
//		{
//			try {
//				Intent intent = new Intent(SignUP.this, submitpage.class);
//				startActivity(intent);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		builder = new AlertDialog.Builder(SignUP.this);
		builder.setView(layout);
		alertDialog = builder.create();
		alertDialog.show();
		
		
	}
}
