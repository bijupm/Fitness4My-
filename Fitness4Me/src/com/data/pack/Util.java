/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.data.pack;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.data.fitness4me.R;

/**
 * Utility class supporting the Facebook Object.
 * 
 * @author ssoneff@facebook.com
 * 
 */
public final class Util {

	/**
	 * Set this to true to enable log output. Remember to turn this back off
	 * before releasing. Sending sensitive data to log is a security risk.
	 */
	private static boolean ENABLE_LOG = false;

	/**
	 * Generate the multi-part post body providing the parameters and boundary
	 * string
	 * 
	 * @param parameters
	 *            the parameters need to be posted
	 * @param boundary
	 *            the random string as boundary
	 * @return a string of the post body
	 */
	public static String encodePostBody(Bundle parameters, String boundary) {
		if (parameters == null)
			return "";
		StringBuilder sb = new StringBuilder();

		for (String key : parameters.keySet()) {
			if (parameters.getByteArray(key) != null) {
				continue;
			}

			sb.append("Content-Disposition: form-data; name=\"" + key
					+ "\"\r\n\r\n" + parameters.getString(key));
			sb.append("\r\n" + "--" + boundary + "\r\n");
		}

		return sb.toString();
	}

	public static String encodeUrl(Bundle parameters) {
		if (parameters == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String key : parameters.keySet()) {
			if (first)
				first = false;
			else
				sb.append("&");
			sb.append(URLEncoder.encode(key) + "="
					+ URLEncoder.encode(parameters.getString(key)));
		}
		return sb.toString();
	}

	public static Bundle decodeUrl(String s) {
		Bundle params = new Bundle();
		if (s != null) {
			String array[] = s.split("&");
			for (String parameter : array) {
				String v[] = parameter.split("=");
				params.putString(URLDecoder.decode(v[0]),
						URLDecoder.decode(v[1]));
			}
		}
		return params;
	}

	/**
	 * Parse a URL query and fragment parameters into a key-value bundle.
	 * 
	 * @param url
	 *            the URL to parse
	 * @return a dictionary bundle of keys and values
	 */
	public static Bundle parseUrl(String url) {
		// hack to prevent MalformedURLException
		url = url.replace("fbconnect", "http");
		try {
			URL u = new URL(url);
			Bundle b = decodeUrl(u.getQuery());
			b.putAll(decodeUrl(u.getRef()));
			return b;
		} catch (MalformedURLException e) {
			return new Bundle();
		}
	}

	/**
	 * Connect to an HTTP URL and return the response as a string.
	 * 
	 * Note that the HTTP method override is used on non-GET requests. (i.e.
	 * requests are made as "POST" with method specified in the body).
	 * 
	 * @param url
	 *            - the resource to open: must be a welformed URL
	 * @param method
	 *            - the HTTP method to use ("GET", "POST", etc.)
	 * @param params
	 *            - the query parameter for the URL (e.g. access_token=foo)
	 * @return the URL contents as a String
	 * @throws MalformedURLException
	 *             - if the URL format is invalid
	 * @throws IOException
	 *             - if a network problem occurs
	 */
	public static String openUrl(String url, String method, Bundle params)
			throws MalformedURLException, IOException {
		// random string as boundary for multi-part http post
		String strBoundary = "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f";
		String endLine = "\r\n";

		OutputStream os;

		if (method.equals("GET")) {
			url = url + "?" + encodeUrl(params);
		}
		Util.logd("Facebook-Util", method + " URL: " + url);
		HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();
		conn.setRequestProperty("User-Agent", System.getProperties()
				.getProperty("http.agent") + " FacebookAndroidSDK");
		if (!method.equals("GET")) {
			Bundle dataparams = new Bundle();
			for (String key : params.keySet()) {
				if (params.getByteArray(key) != null) {
					dataparams.putByteArray(key, params.getByteArray(key));
				}
			}

			// use method override
			if (!params.containsKey("method")) {
				params.putString("method", method);
			}

			if (params.containsKey("access_token")) {
				String decoded_token = URLDecoder.decode(params
						.getString("access_token"));
				params.putString("access_token", decoded_token);
			}

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + strBoundary);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.connect();
			os = new BufferedOutputStream(conn.getOutputStream());

			os.write(("--" + strBoundary + endLine).getBytes());
			os.write((encodePostBody(params, strBoundary)).getBytes());
			os.write((endLine + "--" + strBoundary + endLine).getBytes());

			if (!dataparams.isEmpty()) {

				for (String key : dataparams.keySet()) {
					os.write(("Content-Disposition: form-data; filename=\""
							+ key + "\"" + endLine).getBytes());
					os.write(("Content-Type: content/unknown" + endLine + endLine)
							.getBytes());
					os.write(dataparams.getByteArray(key));
					os.write((endLine + "--" + strBoundary + endLine)
							.getBytes());

				}
			}
			os.flush();
		}

		String response = "";
		try {
			response = read(conn.getInputStream());
		} catch (FileNotFoundException e) {
			// Error Stream contains JSON that we can parse to a FB error
			response = read(conn.getErrorStream());
		}
		return response;
	}

	private static String read(InputStream in) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
		for (String line = r.readLine(); line != null; line = r.readLine()) {
			sb.append(line);
		}
		in.close();
		return sb.toString();
	}

	public static void clearCookies(Context context) {
		// Edge case: an illegal state exception is thrown if an instance of
		// CookieSyncManager has not be created. CookieSyncManager is normally
		// created by a WebKit view, but this might happen if you start the
		// app, restore saved state, and click logout before running a UI
		// dialog in a WebView -- in which case the app crashes
		@SuppressWarnings("unused")
		CookieSyncManager cookieSyncMngr = CookieSyncManager
				.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}

	/**
	 * Parse a server response into a JSON Object. This is a basic
	 * implementation using org.json.JSONObject representation. More
	 * sophisticated applications may wish to do their own parsing.
	 * 
	 * The parsed JSON is checked for a variety of error fields and a
	 * FacebookException is thrown if an error condition is set, populated with
	 * the error message and error type or code if available.
	 * 
	 * @param response
	 *            - string representation of the response
	 * @return the response as a JSON Object
	 * @throws JSONException
	 *             - if the response is not valid JSON
	 * @throws FacebookError
	 *             - if an error condition is set
	 */
	public static JSONObject parseJson(String response) throws JSONException,
			FacebookError {
		// Edge case: when sending a POST request to /[post_id]/likes
		// the return value is 'true' or 'false'. Unfortunately
		// these values cause the JSONObject constructor to throw
		// an exception.
		if (response.equals("false")) {
			throw new FacebookError("request failed");
		}
		if (response.equals("true")) {
			response = "{value : true}";
		}
		JSONObject json = new JSONObject(response);

		// errors set by the server are not consistent
		// they depend on the method and endpoint
		if (json.has("error")) {
			JSONObject error = json.getJSONObject("error");
			throw new FacebookError(error.getString("message"),
					error.getString("type"), 0);
		}
		if (json.has("error_code") && json.has("error_msg")) {
			throw new FacebookError(json.getString("error_msg"), "",
					Integer.parseInt(json.getString("error_code")));
		}
		if (json.has("error_code")) {
			throw new FacebookError("request failed", "", Integer.parseInt(json
					.getString("error_code")));
		}
		if (json.has("error_msg")) {
			throw new FacebookError(json.getString("error_msg"));
		}
		if (json.has("error_reason")) {
			throw new FacebookError(json.getString("error_reason"));
		}
		return json;
	}

	/**
	 * Display a simple alert dialog with the given text and title.
	 * 
	 * @param context
	 *            Android context in which the dialog should be displayed
	 * @param title
	 *            Alert dialog title
	 * @param text
	 *            Alert dialog message
	 */
	public static void showAlert(Context context, String title, String text) {
		Builder alertBuilder = new Builder(context);
		alertBuilder.setTitle(title);
		alertBuilder.setMessage(text);
		alertBuilder.create().show();
	}

	/**
	 * A proxy for Log.d api that kills log messages in release build. It not
	 * recommended to send sensitive information to log output in shipping apps.
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void logd(String tag, String msg) {
		if (ENABLE_LOG) {
			Log.d(tag, msg);
		}
	}

	public static void unbindDrawables(View view) {
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			((ViewGroup) view).removeAllViews();
		}
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static int getVersionCode(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException ex) {
		}
		return 0;
	}

	public static String readversion() {
		String str = "";
		try {
			// Create a URL for the desired page
			URL url = new URL("http://fitness4.me/versioninfo.txt");

			// Read all the text returned by the server
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));

			String line;
			while ((line = in.readLine()) != null) {
				str = str + line + "\n";
			}
			in.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return str;
	}

	public static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	public static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}

	public static void copyFile(InputStream in, OutputStream out, int flag)
			throws IOException {
		byte[] buffer = new byte[1024];
		int read;

		try {

			Cipher encipher = null;
			try {
				encipher = Cipher.getInstance("AES");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Cipher decipher = null;
			try {
				decipher = Cipher.getInstance("AES");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			KeyGenerator kgen = null;
			try {
				kgen = KeyGenerator.getInstance("AES");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			byte[] keyStart = "fitnesSbridge".getBytes();
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			sr.setSeed(keyStart);
			kgen.init(128, sr); // 192 and 256 bits may not be available
			SecretKey skey = kgen.generateKey();

			// byte key[] =
			// {0x00,0x32,0x22,0x11,0x00,0x00,0x00,0x00,0x00,0x23,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
			skey = kgen.generateKey();
			// Lgo
			try {
				encipher.init(Cipher.ENCRYPT_MODE, skey);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CipherInputStream cis = new CipherInputStream(in, encipher);
			try {
				decipher.init(Cipher.DECRYPT_MODE, skey);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CipherOutputStream cos = new CipherOutputStream(out, decipher);

			try {

				if (flag == 2) {
					cos = new CipherOutputStream(out, encipher);
				} else {
					cos = new CipherOutputStream(out, decipher);
				}
				while ((read = in.read()) != -1) {
					cos.write(read);
					cos.flush();
				}

				cos.flush();
				cos.close();
				in.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				out.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		//
		// byte[] keyStart = "this is a key".getBytes();
		// KeyGenerator kgen = KeyGenerator.getInstance("AES");
		// SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		// sr.setSeed(keyStart);
		// kgen.init(128, sr); // 192 and 256 bits may not be available
		// SecretKey skey = kgen.generateKey();
		// byte[] key = skey.getEncoded();
		//
		//
		// byte[] b = baos.toByteArray();
		// while ((read = in.read(buffer)) != -1) {
		//
		// // decrypt
		// byte[] decryptedData = Util.decrypt(key,buffer);
		// out.write(decryptedData, 0, read);
		// }
		// } catch (NoSuchAlgorithmException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// catch (Exception e) {
		// // TODO: handle exception
		// }
		//
	}

	public static void trimCache(Context context) {
		try {
			File cacheDir = context.getCacheDir();

			File[] files = cacheDir.listFiles();

			if (files != null) {
				for (File file : files) {
					String fname = file.getName();
					if (!fname.equalsIgnoreCase("completed_exercise_de.mp4")
							&& !fname
									.equalsIgnoreCase("completed_exercise.mp4")

							&& !fname.equalsIgnoreCase("next_exercise_de.mp4")
							&& !fname.equalsIgnoreCase("next_exercise.mp4")
							&& !fname
									.equalsIgnoreCase("otherside_exercise_de.mp4")
							&& !fname
									.equalsIgnoreCase("otherside_exercise.mp4")
							&& !fname.equalsIgnoreCase("recovery_15_de.mp4")
							&& !fname.equalsIgnoreCase("recovery_15.mp4")
							&& !fname.equalsIgnoreCase("recovery_30_de.mp4")
							&& !fname.equalsIgnoreCase("recovery_30.mp4")
							&& !fname.equalsIgnoreCase("stop_exercise_de.mp4")
							&& !fname.equalsIgnoreCase("stop_exercise.mp4") && fname.contains(".mp4")

					)
					{
						if(file.exists())
							file.delete();
				}
			}
			}
		} catch (Exception e) {
			// TODO: handle exception
			String st = e.toString();
			st = st+"";
		}
	}

	
	
	public static void clearCacheVideos(Context context ,ArrayList<String> list) {
		try {
			ArrayList<String> finalFilelist  = new ArrayList<String>();

			 String deletedFile="";
			for(int i=0;i<list.size();++i)
			{
				finalFilelist.add(list.get(i));
			}
			String cacheFiles = finalFilelist.toString();
			File cacheDir = context.getCacheDir();

			File[] files = cacheDir.listFiles();

			if (files != null) {
				for (File file : files) {
					String fname = file.getName();
					if (!fname.equalsIgnoreCase("completed_exercise_de.mp4")
							&& !fname
									.equalsIgnoreCase("completed_exercise.mp4")

							&& !fname.equalsIgnoreCase("next_exercise_de.mp4")
							&& !fname.equalsIgnoreCase("next_exercise.mp4")
							&& !fname
									.equalsIgnoreCase("otherside_exercise_de.mp4")
							&& !fname
									.equalsIgnoreCase("otherside_exercise.mp4")
							&& !fname.equalsIgnoreCase("recovery_15_de.mp4")
							&& !fname.equalsIgnoreCase("recovery_15.mp4")
							&& !fname.equalsIgnoreCase("recovery_30_de.mp4")
							&& !fname.equalsIgnoreCase("recovery_30.mp4")
							&& !fname.equalsIgnoreCase("stop_exercise_de.mp4")
							&& !fname.equalsIgnoreCase("stop_exercise.mp4") && fname.contains(".mp4")

					)
					if(file.exists()){
					 
						deletedFile=fname;
					if(!cacheFiles.contains(deletedFile) && deletedFile.contains(".mp4"))
					{
						file.delete();
					}
						 
				}
			}
			}
		} catch (Exception e) {
			// TODO: handle exception
			String st = e.toString();
			st = st+"";
		}
	}
	
	
	
	public static boolean deleteDir(java.io.File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}
	
	
	
	
	
	
	
}
