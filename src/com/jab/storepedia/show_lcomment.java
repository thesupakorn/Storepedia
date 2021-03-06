package com.jab.storepedia;

import com.facebook.Session;
import com.jab.storepedia.R.string;
import com.jab.storepedia.adater.Lcomment_adapter;
import com.jab.storepedia.app.AppController;
import com.jab.storepedia.model.Lcomment;

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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
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

public class show_lcomment extends Activity {
	
	private Lcomment_adapter adapter;
	private List<Lcomment> lcommentList = new ArrayList<Lcomment>();
	String flag = "create";
	int PCID;
	
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setTitle("Really Exit?")
	        .setMessage("Are you sure you want to exit?")
	        .setNegativeButton(android.R.string.no, null)
	        .setPositiveButton(android.R.string.yes, new OnClickListener() {

	            public void onClick(DialogInterface arg0, int arg1) {
	            	show_lcomment.super.onBackPressed();
	            }
	        }).create().show();
	}
	
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_lcomment);

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent = getIntent();
		final int SID = intent.getIntExtra("SID" , -1);
		final int LID = intent.getIntExtra("LID" , -1);
		final int UID = intent.getIntExtra("UID" , -1);
		final String store_name = intent.getStringExtra("store_name");
		final String place_name = intent.getStringExtra("place_name");
		
        ImageButton back = (ImageButton) findViewById(R.id.topbar).findViewById(R.id.back);
        ImageButton create_comment = (ImageButton) findViewById(R.id.create_comment_button);
        ImageButton edit_comment = (ImageButton) findViewById(R.id.edit_comment_button);
    	edit_comment.setVisibility(View.GONE);
        create_comment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(show_lcomment.this,create_comment.class);;
                //i.putExtra("place_name", place_name);
                //i.putExtra("LID", LID);
				i.putExtra("UID", UID);
				i.putExtra("LID", LID);
				i.putExtra("SID", SID);
				i.putExtra("place_name", place_name);
				i.putExtra("store_name", store_name);
				startActivity(i);
				finish();
			}
        });
        edit_comment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String url1 = "http://122.155.187.27:9876/find_PCID.php";
				List<NameValuePair> params = new ArrayList<NameValuePair>();
		        params.add(new BasicNameValuePair("SID", Integer.toString(SID)));
		        params.add(new BasicNameValuePair("UID", Integer.toString(UID)));
		        try{
		        	JSONArray data = new JSONArray(getHttpPost(url1,params));
		            JSONObject c = data.getJSONObject(0);
		            PCID = Integer.parseInt(c.getString("PCID"));
		        }catch(JSONException e){
		        	e.printStackTrace();
		     }
				Intent i = new Intent(show_lcomment.this,lcomment_detail.class);;
                //i.putExtra("place_name", place_name);
                //i.putExtra("LID", LID);
				i.putExtra("UID", UID);
				i.putExtra("LID", LID);
				i.putExtra("SID", SID);
				i.putExtra("PCID", PCID);
				i.putExtra("place_name", place_name);
				i.putExtra("store_name", store_name);
				startActivity(i);
				finish();
			}
        });
        back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(show_lcomment.this,store_detail.class);
                //i.putExtra("place_name", place_name);
                //i.putExtra("LID", LID);
				i.putExtra("UID", UID);
				i.putExtra("LID", LID);
				i.putExtra("SID", SID);
				i.putExtra("place_name", place_name);
				i.putExtra("store_name", store_name);
				startActivity(i);
				finish();
			}
        });
        final TextView text = (TextView)findViewById(R.id.status); 
        final ListView lcomment_list = (ListView)findViewById(R.id.lcomment_list); 
        final TextView store_name_textview = (TextView)findViewById(R.id.textView1); 
        //ImageButton write_comment = (ImageButton) findViewById(R.id.create_comment_button);
        //ImageButton edit_comment = (ImageButton) findViewById(R.id.edit_comment_button);
        final TextView write_comment_text = (TextView)findViewById(R.id.textView2); 
        if(!isLoggedIn()){
        	create_comment.setVisibility(View.GONE);
        	edit_comment.setVisibility(View.GONE);
        }
        
        adapter = new Lcomment_adapter(show_lcomment.this,lcommentList);
        lcomment_list.setAdapter(adapter);
        
        
		store_name_textview.setText("Comments For: "+store_name);

		        String url = "http://122.155.187.27:9876/lcomment_list.php";
            	lcommentList.clear();
        		List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("SID", Integer.toString(SID)));
                try{
                	JSONArray data = new JSONArray(getHttpPost(url,params));
                	text.setText("Result Found: 0");
                	for(int i = 0; i < data.length(); i++){
                	JSONObject c = data.getJSONObject(i);
                	Lcomment lcomment = new Lcomment();
                	lcomment.setPCID(c.getInt("PCID"));
                	lcomment.setUsername(c.getString("username"));
                	lcomment.setThumbnailUrl(c.getString("image"));
                	lcomment.setagreed(c.getInt("agreed"));
                	lcomment.setdisagreed(c.getInt("disagreed"));
                	lcomment.setcomment(c.getString("comment"));
                	lcommentList.add(lcomment);
                	text.setText("Result Found: "+(i+1));
                	} 
                
                	adapter.notifyDataSetChanged();
                }catch(JSONException e){
                	e.printStackTrace();
                	text.setText("Connection FAIL. Please check your internet connection!");
                }      
                
                String url2 = "http://122.155.187.27:9876/check_if_comment.php";
                List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                params2.add(new BasicNameValuePair("SID", Integer.toString(SID)));
                params2.add(new BasicNameValuePair("UID", Integer.toString(UID)));
                try{
                	JSONArray data = new JSONArray(getHttpPost(url2,params2));
                	for(int i = 0; i < data.length(); i++){
                    	JSONObject c = data.getJSONObject(i);
                    	create_comment.setVisibility(View.GONE);
                    	edit_comment.setVisibility(View.VISIBLE);
                    	flag = "edit";
                	}
                }catch(JSONException e){
                	e.printStackTrace();  
                	write_comment_text.setText("FAIL TO GET PCID");
                }      
                
        lcomment_list.setOnItemClickListener(new OnItemClickListener(){
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id){
        		Intent intent = new Intent(show_lcomment.this, lcomment_detail.class);
        		int PCID = lcommentList.get(position).getPCID();
        		intent.putExtra("PCID", PCID);
        		intent.putExtra("SID", SID);
        		intent.putExtra("LID", LID);
        		intent.putExtra("place_name", place_name);
        		intent.putExtra("UID", UID);
        		intent.putExtra("store_name", store_name);
                startActivity(intent);
                finish();
        	}
        }); 
    }
    public static boolean isLoggedIn() {
        Session session = Session.getActiveSession();
        return (session != null && session.getAccessToken() != null && session.getAccessToken().length() > 1);
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

