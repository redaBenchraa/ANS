package com.example.reda_benchraa.asn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
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
import com.example.reda_benchraa.asn.Adapters.accountArrayAdapter;
import com.example.reda_benchraa.asn.DAO.Utility;
import com.example.reda_benchraa.asn.Model.Group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class InfoGroup extends AppCompatActivity {
    private Toolbar toolbar;
    ImageView image,ownerImage;
    TextView name,about,ownerName;
    ListView admins,members;
    Context context;
    Group group;
    int id = 7;
    ListAdapter adminAdapter,memberAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_group);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),updateGroup.class);
                i.putExtra("group",group);
                startActivityForResult(i,100);
            }
        });
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ((TextView) toolbar.findViewById(R.id.name)).setText("Info Group");
        context = this;
        image = (ImageView) findViewById(R.id.infoGroup_imageGroup);
        ownerImage = (ImageView) findViewById(R.id.infoGroup_ownerImage);
        name = (TextView) findViewById(R.id.infoGroup_groupName);
        ownerName = (TextView) findViewById(R.id.infoGroup_ownerName);
        about = (TextView) findViewById(R.id.infoGroup_about);
        admins = (ListView) findViewById(R.id.listView_infoGroupAdmins);
        members = (ListView) findViewById(R.id.listView_infoGroupMembers);
        getGroup(context,new HashMap<String,String>(), Utility.getProperty("API_URL",context)+"Groups/"+id+"?include=owner,admins,members");


    }
    private void getGroup(final Context context,final HashMap<String, String> map,final String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    group = Group.mapJson((JSONObject) new JSONArray(response).get(0));
                    name.setText(group.getName());
                    about.setText(group.getAbout());
                    if(group.getImage()!=null){
                        image.setImageBitmap(BitmapFactory.decodeByteArray(group.getImage(),0,group.getImage().length));
                    }
                    ownerName.setText(group.getOwner().getFirstName()+" "+group.getOwner().getLastName());
                    adminAdapter = new accountArrayAdapter(context, R.layout.account_item, group.getAdmins());
                    admins.setAdapter(adminAdapter);
                    memberAdapter = new accountArrayAdapter(context, R.layout.account_item, group.getMembers());
                    members.setAdapter(memberAdapter);
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
