package com.example.dropboximplementation;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

public class DropboxFileUpload extends AsyncTask {

	private DropboxAPI dropboxApi;
	private String path,upload_filepath;
	private Context context;

	public DropboxFileUpload(Context context, DropboxAPI dropboxApi,
			String path,String upload_filepath) {
		this.context = context.getApplicationContext();
		this.dropboxApi = dropboxApi;
		this.path = path;
		this.upload_filepath=upload_filepath;
	}

	@Override
	protected Object doInBackground(Object... params) {
		
		try {
		
			File file=new File(Environment.getExternalStorageDirectory(),upload_filepath);

			// Uploading the newly created file to Dropbox.

			FileInputStream fileInputStream = new FileInputStream(file);
			dropboxApi.putFile(path +upload_filepath, fileInputStream,file.length(), null, null);

			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DropboxException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	
	protected void onPostExecute(Boolean result) {
		if (result) {
			Toast.makeText(context, "File has been successfully uploaded!",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(context, "An error occured while processing the upload request.",
					Toast.LENGTH_LONG).show();
		}
		
	       
	}
	
}