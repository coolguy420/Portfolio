package com.example.rockodile.contactsappliaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class UserSQL  {
    private DBHelper dbHelper;
    private static final String TAG = "UserSQL";

    public UserSQL(Context context) {
        dbHelper = new DBHelper(context);

    }

    public int insert(Contact contact) {

        //This function attempts to open a connection so it can write to it
        //setting up all the variables and putting values from the form into
        //appropriate fields
        Log.i(TAG, "Creating Contact and adding to Database");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contact.KEY_FORENAME, contact.forename);
        values.put(Contact.KEY_SURNAME, contact.surname);
        values.put(Contact.KEY_HOUSENUMEBR, contact.housenumber);
        values.put(Contact.KEY_STREET, contact.street);
        values.put(Contact.KEY_TOWN, contact.town);
        values.put(Contact.KEY_COUNTY, contact.county);
        values.put(Contact.KEY_POSTCODE, contact.postcode);
        values.put(Contact.KEY_PHONE, contact.phone);
        values.put(Contact.KEY_EMAIL, contact.email);

        // Inserting Row
        long contact_Id = db.insert(Contact.TABLE, null, values);
        db.close(); // Closing database connection
        return (int) contact_Id;
    }

    public void delete(int contact_Id) {
        Log.i(TAG, "Deleting Contact from database");

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(Contact.TABLE, Contact.KEY_ID + "= ?", new String[] { String.valueOf(contact_Id) });
        db.close(); // Closing database connection
    }
    // updating the contact information
    // putting new values in the intended fields from form.
    public void update(Contact contact) {
        Log.i(TAG, "Connecting to the database");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Contact.KEY_FORENAME, contact.forename);
        values.put(Contact.KEY_SURNAME, contact.surname);
        values.put(Contact.KEY_HOUSENUMEBR, contact.housenumber);
        values.put(Contact.KEY_STREET, contact.street);
        values.put(Contact.KEY_TOWN, contact.town);
        values.put(Contact.KEY_COUNTY, contact.county);
        values.put(Contact.KEY_POSTCODE, contact.postcode);
        values.put(Contact.KEY_PHONE, contact.phone);
        values.put(Contact.KEY_EMAIL, contact.email);

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(Contact.TABLE, values, Contact.KEY_ID + "= ?", new String[] {String.valueOf(contact.contact_ID) });
        db.close(); // Closing database connection
    }

    // creating an arraylist for the contact and retrieving that information
    // in a list view.
    public ArrayList<HashMap<String, String>> getContactList() {
        //Open connection to read only
        Log.i(TAG, "Retrieving Contact from database");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Contact.KEY_ID + "," +
                Contact.KEY_FORENAME + ", " +
                Contact.KEY_SURNAME + ", " +
                Contact.KEY_HOUSENUMEBR  + ", " +
                Contact.KEY_STREET + ", " +
                Contact.KEY_TOWN + ", " +
                Contact.KEY_COUNTY  + ", " +
                Contact.KEY_POSTCODE + ", " +
                Contact.KEY_PHONE + ", " +
                Contact.KEY_EMAIL +
                " FROM " + Contact.TABLE+
                " ORDER BY "+
                Contact.KEY_FORENAME + " ASC, " +
                Contact.KEY_SURNAME + " ASC ";
                ;


        ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        //putting records into the database
        //each record must be taken to the right destination
        if (cursor.moveToFirst()) {
            do {
                Log.i(TAG, "Ptting information in the database");
                HashMap<String, String> record = new HashMap<String, String>();
                record.put("id", cursor.getString(cursor.getColumnIndex(Contact.KEY_ID)));
                record.put("forename", cursor.getString(cursor.getColumnIndex(Contact.KEY_FORENAME)));
                record.put("surname", cursor.getString(cursor.getColumnIndex(Contact.KEY_SURNAME)));
                record.put("housenumber", cursor.getString(cursor.getColumnIndex(Contact.KEY_HOUSENUMEBR)));
                record.put("street", cursor.getString(cursor.getColumnIndex(Contact.KEY_STREET)));
                record.put("town", cursor.getString(cursor.getColumnIndex(Contact.KEY_TOWN)));
                record.put("county", cursor.getString(cursor.getColumnIndex(Contact.KEY_COUNTY)));
                record.put("postcode", cursor.getString(cursor.getColumnIndex(Contact.KEY_POSTCODE)));
                record.put("phone", cursor.getString(cursor.getColumnIndex(Contact.KEY_PHONE)));
                record.put("email", cursor.getString(cursor.getColumnIndex(Contact.KEY_EMAIL)));
                contactList.add(record);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // closing the connection to the database
        db.close();
        Log.i(TAG, "Closing database");
        return contactList;

    }

    // getting contact by ID.
    // @param KEY_ID
    public Contact getContactById(int Id){
        Log.i(TAG, "Retrieving contact by ID");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Contact.KEY_ID + ", " +
                Contact.KEY_FORENAME + ", " +
                Contact.KEY_SURNAME + ", " +
                Contact.KEY_HOUSENUMEBR  + ", " +
                Contact.KEY_STREET + ", " +
                Contact.KEY_TOWN + ", " +
                Contact.KEY_COUNTY  + ", " +
                Contact.KEY_POSTCODE + ", " +
                Contact.KEY_PHONE + ", " +
                Contact.KEY_EMAIL +
                " FROM " + Contact.TABLE
                + " WHERE " +
                Contact.KEY_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        int iCount =0;
        Contact contact = new Contact();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                Log.i(TAG, "Updating contact in the database");
                contact.contact_ID =cursor.getInt(cursor.getColumnIndex(Contact.KEY_ID));
                contact.forename =cursor.getString(cursor.getColumnIndex(Contact.KEY_FORENAME));
                contact.surname  =cursor.getString(cursor.getColumnIndex(Contact.KEY_SURNAME));
                contact.housenumber =cursor.getInt(cursor.getColumnIndex(Contact.KEY_HOUSENUMEBR));
                contact.street =cursor.getString(cursor.getColumnIndex(Contact.KEY_STREET));
                contact.town =cursor.getString(cursor.getColumnIndex(Contact.KEY_TOWN));
                contact.county =cursor.getString(cursor.getColumnIndex(Contact.KEY_COUNTY));
                contact.postcode =cursor.getString(cursor.getColumnIndex(Contact.KEY_POSTCODE));
                contact.phone =cursor.getString(cursor.getColumnIndex(Contact.KEY_PHONE));
                contact.email =cursor.getString(cursor.getColumnIndex(Contact.KEY_EMAIL));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Log.i(TAG, "Closing Database");
        return contact;
    }

}