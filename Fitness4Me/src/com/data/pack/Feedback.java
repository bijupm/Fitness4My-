package com.data.pack;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.data.fitness4me.R;
public class Feedback extends Activity {
	private Button imgSubButton ;

	private EditText txtfeedcomment ;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private int flag=0;
	private TextView headText;
	private TextView txtthankyou;
	private TextView  txtheadtext;
	private int mProgressStatus = 0;
	private String nullfeedbackmsg="";	
	private ProgressDialog mProgressDialog;
	private VOUserDetails obUser =  new VOUserDetails(this);
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
	public boolean dispatchTouchEvent(MotionEvent event) {
	    boolean ret = false;
		try {
			View v = getCurrentFocus();
			ret = super.dispatchTouchEvent(event);
			InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);;
			View w = getCurrentFocus();
			int scrcoords[] = new int[2];
			w.getLocationOnScreen(scrcoords);
			float x = event.getRawX() + w.getLeft() - scrcoords[0];
			float y = event.getRawY() + w.getTop() - scrcoords[1];

			if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) { 
			    inputManager.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return ret;

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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		txtfeedcomment = (EditText) findViewById(R.id.txtfeedcomment);
		headText = (TextView) findViewById(R.id.txtheadtext);
		txtthankyou = (TextView) findViewById(R.id.txtthankyou);
		imgSubButton = (Button) findViewById(R.id.btnsubmit);
		txtheadtext = (TextView) findViewById(R.id.txtheadtext);
		txtfeedcomment.setClickable(true);
		imgSubButton.setClickable(true);
		  Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/ARIAL.TTF");
		  headText.setTypeface(tf);
		  txtthankyou.setTypeface(tf);
		  txtheadtext.setTypeface(tf);
			if(obUser.getSelectedLanguage().equals("1"))
			 {
				txtthankyou.setText("Please help us!");	
				txtheadtext.setText("We are committed to giving the user the best results using Fitness4.me. But we need your feedback to get better! Please tell us what we can improve");
				nullfeedbackmsg = "Please enter your feedback" ;
			 }
			else
			{
				nullfeedbackmsg ="Sende uns bitte ein Feedback";
				imgSubButton.setText("Feedback senden");
				txtthankyou.setText("Bitte hilf uns!");	
				txtheadtext.setText("Danke! Wir wollen, dass unsere Benutzer die besten Resultate beim Training mit fitness4.me erzielen.Aber wir brauchen dein Feedback, um uns zu verbessern.");
			}
		
			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.flags |= LayoutParams.FLAG_KEEP_SCREEN_ON;
			getWindow().setAttributes(params);
		imgSubButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				txtfeedcomment.setClickable(false);
				imgSubButton.setClickable(false);
				flag = 1;
				if(CheckNetworkAvailability.isNetworkAvailable(Feedback.this))
				{
					String st = txtfeedcomment.getText()+"".trim();
					if(st.trim().equalsIgnoreCase(""))
					{
						Toast.makeText(Feedback.this, nullfeedbackmsg, Toast.LENGTH_SHORT).show();
					}
					else
					{

						 

							try {
								String comment = txtfeedcomment.getText()+"".trim();
								 
								Intent intent = new Intent(Feedback.this, Feedbackfinish.class);
								intent.putExtra("comment", comment);
								startActivity(intent);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
 
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

}
