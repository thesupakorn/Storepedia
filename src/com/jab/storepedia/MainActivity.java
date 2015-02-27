package com.jab.storepedia;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.facebook.model.GraphUser;


import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.database.sqlite.SQLiteDatabase;
import android.app.Activity;


public class MainActivity extends ActionBarActivity {

	SQLiteDatabase db;
	private UiLifecycleHelper uiHelper;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        final TextView fb_status = (TextView) findViewById(R.id.fb_status_text);
        LoginButton loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
        loginBtn.setReadPermissions(Arrays.asList("email"));
        loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {
        	@Override
        	public void onUserInfoFetched(GraphUser user) {
        		if (user != null) {
        			fb_status.setText("You are currently logged in as " + user.getName());
        		} else {
        			fb_status.setText("You are not logged in.");
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
                startActivity(i);
                //finish();
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
    
   
}
