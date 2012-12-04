package com.data.pack;

import sun.text.normalizer.UBiDiProps;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.data.fitness4me.R;

public class congrats  extends Activity {
	private TextView txtcongrats;
		private TextView txtheadcongrats;
		private TextView	txtheadcongratsupgrade;
		private TextView txtcongratsWouldyou;
		private TextView txtcongratsDepending;
	private Button btnYes;
	private Button btnNo;
	private ImageButton popupCongratsClose;
	private String strok="OK";
	private String strAlertMessage="";
	private String strYes= "Ja";
	private VOUserDetails obUser =  new VOUserDetails(this); 
	private Prefs prefs = new Prefs(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.congrats);
		txtcongrats = (TextView)findViewById(R.id.txtcongrats);
		txtheadcongrats = (TextView)findViewById(R.id.txtheadcongrats);
		txtheadcongratsupgrade = (TextView)findViewById(R.id.txtheadcongratsupgrade);
		txtcongratsWouldyou = (TextView)findViewById(R.id.txtcongratsWouldyou);
		txtcongratsDepending = (TextView)findViewById(R.id.txtcongratsDepending);
		
		btnYes = (Button)findViewById(R.id.btnCongratsYes);
		btnNo = (Button)findViewById(R.id.btnCongratsNo);
		popupCongratsClose = (ImageButton)findViewById(R.id.popupCongratsClose);
		 Typeface tf = Typeface.createFromAsset(getAssets(),
         "fonts/ARIAL.TTF");
		 txtcongrats.setTypeface(tf,0) ;
		 txtheadcongrats.setTypeface(tf,0) ;
		 txtheadcongratsupgrade.setTypeface(tf,0) ;
		 txtcongratsDepending.setTypeface(tf,0) ;
		 txtcongratsWouldyou.setTypeface(tf,0) ;
		 btnYes.setTypeface(tf,0) ;
		 btnNo.setTypeface(tf,0) ;
		 if(obUser.getSelectedLanguage().equalsIgnoreCase("1"))
			{
			 txtheadcongrats.setText(Html.fromHtml("<B>Congratulations</B>"));
			 txtcongrats.setText("");
			 txtheadcongratsupgrade.setText("");
			 txtcongratsWouldyou.setText("");
			 txtcongratsDepending.setText("");
			 txtheadcongratsupgrade.setText(" You have upgraded to the full version of ");
			 btnNo.setText(" No I will download later ");
			 txtcongrats.setText(Html.fromHtml("<b>10 minutes Workouts</b>"));
			 txtcongratsWouldyou.setText("Would you like to download your new workouts now ?");
			 txtcongratsDepending.setText("Depending on your internet connection this might take several minutes\n");
			 strAlertMessage = "You can download all the workouts later through the settings";
			 strok ="ok";
			 strYes= "Yes";
			}
		 else
		 {
			 txtheadcongrats.setText(Html.fromHtml("<B>Glückwunsch</B>"));
			 btnNo.setText(" Nein, später ");
			 txtcongrats.setText(Html.fromHtml("<b>10 Minuten Workouts</b>")); 
			 txtheadcongratsupgrade.setText(" Du hast die aktuelle Version aller Workouts ");
			 txtcongratsWouldyou.setText("Möchtest Du jetzt Deine neuen Workouts herunter laden ?");
			 txtcongratsDepending.setText("Abhängig von deiner Internet-Verbindung könnte dies mehrere Minuten dauern\n");
			 strAlertMessage = "Du kannst später alle Workouts unter Einstellungen herunterladen"; 
			 strok ="Ok";
			 strYes= "Ja";
		 }
		btnYes.setText(strYes);
		 popupCongratsClose.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog	  alertDialog = new AlertDialog.Builder(congrats.this).create();  
					
					alertDialog.setTitle("fitness4.me");
					 
					
					alertDialog.setMessage(strAlertMessage);
	
					alertDialog.setButton(-1, strok, new DialogInterface.OnClickListener()
					{  
						public void onClick(DialogInterface dialog, int which) {  
							try
							{
								Intent intent = new Intent(congrats.this, FitnessforMeActivity.class);
								startActivity(intent);
	
							}
							catch (Exception e) {
								
								// TODO: handle exception
							}
						} }); 
					alertDialog.show();
				}});
		 btnYes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						Intent intent = new Intent(congrats.this, fulldownload.class);
						startActivity(intent);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
				 
				}

			});
		 btnNo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					
					
					AlertDialog	  alertDialog = new AlertDialog.Builder(congrats.this).create();  
					
					
									alertDialog.setTitle("fitness4.me");
									alertDialog.setMessage(strAlertMessage);
					
									alertDialog.setButton(-1, strok, new DialogInterface.OnClickListener()
									{  
										public void onClick(DialogInterface dialog, int which) {  
											try
											{
												Intent intent = new Intent(congrats.this, FitnessforMeActivity.class);
												prefs.setPreference("allPurchasedfirstTime", ""+false);
												startActivity(intent);
					
											}
											catch (Exception e) {
												// TODO: handle exception
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
		 
		 
		 
	}
}
