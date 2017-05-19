package com.example.reda_benchraa.asn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.reda_benchraa.asn.Adapters.conversationArrayAdapter;
import com.example.reda_benchraa.asn.Adapters.myGroupsArrayAdapter;
import com.example.reda_benchraa.asn.DAO.Utility;
import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.Model.Conversation;
import com.example.reda_benchraa.asn.Model.Group;
import com.example.reda_benchraa.asn.Model.Message;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MyGroups extends AppCompatActivity {
    private Toolbar toolbar;
    Context context;
    Account account;
    static SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "Account";
    myGroupsArrayAdapter MyGroupsArrayAdapter;
    ListView myGroupsLv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        ((TextView) toolbar.findViewById(R.id.name)).setText(getResources().getString(R.string.myGroups));
        account = new Gson().fromJson(sharedpreferences.getString("myAccount", ""), Account.class);
        context = this;
        myGroupsLv = (ListView) findViewById(R.id.listView_myGroups);
        MyGroupsArrayAdapter = new myGroupsArrayAdapter(getApplicationContext(), R.layout.group_item,account.getGroups());
        myGroupsLv.setAdapter(MyGroupsArrayAdapter);
        getMyGroups(context,new HashMap(),Utility.getProperty("API_URL",context)+"Accounts/"+account.getId()+"?include=groups");
        myGroupsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),group.class).putExtra("group",account.getGroups().get(position)));
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_home:startActivity(new Intent(getApplicationContext(),home.class));break;
            case R.id.action_myProfile:startActivity(new Intent(getApplicationContext(),MyProfile.class));break;
            case R.id.action_myGroups:startActivity(new Intent(getApplicationContext(),MyGroups.class));break;
            case R.id.action_myMessages:startActivity(new Intent(getApplicationContext(),MyMessages.class));break;
            case R.id.action_myNotifications:startActivity(new Intent(getApplicationContext(),mynotification.class));break;
            case R.id.action_search:startActivity(new Intent(getApplicationContext(),search.class));break;
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
    public void getMyGroups(final Context context, final Map map, final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    account.getGroups().addAll(Account.mapJson((JSONObject) new JSONArray(response).get(0)).getGroups());
                    MyGroupsArrayAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    Toast.makeText(context, "Error parsing the groups", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error fetching groups", Toast.LENGTH_SHORT).show();
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
