package com.example.dropboximplementation;

import java.io.File;
import java.net.URISyntaxException;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button upload, download;
	final int ACTIVITY_CHOOSE_FILE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		download = (Button) findViewById(R.id.download_button);
		download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						DropboxDownload.class);
				startActivity(i);
			}
		});

		upload = (Button) findViewById(R.id.upload_button);
		upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			String filePath="";
				
				Intent i=new Intent(getApplicationContext(),DropboxFileUploadMain.class );
				i.putExtra("filepath","inbox.txt");
				startActivity(i);
				
			}
		});

	}


}

//imonBayazid
