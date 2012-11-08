package com.buddy.beacon;

import android.app.Activity;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.TextView;

public class DatabaseViewer extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frienddb);
		
		TextView content = (TextView) findViewById(R.id.friendOutput);
		
		DatabaseControl control = new DatabaseControl(this);
		
		String result = "name\t number \n";
		
		try{
			control.open();
			result = result + "\n" + control.getAllContacts();
			control.close();
			
		}catch(SQLiteException e){
			e.printStackTrace();
		}
		content.setText(result);
		//set textview to database output text
	}
	

}
