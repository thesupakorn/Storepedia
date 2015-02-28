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

import com.facebook.Session;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class lcomment_detail extends Activity{
	@SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lcomment_detail);

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ImageView image = (ImageView) findViewById(R.id.image);
        ImageView vote_up = (ImageView) findViewById(R.id.vote_up);
        ImageView current_vote_up = (ImageView) findViewById(R.id.current_vote_up);
        ImageView vote_down = (ImageView) findViewById(R.id.vote_down);
        ImageView current_vote_down = (ImageView) findViewById(R.id.current_vote_down);
        TextView user_name = (TextView) findViewById(R.id.user_name);
        TextView agreed = (TextView) findViewById(R.id.agreed);
        TextView disagreed = (TextView) findViewById(R.id.disagreed);
        TextView comment = (TextView) findViewById(R.id.comment);
        ImageButton back = (ImageButton) findViewById(R.id.topbar).findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
        });       
        Intent intent = getIntent();
        final int PCID = intent.getIntExtra("PCID", -1);
        final int UID = intent.getIntExtra("UID" , -1);
        
        String url = "http://122.155.187.27:9876/lcomment_detail.php";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("PCID", Integer.toString(PCID)));
        try{
        	JSONArray data = new JSONArray(getHttpPost(url,params));
        	JSONObject c = data.getJSONObject(0);
        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(c.getString("image")).getContent());
        	image.setImageBitmap(bitmap);  
        	user_name.setText(c.getString("username"));
        	agreed.setText(c.getString("agreed"));
        	disagreed.setText(c.getString("disagreed"));
        	comment.setText(c.getString("comment"));
        }catch(JSONException e){
        	e.printStackTrace();
        	user_name.setText("FAIL"+PCID+" "+UID);
        } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      
        if(isLoggedIn())
        {
        	//String url2 = "http://122.155.187.27:9876/store_detail.php";

            //List<NameValuePair> params2 = new ArrayList<NameValuePair>();
            //params2.add(new BasicNameValuePair("UID", Integer.toString(UID)));
            //params2.add(new BasicNameValuePair("PCID", Integer.toString(PCID)));
        }
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
