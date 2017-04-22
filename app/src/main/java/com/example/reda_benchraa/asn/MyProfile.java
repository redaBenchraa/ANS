package com.example.reda_benchraa.asn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.reda_benchraa.asn.DAO.JsonFetcher;
import com.example.reda_benchraa.asn.DAO.Utility;
import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.Model.Group;
import com.google.gson.Gson;

import org.json.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class MyProfile extends AppCompatActivity {
    private Toolbar toolbar;
    EditText firstNameEt;
    EditText lastNameEt;
    EditText emailEt;
    ToggleButton hideEmail;
    Button submit;
    Context context;
    static SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "Account" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        context = this;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        hideEmail = (ToggleButton) findViewById(R.id.myProfile_hideEmail);
        firstNameEt = (EditText) findViewById(R.id.myProfile_firstName);
        lastNameEt = (EditText) findViewById(R.id.myProfile_lastName);
        emailEt = (EditText) findViewById(R.id.myProfile_email);
        submit = (Button) findViewById(R.id.myProfile_save);
        Gson gson = new Gson();
        String json = sharedpreferences.getString("myAccount", "");
        final Account account = gson.fromJson(json, Account.class);
        try {
            emailEt.setText(account.getEmail());
            lastNameEt.setText(account.getLastName());
            firstNameEt.setText(account.getFirstName());
            hideEmail.setChecked(account.isShowEmail());
        } catch (Exception e) {
            startActivity(new Intent(getApplicationContext(), login.class));
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int showEmail = (hideEmail.isChecked()) ? 1 : 0;
                Map map = new HashMap<String, String>();
                map.put("firstName",firstNameEt.getText().toString());
                map.put("lastName",lastNameEt.getText().toString());
                map.put("Email",emailEt.getText().toString());
                map.put("showEmail",Integer.toString(showEmail));
                account.setFirstName(firstNameEt.getText().toString());
                account.setLastName(lastNameEt.getText().toString());
                account.setEmail(emailEt.getText().toString());
                account.setShowEmail(hideEmail.isChecked());
                SharedPreferences.Editor editor = sharedpreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(account);
                editor.putString("myAccount", json);
                editor.apply();
                updateAccount(context,map,Utility.getProperty("API_URL",context)+"Accounts");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }
    public static void updateAccount(final Context context, final Map map, final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.PUT,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    Intent intent = new Intent(context,MyProfile.class);
                    context.startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Utility.getMap();
            }
        };
        queue.add(sr);
    }


}
