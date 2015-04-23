package com.jab.storepedia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
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

import com.facebook.Session;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class store_info extends Activity{
	boolean isImageFitToScreen;
	int UID,SID,LID,PCID;
	String place_name,store_name;
	
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setTitle("Really Exit?")
	        .setMessage("Are you sure you want to exit?")
	        .setNegativeButton(android.R.string.no, null)
	        .setPositiveButton(android.R.string.yes, new OnClickListener() {

	            public void onClick(DialogInterface arg0, int arg1) {
	            	store_info.super.onBackPressed();
	            }
	        }).create().show();
	}
	
	@SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_info);

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ImageView store_image = (ImageView) findViewById(R.id.store_image);
        ImageButton back = (ImageButton) findViewById(R.id.topbar).findViewById(R.id.back);
        TextView detail_info = (TextView) findViewById(R.id.detail_info);
        TextView address_info = (TextView) findViewById(R.id.address_info);
        TextView contact_info = (TextView) findViewById(R.id.contact_info);
        TextView store_name_view = (TextView) findViewById(R.id.store_name);
        Intent intent = getIntent();
        
        final int UID = intent.getIntExtra("UID" , -1);
        final int SID = intent.getIntExtra("SID" , -1);
        final int LID = intent.getIntExtra("LID" , -1);
        final String store_name = intent.getStringExtra("store_name");
        final String place_name = intent.getStringExtra("place_name");
        back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(store_info.this,store_detail.class);
				i.putExtra("UID", UID);
				i.putExtra("LID", LID);
				i.putExtra("SID", SID);
				i.putExtra("place_name", place_name);
				i.putExtra("store_name", store_name);
				startActivity(i);
				finish();
			}
        });       
        
    	
        String url = "http://122.155.187.27:9876/store_info.php";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("SID", Integer.toString(SID)));
        try{
        	JSONArray data = new JSONArray(getHttpPost(url,params));
        	JSONObject c = data.getJSONObject(0);	  
        	detail_info.setText(c.getString("detail"));
        	address_info.setText(c.getString("address"));
        	contact_info.setText(c.getString("contact"));          	
        	}catch(JSONException e)
        	{
        		e.printStackTrace();
        	}
        
        String url2 = "http://122.155.187.27:9876/store_detail.php";
        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
        params2.add(new BasicNameValuePair("SID", Integer.toString(SID)));
        Bitmap bitmap;
        try{
        	JSONArray data = new JSONArray(getHttpPost(url2,params2));
        	JSONObject c = data.getJSONObject(0);
        	try {
				bitmap = BitmapFactory.decodeStream((InputStream)new URL(c.getString("Image")).getContent());
				store_image.setImageBitmap(bitmap);
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	store_name_view.setText(c.getString("Name"));
        }catch(JSONException e)
    	{
    		e.printStackTrace();
    	}

	}	
	
	
	public String getHttpPost(String url,List<NameValuePair> params) {
		StringBuilder str = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params));
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
