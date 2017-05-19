package com.example.reda_benchraa.asn;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.Model.Group;
import com.example.reda_benchraa.asn.Model.Message;
import com.example.reda_benchraa.asn.Model.Poll;
import com.example.reda_benchraa.asn.Model.Post;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class group_poll extends AppCompatActivity {
    private Toolbar toolbar;
    ListView pollsLv;
    ArrayList<String> polls;
    ImageButton add,send;
    Context context;
    Group group;
    EditText content,pollName;
    Account account;
    ArrayAdapter<String> pollsAdapter;
    Post post;
    ImageView image;
    TextView name,about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_poll);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        context = this;
        if(getIntent().hasExtra("group"))
            group = (Group) getIntent().getSerializableExtra("group");
        if(getIntent().hasExtra("account"))
            account = (Account) getIntent().getSerializableExtra("account");
        add = (ImageButton) findViewById(R.id.group_poll_validatePoll);
        send = (ImageButton) findViewById(R.id.group_poll_send);
        content = (EditText) findViewById(R.id.group_poll_writePost);
        pollName = (EditText) findViewById(R.id.group_poll_newPoll);
        image = (ImageView) findViewById(R.id.group_poll_imageGroup);
        name = (TextView) findViewById(R.id.group_poll_groupName);
        about = (TextView) findViewById(R.id.textView7);
        ((TextView) toolbar.findViewById(R.id.name)).setText("Group Poll");
        if(group.getImage() != null)
            image.setImageBitmap(BitmapFactory.decodeByteArray(group.getImage(),0,group.getImage().length));
        else
            image.setImageResource(R.mipmap.avatar);
        name.setText(group.getName());
        about.setText(group.getAbout());
        if(getIntent().hasExtra("content"))
            content.setText(getIntent().getExtras().getString("content"));
        polls = new ArrayList<>();
        pollsLv = (ListView) findViewById(R.id.listView_poll);
        pollsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.blacktext ,polls);
        pollsLv.setAdapter(pollsAdapter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pollName.getText().toString().isEmpty()){
                    polls.add(pollName.getText().toString());
                    pollsAdapter.notifyDataSetChanged();
                    pollName.setText("");
                }
            }
        });
        pollsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                polls.remove(position);
                                pollsAdapter.notifyDataSetChanged();
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want this item to be deleted?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap<String, String>();
                map.put("Type",Integer.toString(4));
                map.put("Content",content.getText().toString());
                map.put("Account_id",Long.toString(account.getId()));
                map.put("Grp_id",Long.toString(group.getId()));
                map.put("postingDate", DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date()).toString());
                postInGroup(context,map,Utility.getProperty("API_URL",context)+"Posts/");
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
    public void postInGroup(final Context context, final Map map, final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    post = Post.mapJson(new JSONObject(response));
                    post.setGroup(group);
                    post.setAccount(account);
                    int i =1;
                    for(String poll : polls){
                        Map map = new HashMap<String, String>();
                        map.put("Content",poll);
                        map.put("Post_id",Long.toString(post.getId()));
                        addPoll(context,map,Utility.getProperty("API_URL",context)+"Polls/",i);
                        i++;
                    }
                    Toast.makeText(context, "Post submitted", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(context, "Error submitting the post", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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
    public void addPoll(final Context context, final Map map, final String url, final int iteration){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    post.getPolls().add(Poll.mapJson(new JSONObject(response)));
                    if(iteration == polls.size()){
                        setResult(Activity.RESULT_OK, new Intent().putExtra("post",post).putExtra("content",""));
                        finish();
                    }
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
    protected void onDestroy() {
        super.onDestroy();
        if(post == null){
            setResult(Activity.RESULT_OK, new Intent().putExtra("content",content.getText().toString()));
            finish();
        }
    }

}
