package com.example.rockodile.contactsappliaction;

public class Contact {

    // this is the name of the table, its static final
    // because it has to be declared only once.
    public static final String TABLE = "contacts";

    // Labels Table Columns names and giving them a variable name to work with
    public static final String KEY_ID = "id";
    public static final String KEY_FORENAME = "forename";
    public static final String KEY_SURNAME = "surname";
    public static final String KEY_HOUSENUMEBR = "housenumber";
    public static final String KEY_STREET = "street";
    public static final String KEY_TOWN = "town";
    public static final String KEY_COUNTY = "county";
    public static final String KEY_POSTCODE = "postcode";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_EMAIL = "email";

    // property help us to keep data
    // declaring variables
    public int contact_ID;
    public String forename;
    public String surname;
    public int housenumber;
    public String street;
    public String town;
    public String county;
    public String postcode;
    public String phone;
    public String email;
}