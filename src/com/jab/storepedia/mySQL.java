package com.jab.storepedia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class mySQL extends SQLiteOpenHelper {
	
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "mydatabase";
 
    // Table Name
    private static final String TABLE_MEMBER = "members";

	public mySQL(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	    db.execSQL("CREATE TABLE " + TABLE_MEMBER + 
		          "(MemberID INTEGER PRIMARY KEY," +
		          " Name TEXT(100)," +
		          " Tel TEXT(100));");
	   
	    Log.d("CREATE TABLE","Create Table Successfully.");
	}
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
