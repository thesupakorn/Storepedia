package com.jab.storepedia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.facebook.widget.ProfilePictureView;
import com.facebook.model.GraphUser;


import android.support.v7.app.ActionBarActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;


public class MainActivity extends ActionBarActivity {

	SQLiteDatabase db;
	private UiLifecycleHelper uiHelper;
	private ProfilePictureView profilePictureView;
	public int UID;

	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setTitle("Really Exit?")
	        .setMessage("Are you sure you want to exit?")
	        .setNegativeButton(android.R.string.no, null)
	        .setPositiveButton(android.R.string.yes, new OnClickListener() {

	            public void onClick(DialogInterface arg0, int arg1) {
	            	MainActivity.super.onBackPressed();
	            }
	        }).create().show();
	}
	
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        final TextView fb_status = (TextView) findViewById(R.id.fb_status_text);               
        LoginButton loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
        loginBtn.setReadPermissions(Arrays.asList("email"));
        loginBtn.setReadPermissions(Arrays.asList("public_profile"));
        profilePictureView = (ProfilePictureView) findViewById(R.id.selection_profile_pic);
        profilePictureView.setCropped(true);
        loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {
        	@Override
        	public void onUserInfoFetched(GraphUser user) {
        		if (user != null) {
        			fb_status.setText("You are currently logged in as " + user.getName());
        			profilePictureView.setVisibility(View.VISIBLE);
        			profilePictureView.setProfileId(user.getId());
        			
        			String url = "http://122.155.187.27:9876/add_user.php";
        			List<NameValuePair> params = new ArrayList<NameValuePair>();
        			params.add(new BasicNameValuePair("username",user.getName().toString()));
        			Object userEmail = user.getProperty("email");
        			String email = (userEmail != null)? userEmail.toString() : "";
        	        params.add(new BasicNameValuePair("email",email));
        	        params.add(new BasicNameValuePair("fbid",user.getId().toString()));
        	        try{
                    	JSONArray data = new JSONArray(getHttpPost(url,params));
                    	JSONObject c = data.getJSONObject(0);
                    	UID = c.getInt("UID");
                    	//fb_status.setText(String.valueOf(UID));
                    	//fb_status.setText("SUCCESS");
        	        }catch(JSONException e){
                    	e.printStackTrace();
                    	fb_status.setText("ERROR");
                    }   
        	             	        
        		} else {
        			fb_status.setText("You are not logged in.");
        			profilePictureView.setVisibility(View.GONE);
        			UID = -1;
        		}
        	}
        });
        
        mySQL myDb = new mySQL(this);
        myDb.getWritableDatabase();
        String Title = "";
        int LID = -1;
        int SID = -1;
        String store_name = "";
        String store_search = "";
        

        Button select_location = (Button) findViewById(R.id.select_location_button);
        select_location.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,select_location.class);
				i.putExtra("UID", UID);
                startActivity(i);
                finish();
			}
        }); 
        
	}
	private Session.StatusCallback statusCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (state.isOpened()) {
				Log.d("MainActivity", "Facebook session opened.");
			} else if (state.isClosed()) {
				Log.d("MainActivity", "Facebook session closed.");
			}
		}
	};
	@Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }
	@Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }
	@Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
	}
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }
	@Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
