package com.jab.storepedia;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
public class edit_comment_text extends Activity {
	ProgressDialog prgDialog;
	RequestParams params = new RequestParams();	
	int SID,LID,UID, PCID, isedit;
	EditText comment_field;
	String store_name,place_name;

	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setTitle("Really Exit?")
	        .setMessage("Are you sure you want to exit?")
	        .setNegativeButton(android.R.string.no, null)
	        .setPositiveButton(android.R.string.yes, new OnClickListener() {

	            public void onClick(DialogInterface arg0, int arg1) {
	            	edit_comment_text.super.onBackPressed();
	            }
	        }).create().show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_comment_text);
		prgDialog = new ProgressDialog(this);
		// Set Cancelable as False
		prgDialog.setCancelable(false);
		
		Intent intent = getIntent();
		SID = intent.getIntExtra("SID" , -1);
		LID = intent.getIntExtra("LID" , -1);
		UID = intent.getIntExtra("UID" , -1);
		PCID = intent.getIntExtra("PCID", -1);
		isedit = UID = intent.getIntExtra("UID" , -1);
		//final String store_name = intent.getStringExtra("store_name");
		//final String place_name = intent.getStringExtra("place_name");
		comment_field = (EditText) findViewById(R.id.comment);
		TextView textView1 = (TextView) findViewById(R.id.textView1);
        //textView1.setText("SID = " + SID + " UID = " + UID);
		ImageButton back = (ImageButton) findViewById(R.id.topbar).findViewById(R.id.back);
		//debug_text.setText("SID = "+SID + ";  UID = " +UID + "; LID = " + LID +";");		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(edit_comment_text.this,lcomment_detail.class);
				i.putExtra("UID", UID);
				i.putExtra("LID", LID);
				i.putExtra("SID", SID);
				i.putExtra("PCID", PCID);
				//i.putExtra("place_name", place_name);
				//i.putExtra("store_name", store_name);
	            startActivity(i);
	            finish();
			}
	    });
		
		 String url = "http://122.155.187.27:9876/edit_comment.php";
		 List<NameValuePair> params = new ArrayList<NameValuePair>();
	     params.add(new BasicNameValuePair("SID", Integer.toString(SID)));
	     params.add(new BasicNameValuePair("UID", Integer.toString(UID)));
	     try{
	        	JSONArray data = new JSONArray(getHttpPost(url,params));
	            JSONObject c = data.getJSONObject(0);
	            comment_field.setText(c.getString("comment"));
	     }catch(JSONException e){
	        	e.printStackTrace();
	     }
	 }

	public void clear_text(View v)
	{
		comment_field.setText("");
	}
	
	public void upload_text(View v)
	{
		comment_field = (EditText) findViewById(R.id.comment);
		
		String url = "http://122.155.187.27:9876/edit_comment_text.php";
		
		List<NameValuePair> params2 = new ArrayList<NameValuePair>();
		params2.add(new BasicNameValuePair("comment", comment_field.getText().toString()));
        
        params2.add(new BasicNameValuePair("PCID", Integer.toString(PCID)));
	     try{
	        	JSONArray data = new JSONArray(getHttpPost(url,params2));
	            JSONObject c = data.getJSONObject(0);
	            String result = c.getString("result");
	            Toast.makeText(
						getApplicationContext(),
						result,
						Toast.LENGTH_LONG).show();
	     }catch(JSONException e){
	        	e.printStackTrace();
	     }
	     Intent i = new Intent(edit_comment_text.this,lcomment_detail.class);;
         //i.putExtra("place_name", place_name);
         //i.putExtra("LID", LID);
			i.putExtra("UID", UID);
			i.putExtra("LID", LID);
			i.putExtra("SID", SID);
			i.putExtra("PCID", PCID);
			//i.putExtra("place_name", place_name);
			//i.putExtra("store_name", store_name);
			startActivity(i);
			finish();
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
