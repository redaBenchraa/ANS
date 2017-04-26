package com.example.reda_benchraa.asn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.reda_benchraa.asn.Adapters.postsArrayAdapter;
import com.example.reda_benchraa.asn.DAO.Utility;
import com.example.reda_benchraa.asn.Model.Group;
import com.example.reda_benchraa.asn.Model.Post;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class group extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    ImageView image;
    TextView name,about;
    Context context;
    Group group;
    ImageButton file,imageFile,requestFile,poll,send;
    static SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "Account";
    postsArrayAdapter postsArrayAdapter;
    ListView postsLv;
    LinkedList<Post> posts;
    final int FILE_REQUEST_INTENT = 1;
    final int FILE_INTENT = 2;
    final int IMAGE_INTENT = 3;
    final int POLL_INTENT = 4;
    byte[] fileAttachement;
    byte[] fileRequestAttachement;
    byte[] imageAttachement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        image = (ImageView) findViewById(R.id.group_groupPhoto);
        name = (TextView) findViewById(R.id.group_groupName);
        about = (TextView) findViewById(R.id.group_groupAbout);
        file = (ImageButton) findViewById(R.id.group_attachment);
        requestFile = (ImageButton) findViewById(R.id.group_upload);
        imageFile = (ImageButton) findViewById(R.id.group_photo);
        poll = (ImageButton) findViewById(R.id.group_poll);
        send = (ImageButton) findViewById(R.id.group_post);
        posts = new LinkedList<>();
        setSupportActionBar(toolbar);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        ((TextView) toolbar.findViewById(R.id.name)).setText(getResources().getString(R.string.group));
        context = this;
        postsLv = (ListView) findViewById(R.id.listView_post);
        postsArrayAdapter = new postsArrayAdapter(getApplicationContext(), R.layout.notification_item_seen,posts);
        postsLv.setAdapter(postsArrayAdapter);
        if(getIntent().hasExtra("group")){
            group = (Group) getIntent().getSerializableExtra("group");
            name.setText(group.getName());
            about.setText(group.getAbout());
            if(group.getImage() != null){
                image.setImageBitmap(BitmapFactory.decodeByteArray(group.getImage(),0,group.getImage().length));
            }else{
                image.setImageResource(R.mipmap.avatar);
            }
            getPosts(context,new HashMap(), Utility.getProperty("API_URL",context)+"Groups/"+group.getId()+"?include=posts");
        }else{
            try {
                getPosts(context,new HashMap(), Utility.getProperty("API_URL",context)+"Groups/"+getIntent().getExtras().getLong("groupId")+"?include=posts");
            }catch (Exception e){
                e.printStackTrace();
                finish();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
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
    public void getPosts(final Context context, final Map map, final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    group = Group.mapJson((JSONObject) new JSONArray(response).get(0));
                    if(group == null){
                        name.setText(group.getName());
                        about.setText(group.getAbout());
                        if(group.getImage() != null){
                            image.setImageBitmap(BitmapFactory.decodeByteArray(group.getImage(),0,group.getImage().length));
                        }else{
                            image.setImageResource(R.mipmap.avatar);
                        }
                    }
                    posts.addAll(group.getPosts());
                    postsArrayAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    Toast.makeText(context, "Error parsing the posts", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error fetching posts", Toast.LENGTH_SHORT).show();
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.group_upload :
                break;
            case R.id.group_photo :
                break;
            case R.id.group_poll :
                break;
            case R.id.group_post :
                break;
        }
    }
}
