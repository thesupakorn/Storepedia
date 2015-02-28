package com.jab.storepedia;
import java.io.BufferedReader;
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

import com.jab.storepedia.adater.Store_Adapter;
import com.jab.storepedia.model.Store;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
public class select_store extends Activity{
	private Store_Adapter adapter;
	private List<Store> storeList = new ArrayList<Store>();
      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.select_store);
            ImageButton back = (ImageButton) findViewById(R.id.topbar).findViewById(R.id.back);
            Intent intent = getIntent();
            final TextView text = (TextView)findViewById(R.id.textView3); 
            final TextView text2 = (TextView)findViewById(R.id.textView4); 
            final ListView store_list = (ListView)findViewById(R.id.store_list); 
            final TextView placename = (TextView)findViewById(R.id.textView2); 
            final EditText input = (EditText)findViewById(R.id.store_search);           
            final String place_name = intent.getStringExtra("place_name");
            final int LID = intent.getIntExtra("LID", -1);
            final int UID = intent.getIntExtra("UID" , -1);
            placename.setText(place_name);
           
            adapter = new Store_Adapter(select_store.this,storeList);
            store_list.setAdapter(adapter);
            final Button getData = (Button) findViewById(R.id.search);
            
            getData.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	 final String store_search = input.getText().toString();
                     text2.setText("Search Results From: "+place_name);
                     String url = "http://122.155.187.27:9876/select_store.php";
                 	//String url2 = "http://10.0.2.2/Storepedia/store_count.php";
                 	storeList.clear();                 	
             		List<NameValuePair> params = new ArrayList<NameValuePair>();
                     params.add(new BasicNameValuePair("LID", Integer.toString(LID)));
                     params.add(new BasicNameValuePair("store_search", store_search));
                 	//String resultServer  = getHttpPost(url,params);
                 	//text.setText(resultServer);
                     try{
                     	JSONArray data = new JSONArray(getHttpPost(url,params));
                     	text.setText("Result Found: 0");
                     	for(int i = 0; i < data.length(); i++){
                     	JSONObject c = data.getJSONObject(i);
                     	Store store = new Store();
                     	store.setTitle(c.getString("Name"));
                     	store.setThumbnailUrl(c.getString("Image"));
                     	store.setSID(c.getInt("SID"));
                     	store.setRating(c.getDouble("Rating"));
                     	store.setGenre(c.getString("Category"));
                     	//List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                        // params2.add(new BasicNameValuePair("strB", Integer.toString(c.getInt("LID"))));
                         //JSONArray data2 = new JSONArray(getHttpPost(url2,params2));
                         //JSONObject c2 = data2.getJSONObject(0);
                         //location.setNum(c2.getInt("COUNT(*)"));
                         
                     	//location.setNum(c.getInt("Number_of_store"));
                     	 //text.setText(Integer.toString(c.getInt("LID")));
                     	//text.setText(c.getString("Name"));
                     	//text.setText("Result Found: " + (i+1));
                     	storeList.add(store);
                     	text.setText("Result Found: "+(i+1));
                     	} 
                     
                     	adapter.notifyDataSetChanged();
                     }catch(JSONException e){
                     	e.printStackTrace();
                     	text.setText("FAIL");
                     }                           
                     InputMethodManager inputManager = (InputMethodManager)
                             getSystemService(Context.INPUT_METHOD_SERVICE); 

inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                }
            });                      
            
            //text.setText(Integer.toString(LID));
            back.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				Intent i = new Intent(select_store.this,select_location.class);
    				i.putExtra("UID", UID);
                    startActivity(i);
                    finish();
    			}
            });
            store_list.setOnItemClickListener(new OnItemClickListener(){
            	@Override
            	public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id){
            		Intent intent = new Intent(select_store.this, store_detail.class);
            		int SID = storeList.get(position).getSID();
            		String store_name = storeList.get(position).getTitle();
            		intent.putExtra("SID", SID);
            		intent.putExtra("LID", LID);
            		intent.putExtra("UID", UID);
            		intent.putExtra("store_name", store_name);
                    startActivity(intent);
                    finish();
            	}
            });  
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
