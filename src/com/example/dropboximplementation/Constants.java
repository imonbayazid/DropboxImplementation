package com.example.dropboximplementation;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.os.Environment;

import com.dropbox.client2.session.Session.AccessType;

public class Constants {

	public static final String OVERRIDEMSG = "File name with this name already exists.Do you want to replace this file?";
	final static public String DROPBOX_APP_KEY = "h11wnw1ssntbs5g";
	final static public String DROPBOX_APP_SECRET = "x91wh636phj30nh";
	public static boolean mLoggedIn = false;

	final static public AccessType ACCESS_TYPE = AccessType.DROPBOX;

	final static public String ACCOUNT_PREFS_NAME = "dropbox_prefs";
	final static public String ACCESS_KEY_NAME = "ACCESS_KEY";
	final static public String ACCESS_SECRET_NAME = "ACCESS_SECRET";


}
