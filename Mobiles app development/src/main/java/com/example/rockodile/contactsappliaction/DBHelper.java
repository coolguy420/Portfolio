package com.example.rockodile.contactsappliaction;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper  extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 1;

    // this is the name of the database. declared as Static as only need one.
    private static final String DATABASE_NAME = "contacts1.db";

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here
        // creating the fields and type of the columns.
        String CREATE_TABLE_CONTACT = "CREATE TABLE " + Contact.TABLE  + "("
                + Contact.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Contact.KEY_FORENAME + " TEXT, "
                + Contact.KEY_SURNAME + " TEXT, "
                + Contact.KEY_HOUSENUMEBR  + " INTEGER, "
                + Contact.KEY_STREET + " TEXT, "
                + Contact.KEY_TOWN + " TEXT, "
                + Contact.KEY_COUNTY  + " TEXT, "
                + Contact.KEY_POSTCODE + " TEXT, "
                + Contact.KEY_PHONE + " INTEGER, "
                + Contact.KEY_EMAIL + " TEXT )";

        db.execSQL(CREATE_TABLE_CONTACT);

    }

    @Override
    // replacing the database with a new one if it already exist
    // if not, create a new one. this is used to update the table.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + Contact.TABLE);

        // Create tables again
        onCreate(db);

    }

}