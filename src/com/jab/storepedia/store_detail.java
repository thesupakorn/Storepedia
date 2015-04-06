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

import com.jab.storepedia.model.Store;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class store_detail extends Activity{
	@SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_detail);

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent = getIntent();
        final int SID = intent.getIntExtra("SID", -1);
        final int LID = intent.getIntExtra("LID", -1);
        final int UID = intent.getIntExtra("UID", -1);
        final String store_name = intent.getStringExtra("store_name");
        final String place_name = intent.getStringExtra("place_name");
        
		ImageView store_image_view = (ImageView) findViewById(R.id.store_image);
		ImageView category_image_view = (ImageView) findViewById(R.id.category_image);
		TextView store_name_view = (TextView) findViewById(R.id.store_name);
		TextView place_name_view = (TextView) findViewById(R.id.place_name); 
		TextView category_view = (TextView) findViewById(R.id.category_text);
		TextView score_view = (TextView) findViewById(R.id.score);
        ImageButton back = (ImageButton) findViewById(R.id.topbar).findViewById(R.id.back);
        ImageView where = (ImageView) findViewById(R.id.where_button);
        //ImageButton info = (ImageButton) findViewById(R.id.info_button);
        //ImageButton review = (ImageButton) findViewById(R.id.review_button);
        back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(store_detail.this,select_store.class);
				i.putExtra("UID", UID);
				i.putExtra("LID", LID);
				i.putExtra("place_name", place_name);
				startActivity(i);
				finish();
			}
        });
        String url = "http://122.155.187.27:9876/store_detail.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("SID", Integer.toString(SID)));
        category_view.setText(Integer.toString(SID));
        score_view.setText(store_name);
        try{
        	JSONArray data = new JSONArray(getHttpPost(url,params));
            	JSONObject c = data.getJSONObject(0);
        	store_name_view.setText(c.getString("Name"));
        	place_name_view.setText(c.getString("Location_Name"));
        	category_view.setText("Category: "+c.getString("Category"));
        	//score_view.setText("Rating: "+c.getString("Rating"));
        	//score_view.setText(c.getString("Image"));
        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(c.getString("Image")).getContent());
        	store_image_view.setImageBitmap(bitmap);   	
        }catch(JSONException e){
        	e.printStackTrace();
        	store_name_view.setText(e.toString());
        } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        where.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			Intent i = new Intent(store_detail.this,show_lcomment.class);
        		i.putExtra("SID", SID);
        		i.putExtra("UID", UID);
        		i.putExtra("LID", LID);
        		i.putExtra("store_name",store_name);
        		i.putExtra("place_name",place_name);
    			startActivity(i);
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