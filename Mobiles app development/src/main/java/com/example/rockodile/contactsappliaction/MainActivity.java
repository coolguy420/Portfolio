package com.example.rockodile.contactsappliaction;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ListActivity implements android.view.View.OnClickListener {
    // declaration of all variables, used for buttons and Log.i
    // declaration of a textView.
    Button btnAdd, btnGetAll;
    private static final String TAG = "MainActivity";
    TextView contact_Id;

    // on create function.
    // creating the buttons on activity_main
    // adding two buttons
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "Creating add button"); // message for the console and developer
        btnAdd = (Button) findViewById(R.id.add);
        btnAdd.setOnClickListener(this);
        Log.i(TAG, "Creating View button"); // message for the console and developer
        btnGetAll = (Button) findViewById(R.id.view);
        btnGetAll.setOnClickListener(this);

    }

    // handling buttons
    @Override
    public void onClick(View view) {
        // if button add was pressed do the following.
        if (view == findViewById(R.id.add)) {
            // a message for the console and developer
            Log.i(TAG, "User Clicked on ADD button");

            //create a new contact ID. Start activity.
            Intent intent = new Intent(this, ContactDetails.class);
            intent.putExtra("contact_Id", 0);
            startActivity(intent);

        } else { // else is the VIEW button or the contact Name, only thing other than ADD button
            Log.i(TAG, "User Clicked on VIEW button");
            // retrieve the contacts from the UserSQL database.
            UserSQL repo = new UserSQL(this);
            // declaring a new ArrayList called contactList
            // which is taken from a method in the ContactDetails class
            ArrayList<HashMap<String, String>> contactList = repo.getContactList();
            if (contactList.size() != 0) {
                ListView lv = getListView();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        contact_Id = (TextView) view.findViewById(R.id.contact_ID);
                        String Id = contact_Id.getText().toString();
                        Intent objIndent = new Intent(getApplicationContext(), ContactDetails.class);
                        objIndent.putExtra("contact_Id", Integer.parseInt(Id));
                        startActivity(objIndent);
                    }
                });
                // Declaring a new ListAdapter
                // retrieving the contact ID, Name and Surname
                ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList, R.layout.view_contact_entry, new String[]{"id", "forename","surname"}
                        , new int[]{R.id.contact_ID, R.id.contact_name, R.id.contact_surname});
                setListAdapter(adapter);
            } else {
                // if contacts dont exist. display this message in toast form,
                Toast.makeText(this, "No contacts found!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}

