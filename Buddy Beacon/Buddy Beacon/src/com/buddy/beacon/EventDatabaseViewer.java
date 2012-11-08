package com.buddy.beacon;

import android.app.Activity;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.TextView;

public class EventDatabaseViewer extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventsdb);
		
		TextView content = (TextView) findViewById(R.id.eventOutput);
		
		EventDatabaseControl control = new EventDatabaseControl(this);
		
		String result = "event name\t event date\t event description \n";
		
		try{
			control.open();
			result = result + "\n" + control.getMyEvents();
			control.close();
			
		}catch(SQLiteException e){
			e.printStackTrace();
		}
		content.setText(result);
		//set textview to database output text
	}
	

}
