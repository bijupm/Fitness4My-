package com.data.pack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.data.fitness4me.R;
public class evalution extends Activity {
	private Button imgYesButton ;
	private Button imgNoButton ;
	private  Button imgLatButton ;
	private Button btnEvldntAsk ;
	private TextView txthead ;
	private TextView txtPleaseHelp ;
	private VOUserDetails obUser =  new VOUserDetails(this); 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.evalution);
	    	if(txthead == null)
		     txthead = (TextView) findViewById(R.id.txtheadEv);
	    	if(txtPleaseHelp == null)
		     txtPleaseHelp = (TextView) findViewById(R.id.txtPleaseHelp);
	    	if(imgYesButton == null)
		    imgYesButton = (Button) findViewById(R.id.btnEvYes);
	    	if(imgNoButton == null)
		    imgNoButton = (Button) findViewById(R.id.btnEvNo);
	    	if(imgLatButton == null)
		    imgLatButton = (Button) findViewById(R.id.btnEvlater);
	    	if(btnEvldntAsk == null)
		    btnEvldntAsk = (Button) findViewById(R.id.btnEvldntAsk);
		    GlobalData.appcount = 0;
		 	  Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/ARIAL.TTF");
		  txthead.setTypeface(tf);
		  txtPleaseHelp.setTypeface(tf);
		  if(obUser.getSelectedLanguage().equals("1"))
			 {
			  imgYesButton.setText(Html.fromHtml("YES!<br>I am satisfied and would like to rate it"));
			  imgNoButton.setText(Html.fromHtml("NO!<br>Fitness4.me can be improved.<br>Let me give you my feedback."));
			  imgLatButton.setText(Html.fromHtml("I will rate this <br> App  later"));
			  btnEvldntAsk.setText(Html.fromHtml("Don’t ask <br> me again"));
			  txthead.setText(Html.fromHtml("<br>If you are satisfied with Fitness4.me please let OTHERS know.<br>If you would like to improve something, please tell US."));
			  txtPleaseHelp.setText("Please help us!");
			 }
			 else
			 {
				  imgYesButton.setText(Html.fromHtml("Ja!<br>ich bin mit fitness4.me zufrieden und möchte es bewerten."));
				  imgNoButton.setText(Html.fromHtml("Nein!<br>Fitness4.me kann verbessert werden. <br>Ich möchte euch mein Feedback mitteilen."));
				  imgLatButton.setText(Html.fromHtml(" Ich werde die App <br> später bewerten "));
				 btnEvldntAsk.setText(" Nicht erneut fragen \n");
				  txthead.setText(Html.fromHtml("<br>Bist du mit fitness4.me zufrieden, dann lass es auch ANDERE wissen.<br> Hast du Verbesserungsvorschläge, wende dich an UNS."));
				  txtPleaseHelp.setText("Bitte hilf uns!");	
			 }
		  imgYesButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 
			 try
			 {
				Intent goToMarket = new Intent(Intent.ACTION_VIEW)
			    .setData(Uri.parse("market://details?id=com.data.fitness4me"));
				startActivity(goToMarket);
			 }
			 catch (Exception e) {
				// TODO: handle exception
			}
			}
		});
		  
		  
		  
		  imgNoButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 
					 if(CheckNetworkAvailability.isNetworkAvailable(evalution.this))
		         	    {
					 try {
						Intent intent = new Intent(evalution.this, Feedback.class);
						   
						   
						 startActivity(intent);
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
		  
		  
		  btnEvldntAsk.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 
					GlobalData.dntAskflag = true;
					 try {
						Intent intent = new Intent(evalution.this, FitnessforMeActivity.class);
						 startActivity(intent);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				 
				}
			});
		  
		  
		  imgLatButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 
					 
					 try {
						Intent intent = new Intent(evalution.this, FitnessforMeActivity.class);
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
	
}
