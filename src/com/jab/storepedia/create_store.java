package com.jab.storepedia;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint("NewApi")
public class create_store extends Activity {
	ProgressDialog prgDialog;
	RequestParams params = new RequestParams();	
	int LID,UID, SID;
	EditText store_name, store_detail;
	String place_name, ex_store_name = "", ex_store_detail="";

	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setTitle("Really Exit?")
	        .setMessage("Are you sure you want to exit?")
	        .setNegativeButton(android.R.string.no, null)
	        .setPositiveButton(android.R.string.yes, new OnClickListener() {

	            public void onClick(DialogInterface arg0, int arg1) {
	            	create_store.super.onBackPressed();
	            }
	        }).create().show();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_store);
		prgDialog = new ProgressDialog(this);
		// Set Cancelable as False
		prgDialog.setCancelable(false);
		
		SID = -1;
		Intent intent = getIntent();
		LID = intent.getIntExtra("LID" , -1);
		SID = intent.getIntExtra("SID" , -1);
		UID = intent.getIntExtra("UID" , -1);
		//place_name = intent.getStringExtra("place_name");
		ex_store_name = intent.getStringExtra("store_name");
		ex_store_detail = intent.getStringExtra("store_detail");
		
		//TextView create_your_store = (TextView) findViewById(R.id.textView1);
		//create_your_store.setText(place_name);
		
		store_name = (EditText) findViewById(R.id.store_name);
		store_detail = (EditText) findViewById(R.id.store_detail);
		store_name.setText(ex_store_name);
		store_detail.setText(ex_store_detail);
		ImageView next_button = (ImageView) findViewById(R.id.upload);
		ImageView edited_button = (ImageView) findViewById(R.id.edited);
		
		edited_button.setVisibility(View.GONE);
		if(SID!=-1)
		{
			next_button.setVisibility(View.INVISIBLE);
			edited_button.setVisibility(View.VISIBLE);
		}
		
		ImageButton back = (ImageButton) findViewById(R.id.topbar).findViewById(R.id.back);		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SID = -1;
				Intent intent = getIntent();
				LID = intent.getIntExtra("LID" , -1);
				SID = intent.getIntExtra("SID" , -1);
				UID = intent.getIntExtra("UID" , -1);
				//store_name = intent.getStringExtra("store_name");
				//place_name = intent.getStringExtra("place_name");
				if(SID!=-1)
				{
					Intent i = new Intent(create_store.this,store_detail.class);
					i.putExtra("SID", SID);
					i.putExtra("UID", UID);
				    i.putExtra("LID", LID);
				    //i.putExtra("place_name", place_name);				  
				    startActivity(i);
	                finish();
				}
				else
				{
				Intent i = new Intent(create_store.this,select_store.class);				
				i.putExtra("UID", UID);
				i.putExtra("LID", LID);
				//i.putExtra("place_name", place_name);
	            startActivity(i); 
	            finish();
			}
			}
	    });
	}		

	public void clear_text(View v)
	{
		store_detail.setText("");
		//store_name.setText("");
	}
	
	public void next(View v)
	{		
	        Intent i = new Intent(create_store.this,create_store2.class);
			i.putExtra("UID", UID);
			i.putExtra("LID", LID);
			//i.putExtra("place_name", place_name);
			i.putExtra("store_name", store_name.getText().toString());
			i.putExtra("store_detail", store_detail.getText().toString());
			startActivity(i);
			finish();
	}
	public void edited(View v)
	{		
		Intent i = new Intent(create_store.this,store_detail.class);
		String url = "http://122.155.187.27:9876/edit_store_detail.php";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("SID", Integer.toString(SID)));
        params.add(new BasicNameValuePair("store_name", store_name.getText().toString()));
        params.add(new BasicNameValuePair("store_detail", store_detail.getText().toString()));
        try{
        	getHttpPost(url,params);	         	
        	i.putExtra("UID", UID);
			i.putExtra("LID", LID);
			i.putExtra("SID", SID);
			//i.putExtra("place_name", place_name);
			//i.putExtra("store_name", store_name.getText().toString());
			startActivity(i);
			finish();
        	}catch(Exception e)
        	{
        		//e.printStackTrace();
        		//TextView create_your_store = (TextView) findViewById(R.id.textView1);
        		//create_your_store.setText("SID: "+Integer.toString(SID)+ "  UID: "+Integer.toString(UID) + "   LID: "+Integer.toString(LID)+ "   place_name: "+place_name + " store_name: "+store_name.getText().toString());
        	}	
	        
			
	}
	public String getHttpPost(String url,List<NameValuePair> params) {
		StringBuilder str = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
			HttpResponse response = client.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) { // Status OK
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					str.append(line);
				}
			} else {
				Log.e("Log", "Failed to download result..");
				return "FAIL";
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str.toString();
	}
}
	    

