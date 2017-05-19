package com.example.reda_benchraa.asn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.example.reda_benchraa.asn.Adapters.groupsArrayAdapter;
import com.example.reda_benchraa.asn.Adapters.myGroupsArrayAdapter;
import com.example.reda_benchraa.asn.DAO.Utility;
import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.Model.Group;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class search extends AppCompatActivity {
    private Toolbar toolbar;
    Context context;
    Account account;
    static SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "Account";
    groupsArrayAdapter MyGroupsArrayAdapter;
    ListView groupsLv;
    EditText groupName;
    ImageButton search;
    ArrayList<Group> groups;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        ((TextView) toolbar.findViewById(R.id.name)).setText(getResources().getString(R.string.myGroups));
        account = new Gson().fromJson(sharedpreferences.getString("myAccount", ""), Account.class);
        context = this;
        groupsLv = (ListView) findViewById(R.id.groupes);
        groups = new ArrayList<>();
        MyGroupsArrayAdapter = new groupsArrayAdapter(getApplicationContext(), R.layout.group_item_add,groups,account);
        groupsLv.setAdapter(MyGroupsArrayAdapter);
        groupName = (EditText) findViewById(R.id.searchBox);
        search = (ImageButton) findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGroups(context,new HashMap(), Utility.getProperty("API_URL",context)+"Groups/searchGroups/"+groupName.getText().toString());
            }
        });
        groupsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), group.class).putExtra("group", account.getGroups().get(position)));
            }
        });
    }
    public void getGroups(final Context context, final Map map, final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.v("Search",response);
                    JSONArray groupsJSON = (JSONArray) new JSONArray(response);
                    for(int i=0;i< groupsJSON.length();i++){
                        groups.add(Group.mapJson((JSONObject) groupsJSON.get(i)));
                    }
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
