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
import com.jab.storepedia.adater.Lcomment_adapter;
import com.jab.storepedia.model.Lcomment;
import com.jab.storepedia.model.Store;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;

public class store_detail extends Activity{
	boolean isImageFitToScreen;
	ImageView pic1,pic2,pic3,pic4,info_yes,info_no,gallery_no,gallery_yes,comments_no,comments_yes, map, setting_button,store_image_view;
	ImageButton create_comment,edit_comment;
	int SID,LID,UID;
	String store_name,place_name,result2;
	private Lcomment_adapter adapter;
	private List<Lcomment> lcommentList = new ArrayList<Lcomment>();
	String flag = "create";
	int PCID;
	Bitmap map_bitmap=null,bitmap=null,bitmap1=null,bitmap2=null,bitmap3=null,bitmap4=null;
	JSONArray data,data2,data3,data4,data5;
	ListView lcomment_list;
	TextView store_name_view, place_name_view,category_view,detail_info,address_info, contact_info,text,store_name_textview,write_comment_text;
	
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setTitle("Really Exit?")
	        .setMessage("Are you sure you want to exit?")
	        .setNegativeButton(android.R.string.no, null)
	        .setPositiveButton(android.R.string.yes, new OnClickListener() {

	            public void onClick(DialogInterface arg0, int arg1) {
	            	store_detail.super.onBackPressed();
	            }
	        }).create().show();
	}
	
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
        SID = intent.getIntExtra("SID", -1);
        LID = intent.getIntExtra("LID", -1);
        UID = intent.getIntExtra("UID", -1);
        //store_name = intent.getStringExtra("store_name");
        //place_name = intent.getStringExtra("place_name");
        
        
        ImageButton back = (ImageButton) findViewById(R.id.topbar).findViewById(R.id.back);
        
        MatchLayoutToID();
        
        new DownloadData().execute();
        
        create_comment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(store_detail.this,create_comment.class);;
                //i.putExtra("place_name", place_name);
                //i.putExtra("LID", LID);
				i.putExtra("UID", UID);
				i.putExtra("LID", LID);
				i.putExtra("SID", SID);
				//i.putExtra("place_name", place_name);
				//i.putExtra("store_name", store_name);
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
		        	Log.d("GGGGGG", "ERROR AT LINE: 141");
		        	e.printStackTrace();
		     }
				Intent i = new Intent(store_detail.this,lcomment_detail.class);;
                //i.putExtra("place_name", place_name);
                //i.putExtra("LID", LID);
				i.putExtra("UID", UID);
				i.putExtra("LID", LID);
				i.putExtra("SID", SID);
				i.putExtra("PCID", PCID);
				//i.putExtra("place_name", place_name);
				//i.putExtra("store_name", store_name);
				startActivity(i);
				finish();
			}
        });
        
        back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(store_detail.this,select_store.class);
				i.putExtra("UID", UID);
				i.putExtra("LID", LID);
				//i.putExtra("place_name", place_name);
				startActivity(i);
				finish();
			}
        });
        text = (TextView)findViewById(R.id.status); 
        lcomment_list = (ListView)findViewById(R.id.lcomment_list); 
        store_name_textview = (TextView)findViewById(R.id.textView1); 
        //ImageButton write_comment = (ImageButton) findViewById(R.id.create_comment_button);
        //ImageButton edit_comment = (ImageButton) findViewById(R.id.edit_comment_button);
        final TextView write_comment_text = (TextView)findViewById(R.id.textView2); 
        if(!isLoggedIn()){
        	create_comment.setVisibility(View.INVISIBLE);
        	edit_comment.setVisibility(View.INVISIBLE);
        }
        
        adapter = new Lcomment_adapter(store_detail.this,lcommentList);
        lcomment_list.setAdapter(adapter);
        
        
		//store_name_textview.setText("Comments For: "+store_name);	            
                
        lcomment_list.setOnItemClickListener(new OnItemClickListener(){
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id){
        		Intent intent = new Intent(store_detail.this, lcomment_detail.class);
        		int PCID = lcommentList.get(position).getPCID();
        		intent.putExtra("PCID", PCID);
        		intent.putExtra("SID", SID);
        		intent.putExtra("LID", LID);
        		//intent.putExtra("place_name", place_name);
        		intent.putExtra("UID", UID);
        		//intent.putExtra("store_name", store_name);
                startActivity(intent);
                finish();
        	}
        }); 
	
        
        
        pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	OpenPhoto(((BitmapDrawable)pic1.getDrawable()).getBitmap());
//                if(isImageFitToScreen) {
//                    isImageFitToScreen=false;  
//                    LayoutParams pic1layout = new RelativeLayout.LayoutParams(150,150);
//                    pic1layout.setMargins(120, 100, 0, 0);
//                
//                    
//                    pic2.setVisibility(View.VISIBLE);
//                    pic3.setVisibility(View.VISIBLE);
//                    pic4.setVisibility(View.VISIBLE);
//                    
//                    pic1.setLayoutParams(pic1layout);
//                    pic1.setAdjustViewBounds(true);
//                }else{
//                    isImageFitToScreen=true;
//                    
//                    pic2.setVisibility(View.INVISIBLE);
//                    pic3.setVisibility(View.INVISIBLE);
//                    pic4.setVisibility(View.INVISIBLE);
//                    
//                    //LayoutParams pic1layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                    LayoutParams pic1layout = new RelativeLayout.LayoutParams(700,700);
//                    pic1layout.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//                    pic1.setLayoutParams(pic1layout);
//                    pic1.setScaleType(ImageView.ScaleType.FIT_XY);
//                }
            }
        });
        pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	OpenPhoto(((BitmapDrawable)pic2.getDrawable()).getBitmap());
//                if(isImageFitToScreen) {
//                    isImageFitToScreen=false;  
//                    LayoutParams pic2layout = new RelativeLayout.LayoutParams(150,150);
//                    pic2layout.setMargins(0, 100, 120, 0);
//                    pic2layout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//                    
//                    pic1.setVisibility(View.VISIBLE);
//                    pic3.setVisibility(View.VISIBLE);
//                    pic4.setVisibility(View.VISIBLE);
//                    
//                    pic2.setLayoutParams(pic2layout);
//                    pic2.setAdjustViewBounds(true);
//                }else{
//                    isImageFitToScreen=true;
//                    
//                    pic1.setVisibility(View.INVISIBLE);
//                    pic3.setVisibility(View.INVISIBLE);
//                    pic4.setVisibility(View.INVISIBLE);
//                    
//                    //LayoutParams pic2layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                    LayoutParams pic2layout = new RelativeLayout.LayoutParams(700,700);
//                    pic2layout.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//                    pic2.setLayoutParams(pic2layout);
//                    pic2.setScaleType(ImageView.ScaleType.FIT_XY);
//                }
            }
        });
        pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	OpenPhoto(((BitmapDrawable)pic3.getDrawable()).getBitmap());
//                if(isImageFitToScreen) {
//                    isImageFitToScreen=false;  
//                    LayoutParams pic3layout = new RelativeLayout.LayoutParams(150,150);
//                    pic3layout.addRule(RelativeLayout.ALIGN_LEFT, R.id.pic1);
//                    pic3layout.addRule(RelativeLayout.BELOW, R.id.pic1);
//                    
//                    pic2.setVisibility(View.VISIBLE);
//                    pic1.setVisibility(View.VISIBLE);
//                    pic4.setVisibility(View.VISIBLE);
//                    
//                    pic3.setLayoutParams(pic3layout);
//                    pic3.setAdjustViewBounds(true);
//                }else{
//                    isImageFitToScreen=true;
//                    
//                    pic2.setVisibility(View.INVISIBLE);
//                    pic1.setVisibility(View.INVISIBLE);
//                    pic4.setVisibility(View.INVISIBLE);
//                    
//                    //LayoutParams pic3layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                    LayoutParams pic3layout = new RelativeLayout.LayoutParams(700,700);
//                    pic3layout.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//                    pic3.setLayoutParams(pic3layout);
//                    pic3.setScaleType(ImageView.ScaleType.FIT_XY);
//                }
            }
        });
        pic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(isImageFitToScreen) {
//                    isImageFitToScreen=false;  
//                    LayoutParams pic4layout = new RelativeLayout.LayoutParams(150,150);
//                    pic4layout.addRule(RelativeLayout.BELOW, R.id.pic2);
//                    pic4layout.addRule(RelativeLayout.ALIGN_RIGHT, R.id.pic2);
//                    
//                    pic2.setVisibility(View.VISIBLE);
//                    pic3.setVisibility(View.VISIBLE);
//                    pic1.setVisibility(View.VISIBLE);
//                    
//                    pic4.setLayoutParams(pic4layout);
//                    pic4.setAdjustViewBounds(true);
//                }else{
//                    isImageFitToScreen=true;
//                    
//
//                    pic2.setVisibility(View.INVISIBLE);
//                    pic3.setVisibility(View.INVISIBLE);
//                    pic1.setVisibility(View.INVISIBLE);
//                    
//                    //LayoutParams pic4layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                    LayoutParams pic4layout = new RelativeLayout.LayoutParams(700,700);
//                    pic4layout.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//                    pic4.setLayoutParams(pic4layout);
//                    pic4.setScaleType(ImageView.ScaleType.FIT_XY);
//                }
            	
            	OpenPhoto(((BitmapDrawable)pic4.getDrawable()).getBitmap());
            }
        });
        
        gallery_no.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			RelativeLayout gallery_layout = (RelativeLayout) findViewById(R.id.gallery_layout);
    			RelativeLayout info_layout = (RelativeLayout) findViewById(R.id.info_layout);
    			RelativeLayout comments_layout = (RelativeLayout) findViewById(R.id.comments_layout);
    			info_layout.setVisibility(View.INVISIBLE);
    			comments_layout.setVisibility(View.INVISIBLE);
    			gallery_layout.setVisibility(View.VISIBLE);
    			
    			info_no.setVisibility(View.VISIBLE);
    			gallery_no.setVisibility(View.INVISIBLE);
    			comments_no.setVisibility(View.VISIBLE);
    			
    			info_yes.setVisibility(View.INVISIBLE);
    			comments_yes.setVisibility(View.INVISIBLE);
    			
    			
    			gallery_yes.setVisibility(View.VISIBLE);
    		}
        });        
        
        info_no.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			RelativeLayout gallery_layout = (RelativeLayout) findViewById(R.id.gallery_layout);
    			RelativeLayout info_layout = (RelativeLayout) findViewById(R.id.info_layout);
    			RelativeLayout comments_layout = (RelativeLayout) findViewById(R.id.comments_layout);
    			info_layout.setVisibility(View.VISIBLE);
    			comments_layout.setVisibility(View.INVISIBLE);
    			gallery_layout.setVisibility(View.INVISIBLE);
    			
    			info_no.setVisibility(View.INVISIBLE);
    			gallery_no.setVisibility(View.VISIBLE);
    			comments_no.setVisibility(View.VISIBLE);
    			
    			gallery_yes.setVisibility(View.INVISIBLE);
    			comments_yes.setVisibility(View.INVISIBLE);
    			
    			info_yes.setVisibility(View.VISIBLE);
    		}
        });
        comments_no.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			RelativeLayout gallery_layout = (RelativeLayout) findViewById(R.id.gallery_layout);
    			RelativeLayout info_layout = (RelativeLayout) findViewById(R.id.info_layout);
    			RelativeLayout comments_layout = (RelativeLayout) findViewById(R.id.comments_layout);
    			info_layout.setVisibility(View.INVISIBLE);
    			comments_layout.setVisibility(View.VISIBLE);
    			gallery_layout.setVisibility(View.INVISIBLE);
    			
    			info_no.setVisibility(View.VISIBLE);
    			gallery_no.setVisibility(View.VISIBLE);
    			comments_no.setVisibility(View.INVISIBLE);
    			
    			gallery_yes.setVisibility(View.INVISIBLE);
    			info_yes.setVisibility(View.INVISIBLE);
    			
    			comments_yes.setVisibility(View.VISIBLE);
    		}
        });
        
        map.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
//    			TextView store_name_view = (TextView) findViewById(R.id.store_name);
//    			Intent i = new Intent(store_detail.this,store_map.class);
//        		i.putExtra("SID", SID);
//        		i.putExtra("UID", UID);
//        		i.putExtra("LID", LID);
//        		i.putExtra("store_name",store_name_view.getText().toString());
//        		i.putExtra("place_name",place_name);
//    			startActivity(i);
    			//finish();
    			OpenPhoto(map_bitmap);
    		}
        });
}	
	
	private void MatchLayoutToID() {
		// TODO Auto-generated method stub
		pic1 = (ImageView) findViewById(R.id.pic1);
        pic2 = (ImageView) findViewById(R.id.pic2);
        pic3 = (ImageView) findViewById(R.id.pic3);
        pic4 = (ImageView) findViewById(R.id.pic4);
        setting_button = (ImageView) findViewById(R.id.setting_button);
        setting_button.setVisibility(View.INVISIBLE);
		store_image_view = (ImageView) findViewById(R.id.store_image);
		store_name_view = (TextView) findViewById(R.id.store_name);
		place_name_view = (TextView) findViewById(R.id.place_name); 
		category_view = (TextView) findViewById(R.id.category_text);
		detail_info = (TextView) findViewById(R.id.detail_info);
		address_info = (TextView) findViewById(R.id.address_info);
		contact_info = (TextView) findViewById(R.id.contact_info);
		//TextView score_view = (TextView) findViewById(R.id.score);
        
        gallery_no = (ImageView) findViewById(R.id.gallery_button_no);
        comments_no = (ImageView) findViewById(R.id.comment_button_no);
        info_no = (ImageView) findViewById(R.id.info_button_no);
        gallery_yes = (ImageView) findViewById(R.id.gallery_button_yes);
        comments_yes = (ImageView) findViewById(R.id.comment_button_yes);
        info_yes = (ImageView) findViewById(R.id.info_button_yes);
        map = (ImageView) findViewById(R.id.map_button);
        
		gallery_yes.setVisibility(View.INVISIBLE);
		comments_yes.setVisibility(View.INVISIBLE);
		map.setVisibility(View.INVISIBLE);
		
        create_comment = (ImageButton) findViewById(R.id.create_comment_button);
        edit_comment = (ImageButton) findViewById(R.id.edit_comment_button);
    	edit_comment.setVisibility(View.INVISIBLE);
	}

	public void setting(View view)
	{		        
		CharSequence choices[] = new CharSequence[] {"Edit name & detail", "Edit information","Edit category", "Edit images","Delete store" ,"Cancel"};
		AlertDialog.Builder OptionDialog = new AlertDialog.Builder(this);
		OptionDialog.setTitle("Setting");
		OptionDialog.setItems(choices, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {		    	
		    	Intent intent = getIntent();
		        UID = intent.getIntExtra("UID" , -1);
		        SID = intent.getIntExtra("SID" , -1);
		        LID = intent.getIntExtra("LID" , -1);
		        //store_name = intent.getStringExtra("store_name");
		        //place_name = intent.getStringExtra("place_name");
		    	
		        // the user clicked on colors[which]
		        
		        // select Edit name & detail
		    	if(which == 0)
		    	{
		    		Intent i = new Intent(store_detail.this,create_store.class);
		    		
		    		String url = "http://122.155.187.27:9876/store_info.php";
		            List<NameValuePair> params = new ArrayList<NameValuePair>();
		            params.add(new BasicNameValuePair("SID", Integer.toString(SID)));
		            try{
		            	TextView store_name_view = (TextView) findViewById(R.id.store_name);
		            	JSONArray data = new JSONArray(getHttpPost(url,params));
		            	JSONObject c = data.getJSONObject(0);	  
		            	i.putExtra("store_detail", c.getString("detail"));    
		            	i.putExtra("UID", UID);
		            	i.putExtra("LID", LID);
						i.putExtra("SID", SID);
						//i.putExtra("place_name", place_name);
						i.putExtra("store_name", store_name_view.getText().toString());
						startActivity(i);
						finish();
		            	}catch(JSONException e)
		            	{
		            		Log.d("GGGGGG", "ERROR AT LINE: 481");
		            		e.printStackTrace();
		            	}		    											
		    	}
		    	//select Edit information
		    	else if(which == 1)
		    	{	  
		    		Intent i = new Intent(store_detail.this,create_store2.class);
		    		
		    		String url = "http://122.155.187.27:9876/store_info.php";
		            List<NameValuePair> params = new ArrayList<NameValuePair>();
		            params.add(new BasicNameValuePair("SID", Integer.toString(SID)));
		            try{
		            	TextView store_name_view = (TextView) findViewById(R.id.store_name);
		            	JSONArray data = new JSONArray(getHttpPost(url,params));
		            	JSONObject c = data.getJSONObject(0);	  
		            	i.putExtra("store_address", c.getString("address"));    
		            	i.putExtra("store_contact", c.getString("contact"));
		            	i.putExtra("UID", UID);
		            	i.putExtra("LID", LID);
						i.putExtra("SID", SID);
						//i.putExtra("place_name", place_name);
						i.putExtra("store_name", store_name_view.getText().toString());
						startActivity(i);
						finish();
		            	}catch(JSONException e)
		            	{
		            		Log.d("GGGGGG", "ERROR AT LINE:508");
		            		e.printStackTrace();
		            	}
		    	}
		    	//select edit category
		    	else if(which == 2)
		    	{
		    		CharSequence choices[] = new CharSequence[] {"Food", "Books", "Clothings", "Electronics", "Entertainments", "Health", "Others", "Cancel"};
		    		AlertDialog.Builder OptionDialog2 = new AlertDialog.Builder(store_detail.this);
		    		OptionDialog2.setTitle("Select Category");    		
		    		OptionDialog2.setItems(choices, new DialogInterface.OnClickListener() {
		    			 @Override
		    			    public void onClick(DialogInterface dialog, int which) {
		    			        // the user clicked on colors[which]
		    				 String cat = "";
		    			    	if(which == 0)
		    			    	{
		    			    	    cat = "Food";
		    			    	}
		    			    	else if(which == 1)
		    			    	{
		    			    		cat = "Books";
		    			    	}
		    			    	else if(which == 2)
		    			    	{
		    			    		cat = "Clothings";
		    			    	}
		    			    	else if(which == 3)
		    			    	{
		    			    		cat = "Electronics";
		    			    	}
		    			    	else if(which == 4)
		    			    	{
		    			    		cat = "Entertainments";
		    			    	}
		    			    	else if(which == 5)
		    			    	{
		    			    		cat = "Health";
		    			    	}
		    			    	else if(which == 6)
		    			    	{
		    			    		cat = "Others";
		    			    	}
		    			    	else if(which == 7)
		    			    	{
		    			    	  dialog.dismiss();
		    			    	}
		    			    	String url1 = "http://122.155.187.27:9876/edit_store_cat.php";
	    						List<NameValuePair> params = new ArrayList<NameValuePair>();
	    				        params.add(new BasicNameValuePair("SID", Integer.toString(SID)));
	    				        params.add(new BasicNameValuePair("store_cat", cat));
	    				        try{
	    				        	JSONArray data = new JSONArray(getHttpPost(url1,params));
	    				            JSONObject c = data.getJSONObject(0);
	    				        }catch(JSONException e){
	    				        	Log.d("GGGGGG", "ERROR AT LINE: 563");
	    				        	e.printStackTrace();
	    				        }
	    				        refresh();
		    		}
		    			});		
		    		OptionDialog2.show();
		    		}
		    	//select edit images
		    	else if(which == 3)
		    	{	  
		    		TextView store_name_view = (TextView) findViewById(R.id.store_name);
                    Intent i = new Intent(store_detail.this,create_store3.class);
		            i.putExtra("UID", UID);
		            i.putExtra("LID", LID);
					i.putExtra("SID", SID);
					//i.putExtra("place_name", place_name);
					i.putExtra("store_name", store_name_view.getText().toString());
					startActivity(i);
					finish();

		    	}
		    	//select delete store
		    	else if(which == 4)
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
		    			    		String url1 = "http://122.155.187.27:9876/delete_store.php";
		    						List<NameValuePair> params = new ArrayList<NameValuePair>();
		    				        params.add(new BasicNameValuePair("SID", Integer.toString(SID)));
		    				        try{
		    				        	JSONArray data = new JSONArray(getHttpPost(url1,params));
		    				            JSONObject c = data.getJSONObject(0);
		    				        }catch(JSONException e){
		    				        	Log.d("GGGGGG", "ERROR AT LINE: 604");
		    				        	e.printStackTrace();
		    				     }
		    				        Intent i = new Intent(store_detail.this,select_store.class);
		    						i.putExtra("UID", UID);
		    						i.putExtra("LID", LID);
		    						//i.putExtra("place_name", place_name);
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
		    	//select cancel
		    	else if(which == 5)
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
	
	@SuppressLint("NewApi")
	public void OpenPhoto(Bitmap mBitmap){
	// custom dialog
				final Dialog dialog = new Dialog(this,R.style.NoTitleDialog);
				dialog.setContentView(R.layout.photo_dialog);
				//dialog.setTitle("Photo");
	 
				// set the custom dialog components - text, image and button
				ImageView image = (ImageView) dialog.findViewById(R.id.photoDialogimg);
//				image.setBackground(mBitmap);
				image.setImageBitmap(mBitmap);
				dialog.show();
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
private class DownloadData extends AsyncTask<Void, Void, Void>{
	Bitmap bitmap;
	JSONArray data;
	ProgressDialog dialog;
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog = ProgressDialog.show(store_detail.this, "", "Loading. Please wait...", true);
		dialog.setCancelable(false);
	}
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		
		String url = "http://122.155.187.27:9876/lcomment_list.php";
    	lcommentList.clear();
		List<NameValuePair> params1 = new ArrayList<NameValuePair>();
        params1.add(new BasicNameValuePair("SID", Integer.toString(SID)));
        try{
        	data = new JSONArray(getHttpPost(url,params1));
        }catch(JSONException e){
        	Log.d("GGGGGG", "ERROR AT LINE: 713");
        	e.printStackTrace();
        }
        
        
        String url2 = "http://122.155.187.27:9876/check_if_comment.php";
        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
        params2.add(new BasicNameValuePair("SID", Integer.toString(SID)));
        params2.add(new BasicNameValuePair("UID", Integer.toString(UID)));
        try{
        	data2 = new JSONArray(getHttpPost(url2,params2));
        	for(int i = 0; i < data2.length(); i++){
            	JSONObject c = data2.getJSONObject(i);
            	flag = "edit";
        	}
        }catch(JSONException e){
        	Log.d("GGGGGG", "ERROR AT LINE: 729");
        	e.printStackTrace();  
        }      
        
        
        String url0 = "http://122.155.187.27:9876/check_if_owner_store.php";

        List<NameValuePair> params0 = new ArrayList<NameValuePair>();
        params0.add(new BasicNameValuePair("UID", Integer.toString(UID)));
        params0.add(new BasicNameValuePair("SID", Integer.toString(SID)));
        //comment.setText("UID: "+UID+" PCID: "+PCID);
        try{
        	data3 = new JSONArray(getHttpPost(url0,params0));
        	JSONObject c = data3.getJSONObject(0);
        	result2 = c.getString("result");
        	//comment.setText(result);
        	
        }catch(JSONException e)
    	{
        	Log.d("GGGGGG", "ERROR AT LINE: 748");
    		e.printStackTrace();
    	}       
        
        String url3 = "http://122.155.187.27:9876/store_info.php";
        List<NameValuePair> params3 = new ArrayList<NameValuePair>();
        params3.add(new BasicNameValuePair("SID", Integer.toString(SID)));
        try{
        	data4 = new JSONArray(getHttpPost(url3,params3));
        	        	
        	}catch(JSONException e)
        	{
        		Log.d("GGGGGG", "ERROR AT LINE: 760");
        		e.printStackTrace();
        	}
        
        
        String url4 = "http://122.155.187.27:9876/store_detail.php";

        List<NameValuePair> params4 = new ArrayList<NameValuePair>();
        params4.add(new BasicNameValuePair("SID", Integer.toString(SID)));
        Log.d("GGGGGG","SID: "+Integer.toString(SID));
        //store_name_view.setText(store_name);
        try{
        	data5 = new JSONArray(getHttpPost(url4,params4));
            JSONObject c = data5.getJSONObject(0);
        	try{
        	    bitmap = BitmapFactory.decodeStream((InputStream)new URL(c.getString("Image")).getContent()); 
        	}catch(JSONException e)
        	{
        		Log.d("GGGGGG", "ERROR AT LINE: 777");
        		e.printStackTrace();        		
        	}
        	try{
        		bitmap1 = BitmapFactory.decodeStream((InputStream)new URL(c.getString("1")).getContent());     		
        	}catch(JSONException e)
        	{
        		Log.d("GGGGGG", "ERROR AT LINE: 784");
        		e.printStackTrace();
        	}
        	try{
        		bitmap2 = BitmapFactory.decodeStream((InputStream)new URL(c.getString("2")).getContent());
        	}catch(JSONException e)
        	{
        		Log.d("GGGGGG", "ERROR AT LINE: 791");
        		e.printStackTrace();
        	}
        	try{
        		bitmap3 = BitmapFactory.decodeStream((InputStream)new URL(c.getString("3")).getContent());
        	}catch(JSONException e)
        	{
        		Log.d("GGGGGG", "ERROR AT LINE: 798");
        		e.printStackTrace();
        	}
        	try{
        		bitmap4 = BitmapFactory.decodeStream((InputStream)new URL(c.getString("4")).getContent());
        	}catch(JSONException e)
        	{
        		Log.d("GGGGGG", "ERROR AT LINE: 805");
        		e.printStackTrace();
        	}
        	try{
        		map_bitmap = BitmapFactory.decodeStream((InputStream)new URL(c.getString("map")).getContent());
        		Log.d("GGGGGG", map_bitmap.toString());
        	}catch(JSONException e)
        	{
        		Log.d("GGGGGG", "ERROR AT LINE: 812");
        		e.printStackTrace();
        		
        	}
        }catch(JSONException e){
        	e.printStackTrace();
        	Log.d("GGGGGG", "ERROR AT LINE: 818");
        } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		try{
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
            	}
			adapter.notifyDataSetChanged();
			}catch(JSONException e){
				Log.d("GGGGGG", "ERROR AT LINE: 849");
                	e.printStackTrace();               	
                }        
		
		if(flag=="edit")
		{
        	create_comment.setVisibility(View.INVISIBLE);
        	edit_comment.setVisibility(View.VISIBLE);
		}
		
		if(result2.equals("yes"))
    	{
    		setting_button.setVisibility(View.VISIBLE);
    	}
		
		try{
		JSONObject c = data4.getJSONObject(0);	  
    	detail_info.setText(c.getString("detail"));
    	address_info.setText(c.getString("address"));
    	contact_info.setText(c.getString("contact"));  
		}catch(JSONException e){
			Log.d("GGGGGG", "ERROR AT LINE: 870");
        	e.printStackTrace();               	
        } 
		
		try{
            JSONObject c = data5.getJSONObject(0);
        	store_name_view.setText(c.getString("Name"));
        	store_name = c.getString("Name");
        	place_name_view.setText(c.getString("Location_Name"));
        	category_view.setText(c.getString("Category"));
        	//score_view.setText("Rating: "+c.getString("Rating"));
        	//score_view.setText(c.getString("Image"));
        	if(bitmap!=null)  {    	    
        	    store_image_view.setImageBitmap(bitmap); 
        	}else
        	{
        		store_image_view.setVisibility(View.INVISIBLE);
        	}
        	if(bitmap1!=null){    		
        		pic1.setImageBitmap(bitmap1);
        	}else
        	{
        		pic1.setVisibility(View.INVISIBLE);
        	}
        	if(bitmap2!=null){
        		pic2.setImageBitmap(bitmap2);
        	}else
        	{
        		pic2.setVisibility(View.INVISIBLE);
        	}
        	if(bitmap3!=null){
        		pic3.setImageBitmap(bitmap3);
        	}else
        	{
        		pic3.setVisibility(View.INVISIBLE);
        	}
        	if(bitmap4!=null){
        		pic4.setImageBitmap(bitmap4);
        	}else
        	{
        		pic4.setVisibility(View.INVISIBLE);
        	}
        	if(map_bitmap!=null){    
        		//Log.d("GGGGGG", map_bitmap.toString());
        	    map.setVisibility(View.VISIBLE);
        	}
        }catch(JSONException e){
        	Log.d("GGGGGG", "ERROR AT LINE: 916");
        	e.printStackTrace();
        } 
		
		dialog.cancel();
	}
}

}