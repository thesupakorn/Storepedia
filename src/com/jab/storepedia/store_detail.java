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
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class store_detail extends Activity{
	boolean isImageFitToScreen;
	ImageView pic1;
	ImageView pic2;
	ImageView pic3;
	ImageView pic4;
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
        
        pic1 = (ImageView) findViewById(R.id.pic1);
        pic2 = (ImageView) findViewById(R.id.pic2);
        pic3 = (ImageView) findViewById(R.id.pic3);
        pic4 = (ImageView) findViewById(R.id.pic4);
        ImageView setting_button = (ImageView) findViewById(R.id.setting_button);
        setting_button.setVisibility(View.GONE);
		ImageView store_image_view = (ImageView) findViewById(R.id.store_image);
		TextView store_name_view = (TextView) findViewById(R.id.store_name);
		TextView place_name_view = (TextView) findViewById(R.id.place_name); 
		TextView category_view = (TextView) findViewById(R.id.category_text);
		TextView score_view = (TextView) findViewById(R.id.score);
        ImageButton back = (ImageButton) findViewById(R.id.topbar).findViewById(R.id.back);
        ImageView where = (ImageView) findViewById(R.id.comment_button);
        ImageView info = (ImageView) findViewById(R.id.info_button);
        ImageView map = (ImageView) findViewById(R.id.map_button);
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
        
        
        String url0 = "http://122.155.187.27:9876/check_if_owner_store.php";

        List<NameValuePair> params0 = new ArrayList<NameValuePair>();
        params0.add(new BasicNameValuePair("UID", Integer.toString(UID)));
        params0.add(new BasicNameValuePair("SID", Integer.toString(SID)));
        //comment.setText("UID: "+UID+" PCID: "+PCID);
        try{
        	JSONArray data = new JSONArray(getHttpPost(url0,params0));
        	JSONObject c = data.getJSONObject(0);
        	String result = c.getString("result");
        	//comment.setText(result);
        	if(result.equals("yes"))
        	{
        		setting_button.setVisibility(View.VISIBLE);
        	}
        }catch(JSONException e)
    	{
    		e.printStackTrace();
    	}
        
        
        
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
        	try{
        		Bitmap bitmap1 = BitmapFactory.decodeStream((InputStream)new URL(c.getString("1")).getContent());     		
        		pic1.setImageBitmap(bitmap1);
        	}catch(JSONException e)
        	{
        		e.printStackTrace();
        	}
        	try{
        		Bitmap bitmap2 = BitmapFactory.decodeStream((InputStream)new URL(c.getString("2")).getContent());
        		pic2.setImageBitmap(bitmap2);
        	}catch(JSONException e)
        	{
        		e.printStackTrace();
        	}
        	try{
        		Bitmap bitmap3 = BitmapFactory.decodeStream((InputStream)new URL(c.getString("3")).getContent());
        		pic3.setImageBitmap(bitmap3);
        	}catch(JSONException e)
        	{
        		e.printStackTrace();
        	}
        	try{
        		Bitmap bitmap4 = BitmapFactory.decodeStream((InputStream)new URL(c.getString("4")).getContent());
        		pic4.setImageBitmap(bitmap4);
        	}catch(JSONException e)
        	{
        		e.printStackTrace();
        	}
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
        
        pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isImageFitToScreen) {
                    isImageFitToScreen=false;  
                    LayoutParams pic1layout = new RelativeLayout.LayoutParams(120,120);
                    pic1layout.setMargins(76, 0, 0, 60);
                    pic1layout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    pic1layout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    
                    pic2.setVisibility(View.VISIBLE);
                    pic3.setVisibility(View.VISIBLE);
                    pic4.setVisibility(View.VISIBLE);
                    
                    pic1.setLayoutParams(pic1layout);
                    pic1.setAdjustViewBounds(true);
                }else{
                    isImageFitToScreen=true;
                    
                    pic2.setVisibility(View.GONE);
                    pic3.setVisibility(View.GONE);
                    pic4.setVisibility(View.GONE);
                    
                    LayoutParams pic1layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    pic1layout.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    pic1.setLayoutParams(pic1layout);
                    pic1.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        });
        pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isImageFitToScreen) {
                    isImageFitToScreen=false;  
                    LayoutParams pic2layout = new RelativeLayout.LayoutParams(120,120);
                    pic2layout.setMargins(220, 0, 0, 60);
                    pic2layout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    pic2layout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    
                    pic1.setVisibility(View.VISIBLE);
                    pic3.setVisibility(View.VISIBLE);
                    pic4.setVisibility(View.VISIBLE);
                    
                    pic2.setLayoutParams(pic2layout);
                    pic2.setAdjustViewBounds(true);
                }else{
                    isImageFitToScreen=true;
                    
                    pic1.setVisibility(View.GONE);
                    pic3.setVisibility(View.GONE);
                    pic4.setVisibility(View.GONE);
                    
                    LayoutParams pic2layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    pic2layout.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    pic2.setLayoutParams(pic2layout);
                    pic2.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        });
        pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isImageFitToScreen) {
                    isImageFitToScreen=false;  
                    LayoutParams pic3layout = new RelativeLayout.LayoutParams(120,120);
                    pic3layout.setMargins(344, 0, 0, 60);
                    pic3layout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    pic3layout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    
                    pic2.setVisibility(View.VISIBLE);
                    pic1.setVisibility(View.VISIBLE);
                    pic4.setVisibility(View.VISIBLE);
                    
                    pic3.setLayoutParams(pic3layout);
                    pic3.setAdjustViewBounds(true);
                }else{
                    isImageFitToScreen=true;
                    
                    pic2.setVisibility(View.GONE);
                    pic1.setVisibility(View.GONE);
                    pic4.setVisibility(View.GONE);
                    
                    LayoutParams pic3layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    pic3layout.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    pic3.setLayoutParams(pic3layout);
                    pic3.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        });
        pic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isImageFitToScreen) {
                    isImageFitToScreen=false;  
                    LayoutParams pic4layout = new RelativeLayout.LayoutParams(120,120);
                    pic4layout.setMargins(468, 0, 0, 60);
                    pic4layout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    pic4layout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    
                    pic2.setVisibility(View.VISIBLE);
                    pic3.setVisibility(View.VISIBLE);
                    pic1.setVisibility(View.VISIBLE);
                    
                    pic4.setLayoutParams(pic4layout);
                    pic4.setAdjustViewBounds(true);
                }else{
                    isImageFitToScreen=true;
                    

                    pic2.setVisibility(View.GONE);
                    pic3.setVisibility(View.GONE);
                    pic1.setVisibility(View.GONE);
                    
                    LayoutParams pic4layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    pic4layout.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    pic4.setLayoutParams(pic4layout);
                    pic4.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        });
        
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
        
        info.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			Intent i = new Intent(store_detail.this,store_info.class);
        		i.putExtra("SID", SID);
        		i.putExtra("UID", UID);
        		i.putExtra("LID", LID);
        		i.putExtra("store_name",store_name);
        		i.putExtra("place_name",place_name);
    			startActivity(i);
    			finish();
    		}
        });
        
        map.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			Intent i = new Intent(store_detail.this,store_map.class);
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
	
	public void setting(View view)
	{
		Intent intent = getIntent();
		final int PCID = intent.getIntExtra("PCID", -1);
        final int UID = intent.getIntExtra("UID" , -1);
        final int SID = intent.getIntExtra("SID" , -1);
        final int LID = intent.getIntExtra("LID" , -1);
        final String store_name = intent.getStringExtra("store_name");
        final String place_name = intent.getStringExtra("place_name");
        
		CharSequence choices[] = new CharSequence[] {"Edit information", "Edit images","Delete store" ,"Cancel"};
		AlertDialog.Builder OptionDialog = new AlertDialog.Builder(this);
		OptionDialog.setTitle("Setting");
		OptionDialog.setItems(choices, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // the user clicked on colors[which]
		    	if(which == 0)
		    	{
		    		Intent i = new Intent(store_detail.this,edit_comment_text.class);
					i.putExtra("UID", UID);
					i.putExtra("LID", LID);
					i.putExtra("SID", SID);
					i.putExtra("place_name", place_name);
					i.putExtra("store_name", store_name);
					startActivity(i);
					finish();
		    	}
		    	else if(which == 1)
		    	{	  
		    		Intent i = new Intent(store_detail.this,edit_comment_image.class);
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
		    	else if(which == 2)
		    	{
		    		CharSequence choices[] = new CharSequence[] {"Yes", "Cancel"};
		    		AlertDialog.Builder OptionDialog2 = new AlertDialog.Builder(store_detail.this);
		    		OptionDialog2.setTitle("Are you sure that you want to delete your store?");
		    		OptionDialog2.setItems(choices, new DialogInterface.OnClickListener() {
		    			 @Override
		    			    public void onClick(DialogInterface dialog, int which) {
		    			        // the user clicked on colors[which]
		    			    	if(which == 0)
		    			    	{
		    			    		String url1 = "http://122.155.187.27:9876/delete_comment.php";
		    						List<NameValuePair> params = new ArrayList<NameValuePair>();
		    				        params.add(new BasicNameValuePair("PCID", Integer.toString(PCID)));
		    				        try{
		    				        	JSONArray data = new JSONArray(getHttpPost(url1,params));
		    				            JSONObject c = data.getJSONObject(0);
		    				        }catch(JSONException e){
		    				        	e.printStackTrace();
		    				     }
		    				        Intent i = new Intent(store_detail.this,show_lcomment.class);
		    						i.putExtra("UID", UID);
		    						i.putExtra("LID", LID);
		    						i.putExtra("SID", SID);
		    						i.putExtra("place_name", place_name);
		    						i.putExtra("store_name", store_name);
		    						startActivity(i);
		    						finish();
		    			    	}
		    			    	else if(which == 1)
		    			    	{
		    			    	  dialog.dismiss();
		    			    	}
		    		}
		    			});		
		    		OptionDialog2.show();
		    		}
		    	else if(which == 3)
		    	{
		    		 dialog.dismiss();
		    	}
		    }
		});		
		OptionDialog.show();
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