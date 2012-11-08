package com.buddy.beacon;
import android.content.Context;
import android.database.sqlite.*;

public class EventDatabaseHelper extends SQLiteOpenHelper{
	private static final String DATABASE_NAME = "buddyBeaconEventsDB";//name of database
	private static final int DATABASE_VERSION = 1;//version of our database
	
	//sql create statement for creating our buddy beacon database table
	private static final String DATABASE_CREATE = "create table buddyBeaconEventsDB (" +
			"id integer primary key autoincrement," +
			"eventname text not null," +
			"eventdate text not null," +
			"eventdesc text not null" + ");";
	
	
	public EventDatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
		
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists buddyBeaconEventsDB");
		onCreate(db);
		
	}

}