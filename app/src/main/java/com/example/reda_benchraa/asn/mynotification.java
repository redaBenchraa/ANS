package com.example.reda_benchraa.asn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.reda_benchraa.asn.Adapters.myNotificationsArrayAdapter;
import com.example.reda_benchraa.asn.DAO.Utility;
import com.example.reda_benchraa.asn.Model.Account;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class mynotification extends AppCompatActivity {
    private Toolbar toolbar;
    Context context;
    Account account;
    static SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "Account";
    myNotificationsArrayAdapter MyNotificationsArrayAdapter;
    ListView myNotificationsLv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mynotification);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        ((TextView) toolbar.findViewById(R.id.name)).setText(getResources().getString(R.string.myNotifications));
        account = new Gson().fromJson(sharedpreferences.getString("myAccount", ""), Account.class);
        context = this;
        account =  new Gson().fromJson(sharedpreferences.getString("myAccount", ""), Account.class);
        myNotificationsLv = (ListView) findViewById(R.id.listView_myNotification);
        MyNotificationsArrayAdapter = new myNotificationsArrayAdapter(getApplicationContext(), R.layout.notification_item_seen,account.getNotifications());
        myNotificationsLv.setAdapter(MyNotificationsArrayAdapter);
        getMyNotifications(context,new HashMap(), Utility.getProperty("API_URL",context)+"Accounts/"+account.getId()+"?include=notifications");
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_home:startActivity(new Intent(getApplicationContext(),home.class));break;
            case R.id.action_myProfile:startActivity(new Intent(getApplicationContext(),MyProfile.class));break;
            case R.id.action_myGroups:startActivity(new Intent(getApplicationContext(),MyGroups.class));break;
            case R.id.action_myMessages:startActivity(new Intent(getApplicationContext(),MyMessages.class));break;
            case R.id.action_myNotifications:startActivity(new Intent(getApplicationContext(),mynotification.class));break;
            case R.id.action_myContacts:startActivity(new Intent(getApplicationContext(),Contacts.class));break;
            case R.id.action_settings:startActivity(new Intent(getApplicationContext(),MyProfile.class));break;
            case R.id.action_help:startActivity(new Intent(getApplicationContext(),MyProfile.class));break;
            case R.id.action_signout:
                SharedPreferences prefs = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(getApplicationContext(),login.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void getMyNotifications(final Context context, final Map map, final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    account.getNotifications().addAll(Account.mapJson((JSONObject) new JSONArray(response).get(0)).getNotifications());
                    MyNotificationsArrayAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    Toast.makeText(context, "Error parsing the notifications", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error fetching notifications", Toast.LENGTH_SHORT).show();
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
