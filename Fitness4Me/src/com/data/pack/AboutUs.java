package com.data.pack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.data.fitness4me.R;
import com.data.pack.Hints.myWebClient;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AboutUs extends Activity {
	private WebView webView; 
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private  String mesString="Please wait.."; 
	private  String Back = "zurück";
	private Button btnaboutback;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutus);
		 	webView = (WebView) findViewById(R.id.mywebview);
		webView.setWebViewClient(new WebViewClient());
		 
		WebSettings webSettings = webView.getSettings();
		 
		webSettings.setFixedFontFamily(getAssets()+"/fonts/ARIAL.TTF");
		webSettings.setJavaScriptEnabled(true);
		Button imgAboutpolicy = (Button) findViewById(R.id.imgAboutpolicy);
		Button imgAboutterms = (Button) findViewById(R.id.imgAboutterms);
		if(btnaboutback == null)
			  btnaboutback = (Button) findViewById(R.id.btnaboutback);
		 Typeface tf = Typeface.createFromAsset(getAssets(),
         "fonts/ARIAL.TTF");
		imgAboutpolicy.setTypeface(tf,0) ;
		imgAboutterms.setTypeface(tf,0) ;
		
		
		
		btnaboutback.setTypeface(tf,0) ;
		//		  TextView tipsandadvice = (TextView)findViewById(R.id.txthinttext);
		//		  tipsandadvice.setText(Html.fromHtml(getString(R.string.tipsandadvice)));
		if(obUser.getSelectedLanguage().equals("1"))
		{
		webView.loadUrl("file:///android_asset/html/aboutnew.html");  
		Back = "Back";
		imgAboutpolicy.setText("Privacy policy");
		imgAboutterms.setText("Terms of use");
		}
		else
		{
			webView.loadUrl("file:///android_asset/html/aboutusDe.html");  
			Back = "zurück";
			imgAboutpolicy.setText("Datenschutzerklärung"); 
			imgAboutterms.setText("Nutzungsbedingungen");
		}
		btnaboutback.setText(Back);
		webView.setWebViewClient(new myWebClient());  
		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different scales.
				showDialog(DIALOG_DOWNLOAD_PROGRESS); 

				if(progress >=100)
				{
					dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
				}
			}
		});
		imgAboutpolicy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				
				  try {
					Intent intent = new Intent(AboutUs.this,Privacypage.class);
						startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				

			}
		});
		
		imgAboutterms.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 
	                    
	                    try {
							Intent intent = new Intent(AboutUs.this, submitpage.class);
							startActivity(intent);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}  
	                    
				 
				 
			}
		});
		
		btnaboutback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				
				  try {
					Intent intent = new Intent(AboutUs.this,HomeScreen.class);
						startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				

			}
		});
		
		
	}
	public class myWebClient extends WebViewClient  
	{  
		@Override  
		public void onPageStarted(WebView view, String url, Bitmap favicon) {  
			// TODO Auto-generated method stub  
			super.onPageStarted(view, url, favicon);  
		}  

		@Override  
		public boolean shouldOverrideUrlLoading(WebView view, String url) {  
			// TODO Auto-generated method stub  

			view.loadUrl(url);  
			return true;  

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
			Intent intent = new Intent(AboutUs.this, HomeScreen.class);
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}  
	public void onDestroy(){
	    super.onDestroy();
	    webView.destroy();
	    webView = null;
	    finish();
	    System.gc();
	}
	
}


 