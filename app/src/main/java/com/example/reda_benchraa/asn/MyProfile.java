package com.example.reda_benchraa.asn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;
import com.example.reda_benchraa.asn.DAO.JsonFetcher;
import com.example.reda_benchraa.asn.DAO.Utility;
import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.Model.Group;

import org.json.*;
import java.text.ParseException;

public class MyProfile extends AppCompatActivity implements JsonFetcher.Listener {
    private Toolbar toolbar;
    EditText firstNameEt;
    EditText lastNameEt;
    EditText emailEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        firstNameEt = (EditText) findViewById(R.id.myProfile_firstName);
        lastNameEt = (EditText) findViewById(R.id.myProfile_lastName);
        emailEt = (EditText) findViewById(R.id.myProfile_email);
        new JsonFetcher(this).execute(Utility.getProperty("API_URL",this)+"Accounts/3?include=groups");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public void onLoaded(String response) {
        try {
            //We have only one object in our array
            // https://www.tutorialspoint.com/android/android_json_parser.htm
            JSONObject object = (JSONObject) new JSONArray(response).get(0);
            try {
                // Map the Json response to the Account using the function inside the Account model
                Account account = Account.mapJson(object);
                //Now the account object is populated and you can use it in the view
                firstNameEt.setText(account.getFirstName());
                lastNameEt.setText(account.getLastName());
                emailEt.setText(account.getEmail());
                // ... ect
                // test groups
                for (Group group : account.getGroups()) {
                    Log.v("API GRPS",group.getName());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("API", response);
    }

    @Override
    public void onError() {
        Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
    }
}
