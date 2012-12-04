package com.data.pack;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import com.data.fitness4me.R;

public class VOUserDetails {
	 
	private  Prefs prefs;
 


	Context myContext;

	public VOUserDetails(Context ctx) {
		myContext = ctx;
		prefs = new Prefs(myContext);
	}

	boolean getAllPurchased() { return  Boolean.parseBoolean(prefs.getPreference("allPurchased")) ; }
	String getUserId() { return prefs.getPreference("selectedUserID"); }
	String getSelectedUserName() { return prefs.getPreference("selectedUserName"); }
	String getSelectedFirstName() { return prefs.getPreference("selectedFirstName"); }
	String getUseremail() { return prefs.getPreference("useremail"); }
	String getLevel() { return prefs.getPreference("level"); }
	String getSelectedLanguage() { return prefs.getPreference("SelectedLanguage"); }
	void setUserId( String _UserId ) { 
		prefs.setPreference("selectedUserID", ""+_UserId);
	}
	void setSelectedUserName( String _SelectedUserName ) { 
		prefs.setPreference("selectedUserName", ""+_SelectedUserName);
	}
	void setSelectedFirstName( String _selectedFirstName ) { 
		prefs.setPreference("selectedFirstName", ""+_selectedFirstName);
	}
	void setSelectedLanguage( String _SelectedLanguage ) { 
		prefs.setPreference("SelectedLanguage", ""+_SelectedLanguage);
	}
	void setUseremail( String _useremail ) { 
		prefs.setPreference("useremail", ""+_useremail);
	}
	void setLevel( String _level ) { 
		prefs.setPreference("level", ""+_level);
	}
	void setAllPurchased( boolean _allPurchased ) {
		prefs.setPreference("allPurchased", ""+_allPurchased);

	}

}
