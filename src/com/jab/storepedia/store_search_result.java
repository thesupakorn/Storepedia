package com.jab.storepedia;

import com.jab.storepedia.R.string;
import com.jab.storepedia.adater.Location_Adapter;
import com.jab.storepedia.adater.Store_Adapter;
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
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class store_search_result extends Activity {
	
	private Store_Adapter adapter;
	private List<Store> storeList = new ArrayList<Store>();
	
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_search_result);

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent = getIntent();
		final int LID = intent.getIntExtra("LID", -1);
		final String store_search = intent.getStringExtra("store_search");
		final String place_name = intent.getStringExtra("place_name");
		final int CID = intent.getIntExtra("CID" , -1);
        ImageButton back = (ImageButton) findViewById(R.id.topbar).findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(store_search_result.this,select_store.class);
                i.putExtra("place_name", place_name);
                i.putExtra("LID", LID);
				startActivity(i);
				finish();
			}
        });
        final TextView text = (TextView)findViewById(R.id.status); 
        final ListView store_list = (ListView)findViewById(R.id.store_list); 
        final TextView store_search_textview = (TextView)findViewById(R.id.textView1); 
        
        adapter = new Store_Adapter(store_search_result.this,storeList);
        store_list.setAdapter(adapter);
        
        
		store_search_textview.setText("Search Results From: "+place_name+" With Keyword: "+store_search);

            	//String url = "http://10.0.2.2/Storepedia/select_store.php";
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
                	store.setLID(c.getInt("SID"));
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
        store_list.setOnItemClickListener(new OnItemClickListener(){
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id){
        		Intent intent = new Intent(store_search_result.this, store_detail.class);
        		int SID = storeList.get(position).getLID();
        		String store_name = storeList.get(position).getTitle();
        		intent.putExtra("SID", SID);
        		intent.putExtra("LID", LID);
        		intent.putExtra("store_name", store_name);
        		intent.putExtra("store_search",store_search);
        		intent.putExtra("CID", CID);
        		intent.putExtra("place_name", place_name);
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

