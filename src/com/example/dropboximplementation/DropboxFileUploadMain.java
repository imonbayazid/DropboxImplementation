package com.example.dropboximplementation;

import java.io.File;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.UploadRequest;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.TokenPair;


public class DropboxFileUploadMain extends Activity  {
	private static final int TAKE_PHOTO = 1;
	private final String DIR = "/";
	private File f;
	private boolean mLoggedIn, onResume;
	private DropboxAPI<AndroidAuthSession> mApi;
	 String upload_filepath;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    upload_filepath = extras.getString("filepath");
		}
		
		AndroidAuthSession session = buildSession();
		mApi = new DropboxAPI<AndroidAuthSession>(session);
		
		setLoggedIn(false);
		
		createDir();
		if (mLoggedIn) {
			logOut();
		}
		if (Utils.isOnline(DropboxFileUploadMain.this)) {
			mApi.getSession().startAuthentication(DropboxFileUploadMain.this);
			onResume = true;
		} else {
			Utils.showNetworkAlert(DropboxFileUploadMain.this);
		}
		
	}

	private AndroidAuthSession buildSession() {
		AppKeyPair appKeyPair = new AppKeyPair(Constants.DROPBOX_APP_KEY,
				Constants.DROPBOX_APP_SECRET);
		AndroidAuthSession session;

		String[] stored = getKeys();
		if (stored != null) {
			AccessTokenPair accessToken = new AccessTokenPair(stored[0],
					stored[1]);
			session = new AndroidAuthSession(appKeyPair, Constants.ACCESS_TYPE,
					accessToken);
		} else {
			session = new AndroidAuthSession(appKeyPair, Constants.ACCESS_TYPE);
		}

		return session;
	}

	private String[] getKeys() {
		SharedPreferences prefs = getSharedPreferences(
				Constants.ACCOUNT_PREFS_NAME, 0);
		String key = prefs.getString(Constants.ACCESS_KEY_NAME, null);
		String secret = prefs.getString(Constants.ACCESS_SECRET_NAME, null);
		if (key != null && secret != null) {
			String[] ret = new String[2];
			ret[0] = key;
			ret[1] = secret;
			return ret;
		} else {
			return null;
		}
	}


	private void logOut() {
		mApi.getSession().unlink();

		clearKeys();
	}

	private void clearKeys() {
		SharedPreferences prefs = getSharedPreferences(
				Constants.ACCOUNT_PREFS_NAME, 0);
		Editor edit = prefs.edit();
		edit.clear();
		edit.commit();
	}

	private void createDir() {
		File dir = new File(Utils.getPath());
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == TAKE_PHOTO) {
//				f = new File(Utils.getPath() + "/temp.jpg");
				if (Utils.isOnline(DropboxFileUploadMain.this)) {
					mApi.getSession().startAuthentication(DropboxFileUploadMain.this);
					onResume = true;
				} else {
					Utils.showNetworkAlert(DropboxFileUploadMain.this);
				}
			}
		}
	}

	public void setLoggedIn(boolean loggedIn) {
		mLoggedIn = loggedIn;
		if (loggedIn) {
			// new
			DropboxFileUpload uploadFile = new DropboxFileUpload(this, mApi,DIR,upload_filepath);
			uploadFile.execute();
			onResume = false;
			finish();

		}
	}

	private void storeKeys(String key, String secret) {
		SharedPreferences prefs = getSharedPreferences(
				Constants.ACCOUNT_PREFS_NAME, 0);
		Editor edit = prefs.edit();
		edit.putString(Constants.ACCESS_KEY_NAME, key);
		edit.putString(Constants.ACCESS_SECRET_NAME, secret);
		edit.commit();
	}

	private void showToast(String msg) {
		Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
		error.show();
	}

	@Override
	protected void onResume() {

		AndroidAuthSession session = mApi.getSession();

		if (session.authenticationSuccessful()) {
			try {
				session.finishAuthentication();

				TokenPair tokens = session.getAccessTokenPair();
				storeKeys(tokens.key, tokens.secret);
				setLoggedIn(onResume);
			} catch (IllegalStateException e) {
				showToast("Couldn't authenticate with Dropbox:"
						+ e.getLocalizedMessage());
			}
		}
		super.onResume();
	}

	
}
