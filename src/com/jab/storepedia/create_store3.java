package com.jab.storepedia;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint("NewApi")
public class create_store3 extends Activity {
	ProgressDialog prgDialog;
	String encodedString1, encodedString2, encodedString3, encodedString4,encodedString5,encodedString6;
	RequestParams params = new RequestParams();
	String cat, imgPath1, imgPath2, imgPath3, imgPath4, imgPath5, imgPath6, fileName, place_name, store_name,store_detail,store_address,store_contact;
	Bitmap bitmap;	
	RadioButton food, books, clothings, electronics, entertainments, health, others;
	int imgFlag = 0,LID,UID, SID;
	
	private static int RESULT_LOAD_IMG = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_store3);
		prgDialog = new ProgressDialog(this);
		// Set Cancelable as False
		prgDialog.setCancelable(false);
		
		SID = -1;
		Intent intent = getIntent();
		LID = intent.getIntExtra("LID" , -1);
		SID = intent.getIntExtra("SID" , -1);
		UID = intent.getIntExtra("UID" , -1);
		store_name = intent.getStringExtra("store_name");
		place_name = intent.getStringExtra("place_name");
		store_detail = intent.getStringExtra("store_detail");
		store_address = intent.getStringExtra("store_address");
		store_contact = intent.getStringExtra("store_contact");
		//final TextView choose_store_image = (TextView) findViewById(R.id.textView1);
		ImageButton back = (ImageButton) findViewById(R.id.topbar).findViewById(R.id.back);
				
		cat = "Food";
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.category);        
	    radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	    {
	        @Override
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	            // checkedId is the RadioButton selected
	        	RadioButton rb=(RadioButton)findViewById(checkedId);	        	
	        	cat = rb.getText().toString();
	        	//choose_store_image.setText(cat);
	        	
	        }
	    });
		
		
		//debug_text.setText("SID = "+SID + ";  UID = " +UID + "; LID = " + LID +";");		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SID = -1;
				Intent intent = getIntent();
				LID = intent.getIntExtra("LID" , -1);
				SID = intent.getIntExtra("SID" , -1);
				UID = intent.getIntExtra("UID" , -1);
				store_name = intent.getStringExtra("store_name");
				place_name = intent.getStringExtra("place_name");
				if(SID!=-1)
				{
					Intent i = new Intent(create_store3.this,store_detail.class);
					i.putExtra("SID", SID);
					i.putExtra("UID", UID);
				    i.putExtra("LID", LID);
				    i.putExtra("place_name", place_name);
				    i.putExtra("store_name", store_name);
				    startActivity(i);
	                finish();
				}
				else
				{
				    Intent i = new Intent(create_store3.this,create_store2.class);
				    i.putExtra("UID", UID);
				    i.putExtra("LID", LID);
				    i.putExtra("place_name", place_name);
				    i.putExtra("store_name", store_name);
				    i.putExtra("store_detail", store_detail);
				    i.putExtra("store_address", store_address);
				    i.putExtra("store_contact", store_contact);
	                startActivity(i);
	                finish();
				}
			}
	    });
	}

	
	public void loadImagefromGallery1(View view) {
		CharSequence choices[] = new CharSequence[] {"Upload Image", "Delete Image", "Cancel"};
		AlertDialog.Builder OptionDialog = new AlertDialog.Builder(this);
		//builder.setTitle("Choose an action");
		OptionDialog.setItems(choices, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // the user clicked on colors[which]
		    	if(which == 0)
		    	{
		    		// Create intent to Open Image applications like Gallery, Google Photos
		    		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
		    				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		    		// Start the Intent
		    		imgFlag = 1;
		    		startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
		    	}
		    	else if(which == 1)
		    	{
		    		imgPath1 = null;
		    		params.remove("image1");
				    ImageView imgView = (ImageView) findViewById(R.id.pic1);
				    // Set the Image in ImageView
				    Resources res = getResources();
				    imgView.setImageDrawable(res.getDrawable(R.drawable.insert_image_icon));	    		
		    	}
		    	else if(which == 2)
		    	{
		    		 dialog.dismiss();
		    	}
		    }
		});		
		OptionDialog.show();
	}
	public void loadImagefromGallery2(View view) {
		CharSequence choices[] = new CharSequence[] {"Upload Image", "Delete Image", "Cancel"};
		AlertDialog.Builder OptionDialog = new AlertDialog.Builder(this);
		OptionDialog.setItems(choices, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // the user clicked on colors[which]
		    	if(which == 0)
		    	{
		          // Create intent to Open Image applications like Gallery, Google Photos
		          Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				  android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		          // Start the Intent
		          imgFlag = 2;
		          startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
		    	}
		    	else if(which == 1)
		    	{
		    		imgPath2 = null;
		    		params.remove("image2");
				    ImageView imgView = (ImageView) findViewById(R.id.pic2);
				    // Set the Image in ImageView
				    Resources res = getResources();
				    imgView.setImageDrawable(res.getDrawable(R.drawable.insert_image_icon));	    		
		    	}
		    	else if(which == 2)
		    	{
		    		 dialog.dismiss();
		    	}
		    }
		});		
		OptionDialog.show();
	}
	public void loadImagefromGallery3(View view) {
		CharSequence choices[] = new CharSequence[] {"Upload Image", "Delete Image", "Cancel"};
		AlertDialog.Builder OptionDialog = new AlertDialog.Builder(this);
		OptionDialog.setItems(choices, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // the user clicked on colors[which]
		    	if(which == 0)
		    	{
		          // Create intent to Open Image applications like Gallery, Google Photos
		          Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				  android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		          // Start the Intent
		          imgFlag = 3;
		          startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
		    	}
		    	else if(which == 1)
		    	{
		    		imgPath3 = null;
		    		params.remove("image3");
				    ImageView imgView = (ImageView) findViewById(R.id.pic3);
				    // Set the Image in ImageView
				    Resources res = getResources();
				    imgView.setImageDrawable(res.getDrawable(R.drawable.insert_image_icon));	    		
		    	}
		    	else if(which == 2)
		    	{
		    		 dialog.dismiss();
		    	}
		    }
		});		
		OptionDialog.show();
	}		   
	public void loadImagefromGallery4(View view) {
		CharSequence choices[] = new CharSequence[] {"Upload Image", "Delete Image", "Cancel"};
		AlertDialog.Builder OptionDialog = new AlertDialog.Builder(this);
		OptionDialog.setItems(choices, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // the user clicked on colors[which]
		    	if(which == 0)
		    	{
		          // Create intent to Open Image applications like Gallery, Google Photos
		          Intent galleryIntent = new Intent(Intent.ACTION_PICK,
			      android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		          // Start the Intent
		          imgFlag = 4;
		          startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
		    	}
		    	else if(which == 1)
		    	{
		    		imgPath4 = null;
		    		params.remove("image4");
				    ImageView imgView = (ImageView) findViewById(R.id.pic4);
				    // Set the Image in ImageView
				    Resources res = getResources();
				    imgView.setImageDrawable(res.getDrawable(R.drawable.insert_image_icon));	    		
		    	}
		    	else if(which == 2)
		    	{
		    		 dialog.dismiss();
		    	}
		    }
		});		
		OptionDialog.show();
	}		   
	public void loadImagefromGallery5(View view) {
		CharSequence choices[] = new CharSequence[] {"Upload Image", "Delete Image", "Cancel"};
		AlertDialog.Builder OptionDialog = new AlertDialog.Builder(this);
		OptionDialog.setItems(choices, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // the user clicked on colors[which]
		    	if(which == 0)
		    	{
		          // Create intent to Open Image applications like Gallery, Google Photos
		          Intent galleryIntent = new Intent(Intent.ACTION_PICK,
			      android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		          // Start the Intent
		          imgFlag = 5;
		          startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
		    	}
		    	else if(which == 1)
		    	{
		    		imgPath5 = null;
		    		params.remove("image5");
				    ImageView imgView = (ImageView) findViewById(R.id.pic5);
				    // Set the Image in ImageView
				    Resources res = getResources();
				    imgView.setImageDrawable(res.getDrawable(R.drawable.insert_image_icon));	    		
		    	}
		    	else if(which == 2)
		    	{
		    		 dialog.dismiss();
		    	}
		    }
		});		
		OptionDialog.show();
	}		   
	public void loadImagefromGallery6(View view) {
		CharSequence choices[] = new CharSequence[] {"Upload Image", "Delete Image", "Cancel"};
		AlertDialog.Builder OptionDialog = new AlertDialog.Builder(this);
		OptionDialog.setItems(choices, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // the user clicked on colors[which]
		    	if(which == 0)
		    	{
		          // Create intent to Open Image applications like Gallery, Google Photos
		          Intent galleryIntent = new Intent(Intent.ACTION_PICK,
			      android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		          // Start the Intent
		          imgFlag = 6;
		          startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
		    	}
		    	else if(which == 1)
		    	{
		    		imgPath6 = null;
		    		params.remove("image6");
				    ImageView imgView = (ImageView) findViewById(R.id.pic6);
				    // Set the Image in ImageView
				    Resources res = getResources();
				    imgView.setImageDrawable(res.getDrawable(R.drawable.insert_image_icon));	    		
		    	}
		    	else if(which == 2)
		    	{
		    		 dialog.dismiss();
		    	}
		    }
		});		
		OptionDialog.show();
	}		   
	// When Image is selected from Gallery
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			// When an Image is picked
			if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
					&& null != data) {
				// Get the Image from data

				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				// Get the cursor
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				// Move to first row
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);				
				if(imgFlag == 1){
					imgPath1 = cursor.getString(columnIndex);
				    ImageView imgView = (ImageView) findViewById(R.id.pic1);
				    // Set the Image in ImageView
				    imgView.setImageBitmap(BitmapFactory
						    .decodeFile(imgPath1));
				    // Get the Image's file name
				    //String fileNameSegments[] = imgPath1.split("/");
				    //fileName = fileNameSegments[fileNameSegments.length - 1];
				    // Put file name in Async Http Post Param which will used in Php web app
				//params.put("filename1", fileName);
				    String filenameArray[] = imgPath1.split("\\.");
				    String extension = filenameArray[filenameArray.length-1];
				params.put("filename1", extension);
				}
				else if(imgFlag == 2){
					imgPath2 = cursor.getString(columnIndex);
					ImageView imgView = (ImageView) findViewById(R.id.pic2);
					// Set the Image in ImageView
					imgView.setImageBitmap(BitmapFactory
							.decodeFile(imgPath2));
					// Get the Image's file name
					//String fileNameSegments[] = imgPath2.split("/");
					//fileName = fileNameSegments[fileNameSegments.length - 1];
					// Put file name in Async Http Post Param which will used in Php web app
					//params.put("filename2", fileName);
					String filenameArray[] = imgPath2.split("\\.");
				    String extension = filenameArray[filenameArray.length-1];
				params.put("filename2", extension);
					}
				else if(imgFlag == 3){
					imgPath3 = cursor.getString(columnIndex);
					ImageView imgView = (ImageView) findViewById(R.id.pic3);
					// Set the Image in ImageView
					imgView.setImageBitmap(BitmapFactory
							.decodeFile(imgPath3));
					// Get the Image's file name
					//String fileNameSegments[] = imgPath3.split("/");
					//fileName = fileNameSegments[fileNameSegments.length - 1];
					// Put file name in Async Http Post Param which will used in Php web app
					//params.put("filename3", fileName);
					String filenameArray[] = imgPath3.split("\\.");
				    String extension = filenameArray[filenameArray.length-1];
				params.put("filename3", extension);
					}
				else if(imgFlag == 4){
					imgPath4 = cursor.getString(columnIndex);
					ImageView imgView = (ImageView) findViewById(R.id.pic4);
					// Set the Image in ImageView
					imgView.setImageBitmap(BitmapFactory
							.decodeFile(imgPath4));
					// Get the Image's file name
					//String fileNameSegments[] = imgPath4.split("/");
					//fileName = fileNameSegments[fileNameSegments.length - 1];
					// Put file name in Async Http Post Param which will used in Php web app
					//params.put("filename4", fileName);
					String filenameArray[] = imgPath4.split("\\.");
				    String extension = filenameArray[filenameArray.length-1];
				params.put("filename4", extension);
					}
				else if(imgFlag == 5){
					imgPath5 = cursor.getString(columnIndex);
					ImageView imgView = (ImageView) findViewById(R.id.pic5);
					// Set the Image in ImageView
					imgView.setImageBitmap(BitmapFactory
							.decodeFile(imgPath5));
					// Get the Image's file name
					//String fileNameSegments[] = imgPath5.split("/");
					//fileName = fileNameSegments[fileNameSegments.length - 1];
					// Put file name in Async Http Post Param which will used in Php web app
					//params.put("filename5", fileName);
					String filenameArray[] = imgPath5.split("\\.");
				    String extension = filenameArray[filenameArray.length-1];
				params.put("filename5", extension);
					}
				else if(imgFlag == 6){
					imgPath6 = cursor.getString(columnIndex);
					ImageView imgView = (ImageView) findViewById(R.id.pic6);
					// Set the Image in ImageView
					imgView.setImageBitmap(BitmapFactory
							.decodeFile(imgPath6));
					// Get the Image's file name
					//String fileNameSegments[] = imgPath6.split("/");
					//fileName = fileNameSegments[fileNameSegments.length - 1];
					// Put file name in Async Http Post Param which will used in Php web app
					//params.put("filename6", fileName);
					String filenameArray[] = imgPath6.split("\\.");
				    String extension = filenameArray[filenameArray.length-1];
				params.put("filename6", extension);
					}
				cursor.close();
			} else {
				Toast.makeText(this, "You haven't picked Image",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
					.show();
			e.printStackTrace();
		}

	}
	
	// When Upload button is clicked
	public void uploadImage(View v) {
		// When Image is selected from Gallery		
			prgDialog.setMessage("Uploading data");
			prgDialog.show();
			// Convert image to String using Base64
			encodeImagetoString();
		// When Image is not selected from Gallery		 
	}

	// AsyncTask - To convert Image to String
	public void encodeImagetoString() {
		new AsyncTask<Void, Void, String>() {

			protected void onPreExecute() {

			};

			@Override
			protected String doInBackground(Void... params) {
				BitmapFactory.Options options = null;
				options = new BitmapFactory.Options();
				options.inSampleSize = 3;
				if (imgPath1 != null && !imgPath1.isEmpty())
				{
				bitmap = BitmapFactory.decodeFile(imgPath1,
						options);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				// Must compress the Image to reduce image size to make upload easy
				bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream); 
				byte[] byte_arr = stream.toByteArray();
				// Encode Image to String
				encodedString1 = Base64.encodeToString(byte_arr, 0);
				}
				if (imgPath2 != null && !imgPath2.isEmpty())
				{
				bitmap = BitmapFactory.decodeFile(imgPath2,
						options);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				// Must compress the Image to reduce image size to make upload easy
				bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream); 
				byte[] byte_arr = stream.toByteArray();
				// Encode Image to String
				encodedString2 = Base64.encodeToString(byte_arr, 0);
				}
				if (imgPath3 != null && !imgPath3.isEmpty())
				{
				bitmap = BitmapFactory.decodeFile(imgPath3,
						options);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				// Must compress the Image to reduce image size to make upload easy
				bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream); 
				byte[] byte_arr = stream.toByteArray();
				// Encode Image to String
				encodedString3 = Base64.encodeToString(byte_arr, 0);
				}
				if (imgPath4 != null && !imgPath4.isEmpty())
				{
				bitmap = BitmapFactory.decodeFile(imgPath4,
						options);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				// Must compress the Image to reduce image size to make upload easy
				bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream); 
				byte[] byte_arr = stream.toByteArray();
				// Encode Image to String
				encodedString4 = Base64.encodeToString(byte_arr, 0);
				}
				if (imgPath5 != null && !imgPath5.isEmpty())
				{
				bitmap = BitmapFactory.decodeFile(imgPath5,
						options);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				// Must compress the Image to reduce image size to make upload easy
				bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream); 
				byte[] byte_arr = stream.toByteArray();
				// Encode Image to String
				encodedString5 = Base64.encodeToString(byte_arr, 0);
				}
				if (imgPath6 != null && !imgPath6.isEmpty())
				{
				bitmap = BitmapFactory.decodeFile(imgPath6,
						options);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				// Must compress the Image to reduce image size to make upload easy
				bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream); 
				byte[] byte_arr = stream.toByteArray();
				// Encode Image to String
				encodedString6 = Base64.encodeToString(byte_arr, 0);
				}
				return "";
			}

			@Override
			protected void onPostExecute(String msg) {
				prgDialog.setMessage("Calling Upload");
				// Put converted Image string into Async Http Post param
				if (imgPath1 != null && !imgPath1.isEmpty()){
				    params.put("image1", encodedString1);
				}
				if (imgPath2 != null && !imgPath2.isEmpty()){
				    params.put("image2", encodedString2);
				}
				if (imgPath3 != null && !imgPath3.isEmpty()){
				    params.put("image3", encodedString3);
				}
				if (imgPath4 != null && !imgPath4.isEmpty()){
				    params.put("image4", encodedString4);
				}
				if (imgPath5 != null && !imgPath5.isEmpty()){
				    params.put("image5", encodedString5);
				}
				if (imgPath6 != null && !imgPath6.isEmpty()){
				    params.put("image6", encodedString6);
				}
				// Trigger Image upload
				
				params.put("store_name", store_name);
				params.put("store_detail", store_detail);
				params.put("store_address", store_address);
				params.put("store_contact", store_contact);
				params.put("store_cat", cat);
				params.put("UID", Integer.toString(UID));
				params.put("LID", Integer.toString(LID));
				triggerImageUpload();
			}
		}.execute(null, null, null);
	}
	
	public void triggerImageUpload() {
		makeHTTPCall();
	}

	// http://192.168.2.4:9000/imgupload/upload_image.php
	// http://192.168.2.4:9999/ImageUploadWebApp/uploadimg.jsp
	// Make Http call to upload Image to Php server
	public void makeHTTPCall() {
		Intent intent = getIntent();
		SID = -1;
		LID = intent.getIntExtra("LID" , -1);
		UID = intent.getIntExtra("UID" , -1);
		SID = intent.getIntExtra("SID" , -1);
		store_name = intent.getStringExtra("store_name");
		place_name = intent.getStringExtra("place_name");
		store_detail = intent.getStringExtra("store_detail");
		store_address = intent.getStringExtra("store_address");
		store_contact = intent.getStringExtra("store_contact");
		prgDialog.setMessage("Invoking Php");		
		AsyncHttpClient client = new AsyncHttpClient();
		// Don't forget to change the IP address to your LAN address. Port no as well.
		String client_post_url = "http://122.155.187.27:9876/create_store.php";
		if(SID!=-1)
		{
			params.put("SID", Integer.toString(SID));
			client_post_url = "http://122.155.187.27:9876/edit_store_image.php";
		}
		client.post(client_post_url,
				params, new AsyncHttpResponseHandler() {
					// When the response returned by REST has Http
					// response code '200'
					@Override
					public void onSuccess(String response) {
						// Hide Progress Dialog
						prgDialog.hide();
						Toast.makeText(getApplicationContext(), response,
								Toast.LENGTH_LONG).show();
						
						String filenameArray[] = response.toString().split(" ");
					    String SID = filenameArray[filenameArray.length-1];
						
						Intent i = new Intent(create_store3.this,store_detail.class);
						i.putExtra("UID", UID);
						i.putExtra("LID", LID);
						i.putExtra("SID", Integer.parseInt(SID));
						i.putExtra("place_name", place_name);
						i.putExtra("store_name", store_name);
						//Toast.makeText(getApplicationContext(), "SID = "+SID + "UID = "+UID+" LID = "+LID + "place_name = "+place_name + " store_name = "+store_name,
								//Toast.LENGTH_LONG).show();
				        startActivity(i);
				        finish();
					}

					// When the response returned by REST has Http
					// response code other than '200' such as '404',
					// '500' or '403' etc
					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						// Hide Progress Dialog
						prgDialog.hide();
						// When Http response code is '404'
						if (statusCode == 404) {
							Toast.makeText(getApplicationContext(),
									"Requested resource not found",
									Toast.LENGTH_LONG).show();
						}
						// When Http response code is '500'
						else if (statusCode == 500) {
							Toast.makeText(getApplicationContext(),
									"Something went wrong at server end",
									Toast.LENGTH_LONG).show();
						}
						// When Http response code other than 404, 500
						else if (statusCode == 413) {
							Toast.makeText(getApplicationContext(),
									"Some of your images is too large.",
									Toast.LENGTH_LONG).show();
						}
						else {
							Toast.makeText(
									getApplicationContext(),
									"Error Occured. Status code : "
											+ statusCode, Toast.LENGTH_LONG)
									.show();
						}
					}
				});	
}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// Dismiss the progress bar when application is closed
		if (prgDialog != null) {
			prgDialog.dismiss();
		}		
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
