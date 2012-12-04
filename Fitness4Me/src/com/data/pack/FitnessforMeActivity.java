package com.data.pack;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.anim;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.data.fitness4me.R;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

/**
 * @author biju
 * 
 */
public class FitnessforMeActivity extends ListActivity {
	private PlaceDataSQL placeData;
	private Cursor cursorList;
	private String workOutID = "0";
	public ListView lv;
	private String strYes = "Yes";
	private AdView adView;
	private Prefs prefs;
	private VOUserDetails obUser;
	private Typeface tf;
	private ProgressDialog m_ProgressDialog = null;
	private ArrayList<VOItem> mItems = null;
	private ItemsAdapter m_adapter;
	private Runnable viewItems;

	private String netStatus = "No internet connection!";

	private String stProgressMessage = "Please wait..";
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			if (GlobalData.allPurchased == true) {

				adView.setVisibility(View.GONE);
			} else {

				(new Thread() {
					public void run() {
						adView.setVisibility(View.VISIBLE);
						 
					//	adView.loadAd(new AdRequest());
					}
				}).start();

			}
		} catch (Exception e) {
			String st = e.toString();
			st = st + "";
		}
//		 if(!dialog.isShowing())
//				dialog.show();
		
		
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			setContentView(R.layout.main);
			if (adView == null)
				adView = (AdView) this.findViewById(R.id.adView);
			if(obUser == null)
			obUser = new VOUserDetails(this);
			if (obUser.getSelectedLanguage().equals("1")) {
				netStatus = "No internet connection!";
				stProgressMessage = "Please wait..";
				strYes = "Yes";
			} else {
				netStatus = "Es besteht keine Internetverbindung - bitte versuche es später.";
				stProgressMessage = "Bitte warten ..";
				strYes = "Ja";
			}
			 
			if (prefs == null)
				prefs = new Prefs(this);
			if (tf == null)
				tf = Typeface.createFromAsset(getAssets(), "fonts/ARIAL.TTF");
			if (placeData == null)
				placeData = new PlaceDataSQL(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.flags |= LayoutParams.FLAG_KEEP_SCREEN_ON;
			// params.screenBrightness = 10;
			getWindow().setAttributes(params);
			getDataAndPopulate();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// cursors.close();
		}

		if (GlobalData.allPurchased) {
			if (Boolean.parseBoolean(prefs
					.getPreference("allPurchasedfirstTime"))) {
				if (CheckNetworkAvailability
						.isNetworkAvailable(FitnessforMeActivity.this)) {
					try {
						Intent intent = new Intent(FitnessforMeActivity.this,
								congrats.class);
						startActivity(intent);
						prefs.setPreference("allPurchasedfirstTime", "" + false);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void checkAndCreateDirectory(String dirName) {
		// File new_dir = new File( getCacheDir()+ dirName );
		File new_dir = new File(Environment.getExternalStorageDirectory()
				+ dirName);
		if (!new_dir.exists()) {
			new_dir.mkdirs();
		}
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			onBackPressed();
		}

		return super.onKeyDown(keyCode, event);
	}

	public void onBackPressed() {
		try {
			Intent intent = new Intent(FitnessforMeActivity.this,
					HomeScreen.class);
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return;
	}

 
	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {

			if (mItems != null && mItems.size() > 0) {
				m_adapter.notifyDataSetChanged();
				for (int i = 0; i < mItems.size(); i++)
					m_adapter.add(mItems.get(i));
			}
			m_ProgressDialog.dismiss();
			m_adapter.notifyDataSetChanged();
		}

	};
	 
	/**
	 * load bitmap data
	 * 
	 * @return
	 */
	public static Bitmap loadImageFromUrl(String url) {

		Bitmap bm;
		try {

			URL aURL = new URL(url);
			URLConnection conn = aURL.openConnection();

			conn.connect();
			InputStream is = null;
			try {
				is = conn.getInputStream();
			} catch (IOException e) {
				return null;
			}

			BufferedInputStream bis = new BufferedInputStream(is);

			bm = BitmapFactory.decodeStream(bis);

			bis.close();
			is.close();

		} catch (IOException e) {
			return null;
		}

		return Bitmap.createScaledBitmap(bm, 100, 100, true);

	}

	/**
	 * Get items from database
	 * 
	 * @return
	 */
	private void getItems() {
		mItems = new ArrayList<VOItem>();
		try {
			if (cursorList == null)
				cursorList = getRawEvents("select * from FitnessWorkouts order by  IsLocked ASC");

			while (cursorList.moveToNext()) {
				VOItem item = new VOItem();
				if (item != null) {
					item.setId(cursorList.getString(cursorList
							.getColumnIndex("Id")));
					item.setDesc(cursorList.getString(cursorList
							.getColumnIndex("Description")));
					item.setLabel(cursorList.getString(cursorList
							.getColumnIndex("Name")));
					item.setImgString(cursorList.getString(cursorList
							.getColumnIndex("Image")));
					item.setImgLocked(cursorList.getString(cursorList
							.getColumnIndex("IsLocked")));
					if (item.getLabel().length() > 1)
						mItems.add(item);

				}

			}
			Thread.sleep(2000);
		} catch (Exception e) {
			if (cursorList != null) {
				cursorList.close();
				cursorList = null;
			}

		} finally {
			if (cursorList != null)
				cursorList.close();
		}
		runOnUiThread(returnRes);
	}

	/**
	 * Get and populate data
	 * 
	 * @return
	 */
	private void getDataAndPopulate() {
		lv = getListView();

		mItems = new ArrayList<VOItem>();
		this.m_adapter = new ItemsAdapter(this, R.layout.item, mItems);
		setListAdapter(this.m_adapter);
		viewItems = new Runnable() {
			@Override
			public void run() {
				getItems();
			}
		};
		Thread thread = new Thread(null, viewItems, "ThreadItemList");
		thread.start();

		m_ProgressDialog = ProgressDialog.show(FitnessforMeActivity.this, "",
				stProgressMessage, true);

 		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View v, int position,
					long id) {

				VOItem items = mItems.get(position);
				;
				workOutID = items.getId();
				String str = items.getImgLocked();

				if (Boolean.parseBoolean(str) == false) {

					try {

						Intent intent = new Intent(FitnessforMeActivity.this,
								videostart.class);
						intent.putExtra("workoutID", workOutID);
						intent.putExtra("userID", obUser.getUserId());
						intent.putExtra("UserName",
								obUser.getSelectedUserName());
						startActivity(intent);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					if (CheckNetworkAvailability
							.isNetworkAvailable(FitnessforMeActivity.this)) {

						try {
							View popupView;
							Display display;
							LayoutInflater layoutInflaterPopup = (LayoutInflater) getBaseContext()
									.getSystemService(LAYOUT_INFLATER_SERVICE);

							popupView = layoutInflaterPopup.inflate(
									R.layout.popup, null);
							display = getWindowManager().getDefaultDisplay();

							int width = display.getWidth(); // deprecated
							int height = display.getHeight(); // deprecated
							final PopupWindow popupWindow = new PopupWindow(
									popupView, width - 30, height - 40, true);

							String url = getResources().getString(
									R.string.servername);
							JSONObject json = JSONfunctions.getJSONfromURL(url
									+ "workoutrate=yes&version=1");
							try {

								if (json != null) {
									JSONArray workoutList = json
											.getJSONArray("items");

									JSONObject e = workoutList.getJSONObject(0);
									setData(e.getString("single"),
											e.getString("all"));

								}

								// setListAdapter(adapter);
							} catch (JSONException e) {
								Log.e("log_tag",
										"Error parsing data " + e.toString());
							}

							TextView popupAll = (TextView) popupView
									.findViewById(R.id.popupall);
							Button btnAll = (Button) popupView
									.findViewById(R.id.btnAll);
							TextView txtHead = (TextView) popupView
							.findViewById(R.id.txtHead);
							String text = "  Get 30 new and challenging workouts for just 4.99 $";
							if (obUser.getSelectedLanguage().equals("1")) {
								text = " Get all 30 workouts for just "
										+ AllPrice + " $";
								txtHead.setText("Supersaver offer");
							} else {
								text = "Erhalte das komplette \nPaket mit 30 Workouts für \nnur " + AllPrice + "  EUR";
								txtHead.setText("Super Spar-Angebot");

							}
							popupAll.setText(text);

							btnAll.setText(strYes);

							btnAll.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									if (CheckNetworkAvailability
											.isNetworkAvailable(FitnessforMeActivity.this)) {
										try {
											Intent intent = new Intent(
													FitnessforMeActivity.this,
													PaymanetPage.class);

											intent.putExtra("workoutID",
													workOutID);
											intent.putExtra("UserName", obUser
													.getSelectedUserName());
											intent.putExtra("userID",
													obUser.getUserId());
											intent.putExtra("workoutStatus",
													"all");
											intent.putExtra("amout", AllPrice);

											startActivity(intent);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									} else {

										int duration = Toast.LENGTH_SHORT;
										Context context = getApplicationContext();
										Toast toast = Toast.makeText(context,
												netStatus, duration);
										toast.show();

									}
								}
							});

							ImageButton btnPopupclose = (ImageButton) popupView
									.findViewById(R.id.popupClose);
							btnPopupclose
									.setOnClickListener(new Button.OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											popupWindow.dismiss();

										}
									});

							if (!popupWindow.isShowing()) {

								popupWindow.showAtLocation(lv, Gravity.CENTER,
										0, 0);
							}

						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						// alert.show();
					} else {

						int duration = Toast.LENGTH_SHORT;
						Context context = getApplicationContext();
						Toast toast = Toast.makeText(context, netStatus,
								duration);
						toast.show();
					}
				}
			}
		});

		if (GlobalData.appcount == 5 && GlobalData.dntAskflag == false) {
			try {
				Intent intent = new Intent(FitnessforMeActivity.this,
						evalution.class);
				startActivity(intent);
				GlobalData.appcount = 0;
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		}

	}

	private String AllPrice;

	/**
	 * set price
	 * 
	 * @return
	 */
	private void setData(String single, String all) {
		AllPrice = all;
		GlobalData.AllPrice = all;
	}

	/**
	 * class item adapter
	 * 
	 * @return
	 */
	private class ItemsAdapter extends ArrayAdapter<VOItem> {

		public ItemsAdapter(Context context, int textViewResourceId,
				ArrayList<VOItem> items) {
			super(context, textViewResourceId, items);
		}

		public String getFileName(String wholePath) {
			String name = null;
			int start, end;
			start = wholePath.lastIndexOf('/');
			end = wholePath.length(); // lastIndexOf('.');
			name = wholePath.substring((start + 1), end);
			// name = "fitness4meimages/"+name;
			name = getCacheDir() + "/" + name;
			return name;
		}
		private Drawable grabImageFromUrl(String url) throws Exception {
			 
			return Drawable.createFromStream(
					(InputStream) new URL(url).getContent(), "src");

		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			 ProgressBar title;
			ImageView img = null;
			if (view == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.item, null);
				
		          
			}
			VOItem o = getItem(position);
			if (o != null) {
				img = (ImageView) view.findViewById(R.id.image);
				title = (ProgressBar) view.findViewById(R.id.progress);
				TextView desc = (TextView) view.findViewById(R.id.description);
				TextView label = (TextView) view.findViewById(R.id.label);
				title.setVisibility(View.GONE);
				if (label != null)
					label.setTypeface(tf);
				if (desc != null)
					desc.setTypeface(tf);
				ImageView imgLocked = (ImageView) view
						.findViewById(R.id.lockimage);
				ImageView imgUnLocked = (ImageView) view
						.findViewById(R.id.unlockimage);
				if (label != null)
					label.setText(o.getLabel());
				if (desc != null)
					desc.setText(o.getDesc());
				String fileMainUrl = getFileName(o.getImgString());
				File filecheck = new File(fileMainUrl);
				if(img !=null)
				{
				if (!filecheck.exists()) {
					// imgshare.setImageURI(Uri.parse(getFileName(temp_img)));
					 
	            		try {
	        				 
	            			img.setImageDrawable(grabImageFromUrl(o.getImgString()));
	        			 
	        			} catch (Exception e) {

	        				String st = "" + e;
	        				st = st + "";
	        			}
//					try {
//						img.setTag(o.getImgString());
//						new LoadImage(img,title).execute();
//						
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
					System.gc();
				}
				else
				{
					try {
						img.setImageURI(Uri.parse(fileMainUrl));
						img.setBackgroundDrawable(null);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				}
				if (imgLocked != null) {
					if (!Boolean.parseBoolean(o.getImgLocked())) {
						imgUnLocked.setVisibility(View.VISIBLE);
					} else {
						imgUnLocked.setVisibility(View.GONE);
					}

					if (!Boolean.parseBoolean((o.getImgLocked()))) {
						imgLocked.setVisibility(View.GONE);

					} else {
						imgLocked.setVisibility(View.VISIBLE);
						imgUnLocked.setVisibility(View.GONE);

					}
				}

			}

			return view;
		}

	}

	/**
	 * Get raw data
	 * 
	 * @param String
	 * @return Cursor
	 */
	private Cursor getRawEvents(String sql) {
		Cursor cursor = null;
		try {
			SQLiteDatabase db = (placeData).getReadableDatabase();
			cursor = db.rawQuery(sql, null);

			startManagingCursor(cursor);
		} catch (Exception e) {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return cursor;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		
		System.gc();
		if (lv != null) {
			lv = null;
		}
		if (placeData != null) {
			placeData = null;
		}
		if (m_adapter != null) {
			m_adapter.clear();
			m_adapter = null;
		}
		if (mItems != null) {
			mItems = null;
		}
		if (obUser != null) {
			obUser = null;
		}
	}

	
	
	@Override
	protected void onStop() {
		try {
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onStop();
		
	}

	class LoadImage extends AsyncTask<String, Void, Bitmap>{
		public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
        private ImageView imv;
        private String path;
        private ProgressBar title;
    	String fpath;
        public LoadImage(ImageView imv, ProgressBar title) {
             this.imv = imv;
             this.title = title;
             this.path = imv.getTag().toString();
        }
@Override
protected void onPreExecute() {
	// TODO Auto-generated method stub
	super.onPreExecute();
	 
	title.setVisibility(View.VISIBLE);
	 
}

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap = null;
    	int count;
        fpath = getFileName(this.path);
        
        if(imv != null){
            imv.setVisibility(View.VISIBLE);
            try {
            	String fileMainUrl = getFileName(this.path);
				File filecheck = new File(fileMainUrl);
            	if(filecheck.exists())
            		 
            		imv.setImageURI(Uri.parse(fileMainUrl));
            	else
            	{
            		try {
        				 
            			//imv.setImageDrawable(grabImageFromUrl(this.path));
        			 
        			} catch (Exception e) {

        				String st = "" + e;
        				st = st + "";
        			}

            	}
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
        }

        return bitmap;
    }
    
    @Override
    protected void onPostExecute(Bitmap result) {
       
       
          
        title.setVisibility(View.GONE);
   
    }
    /**
	 * getting file name
	 * 
	 * @return
	 */
	public String getFileName(String wholePath) {
		String name = null;
		int start, end;
		start = wholePath.lastIndexOf('/');
		end = wholePath.length(); // lastIndexOf('.');
		name = wholePath.substring((start + 1), end);
		name = getCacheDir() + "/" + name;
		return name;
	}
}
	
	/**
	 * class download image
	 * 
	 * @return
	 */
	class DownloadImageFileAsync extends AsyncTask<String, String, String> {
		int current = 0;
		String[] paths;
		String fpath;
		boolean show = false;
		public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
		ProgressBar prgBar1;

		public DownloadImageFileAsync(String[] paths) {
			super();
			this.paths = paths;

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... aurl) {
			int rows = aurl.length;
			while (current < rows) {
				int count;
				try {
					fpath = getFileName(this.paths[current]);

					// String fileMainUrl =
					// android.os.Environment.getExternalStorageDirectory().getPath()
					// +"/"+ fpath;
					String fileMainUrl = fpath;
					File filecheck = new File(fileMainUrl);
					if (!filecheck.exists()) {
						URL url = new URL(this.paths[current]);
						URLConnection conexion = url.openConnection();
						conexion.connect();
						flagcount = flagcount + 1;
						InputStream input = new BufferedInputStream(
								url.openStream(), 512);
						OutputStream output = new FileOutputStream(fpath);
						byte data[] = new byte[1512];
						long total = 0;
						while ((count = input.read(data)) != -1) {
							total += count;
							output.write(data, 0, count);
						}

						show = true;
						output.flush();
						output.close();
						input.close();
						output = null;
						input = null;
					}
					current++;
				} catch (Exception e) {
					String st = e.toString();
					st = st + "";
				} finally {

				}
				if (flagcount == 0) {
					// if(dialog.isShowing())
					// dialog.dismiss();
				}
			} // while end
			return null;
		}

		@Override
		protected void onProgressUpdate(String... progress) {

		}

		private int flagcount = 0;

		@Override
		protected void onPostExecute(String unused) {
			super.onPostExecute(unused);
			 dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		/**
		 * getting file name
		 * 
		 * @return
		 */
		public String getFileName(String wholePath) {
			String name = null;
			int start, end;
			start = wholePath.lastIndexOf('/');
			end = wholePath.length(); // lastIndexOf('.');
			name = wholePath.substring((start + 1), end);
			name = getCacheDir() + "/" + name;
			return name;
		}

	}

}