package com.data.pack;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.data.fitness4me.R;

public class upgradepopup  extends Activity {
	private TextView txtupgrade;
	private TextView 	popupallupgrade;
	private Button btnYes;
	private Button btnNo;
	private ImageButton popupCongratsClose;
	private String strYes="Yes";
	private VOUserDetails obUser =  new VOUserDetails(this); 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upgradepopup);
		txtupgrade = (TextView)findViewById(R.id.txtupgrade);
		popupallupgrade = (TextView)findViewById(R.id.popupallupgrade);
		
		btnYes = (Button)findViewById(R.id.btnyes);
	 GlobalData.upgradepopupshow = true;
		popupCongratsClose = (ImageButton)findViewById(R.id.popupClose);
		 Typeface tf = Typeface.createFromAsset(getAssets(),
         "fonts/ARIAL.TTF");
		
		 
		 popupallupgrade.setTypeface(tf,0) ;

		 if(obUser.getSelectedLanguage().equals("1"))
		 		{
			 txtupgrade.setText("Supersaver offer");
		  popupallupgrade.setText("Get 30 new and challenging \nworkouts for just 4.99 $");
		  strYes="Yes";
		 		}
		 else
		 {
			 txtupgrade.setTextSize(29);
			 txtupgrade.setText(" Super Spar-Angebot");
			 strYes= "Ja";
			 
			  popupallupgrade.setText("Erhalte das komplette \nPaket mit 30 Workouts für \nnur 4,99  EUR"); 
		 }
		 popupCongratsClose.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 
							try
							{
								Intent intent = new Intent(upgradepopup.this, endscreen.class);
								intent.putExtra("userID", obUser.getUserId());
								intent.putExtra("workoutID", GlobalData.selectedWorkOutID);
								intent.putExtra("UserName",obUser.getSelectedUserName());
								startActivity(intent);
								 
	
							}
							catch (Exception e) {
								
								// TODO: handle exception
							}
						 
					 
				}});
		 btnYes.setText(strYes);
		 btnYes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						Intent intent = new Intent(upgradepopup.this, PaymanetPage.class);


						intent.putExtra("workoutID",  GlobalData.selectedWorkOutID);
						intent.putExtra("UserName", obUser.getSelectedUserName());
						intent.putExtra("userID", obUser.getUserId());
						intent.putExtra("workoutStatus", "all");
						intent.putExtra("amout",GlobalData.AllPrice );


						startActivity(intent);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					 
				 
				}

			});
		 
		 
		 
		 
	}
	
	
	
	
	
	
}
