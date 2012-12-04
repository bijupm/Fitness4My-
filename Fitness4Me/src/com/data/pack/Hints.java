package com.data.pack;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import com.data.fitness4me.R;

public class Hints extends Activity {
	private WebView webView; 
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
	private VOUserDetails obUser =  new VOUserDetails(this); 
	private String Back="Back";
	private Button imghintsButton;
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
		setContentView(R.layout.hints);

		webView = (WebView) findViewById(R.id.mywebview);
		if(imghintsButton == null)
			imghintsButton = (Button) findViewById(R.id.imghintsButton);
		webView.setWebViewClient(new WebViewClient());
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		//		  TextView tipsandadvice = (TextView)findViewById(R.id.txthinttext);
		//		  tipsandadvice.setText(Html.fromHtml(getString(R.string.tipsandadvice)));
		if(obUser.getSelectedLanguage().equals("1"))
		{
				webView.loadUrl("file:///android_asset/html/Hintspagenew.html");  
				Back = "Back";
		}
		else
		{
			webView.loadUrl("file:///android_asset/html/hintsandTips_de.html");  
			Back = "zurück";
		}
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
		 Typeface tf = Typeface.createFromAsset(getAssets(),
         "fonts/ARIAL.TTF");
		 imghintsButton.setTypeface(tf,0) ;
		imghintsButton.setText(Back);
		imghintsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				
				  try {
					Intent intent = new Intent(Hints.this,HomeScreen.class);
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
	
	
	public void onDestroy(){
	    super.onDestroy();
	    webView.destroy();
	    webView = null;
	    finish();
	    System.gc();
	}
}
