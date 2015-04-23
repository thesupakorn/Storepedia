package com.jab.storepedia;

import com.jab.storepedia.R.string;
import com.jab.storepedia.adater.Location_Adapter;
import com.jab.storepedia.app.AppController;
import com.jab.storepedia.model.Location;
import com.jab.storepedia.model.Store;

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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.text.Editable;
import android.text.TextWatcher;
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
	EditText input;
	
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setTitle("Really Exit?")
	        .setMessage("Are you sure you want to exit?")
	        .setNegativeButton(android.R.string.no, null)
	        .setPositiveButton(android.R.string.yes, new OnClickListener() {

	            public void onClick(DialogInterface arg0, int arg1) {
	            	select_location.super.onBackPressed();
	            }
	        }).create().show();
	}
	
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
		input = (EditText)findViewById(R.id.place_name); 
		
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
    	
        
        
        
        input = (EditText)findViewById(R.id.place_name); 
        
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
        	} 
        
        	adapter.notifyDataSetChanged();
        }catch(JSONException e){
        	e.printStackTrace();
        }  
        
        
        
        
        
        input.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            	if(locationList.size()==0){
            		Log.d("GGGGGG","set Data");
            		String store_search = input.getText().toString();
                    //status_text.setText("Search Results From: "+place_name);
                    String url = "http://122.155.187.27:9876/select_store.php";
                	//String url2 = "http://10.0.2.2/Storepedia/store_count.php";
                	locationList.clear();                 	
            		List<NameValuePair> params = new ArrayList<NameValuePair>();
                	//String resultServer  = getHttpPost(url,params);
                	//text.setText(resultServer);
                    try{
                    	JSONArray data = new JSONArray(getHttpPost(url,params));
                    	//status_text2.setText("Result Found: 0");
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
                    	//status_text2.setText("Result Found: "+(i+1));
                    	} 
                    
                    	adapter.notifyDataSetChanged();
                    }catch(JSONException e){
                    	e.printStackTrace();
                    }       
            } else{
        		FilterData();
        	}
            }
        });
        
        
        
        
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
    	EditText input = (EditText)findViewById(R.id.place_name); 
         
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
    
    
    private void FilterData(){
   	 Log.d("GGGGGG","storeList.size(): " + locationList.size());
 		adapter.resetData(locationList);
 		String name = "";
 		if(input.getText()!=null)
 			name = input.getText().toString();
 		Log.d("GGGGGG","name:" + name);
 		adapter.getFilter().filter(name);
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