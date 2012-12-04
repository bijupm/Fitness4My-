package com.data.pack;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
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
import com.data.fitness4me.R;

public class submitpage extends Activity {
	private static PlaceDataSQL placeData;
	private String name;
	private String username;
	private String password;
	private String email;
	private String level  ;
	private WebView webView; 
	private VOUserDetails obUser =  new VOUserDetails(this); 
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:

			mProgressDialog = new ProgressDialog(this);
			//    mProgressDialog.show(videostart.this, "", "Loading...");
			String st = "";
			if(obUser.getSelectedLanguage().equals("1"))
			{
			mProgressDialog.setMessage("Please wait..");
			}
			else
			{
			 mProgressDialog.setMessage("Bitte warten ..");
			}
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
		setContentView(R.layout.submitpage);
		placeData = new PlaceDataSQL(this);
		webView = (WebView) findViewById(R.id.mywebview);
		Button imgButton = (Button)findViewById(R.id.btnfinish);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		if(obUser.getSelectedLanguage().equals("1"))
		{
		webView.loadUrl("file:///android_asset/html/termsofUsenew.html");  
		if(obUser.getUserId().equals("0") || obUser.getUserId().equals("") )
		{
			imgButton.setText("I ACCEPT");
		}
		else
		{
			imgButton.setText("Back");
		}
		}
		else
		{
			if(obUser.getUserId().equals("0") || obUser.getUserId().equals("") )
			{
			
			imgButton.setText("Ich akzeptiere");
			}
			else
			{
				imgButton.setText("zurück");
			}
			webView.loadUrl("file:///android_asset/html/termsOfUseDe.html"); 
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
		//		TextView txtfinish= (TextView)findViewById(R.id.txtfinish);
		//		txtfinish.setText(Html.fromHtml(getString(R.string.finishinfo)));
	

		 Typeface tf = Typeface.createFromAsset(getAssets(),
        "fonts/ARIAL.TTF");
		 
		 imgButton.setTypeface(tf,0) ;
		imgButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GlobalData.termsShow = 1;
				onBackPressed();

			}
		});

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5
				&& keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			Log.d("CDA", "onKeyDown Called");
			onBackPressed();
		}

		return super.onKeyDown(keyCode, event);
	}

	public void onBackPressed() {
		GlobalData.termsShow = 1;
		this.finish();
		return;
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
