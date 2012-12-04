package com.data.pack;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookiePolicy;

import oauth.signpost.http.HttpParameters;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import com.data.fitness4me.R;

public class jsontest {
  
 

	 private static final String TAG = jsontest.class.getSimpleName();
	 
	 private static DefaultHttpClient client = getClient();
	 
	 public String getContent(String url) throws Exception {
	  HttpGet request = new HttpGet(url);
	  
	  String content = null;

	  HttpResponse response = client.execute(request);
	  content = streamToString(response.getEntity().getContent());
	  
	  return content;
	 }

	 private String streamToString(InputStream input) throws Exception {
	  String result;

	  BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	  StringBuilder str = new StringBuilder();
	  String line = null;

	  while((line = reader.readLine()) != null){
	   str.append(line + "\n");
	  }

	  input.close();
	  result = str.toString();

	  return result;
	 }

	 private static synchronized DefaultHttpClient getClient(){
	  
	  DefaultHttpClient defaultClient = new DefaultHttpClient();

	  ClientConnectionManager mgr = defaultClient.getConnectionManager();

	  HttpParams params = defaultClient.getParams();
	  client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,mgr.getSchemeRegistry()), params);

	  return client;
	 }
	}
