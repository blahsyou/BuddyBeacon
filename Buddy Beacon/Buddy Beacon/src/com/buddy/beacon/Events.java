package com.buddy.beacon;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Events extends Activity implements OnClickListener{
	
	Button AddEvent;
	Button EditEvent;
	Button ViewEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);
        AddEvent = (Button) findViewById(R.id.add_event_button);
        EditEvent = (Button) findViewById(R.id.edit_event_button);
        ViewEvent = (Button) findViewById(R.id.view_events_button);
        
        AddEvent.setOnClickListener(this);
        EditEvent.setOnClickListener(this);
        ViewEvent.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View btn) {
    	if(btn.getId() == R.id.add_event_button){
    		startActivity(new Intent(this, AddEvent.class));
    	}
    	if (btn.getId() == R.id.view_events_button) {
			startActivity(new Intent(this, EventDatabaseViewer.class));
		}
    }
}
