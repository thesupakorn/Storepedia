package com.jab.storepedia;

import java.io.ByteArrayOutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint("NewApi")
public class create_comment extends Activity {
	ProgressDialog prgDialog;
	String encodedString1, encodedString2, encodedString3, encodedString4;
	RequestParams params = new RequestParams();
	String imgPath1, imgPath2, imgPath3, imgPath4, fileName, place_name, store_name;
	Bitmap bitmap;	
	int imgFlag = 0, SID,LID,UID;
	private static int RESULT_LOAD_IMG = 1;
	EditText comment_field;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_comment);
		prgDialog = new ProgressDialog(this);
		// Set Cancelable as False
		prgDialog.setCancelable(false);
		
		Intent intent = getIntent();
		SID = intent.getIntExtra("SID" , -1);
		LID = intent.getIntExtra("LID" , -1);
		UID = intent.getIntExtra("UID" , -1);
		final String store_name = intent.getStringExtra("store_name");
		final String place_name = intent.getStringExtra("place_name");
		comment_field = (EditText) findViewById(R.id.comment);
		TextView debug_text = (TextView) findViewById(R.id.textView1);
		ImageButton back = (ImageButton) findViewById(R.id.topbar).findViewById(R.id.back);
		debug_text.setText("SID = "+SID + ";  UID = " +UID + "; LID = " + LID +";");		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(create_comment.this,show_lcomment.class);
				i.putExtra("UID", UID);
				i.putExtra("LID", LID);
				i.putExtra("SID", SID);
				i.putExtra("place_name", place_name);
				i.putExtra("store_name", store_name);
	            startActivity(i);
	            finish();
			}
	    });
	}

	
	public void loadImagefromGallery1(View view) {
		// Create intent to Open Image applications like Gallery, Google Photos
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// Start the Intent
		imgFlag = 1;
		startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
	}
	public void loadImagefromGallery2(View view) {
		// Create intent to Open Image applications like Gallery, Google Photos
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// Start the Intent
		imgFlag = 2;
		startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
	}
	public void loadImagefromGallery3(View view) {
		// Create intent to Open Image applications like Gallery, Google Photos
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// Start the Intent
		imgFlag = 3;
		startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
	}
	public void loadImagefromGallery4(View view) {
		// Create intent to Open Image applications like Gallery, Google Photos
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// Start the Intent
		imgFlag = 4;
		startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
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
				    String fileNameSegments[] = imgPath1.split("/");
				    fileName = fileNameSegments[fileNameSegments.length - 1];
				    // Put file name in Async Http Post Param which will used in Php web app
				params.put("filename1", fileName);
				}
				else if(imgFlag == 2){
					imgPath2 = cursor.getString(columnIndex);
					ImageView imgView = (ImageView) findViewById(R.id.pic2);
					// Set the Image in ImageView
					imgView.setImageBitmap(BitmapFactory
							.decodeFile(imgPath2));
					// Get the Image's file name
					String fileNameSegments[] = imgPath2.split("/");
					fileName = fileNameSegments[fileNameSegments.length - 1];
					// Put file name in Async Http Post Param which will used in Php web app
					params.put("filename2", fileName);
					}
				else if(imgFlag == 3){
					imgPath3 = cursor.getString(columnIndex);
					ImageView imgView = (ImageView) findViewById(R.id.pic3);
					// Set the Image in ImageView
					imgView.setImageBitmap(BitmapFactory
							.decodeFile(imgPath3));
					// Get the Image's file name
					String fileNameSegments[] = imgPath3.split("/");
					fileName = fileNameSegments[fileNameSegments.length - 1];
					// Put file name in Async Http Post Param which will used in Php web app
					params.put("filename3", fileName);
					}
				else if(imgFlag == 4){
					imgPath4 = cursor.getString(columnIndex);
					ImageView imgView = (ImageView) findViewById(R.id.pic4);
					// Set the Image in ImageView
					imgView.setImageBitmap(BitmapFactory
							.decodeFile(imgPath4));
					// Get the Image's file name
					String fileNameSegments[] = imgPath4.split("/");
					fileName = fileNameSegments[fileNameSegments.length - 1];
					// Put file name in Async Http Post Param which will used in Php web app
					params.put("filename4", fileName);
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
		if ((imgPath1 != null && !imgPath1.isEmpty()) || (imgPath2 != null && !imgPath2.isEmpty()) || (imgPath3 != null && !imgPath3.isEmpty()) || (imgPath4 != null && !imgPath4.isEmpty())) {
			prgDialog.setMessage("Converting Image to Binary Data");
			prgDialog.show();
			// Convert image to String using Base64
			encodeImagetoString();
		// When Image is not selected from Gallery
		} else {
			Toast.makeText(
					getApplicationContext(),
					"You must select image from gallery before you try to upload",
					Toast.LENGTH_LONG).show();
		}
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
				// Trigger Image upload
				params.put("comment", comment_field.getText().toString());
				params.put("SID", Integer.toString(SID));
				params.put("UID", Integer.toString(UID));
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
		prgDialog.setMessage("Invoking Php");		
		AsyncHttpClient client = new AsyncHttpClient();
		// Don't forget to change the IP address to your LAN address. Port no as well.
		client.post("http://122.155.187.27:9876/create_comment.php",
				params, new AsyncHttpResponseHandler() {
					// When the response returned by REST has Http
					// response code '200'
					@Override
					public void onSuccess(String response) {
						// Hide Progress Dialog
						prgDialog.hide();
						Toast.makeText(getApplicationContext(), response,
								Toast.LENGTH_LONG).show();
						Intent i = new Intent(create_comment.this,show_lcomment.class);
						i.putExtra("UID", UID);
						i.putExtra("LID", LID);
						i.putExtra("SID", SID);
						i.putExtra("place_name", place_name);
						i.putExtra("store_name", store_name);
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
						else {
							Toast.makeText(
									getApplicationContext(),
									"Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
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
	
}
