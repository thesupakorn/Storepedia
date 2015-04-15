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
public class create_store2 extends Activity {
	ProgressDialog prgDialog;
	RequestParams params = new RequestParams();	
	int LID,UID;
	EditText store_address, store_contact;
	String place_name, store_name, store_detail, ex_store_address="", ex_store_contact="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_store2);
		prgDialog = new ProgressDialog(this);
		// Set Cancelable as False
		prgDialog.setCancelable(false);
		
		Intent intent = getIntent();
		LID = intent.getIntExtra("LID" , -1);
		UID = intent.getIntExtra("UID" , -1);
		store_name = intent.getStringExtra("store_name");
		store_detail = intent.getStringExtra("store_detail");
		place_name = intent.getStringExtra("place_name");
		ex_store_address = intent.getStringExtra("store_address");
		ex_store_contact = intent.getStringExtra("store_contact");

		//TextView enter_address = (TextView) findViewById(R.id.textView1);
		//enter_address.setText(place_name);
		
		store_address = (EditText) findViewById(R.id.store_address);
		store_contact = (EditText) findViewById(R.id.store_contact);
		store_address.setText(ex_store_address);
		store_contact.setText(ex_store_contact);
		ImageButton back = (ImageButton) findViewById(R.id.topbar).findViewById(R.id.back);		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(create_store2.this,create_store.class);				
				i.putExtra("UID", UID);
				i.putExtra("LID", LID);
				i.putExtra("place_name", place_name);
				i.putExtra("store_name", store_name);
				i.putExtra("store_detail", store_detail);
	            startActivity(i);
	            finish();
			}
	    });
	}		

	public void clear_text(View v)
	{
		store_address.setText("");
		store_contact.setText("");
	}
	
	public void next(View v)
	{		
	     Intent i = new Intent(create_store2.this,create_store3.class);;
			i.putExtra("UID", UID);
			i.putExtra("LID", LID);
			i.putExtra("place_name", place_name);
			i.putExtra("store_name", store_name);
			i.putExtra("store_detail", store_detail);
			i.putExtra("store_address", store_address.getText().toString());
			i.putExtra("store_contact", store_contact.getText().toString());
			startActivity(i);
			finish();
	}
	
	    
}
