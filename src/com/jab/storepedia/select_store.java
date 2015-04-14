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

import com.jab.storepedia.adater.Store_Adapter;
import com.jab.storepedia.model.Location;
import com.jab.storepedia.model.Store;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
public class select_store extends Activity{
	private Store_Adapter adapter;
	int LID;
	String cat ="all";
	TextView status_text;
	EditText input;
	private ImageView all_no,all_yes,food_no,food_yes,books_no,books_yes,clothings_no, clothings_yes,electronics_no,electronics_yes,entertainments_no,entertainments_yes,health_no,health_yes,others_no,others_yes;
	
	private List<Store> storeList = new ArrayList<Store>();
      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.select_store);
            ImageButton back = (ImageButton) findViewById(R.id.topbar).findViewById(R.id.back);
            Intent intent = getIntent();
            
            all_no = (ImageView) findViewById(R.id.all_no);
            all_yes = (ImageView) findViewById(R.id.all_yes);
            food_no = (ImageView) findViewById(R.id.food_no);
            food_yes = (ImageView) findViewById(R.id.food_yes);
            books_no = (ImageView) findViewById(R.id.books_no);
            books_yes = (ImageView) findViewById(R.id.books_yes);
            clothings_no = (ImageView) findViewById(R.id.clothings_no);
            clothings_yes = (ImageView) findViewById(R.id.clothings_yes);
            electronics_no = (ImageView) findViewById(R.id.electronics_no);
            electronics_yes = (ImageView) findViewById(R.id.electronics_yes);
            entertainments_no = (ImageView) findViewById(R.id.entertainments_no);
            entertainments_yes = (ImageView) findViewById(R.id.entertainments_yes);
            health_no = (ImageView) findViewById(R.id.health_no);
            health_yes = (ImageView) findViewById(R.id.health_yes);
            others_no = (ImageView) findViewById(R.id.others_no);
            others_yes = (ImageView) findViewById(R.id.others_yes);
            
            all_no.setVisibility(View.GONE);
            //food_no.setVisibility(View.GONE);
            food_yes.setVisibility(View.GONE);
            //books_no.setVisibility(View.GONE);
            books_yes.setVisibility(View.GONE);
            //clothings_no.setVisibility(View.GONE);
            clothings_yes.setVisibility(View.GONE);
            //electronics_no.setVisibility(View.GONE);
            electronics_yes.setVisibility(View.GONE);
            //entertainments_no.setVisibility(View.GONE);
            entertainments_yes.setVisibility(View.GONE);
            //health_no.setVisibility(View.GONE);
            health_yes.setVisibility(View.GONE);
            //others_no.setVisibility(View.GONE);
            others_yes.setVisibility(View.GONE);
            
            
            final TextView status_text2 = (TextView)findViewById(R.id.status_text2); 
            final TextView status_text = (TextView)findViewById(R.id.status_text); 
            final ListView store_list = (ListView)findViewById(R.id.store_list); 
            final TextView placename = (TextView)findViewById(R.id.placename); 
            final EditText input = (EditText)findViewById(R.id.store_search);  
            
            final String place_name = intent.getStringExtra("place_name");
            final int LID = intent.getIntExtra("LID", -1);
            final int UID = intent.getIntExtra("UID" , -1);
            
            placename.setText(place_name);
           
            adapter = new Store_Adapter(select_store.this,storeList);
            store_list.setAdapter(adapter);
            final ImageView getData = (ImageView) findViewById(R.id.search);
            ImageView place_image = (ImageView) findViewById(R.id.place_image);
            
            String url2 = "http://122.155.187.27:9876/select_location.php";
            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
            params2.add(new BasicNameValuePair("strA", place_name));
            try{
            	JSONArray data = new JSONArray(getHttpPost(url2,params2));
            	for(int i = 0; i < data.length(); i++){
            	JSONObject c2 = data.getJSONObject(i);
            	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(c2.getString("Image")).getContent());
            	place_image.setImageBitmap(bitmap);   	
            	}       
            	}catch(JSONException e){
                	e.printStackTrace();
            	} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            
            
            all_no.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                  food_yes.setVisibility(View.GONE);
                  all_yes.setVisibility(View.VISIBLE);
                  books_yes.setVisibility(View.GONE);
                  clothings_yes.setVisibility(View.GONE);
                  electronics_yes.setVisibility(View.GONE);
                  entertainments_yes.setVisibility(View.GONE);
                  health_yes.setVisibility(View.GONE);
                  others_yes.setVisibility(View.GONE);
                	  
              	  all_no.setVisibility(View.GONE);
              	  food_no.setVisibility(View.VISIBLE);
              	  books_no.setVisibility(View.VISIBLE);
              	  clothings_no.setVisibility(View.VISIBLE);
              	  electronics_no.setVisibility(View.VISIBLE);
              	  entertainments_no.setVisibility(View.VISIBLE);
              	  health_no.setVisibility(View.VISIBLE);
              	  others_no.setVisibility(View.VISIBLE);
                  cat = "all";
                  getData.performClick();
                }
                });
            
            food_no.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
              	  food_yes.setVisibility(View.VISIBLE);
              	  all_yes.setVisibility(View.GONE);
              	  books_yes.setVisibility(View.GONE);
              	  clothings_yes.setVisibility(View.GONE);
              	  electronics_yes.setVisibility(View.GONE);
              	  entertainments_yes.setVisibility(View.GONE);
              	  health_yes.setVisibility(View.GONE);
              	  others_yes.setVisibility(View.GONE);
              	  
            	  all_no.setVisibility(View.VISIBLE);
            	  food_no.setVisibility(View.GONE);
            	  books_no.setVisibility(View.VISIBLE);
            	  clothings_no.setVisibility(View.VISIBLE);
            	  electronics_no.setVisibility(View.VISIBLE);
            	  entertainments_no.setVisibility(View.VISIBLE);
            	  health_no.setVisibility(View.VISIBLE);
            	  others_no.setVisibility(View.VISIBLE);
              	  cat = "food";             	
              	  getData.performClick();
                }
                });
            
            books_no.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    food_yes.setVisibility(View.GONE);
                    all_yes.setVisibility(View.GONE);
                    books_yes.setVisibility(View.VISIBLE);
                    clothings_yes.setVisibility(View.GONE);
                    electronics_yes.setVisibility(View.GONE);
                    entertainments_yes.setVisibility(View.GONE);
                    health_yes.setVisibility(View.GONE);
                    others_yes.setVisibility(View.GONE);
                  	  
                    all_no.setVisibility(View.VISIBLE);
                    food_no.setVisibility(View.VISIBLE);
                    books_no.setVisibility(View.GONE);
                    clothings_no.setVisibility(View.VISIBLE);
                    electronics_no.setVisibility(View.VISIBLE);
                	entertainments_no.setVisibility(View.VISIBLE);
                    health_no.setVisibility(View.VISIBLE);
                	others_no.setVisibility(View.VISIBLE);
                    cat = "books";
                    getData.performClick();
                }
                });
            clothings_no.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    food_yes.setVisibility(View.GONE);
                    all_yes.setVisibility(View.GONE);
                    books_yes.setVisibility(View.GONE);
                    clothings_yes.setVisibility(View.VISIBLE);
                    electronics_yes.setVisibility(View.GONE);
                    entertainments_yes.setVisibility(View.GONE);
                    health_yes.setVisibility(View.GONE);
                    others_yes.setVisibility(View.GONE);
                  	  
                	all_no.setVisibility(View.VISIBLE);
                	food_no.setVisibility(View.VISIBLE);
                	books_no.setVisibility(View.VISIBLE);
                	clothings_no.setVisibility(View.GONE);
                	electronics_no.setVisibility(View.VISIBLE);
                	entertainments_no.setVisibility(View.VISIBLE);
                	health_no.setVisibility(View.VISIBLE);
                	others_no.setVisibility(View.VISIBLE);
                    cat = "clothings";
                    getData.performClick();
                }
                });
            electronics_no.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    food_yes.setVisibility(View.GONE);
                    all_yes.setVisibility(View.GONE);
                    books_yes.setVisibility(View.GONE);
                    clothings_yes.setVisibility(View.GONE);
                    electronics_yes.setVisibility(View.VISIBLE);
                    entertainments_yes.setVisibility(View.GONE);
                    health_yes.setVisibility(View.GONE);
                    others_yes.setVisibility(View.GONE);
                  	  
                	all_no.setVisibility(View.VISIBLE);
                	food_no.setVisibility(View.VISIBLE);
                	books_no.setVisibility(View.VISIBLE);
                	clothings_no.setVisibility(View.VISIBLE);
                	electronics_no.setVisibility(View.GONE);
                	entertainments_no.setVisibility(View.VISIBLE);
                	health_no.setVisibility(View.VISIBLE);
                	others_no.setVisibility(View.VISIBLE);
                    cat = "electronics";
                    getData.performClick();
                }
                });
            entertainments_no.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    food_yes.setVisibility(View.GONE);
                    all_yes.setVisibility(View.GONE);
                    books_yes.setVisibility(View.GONE);
                    clothings_yes.setVisibility(View.GONE);
                    electronics_yes.setVisibility(View.GONE);
                    entertainments_yes.setVisibility(View.VISIBLE);
                    health_yes.setVisibility(View.GONE);
                    others_yes.setVisibility(View.GONE);
                  	  
                	all_no.setVisibility(View.VISIBLE);
                	food_no.setVisibility(View.VISIBLE);
                	books_no.setVisibility(View.VISIBLE);
                	clothings_no.setVisibility(View.VISIBLE);
                	electronics_no.setVisibility(View.VISIBLE);
                	entertainments_no.setVisibility(View.GONE);
                	health_no.setVisibility(View.VISIBLE);
                	others_no.setVisibility(View.VISIBLE);
                    cat = "entertainments";
                    getData.performClick();
                }
                });
            health_no.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    food_yes.setVisibility(View.GONE);
                    all_yes.setVisibility(View.GONE);
                    books_yes.setVisibility(View.GONE);
                    clothings_yes.setVisibility(View.GONE);
                    electronics_yes.setVisibility(View.GONE);
                    entertainments_yes.setVisibility(View.GONE);
                    health_yes.setVisibility(View.VISIBLE);
                    others_yes.setVisibility(View.GONE);
                  	  
                	all_no.setVisibility(View.VISIBLE);
                	food_no.setVisibility(View.VISIBLE);
                	books_no.setVisibility(View.VISIBLE);
                	clothings_no.setVisibility(View.VISIBLE);
                	electronics_no.setVisibility(View.VISIBLE);
                	entertainments_no.setVisibility(View.VISIBLE);
                	health_no.setVisibility(View.GONE);
                	others_no.setVisibility(View.VISIBLE);
                    cat = "health";
                    getData.performClick();
                }
                });
            others_no.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    food_yes.setVisibility(View.GONE);
                    all_yes.setVisibility(View.GONE);
                    books_yes.setVisibility(View.GONE);
                    clothings_yes.setVisibility(View.GONE);
                    electronics_yes.setVisibility(View.GONE);
                    entertainments_yes.setVisibility(View.GONE);
                    health_yes.setVisibility(View.GONE);
                    others_yes.setVisibility(View.VISIBLE);
                  	  
                	all_no.setVisibility(View.VISIBLE);
                	food_no.setVisibility(View.VISIBLE);
                	books_no.setVisibility(View.VISIBLE);
                	clothings_no.setVisibility(View.VISIBLE);
                	electronics_no.setVisibility(View.VISIBLE);
                	entertainments_no.setVisibility(View.VISIBLE);
                	health_no.setVisibility(View.VISIBLE);
                	others_no.setVisibility(View.GONE);
                    cat = "others";
                    getData.performClick();
                }
                });
            
            getData.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	 final String store_search = input.getText().toString();
                     //status_text.setText("Search Results From: "+place_name);
                     String url = "http://122.155.187.27:9876/select_store.php";
                 	//String url2 = "http://10.0.2.2/Storepedia/store_count.php";
                 	storeList.clear();                 	
             		List<NameValuePair> params = new ArrayList<NameValuePair>();
                     params.add(new BasicNameValuePair("LID", Integer.toString(LID)));
                     params.add(new BasicNameValuePair("store_search", store_search));
                     params.add(new BasicNameValuePair("cat", cat));
                 	//String resultServer  = getHttpPost(url,params);
                 	//text.setText(resultServer);
                     try{
                     	JSONArray data = new JSONArray(getHttpPost(url,params));
                     	//status_text2.setText("Result Found: 0");
                     	for(int i = 0; i < data.length(); i++){
                     	JSONObject c = data.getJSONObject(i);
                     	Store store = new Store();
                     	store.setTitle(c.getString("Name"));
                     	store.setThumbnailUrl(c.getString("Image"));
                     	store.setSID(c.getInt("SID"));
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
                     	//status_text2.setText("Result Found: "+(i+1));
                     	} 
                     
                     	adapter.notifyDataSetChanged();
                     }catch(JSONException e){
                     	e.printStackTrace();
                     	status_text.setText("Connection FAIL. Please check your internet connection!");
                     }                 //placename.setText(cat);           
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
    				i.putExtra("LID", LID);
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
            		intent.putExtra("place_name", place_name);
                    startActivity(intent);
                    finish();
            	}
            });  
        }                
      public void newstore(View v)
      {
    	  Intent intent = getIntent();
    	  final String place_name = intent.getStringExtra("place_name");
          final int LID = intent.getIntExtra("LID", -1);
          final int UID = intent.getIntExtra("UID" , -1);
          
          Intent i = new Intent(select_store.this,create_store.class);
		  i.putExtra("UID", UID);
		  i.putExtra("LID", LID);
          startActivity(i);
          finish();
          
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
