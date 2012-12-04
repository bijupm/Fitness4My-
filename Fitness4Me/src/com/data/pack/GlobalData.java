package com.data.pack;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.View;
import com.data.fitness4me.R;

public class GlobalData {
	 public static String selectedWorkOutID = "0";
	// public static String selectedUserID = "0";
	 public static String mailComment = "";
	 
	 public static String SelectedLanguage = "";
	 public static String selectedUserName = "";
	 public static String selectedFirstName = "";
	 public static String useremail = "";
	 public static String level = "1";
	 public static int lockedcount = 0;
	 public static int appcount = 0;
	 public static boolean allPurchased =false;
	 public static boolean dntAskflag =false;
	 public static String notification ="0";
	 	 public static String dataAcceptFlag ="0";
	 	 public static int datarefresh = 0;
	 public static ArrayList<String> arrImagedownload = new ArrayList<String>();
	 	 public static ArrayList<String> arrImagedownloadList = new ArrayList<String>();
	 public static int viewvideochange = 0;
	 public static double netSpeed = 0;
	 public static  int fullcountdownload;
	 public static  ProgressDialog progressdownload;
	 public static int termsShow = 0;
	 	 public static int arrvideoUrlCount = 0;
	 	 public static int arrvideoUrlCountincrement = 0;
	 public static boolean allPurchasedfirstTime =true;
	 public static boolean upgradepopupshow =false;
	 	 public static String AllPrice = "4.99";
	 	 public static boolean fulldownloadcomplete =false;
	 	 public static boolean freedownloadcomplete =false;
}
