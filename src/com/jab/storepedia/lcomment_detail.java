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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class lcomment_detail extends Activity{
	boolean isImageFitToScreen;
	ImageView pic1;
	ImageView pic2;
	ImageView pic3;
	ImageView pic4;
	int UID,SID,LID,PCID;
	String place_name,store_name;
	
	
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setTitle("Really Exit?")
	        .setMessage("Are you sure you want to exit?")
	        .setNegativeButton(android.R.string.no, null)
	        .setPositiveButton(android.R.string.yes, new OnClickListener() {

	            public void onClick(DialogInterface arg0, int arg1) {
	            	lcomment_detail.super.onBackPressed();
	            }
	        }).create().show();
	}
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
        String vote = "0";
        ImageView image = (ImageView) findViewById(R.id.image);
        ImageView vote_up = (ImageView) findViewById(R.id.vote_up);
        ImageView current_vote_up = (ImageView) findViewById(R.id.current_vote_up);
        ImageView vote_down = (ImageView) findViewById(R.id.vote_down);
        ImageView current_vote_down = (ImageView) findViewById(R.id.current_vote_down);
        ImageView setting_button = (ImageView) findViewById(R.id.setting_button);
        setting_button.setVisibility(View.GONE);
        pic1 = (ImageView) findViewById(R.id.pic1);
        pic2 = (ImageView) findViewById(R.id.pic2);
        pic3 = (ImageView) findViewById(R.id.pic3);
        pic4 = (ImageView) findViewById(R.id.pic4);
        TextView user_name = (TextView) findViewById(R.id.user_name);
        TextView agreed = (TextView) findViewById(R.id.agreed);
        TextView disagreed = (TextView) findViewById(R.id.disagreed);
        TextView comment = (TextView) findViewById(R.id.comment);
        ImageButton back = (ImageButton) findViewById(R.id.topbar).findViewById(R.id.back);
        Intent intent = getIntent();
        
        final int PCID = intent.getIntExtra("PCID", -1);
        final int UID = intent.getIntExtra("UID" , -1);
        final int SID = intent.getIntExtra("SID" , -1);
        final int LID = intent.getIntExtra("LID" , -1);
        final String store_name = intent.getStringExtra("store_name");
        final String place_name = intent.getStringExtra("place_name");
        back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent i = new Intent(lcomment_detail.this,show_lcomment.class);
				i.putExtra("UID", UID);
				i.putExtra("LID", LID);
				i.putExtra("SID", SID);
				i.putExtra("place_name", place_name);
				i.putExtra("store_name", store_name);
				startActivity(i);*/
				finish();
			}
        });       
        
    	String url0 = "http://122.155.187.27:9876/check_if_owner.php";

        List<NameValuePair> params0 = new ArrayList<NameValuePair>();
        params0.add(new BasicNameValuePair("UID", Integer.toString(UID)));
        params0.add(new BasicNameValuePair("PCID", Integer.toString(PCID)));
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
        
        vote_up.setVisibility(View.GONE);
		vote_down.setVisibility(View.GONE);
        String url = "http://122.155.187.27:9876/lcomment_detail.php";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("PCID", Integer.toString(PCID)));
        //comment.setText(Integer.toString(PCID));
        try{
        	JSONArray data = new JSONArray(getHttpPost(url,params));
        	JSONObject c = data.getJSONObject(0);
        	user_name.setText(c.getString("username"));
        	agreed.setText(c.getString("agreed"));
        	disagreed.setText(c.getString("disagreed"));
        	comment.setText(c.getString("comment"));
        	try{
            	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(c.getString("image")).getContent());
            	image.setImageBitmap(bitmap);  
        	}
        	catch(JSONException e)
        	{
        		e.printStackTrace();
        	}
        	try{
        		Bitmap bitmap1 = BitmapFactory.decodeStream((InputStream)new URL(c.getString("1")).getContent());     		
        		pic1.setImageBitmap(bitmap1);
        	}catch(JSONException e)
        	{
        		e.printStackTrace();
        		pic1.setVisibility(View.INVISIBLE);
        	}
        	try{
        		Bitmap bitmap2 = BitmapFactory.decodeStream((InputStream)new URL(c.getString("2")).getContent());
        		pic2.setImageBitmap(bitmap2);
        	}catch(JSONException e)
        	{
        		e.printStackTrace();
        		pic2.setVisibility(View.INVISIBLE);
        	}
        	try{
        		Bitmap bitmap3 = BitmapFactory.decodeStream((InputStream)new URL(c.getString("3")).getContent());
        		pic3.setImageBitmap(bitmap3);
        	}catch(JSONException e)
        	{
        		e.printStackTrace();
        		pic3.setVisibility(View.INVISIBLE);
        	}
        	try{
        		Bitmap bitmap4 = BitmapFactory.decodeStream((InputStream)new URL(c.getString("4")).getContent());
        		pic4.setImageBitmap(bitmap4);
        	}catch(JSONException e)
        	{
        		e.printStackTrace();
        		pic4.setVisibility(View.INVISIBLE);
        	}
        	//comment.setText(Integer.toString(UID));
        }catch(JSONException e){
        	e.printStackTrace();
        	user_name.setText("Connection FAIL. Please check your internet connection!");
        } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        if(isLoggedIn())
        {
        	String url2 = "http://122.155.187.27:9876/check_vote.php";

            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
            params2.add(new BasicNameValuePair("UID", Integer.toString(UID)));
            params2.add(new BasicNameValuePair("PCID", Integer.toString(PCID)));
            try{
            	JSONArray data = new JSONArray(getHttpPost(url2,params2));
            	JSONObject c = data.getJSONObject(0);
            	vote = c.getString("vote");
            	if(vote.equals("1"))
            	{
            		current_vote_up.setVisibility(View.VISIBLE);
            		current_vote_down.setVisibility(View.GONE);
            		vote_down.setVisibility(View.VISIBLE);
            		vote_up.setVisibility(View.GONE); 
            		//user_name.setText("VOTED UP");
            	}
            	else if(vote.equals("-1"))
            	{
            		current_vote_up.setVisibility(View.GONE);
            		current_vote_down.setVisibility(View.VISIBLE);
            		vote_down.setVisibility(View.GONE);
            		vote_up.setVisibility(View.VISIBLE);
            		//user_name.setText("VOTED DOWN");
            	}
            }catch(JSONException e){
            	e.printStackTrace();
            	current_vote_up.setVisibility(View.GONE);
        		current_vote_down.setVisibility(View.GONE);
        		vote_down.setVisibility(View.VISIBLE);
        		vote_up.setVisibility(View.VISIBLE);
            }
        }
        else if(!isLoggedIn())
        {
        	current_vote_up.setVisibility(View.VISIBLE);
    		current_vote_down.setVisibility(View.VISIBLE);
    		vote_down.setVisibility(View.GONE);
    		vote_up.setVisibility(View.GONE);
    		//user_name.setText("NOT LOGGED IN");
        }
        
        final String vote2 = vote; 
        //user_name.setText(vote2);
        vote_up.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			String url3 = "http://122.155.187.27:9876/vote.php";
    			List<NameValuePair> params3 = new ArrayList<NameValuePair>();
    			params3.add(new BasicNameValuePair("UID", Integer.toString(UID)));
    			params3.add(new BasicNameValuePair("PCID", Integer.toString(PCID)));
    			params3.add(new BasicNameValuePair("vote", "1"));
    			params3.add(new BasicNameValuePair("voted",vote2));
    			 try{
    	            	JSONArray data = new JSONArray(getHttpPost(url3,params3));
    			 }catch(JSONException e){
    	            	e.printStackTrace();
    	         }
     			refresh();
    		}
        }); 
        
        vote_down.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			String url4 = "http://122.155.187.27:9876/vote.php";
    			List<NameValuePair> params4 = new ArrayList<NameValuePair>();
    			params4.add(new BasicNameValuePair("UID", Integer.toString(UID)));
    			params4.add(new BasicNameValuePair("PCID", Integer.toString(PCID)));
    			params4.add(new BasicNameValuePair("vote", "-1"));
    			params4.add(new BasicNameValuePair("voted",vote2));
    			try{
	            	JSONArray data = new JSONArray(getHttpPost(url4,params4));
			 }catch(JSONException e){
	            	e.printStackTrace();
	         }
 			refresh();
    		}
        });        
        pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                OpenPhoto(((BitmapDrawable)pic1.getDrawable()).getBitmap());
            }
        });
        pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	OpenPhoto(((BitmapDrawable)pic2.getDrawable()).getBitmap());
            }
        });
        pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	OpenPhoto(((BitmapDrawable)pic3.getDrawable()).getBitmap());               
            }
        });
        pic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               
            	OpenPhoto(((BitmapDrawable)pic4.getDrawable()).getBitmap());
            }
        });
    }
	
	
	public void refresh(){
		
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    finish();
	    
	    overridePendingTransition(0, 0);
	    startActivity(intent);
	}
	public static boolean isLoggedIn() {
        Session session = Session.getActiveSession();
        return (session != null && session.getAccessToken() != null && session.getAccessToken().length() > 1);
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
        
		CharSequence choices[] = new CharSequence[] {"Edit comment", "Edit images","Delete comment" ,"Cancel"};
		AlertDialog.Builder OptionDialog = new AlertDialog.Builder(this);
		OptionDialog.setTitle("Setting");
		OptionDialog.setItems(choices, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // the user clicked on colors[which]
		    	if(which == 0)
		    	{
		    		Intent i = new Intent(lcomment_detail.this,edit_comment_text.class);
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
		    		Intent i = new Intent(lcomment_detail.this,edit_comment_image.class);
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
		    		AlertDialog.Builder OptionDialog2 = new AlertDialog.Builder(lcomment_detail.this);
		    		OptionDialog2.setTitle("Are you sure that you want to delete your post?");
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
		    				        Toast.makeText(getApplicationContext(), "Your comment has been deleted!",
		    								Toast.LENGTH_LONG).show();
		    				        Intent i = new Intent(lcomment_detail.this,show_lcomment.class);
		    						/*i.putExtra("UID", UID);
		    						i.putExtra("LID", LID);
		    						i.putExtra("SID", SID);
		    						i.putExtra("place_name", place_name);
		    						i.putExtra("store_name", store_name);
		    						startActivity(i);*/
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
	public void OpenPhoto(Bitmap mBitmap){
		// custom dialog
					final Dialog dialog = new Dialog(this,R.style.NoTitleDialog);
					dialog.setContentView(R.layout.photo_dialog);
					//dialog.setTitle("Photo");
		 
					// set the custom dialog components - text, image and button
					ImageView image = (ImageView) dialog.findViewById(R.id.photoDialogimg);
//					image.setBackground(mBitmap);
					image.setImageBitmap(mBitmap);
					dialog.show();
		}
}
