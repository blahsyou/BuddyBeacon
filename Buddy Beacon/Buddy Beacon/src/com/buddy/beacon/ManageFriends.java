
package com.buddy.beacon;

import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ManageFriends extends Activity implements OnClickListener {

	static final int PICK_CONTACT_REQUEST = 1;

	EditText nameInput; //refers to input box for name
	EditText numberInput; //refers to input box for number
	Button AddButton;
	Button ImportButton;
	Button DeleteButton;
	Button BuddyBeaconButton;
	private DatabaseControl dbControl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends);

		nameInput = (EditText) findViewById(R.id.friends_name_textfield);
		numberInput = (EditText) findViewById(R.id.friends_number_textfield);

		AddButton = (Button) findViewById(R.id.friends_add_contact);
		ImportButton = (Button) findViewById(R.id.friends_import_contact);
		DeleteButton = (Button) findViewById(R.id.friends_delete_contact);
		BuddyBeaconButton = (Button) findViewById(R.id.friends_buddybeaconers);

		AddButton.setOnClickListener(this);
		ImportButton.setOnClickListener(this);
		DeleteButton.setOnClickListener(this);
		BuddyBeaconButton.setOnClickListener(this);
		
		dbControl = new DatabaseControl(this);
	}

	

	@Override
	// This is used to Import an Existing Contact
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		if (reqCode == PICK_CONTACT_REQUEST) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				// The user picked a contact.
				// The Intent's data Uri identifies which contact was selected.
				// Get the URI that points to the selected contact
				final Uri contactUri = data.getData();
				// We only need the NUMBER column, because there will be only one row in the result
				final String[] projection = { Phone.NUMBER };

				// Perform the query on the contact to get the NUMBER and NAME column

				new Thread(new Runnable() {
					public void run() {

						runOnUiThread(new Runnable() {
							public void run() {
								Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
								cursor.moveToFirst();

								// Retrieve the phone number from the NUMBER column
								int PhoneNumber = cursor.getColumnIndex(Phone.NUMBER);
								String number = cursor.getString(PhoneNumber);

								// Retrieve the Name
								String name = getContactName(number);

								// Creating the Database
								SQLiteDatabase db = openOrCreateDatabase("ContactDB", MODE_PRIVATE, null);

								db.setVersion(1);
								db.setLocale(Locale.getDefault());
								db.setLockingEnabled(true);

								// Creatign the Table
								final String CREATE_TABLE_CONTACTS = "CREATE TABLE IF NOT EXISTS tbl_contacts (Name VARCHAR, PhoneNumber TEXT);";

								
								db.execSQL(CREATE_TABLE_CONTACTS);
								
								// Do something with the number and name
								
								db.execSQL("INSERT INTO tbl_contacts VALUES ('"+name+"', '"+number+"');");
																						
								Cursor c = db.rawQuery("SELECT * FROM tbl_contacts WHERE Name = '"+name+"' AND PhoneNumber = '"+number+"'", null);
							    c.moveToFirst();
								
							    Context context = getApplicationContext();
								CharSequence text = "Contact Added";
								int duration = Toast.LENGTH_SHORT;

								Toast toast = Toast.makeText(context, text, duration);
								toast.show();
							    numberInput.setText(c.getString((c.getColumnIndex("PhoneNumber"))));
								nameInput.setText(c.getString((c.getColumnIndex("Name"))));
								
								cursor.close();
								cursor = null;
							}
						});
					}
				}).start();
			}
		}

	}

	@Override
	public void onClick(View btn) {
		
		String nameData = nameInput.getText().toString().toLowerCase();
		String numberData = numberInput.getText().toString();
		Dialog notice = new Dialog(this);
		TextView msgBody = new TextView(this);
		msgBody.setTextSize(18);
		long tempVal = 0;
		
		if (btn.getId() == R.id.friends_import_contact) {

			Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse(("content://contacts")));
			pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
			startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
		}
		if (btn.getId() == R.id.friends_add_contact) {
			try{
				nameData = nameInput.getText().toString().toLowerCase();
				numberData = numberInput.getText().toString();
				//open database
				dbControl.open();
				//get contact by name and if value is not -1 then update contact is it is in there, 
				//if not, then add contact
				if((tempVal = dbControl.getContactByName(nameData)) != -1){
					if(dbControl.updateContact(tempVal, nameData, numberData)){
						notice.setTitle("Contact Updated!");
						msgBody.setText("Contact already existed, therefore was update instead");
					}
					else{
						notice.setTitle("Update failed!");
						msgBody.setText("Contact already existed but failed to update");
					}
					
				}
				else{
					long rowId = 0;
					rowId = dbControl.addContact(nameData, numberData);
					notice.setTitle("Contact Added");
					msgBody.setText("Contact Added!");
				}
				dbControl.close();
			}
			catch(SQLiteException e){ //notify user if sql error occured
				e.printStackTrace();
				notice.setTitle("Insert Failed!");
				msgBody.setText("SQL Error");
				
			}
			notice.setContentView(msgBody);
			notice.show();
		}
		
		//delete contact when button is clicked
		if (btn.getId() == R.id.friends_delete_contact) {
			try{
				dbControl.open();
				
				if((tempVal = dbControl.getContactByName(nameData)) != -1){
					if(dbControl.deleteContact(tempVal)){
						notice.setTitle("Contact deleted!");
						msgBody.setText("Contact has been deleted");
					}
					else{
						notice.setTitle("Delete failed!");
						msgBody.setText("Delete failed, no records found");
					}
					
				}
				else{
					notice.setTitle("Delete Failed");
					msgBody.setText("Contact does not exist");
				}
				dbControl.close();
			}
			catch(SQLiteException e){ //notify user if sql error occured
				e.printStackTrace();
				notice.setTitle("Delete Failed!");
				msgBody.setText("SQL Error");
				
			}
			notice.setContentView(msgBody);
			notice.show();
		}
		//show buddies in database if button is clicked
		if (btn.getId() == R.id.friends_buddybeaconers) {
			startActivity(new Intent(this, DatabaseViewer.class));
		}
	}
	
	//get contact info from importing from address list
	public String getContactName(final String phoneNumber) {
		Uri uri;
		String[] projection;

		if (Build.VERSION.SDK_INT >= 5) {
			uri = Uri.parse("content://com.android.contacts/phone_lookup");
			projection = new String[] { "display_name" };
		} else {
			uri = Uri.parse("content://contacts/phones/filter");
			projection = new String[] { "name" };
		}

		uri = Uri.withAppendedPath(uri, Uri.encode(phoneNumber));
		Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);

		String contactName = "";

		if (cursor.moveToFirst()) {
			contactName = cursor.getString(0);
		}

		cursor.close();
		cursor = null;

		return contactName;
	}
}