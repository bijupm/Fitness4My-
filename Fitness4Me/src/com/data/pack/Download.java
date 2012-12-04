package com.data.pack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;
import com.data.fitness4me.R;

public class Download extends AsyncTask<Void, Void, String> {
	ProgressDialog mProgressDialog;
	private volatile boolean running = true;
	Context context;
	String urlString ;

	public Download(Context context,String url) {
		this.context = context;
		this.urlString= url;

	}
	@Override
	protected void onPreExecute() {


	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if(GlobalData.progressdownload.isShowing())
		{
			GlobalData.progressdownload.cancel();
			Toast.makeText(context, "Downloaded videos", 1).show();
		}
	}
	@Override
	protected void onCancelled() {
		running = false;
	}
	@Override
	protected String doInBackground(Void...params) {


		while (running) {
			// //////////////////////
			try {

				URL url = new URL(urlString);
				HttpURLConnection c = (HttpURLConnection) url.openConnection();
				c.setRequestMethod("GET");
				c.setDoOutput(true);
				c.connect();
				String[] path = url.getPath().split("/");
				String mp3 = path[path.length - 1];
				int lengthOfFile = c.getContentLength();

				String PATH =  android.os.Environment.getExternalStorageDirectory().getPath() +"/"+ "fitness4vid";

				File file = new File(PATH);


				String fileName = mp3;

				File outputFile = new File(file , fileName);
				FileOutputStream fos = new FileOutputStream(outputFile);

				InputStream is = c.getInputStream();

				byte[] buffer = new byte[1024];
				int len1 = 0;
				while ((len1 = is.read(buffer)) != -1) {

					fos.write(buffer, 0, len1);
				}
				fos.close();
				is.close();

			} catch (IOException e) {
				String st = e.toString();
				st = st+"";
			}
			return "done";
		}
		return "";
	}
}