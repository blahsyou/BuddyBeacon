package com.buddy.beacon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DatabaseControl {
	// column names for the buddyBeaconDB
	private static final String KEY_ROWID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_PHONENUMBER = "phonenumber";

	// table name
	private static final String DATABASE_TABLE = "buddyBeaconContactsDB";

	private Context context;
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;

	public DatabaseControl(Context context) {
		this.context = context;
	}

	public DatabaseControl open() throws SQLiteException {
		dbHelper = new DatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	// when program close, also close the db helper
	public void close() {
		dbHelper.close();
	}

	// add contact to the database using content values
	public long addContact(String name, String number) {
		ContentValues setUpVals = createContentValues(name, number);
		return database.insert(DATABASE_TABLE, null, setUpVals);
	}

	// update the contact using content values, the > 0 is to check to see if anything has updated
	public boolean updateContact(long id, String name, String number) {
		ContentValues updateVals = createContentValues(name, number);
		return database.update(DATABASE_TABLE, updateVals, KEY_ROWID + "=" + id, null) > 0;
	}

	// get contact by their name using cursors
	public long getContactByName(String name) {
		Cursor dbCursor;
		long id = 0;
		try {
			dbCursor = database.query(true, DATABASE_TABLE, new String[] { KEY_ROWID }, KEY_NAME + "=" + "'" + name + "'", null, null, null, null, null);
			dbCursor.moveToFirst();
			id = dbCursor.getLong(dbCursor.getColumnIndex(KEY_ROWID));
		} catch (Exception e) {
			id = -1;
		}
		return id;

	}

	// get contact by their number, same as name
	public long getContactByNumber(String phonenumber) {
		Cursor dbCursor;
		long id = 0;
		try {
			dbCursor = database.query(true, DATABASE_TABLE, new String[] { KEY_ROWID }, KEY_PHONENUMBER + "=" + "'" + phonenumber + "'", null, null, null,
					null, null);
			dbCursor.moveToFirst();
			id = dbCursor.getLong(dbCursor.getColumnIndex(KEY_ROWID));
		} catch (Exception e) {
			id = -1;
		}
		return id;
	}

	// get all contacts from table
	public String getAllContacts() {
		String allData = "";

		try {
			Cursor dbCursor;
			dbCursor = database.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME, KEY_PHONENUMBER }, null, null, null, null, null);
			int iName = dbCursor.getColumnIndex(KEY_NAME);
			int iPhoneNumber = dbCursor.getColumnIndex(KEY_PHONENUMBER);

			for (dbCursor.moveToFirst(); !dbCursor.isAfterLast(); dbCursor.moveToNext()) {
				allData = allData + " "  + "\t " + dbCursor.getString(iName) + "\t " + dbCursor.getString(iPhoneNumber) + "\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
			allData = "error";
		}
		return allData;
	}

	// }
	// delete contact from table
	public boolean deleteContact(long id) {
		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + id, null) > 0;

	}

	// method to create content values
	public ContentValues createContentValues(String name, String number) {
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name);
		values.put(KEY_PHONENUMBER, number);
		return values;
	}

}
