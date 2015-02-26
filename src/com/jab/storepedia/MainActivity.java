package com.jab.storepedia;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.database.sqlite.SQLiteDatabase;
import android.app.Activity;


public class MainActivity extends ActionBarActivity {

	SQLiteDatabase db;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySQL myDb = new mySQL(this);
        myDb.getWritableDatabase();
        String Title = "";
        int LID = 0;
        int SID = 0;
        String store_name = "";
        String store_search = "";
        Button select_location = (Button) findViewById(R.id.select_location_button);
        select_location.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,select_location.class);
                startActivity(i);
                finish();
			}
        });
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
