package com.data.pack;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.data.fitness4me.R;

/** Helper to the database, manages versions and creation */
public class PlaceDataSQL extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "Fitness";
	private static final int DATABASE_VERSION = 1;

	private Context context;

	public PlaceDataSQL(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			// db.execSQL("CREATE TABLE FitnessVideos (Id VARCHAR(20), Title VARCHAR(500),VideoURL VARCHAR(2500),VideoName VARCHAR(250),Description VARCHAR(1200),ImageURL VARCHAR(1112),IsLocked BOOLEAN)");

			db.execSQL("CREATE TABLE FitnessWorkouts (Id VARCHAR(20), Name VARCHAR(500),Rate VARCHAR(25) , Image  VARCHAR(2500) ,ImageThumb  VARCHAR(2500) ,Description VARCHAR(2500),DescriptionBig  VARCHAR(2500),IsLocked BOOLEAN,Props VARCHAR(1000),ImageAndroid  VARCHAR(2500))");
			// db.execSQL("CREATE TABLE FitnessWorkoutVideos (WorkoutId VARCHAR(20), Name VARCHAR(500),PosterVideo VARCHAR(1225) ,  PosterRep VARCHAR(25))");
			db.execSQL("CREATE TABLE FitnessWorkoutVideos (WorkoutID  VARCHAR(20),MainvideoUrl varchar(500),MainvideoName varchar(200),MainvideoRepeatCount  VARCHAR(20),PosterUrl VARCHAR(500),PosterName VARCHAR(200),"
					+ "PosterRepeatCount  VARCHAR(20),RecoveryVideo VARCHAR(500),RecoveryVideoName VARCHAR(500),StopVideo VARCHAR(500),StopName VARCHAR(500),StopRepeatCount VARCHAR(20),"
					+ "OtherSidePosterVideo VAECHAR(500), OtherSidePosterName VARCHAR(500) , OtherSidePosterRepeatCount VARCHAR(20) ,"
					+ "OtherSideVideo VAECHAR(500), OtherSideName VARCHAR(500) , OtherSideRepeatCount VARCHAR(20) ,"
					+ "NextVideo VAECHAR(500), NextVideoName VARCHAR(500) , NextRepeatCount VARCHAR(20) ,"
					+ "CompletedVideo VAECHAR(500), CompletedVideoName VARCHAR(500) , CompletedRepeatCount VARCHAR(20) , MainVideoSize VARCHAR(20), PosterSize VARCHAR(20), OtherSideSize VARCHAR(20)) ");
			// db.execSQL("CREATE TABLE FitnessUser (ID  VARCHAR(20),Username varchar(25),Fname varchar(200),Level varchar(20),UserEmail varchar(220),SelectedLang varchar(10))");
			db.execSQL("CREATE TABLE FitnessWorkoutDuration (ID VARCHAR(20),Duration VARCHAR(10))");
			// db.execSQL("CREATE TABLE FitnessWorkoutLocked (ID VARCHAR(20),workoutID VARCHAR(2))");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	public boolean checkDataBase(String db) {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = "data/data/com.data.pack/databases/" + db;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.

		} catch (Exception e) {

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= newVersion)
			return;

		if (oldVersion == 1) {
			Log.d("New Version", "Datas can be upgraded");
		}

		Log.d("Sample Data", "onUpgrade	: " + newVersion);
	}

}
