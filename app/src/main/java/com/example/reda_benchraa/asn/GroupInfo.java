package com.example.reda_benchraa.asn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.reda_benchraa.asn.DAO.Utility;
import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.Model.Group;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import layout.ViewAdmins;
import layout.ViewInfoGroup;
import layout.ViewMembers;
import layout.ViewNestedGroups;

public class GroupInfo extends AppCompatActivity {

    Group group;
    String id;
    Toolbar toolbar;
    private TextView toolbarMessage;
    FloatingActionButton fab;
    Fragment selectedFragment;
    static SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "Account" ;
    Account account;
    Context context;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.menu_info:
                    toolbarMessage.setText("General Info");
                    selectedFragment = ViewInfoGroup.newInstance(group);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment).addToBackStack(null).commit();
                    return true;
                case R.id.menu_members:
                    toolbarMessage.setText("Members");
                    selectedFragment = ViewMembers.newInstance(group);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment).addToBackStack(null).commit();
                    return true;
                case R.id.menu_admins:
                    toolbarMessage.setText("Admins");
                    selectedFragment = ViewAdmins.newInstance(group);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment).addToBackStack(null).commit();
                    return true;
                case R.id.menu_groups:
                    toolbarMessage.setText("Nested Groups");
                    selectedFragment = ViewNestedGroups.newInstance(group);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment).addToBackStack(null).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        context = this;
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        fab = (FloatingActionButton) findViewById(R.id.floatingEdit);
        fab.hide();
        toolbarMessage = (TextView) toolbar.findViewById(R.id.name);
        ((TextView) toolbar.findViewById(R.id.name)).setText("Group info");
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedpreferences.getString("myAccount", "");
        account = gson.fromJson(json, Account.class);


        if(getIntent().hasExtra("group")){
            group = (Group) getIntent().getSerializableExtra("group");

        }else{
            //testing
            group = new Group();
            group.setId(2);
        }
        if(group.getAdmins().isEmpty() && group.getOwner() == null){
            getGroup(this,new HashMap<String,String>(), Utility.getProperty("API_URL",this)+"Groups/"+group.getId()+"?include=owner,admins,members");
        }else{
            if(isAdmin(account,group.getOwner(),group.getAdmins())){
                fab.show();
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getApplicationContext(),updateGroup.class);
                        i.putExtra("group",group);
                        startActivityForResult(i,100);
                    }
                });
            }
        }
        toolbarMessage.setText("General Info");
        selectedFragment = ViewInfoGroup.newInstance(group);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment).addToBackStack(null).commit();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private void getGroup(final Context context, final HashMap<String, String> map, final String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    group = Group.mapJson((JSONObject) new JSONArray(response).get(0));


                    if(isAdmin(account,group.getOwner(),group.getAdmins())){
                        fab.show();
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getApplicationContext(),updateGroup.class);
                                i.putExtra("group",group);
                                startActivityForResult(i,100);
                            }
                        });
                    }


                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
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

    private static boolean isAdmin(Account account, Account owner,LinkedList<Account> admins){
        int i,size=admins.size();
        for (i=0; i<size;i++) {
            if(admins.get(i).getEmail().equals(account.getEmail())){
                return true;
            }
        }
        if(owner.getEmail().equals(account.getEmail())){
           return true;
        }
        return false;
    }

    //ON BACK CLICK USING public ComponentName getCallingActivity()//
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),Group.class);
        startActivity(i);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }
}
