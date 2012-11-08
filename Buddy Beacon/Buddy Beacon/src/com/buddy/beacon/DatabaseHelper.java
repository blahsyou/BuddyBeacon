package com.buddy.beacon;
import android.content.Context;
import android.database.sqlite.*;

public class DatabaseHelper extends SQLiteOpenHelper{
	private static final String DATABASE_NAME = "buddyBeaconContactsDB";//name of database
	private static final int DATABASE_VERSION = 1;//version of our database
	
	//sql create statement for creating our buddy beacon database table
	private static final String DATABASE_CREATE = "create table buddyBeaconContactsDB (" +
			"id integer primary key autoincrement," +
			"name text not null," +
			"phonenumber text not null" + ");";
	
	
	public DatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
		
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists buddyBeaconContactsDB");
		onCreate(db);
		
	}

}
