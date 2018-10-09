package com.example.rockodile.contactsappliaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class ContactDetails extends AppCompatActivity implements android.view.View.OnClickListener {
    // declaring the variables, buttons, and edit text names
    Button btnSave ,  btnDelete;
    Button btnClose;
    EditText forename;
    EditText surname;
    EditText housenumber;
    EditText street;
    EditText town;
    EditText county;
    EditText postcode;
    EditText phone;
    EditText email;
    // a static final string to record Log.i messages which are done through this class
    // so the developer can identify which class is doing what and which isnt.
    private static final String TAG = "ContactDetails";
    private int _contact_Id = 0; // private int which is used to hold the ID of the contact



    // on create method setting everyhing up.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        // creating all buttons and edit texts
        // giving edit text variables to hold values.
        btnSave = (Button) findViewById(R.id.save);
        Log.i(TAG, "Creating Save button");
        btnDelete = (Button) findViewById(R.id.delete);
        Log.i(TAG, "Creating Delete Button");
        btnClose = (Button) findViewById(R.id.close);
        Log.i(TAG, "Creating Close Button");

        Log.i(TAG, "creating edit text Fields");
        forename = (EditText) findViewById(R.id.forename);
        surname = (EditText) findViewById(R.id.surname);
        housenumber = (EditText) findViewById(R.id.housenumber);
        street = (EditText) findViewById(R.id.street);
        town = (EditText) findViewById(R.id.town);
        county = (EditText) findViewById(R.id.county);
        postcode = (EditText) findViewById(R.id.postcode);
        postcode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);

        // making an onClickListener for buttons.
        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnClose.setOnClickListener(this);


        // declaring and initialising the contact ID variable.
        // and what it will hold
        _contact_Id =0;
        Intent intent = getIntent();
        _contact_Id  =intent.getIntExtra("contact_Id", 0);
        UserSQL repo = new UserSQL(this);
        Contact contact_information;
        contact_information = repo.getContactById(_contact_Id);
        Log.i(TAG, "connection fields to database");
        housenumber.setText(String.valueOf(contact_information.housenumber));
        surname.setText(contact_information.surname);
        forename.setText(contact_information.forename);
        street.setText(contact_information.street);
        town.setText(contact_information.town);
        county.setText(contact_information.county);
        postcode.setText(contact_information.postcode);
        phone.setText(contact_information.phone);
        email.setText(contact_information.email);
    }


    public void onClick(View view) {
        // if the save button is pressed, take all values
        // and turn them into a String.
        if (view == findViewById(R.id.save)) {
            Log.i(TAG, "Save button was pressed, Saving...");

            UserSQL repo = new UserSQL(this);
            Contact contact = new Contact();
            contact.housenumber = Integer.parseInt(housenumber.getText().toString());
            contact.phone = phone.getText().toString();
            contact.forename = forename.getText().toString();
            contact.surname = surname.getText().toString();
            contact.street = street.getText().toString();
            contact.town = town.getText().toString();
            contact.county = county.getText().toString();
            contact.postcode = postcode.getText().toString();
            contact.email = email.getText().toString();

            contact.contact_ID = _contact_Id;


            String fname = forename.getText().toString().trim();
            String sname = surname.getText().toString().trim();
            String strt = street.getText().toString().trim();
            String twn = town.getText().toString().trim();
            String cnty = county.getText().toString().trim();
            String phoneNumber = phone.getText().toString().trim();
            int houseNumber = Integer.parseInt(housenumber.getText().toString());
            String pCode = postcode.getText().toString().trim();

            // a UK standard postcode regex used to validate the postcode entered by user.
            String regex = "^[A-Z]{1,2}[0-9][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$";

            // if statments validating every field.
            // when save button is pressed, run these validations firsts.
            // if it meets the requirements, save or update contact
            // if not. send messages to the user via toast methods.

            // name must be filled
            if (fname.isEmpty() || fname.length() == 0 || fname.equals("") && sname.isEmpty() || sname.length() == 0 || sname.equals("")) {
                Toast.makeText(this, "Please fill Forename and Surname ", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "User attempting to fill name"); // a message for the developer in the console.

                // street, town and county MUST be filled
            } else if (strt.isEmpty() || strt.length() == 0 || strt.equals("")
                    && twn.isEmpty() || twn.length() == 0 || twn.equals("") && cnty.isEmpty() || cnty.length() == 0 || cnty.equals("")) {
                Log.i(TAG, "User attempting to fill Address"); // a message for the developer in the console.
                Toast.makeText(this, "Please fill Street, Town and County ", Toast.LENGTH_SHORT).show();

                // House number must start with a number higher than 1
            } else if (houseNumber < 1) {
                Log.i(TAG, "User attempting to enter a house number"); // a message for the developer in the console.
                Toast.makeText(this, "House Number Must be numeric and higher than 1 ", Toast.LENGTH_SHORT).show();

                // phone number must be filled. 11 digits
            } else if (phoneNumber.isEmpty()) {
                Log.i(TAG, "User attempting to enter phone number");// a message for the developer in the console.
                Toast.makeText(this, "Please enter a valid phone number ", Toast.LENGTH_SHORT).show();

                // postcode validation using regex, if incorrect, give example message
            } else if (!pCode.matches(regex)) {
                Log.i(TAG, "User attempting to enter postcode");// a message for the developer in the console.
                Toast.makeText(this, "Please enter a valid Postcode, example: NE11 2FQ ", Toast.LENGTH_SHORT).show();

                // if new contact, make a new contact and save it to database.
            } else if (_contact_Id == 0) {
                _contact_Id = repo.insert(contact);
                Log.i(TAG, "New Contact being created");// a message for the developer in the console.
                Toast.makeText(this, "New contact created", Toast.LENGTH_SHORT).show();

            } else {
                Log.i(TAG, "Contact exists, Updating contact information");// a message for the developer in the console.
                repo.update(contact);
                Toast.makeText(this, "Contact updated", Toast.LENGTH_SHORT).show(); // if all validation goes through, update the contact if contact already exists.
            }


            // handling delete button if pressed.
            // pop up a new dialog box if pressed
            // ask for confirmation.
            // if YES, proceed
            // if NO, do nothing, return to contact details.
        } else if (view == findViewById(R.id.delete)) {
            final Activity thisActivity = this;
            AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
            builder.setMessage("Are you sure you want to delete this contact?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            UserSQL repo = new UserSQL(thisActivity);
                            repo.delete(_contact_Id);
                            Toast.makeText(thisActivity, "Contact Record Deleted", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "User Deleted");
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i(TAG, "Deletion cancelled");// a message for the developer in the console.
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            // handling close button when pressed
        } else if (view == findViewById(R.id.close)){
            Log.i(TAG, "Closing contact page"); // a message for the developer in the console.
        finish();
        }
        }
        }

