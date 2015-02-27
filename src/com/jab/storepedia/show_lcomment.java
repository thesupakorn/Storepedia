package com.jab.storepedia;

import com.facebook.Session;
import com.jab.storepedia.R.string;
import com.jab.storepedia.adater.Lcomment_adapter;
import com.jab.storepedia.adater.Location_Adapter;
import com.jab.storepedia.adater.Store_Adapter;
import com.jab.storepedia.app.AppController;
import com.jab.storepedia.model.Lcomment;
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

public class show_lcomment extends Activity {
	
	private Lcomment_adapter adapter;
	private List<Lcomment> lcommentList = new ArrayList<Lcomment>();
	
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
		final String store_name = intent.getStringExtra("store_name");
        ImageButton back = (ImageButton) findViewById(R.id.topbar).findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(show_lcomment.this,select_store.class);
                //i.putExtra("place_name", place_name);
                //i.putExtra("LID", LID);
				startActivity(i);
				finish();
			}
        });
        final TextView text = (TextView)findViewById(R.id.status); 
        final ListView lcomment_list = (ListView)findViewById(R.id.lcomment_list); 
        final TextView store_name_textview = (TextView)findViewById(R.id.textView1); 
        ImageButton write_comment = (ImageButton) findViewById(R.id.comment_button);
        if(!isLoggedIn())
        	write_comment.setVisibility(View.GONE);
        
        adapter = new Lcomment_adapter(show_lcomment.this,lcommentList);
        lcomment_list.setAdapter(adapter);
        
        
		store_name_textview.setText("Location Comments For: "+store_name);

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
                	text.setText("FAIL");
                }                       
        lcomment_list.setOnItemClickListener(new OnItemClickListener(){
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id){
        		Intent intent = new Intent(show_lcomment.this, store_detail.class);
        		int CPID = lcommentList.get(position).getCPID();
        		intent.putExtra("CPID", CPID);
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

