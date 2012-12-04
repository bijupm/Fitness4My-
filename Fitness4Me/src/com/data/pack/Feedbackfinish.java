package com.data.pack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.data.fitness4me.R;

public class Feedbackfinish extends Activity {
	private Button imgSubFinishButton ;
	private String stComment ="";
	String strmailsend ="Email wurde erfolgreich versendet";
	private TextView txtfinish;
	private Typeface tf;
	private VOUserDetails obUser =  new VOUserDetails(this); 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedbackfinish);
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		stComment = extras.getString("comment");

		GlobalData.mailComment = stComment;
		String[] url =  new String[0];
		if(txtfinish == null)
		{
			txtfinish = (TextView)findViewById(R.id.txtfinish);
		}
		if (tf == null) {
			tf = Typeface.createFromAsset(getAssets(), "fonts/ARIAL.TTF");
			txtfinish.setTypeface(tf, 0);
		}
		try {
			MailSendTask mailSendTask = new MailSendTask(url);
			mailSendTask.execute(url);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.flags |= LayoutParams.FLAG_KEEP_SCREEN_ON;
			getWindow().setAttributes(params);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		imgSubFinishButton = (Button) findViewById(R.id.btnfinishsubmit);


		if(obUser.getSelectedLanguage().equals("1"))
		 {
			strmailsend ="Email was sent successfully.";
			txtfinish.setText("Thank you very much for your feedback");
		 }
		else
		{
			imgSubFinishButton.setText("Fortfahren");
			strmailsend ="E-Mail wurde Erfolgreich Versendet";
			txtfinish.setText("Vielen Dank für deine Berwertung");
		}
		
		imgSubFinishButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					
					
					Toast.makeText(getApplicationContext(), strmailsend, Toast.LENGTH_LONG).show(); 
					Intent intent = new Intent(Feedbackfinish.this, FitnessforMeActivity.class);
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
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		if ( keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			Log.d("CDA", "onKeyDown Called");

		}

		return  false;
	}


	class MailSendTask extends AsyncTask<String, String, String>  {

		String[] paths;

		public MailSendTask(String[] paths) {
			super();
			try
			{
				this.paths = paths;
			}
			catch (Exception e) {


			}
			// for(int i=0; i<paths.length; i++)
			// System.out.println((i+1)+":  "+paths[i]);
		}
		private ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//			   dialog = ProgressDialog.show(Feedbackfinish.this, "", 
			//	                "mail sending. Please wait...", true);
			//dialog.show();
		}

		@Override
		protected String doInBackground(String... aurl) {

			try {

				if(CheckNetworkAvailability.isNetworkAvailable(getApplicationContext()))
				{


					if(GlobalData.mailComment.length() > 0)
					{



						Mail m = new Mail("fitness4me.android@gmail.com", "fitness4me"); 
						//erik@competenz-management.com
					 	String[] toArr = {"erik@competenz-management.com","support@fitness4.me"};
						//String[] toArr = {"biju.pm@bridge-india.in","lekbin@gmail.com"}; 
						m.setTo(toArr); 
						m.setFrom("fitness4me.android@gmail.com"); 
						  
						m.setSubject("Feedback from android"); 
						String body = new String("<html><body><table><tr><td><br/> You have received a feedback from : " +obUser.getSelectedFirstName()+"</td></tr><br/>Sender Email :  " +obUser.getUseremail() + "<br/><br/>Feedback: "+ GlobalData.mailComment+ "");

						m.setBody(body); 
						GlobalData.mailComment="";
						try { 

							if(m.send()) { 
								//Toast.makeText(getApplicationContext(), "Email was sent successfully.", Toast.LENGTH_LONG).show(); 
							} else { 


							} 
						} catch(Exception e) { 
							//Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show(); 

						} 


					}	 



				}
			} catch(Exception e) { 
				//Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show(); 

			} 
			return null;
		}

		@Override
		protected void onProgressUpdate(String... progress) {


		}
		private int flagcount =0;
		@Override
		protected void onPostExecute(String unused) {
			//lv.refreshDrawableState();
			super.onPostExecute(unused);
			// if(dialog.isShowing())
			// dialog.dismiss();
		}

	}



}
