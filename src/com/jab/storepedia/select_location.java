package com.jab.storepedia;

import com.jab.storepedia.R.string;
import com.jab.storepedia.adater.Location_Adapter;
import com.jab.storepedia.app.AppController;
import com.jab.storepedia.model.Location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
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

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class select_location extends Activity {
	
	private Location_Adapter adapter;
	private List<Location> locationList = new ArrayList<Location>();	
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_location);
     
        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent = getIntent();
        final ListView location_list = (ListView)findViewById(R.id.location_list);
		final int UID = intent.getIntExtra("UID" , -1);
		
		
		//text.setText(String.valueOf(UID));       
        ImageButton back = (ImageButton) findViewById(R.id.topbar).findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(select_location.this,MainActivity.class);
                startActivity(i);
                finish();
			}
        });
        adapter = new Location_Adapter(select_location.this,locationList);
        location_list.setAdapter(adapter);
    	
        // txtResult
        // btnGetData
       // final ImageButton getData = (ImageButton) findViewById(R.id.search);
        // Perform action on click
            
        
        location_list.setOnItemClickListener(new OnItemClickListener(){
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id){
        		Intent intent = new Intent(select_location.this, select_store.class);
        		int LID = locationList.get(position).getLID();
        		String place_name = locationList.get(position).getTitle();
        		intent.putExtra("LID", LID);
        		intent.putExtra("UID", UID);
        		intent.putExtra("place_name", place_name);
                startActivity(intent);
                finish();
        	}
        });   
    }

    public void search(View v) {
    	final TextView text = (TextView)findViewById(R.id.status);
    	final EditText input = (EditText)findViewById(R.id.place_name); 
         
    	//String url = "http://10.0.2.2/Storepedia/select_location.php";
    	String url = "http://122.155.187.27:9876/select_location.php";
    	//String url2 = "http://10.0.2.2/Storepedia/store_count.php";
    	locationList.clear();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("strA", input.getText().toString()));
        
    	//String resultServer  = getHttpPost(url,params);
    	//text.setText(resultServer);
        try{
        	JSONArray data = new JSONArray(getHttpPost(url,params));
        	text.setText("Result Found: 0");
        	for(int i = 0; i < data.length(); i++){
        	JSONObject c = data.getJSONObject(i);
        	Location location = new Location();
        	location.setTitle(c.getString("Name"));
        	location.setThumbnailUrl(c.getString("Image"));
        	location.setLID(c.getInt("LID"));
        	//List<NameValuePair> params2 = new ArrayList<NameValuePair>();
           // params2.add(new BasicNameValuePair("strB", Integer.toString(c.getInt("LID"))));
            //JSONArray data2 = new JSONArray(getHttpPost(url2,params2));
            //JSONObject c2 = data2.getJSONObject(0);
            //location.setNum(c2.getInt("COUNT(*)"));
            
        	//location.setNum(c.getInt("Number_of_store"));
        	//text.setText(Integer.toString(c.getInt("LID")));
        	//text.setText(c.getString("Name"));
        	//text.setText("Result Found: " + (i+1));
        	locationList.add(location);
        	text.setText("Result Found: "+(i+1));
        	} 
        
        	adapter.notifyDataSetChanged();
        }catch(JSONException e){
        	e.printStackTrace();
        	text.setText("Connection FAIL. Please check your internet connection!");
        }  
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE); 

inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                   InputMethodManager.HIDE_NOT_ALWAYS);
                   
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