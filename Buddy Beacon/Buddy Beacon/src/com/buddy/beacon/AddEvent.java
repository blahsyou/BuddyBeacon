package com.buddy.beacon;

import android.app.Activity;
import android.app.Dialog;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddEvent extends Activity implements OnClickListener {

	EditText eventNameInput;
	EditText eventDateInput;
	EditText eventDescInput;
	Button AddEventToDb;

	private EventDatabaseControl eDbControl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addevents);

		eventNameInput = (EditText) findViewById(R.id.event_name_text);
		eventDateInput = (EditText) findViewById(R.id.event_date_text);
		eventDescInput = (EditText) findViewById(R.id.event_description_text);

		AddEventToDb = (Button) findViewById(R.id.add_event_to_db);

		AddEventToDb.setOnClickListener(this);

		eDbControl = new EventDatabaseControl(this);

	}

	@Override
	public void onClick(View btn) {

		String eventNameData = eventNameInput.getText().toString();
		String eventDateData = eventDateInput.getText().toString();
		String eventDescData = eventDescInput.getText().toString();
		Dialog notice = new Dialog(this);
		TextView msgBody = new TextView(this);
		msgBody.setTextSize(18);
		long tempVal = 0;

		if (btn.getId() == R.id.add_event_to_db) {
			try {
				eventNameData = eventNameInput.getText().toString();
				eventDateData = eventDateInput.getText().toString();
				eventDescData = eventDescInput.getText().toString();
				// open database
				eDbControl.open();
				// get event by name and if value is not -1 then update contact is it is in there,
				// if not, then add contact
				if ((tempVal = eDbControl.getEventByName(eventNameData)) != -1) {
					if (eDbControl.updateEvent(tempVal, eventNameData, eventDateData, eventDescData)) {
						notice.setTitle("Event Updated!");
						msgBody.setText("Event already existed, therefore was update instead");
					} else {
						notice.setTitle("Update failed!");
						msgBody.setText("Event already existed but failed to update");
					}

				} else {
					long rowId = 0;
					rowId = eDbControl.addEvent(eventNameData, eventDateData, eventDescData);
					notice.setTitle("Event Added");
					msgBody.setText("Event Added!");
					
					//
				}
				eDbControl.close();
			} catch (SQLiteException e) { // notify user if sql error occured
				e.printStackTrace();
				notice.setTitle("Insert Failed!");
				msgBody.setText("SQL Error");

			}
			notice.setContentView(msgBody);
			notice.show();
		}

	}
}
