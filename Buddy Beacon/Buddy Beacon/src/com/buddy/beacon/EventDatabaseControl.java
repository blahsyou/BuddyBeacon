package com.buddy.beacon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class EventDatabaseControl {
	// column names for the buddyBeaconDB
	private static final String KEY_ROWID = "id";
	private static final String KEY_EVENTNAME = "eventname";
	private static final String KEY_EVENTDATE = "eventdate";
	private static final String KEY_EVENTDESC = "eventdesc";

	// table name
	private static final String DATABASE_TABLE = "buddyBeaconEventsDB";

	private Context context;
	private SQLiteDatabase database;
	private EventDatabaseHelper dbHelper;

	public EventDatabaseControl(Context context) {
		this.context = context;
	}

	public EventDatabaseControl open() throws SQLiteException {
		dbHelper = new EventDatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	// when program closes, also close the db helper
	public void close() {
		dbHelper.close();
	}

	// add event to the database using content values
	public long addEvent(String eventName, String eventDate, String eventDesc) {
		ContentValues setUpVals = createContentValues(eventName, eventDate, eventDesc);
		return database.insert(DATABASE_TABLE, null, setUpVals);
	}

	// update the update using content values, the > 0 is to check to see if anything has updated
	public boolean updateEvent(long id, String eventName, String eventDate, String eventDesc) {
		ContentValues updateVals = createContentValues(eventName, eventDate, eventDesc);
		return database.update(DATABASE_TABLE, updateVals, KEY_ROWID + "=" + id, null) > 0;
	}

	// get event by their name using cursors
	public long getEventByName(String eventName) {
		Cursor dbCursor;
		long id = 0;
		try {
			dbCursor = database.query(true, DATABASE_TABLE, new String[] { KEY_ROWID }, KEY_EVENTNAME + "=" + "'" + eventName + "'", null, null, null, null,
					null);
			dbCursor.moveToFirst();
			id = dbCursor.getLong(dbCursor.getColumnIndex(KEY_ROWID));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			id = -1;
		}
		return id;

	}

	// get contact by their number, same as name
	public long getEventByDate(String eventDate) {
		Cursor dbCursor;
		long id = 0;
		try {
			dbCursor = database.query(true, DATABASE_TABLE, new String[] { KEY_ROWID }, KEY_EVENTDATE + "=" + "'" + eventDate + "'", null, null, null, null,
					null);
			dbCursor.moveToFirst();
			id = dbCursor.getLong(dbCursor.getColumnIndex(KEY_ROWID));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			id = -1;
		}
		return id;
	}

	// get all contacts from table
	public String getMyEvents() {
		String allData = "";

		try {
			Cursor dbCursor;
			dbCursor = database.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_EVENTNAME, KEY_EVENTDATE, KEY_EVENTDESC }, null, null, null, null, null);
			int iEventName = dbCursor.getColumnIndex(KEY_EVENTNAME);
			int iEventDate = dbCursor.getColumnIndex(KEY_EVENTDATE);
			int iEventDesc = dbCursor.getColumnIndex(KEY_EVENTDESC);

			for (dbCursor.moveToFirst(); !dbCursor.isAfterLast(); dbCursor.moveToNext()) {
				allData = allData + " " + "\t " + dbCursor.getString(iEventName) + "\t " + dbCursor.getString(iEventDate) + "\t"
						+ dbCursor.getString(iEventDesc) + "\n";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			allData = "error";
		}
		return allData;
	}

	// }
	// delete contact from table
	public boolean deleteEvent(long id) {
		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + id, null) > 0;

	}

	// method to create content values
	public ContentValues createContentValues(String eventName, String eventDate, String eventDesc) {
		ContentValues values = new ContentValues();
		values.put(KEY_EVENTNAME, eventName);
		values.put(KEY_EVENTDATE, eventDate);
		values.put(KEY_EVENTDESC, eventDesc);
		return values;
	}

}
