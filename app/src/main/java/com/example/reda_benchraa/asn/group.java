package com.example.reda_benchraa.asn;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.reda_benchraa.asn.Model.Post;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class group extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    ImageView image,showPost;
    TextView name,about;
    Context context;
    Group group;
    Account account;
    ImageButton file,imageFile,requestFile,poll,send;
    EditText content;
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
    View postLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        image = (ImageView) findViewById(R.id.group_groupPhoto);
        showPost = (ImageView) findViewById(R.id.show_hide);
        name = (TextView) findViewById(R.id.group_groupName);
        about = (TextView) findViewById(R.id.group_groupAbout);

        file = (ImageButton) findViewById(R.id.group_attachment);
        requestFile = (ImageButton) findViewById(R.id.group_upload);
        imageFile = (ImageButton) findViewById(R.id.group_photo);
        poll = (ImageButton) findViewById(R.id.group_poll);
        send = (ImageButton) findViewById(R.id.group_post);
        content = (EditText) findViewById(R.id.group_writePost);
        postLayout = findViewById(R.id.post_layout);

        imageFile.setOnClickListener(this);
        requestFile.setOnClickListener(this);
        file.setOnClickListener(this);
        send.setOnClickListener(this);
        poll.setOnClickListener(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        account = new Gson().fromJson(sharedpreferences.getString("myAccount", ""), Account.class);
        posts = new LinkedList<>();
        setSupportActionBar(toolbar);
        ((TextView) toolbar.findViewById(R.id.name)).setText(getResources().getString(R.string.group));
        context = this;
        postsLv = (ListView) findViewById(R.id.listView_post);
        postsArrayAdapter = new postsArrayAdapter(getApplicationContext(), R.layout.post_item,posts,account);
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
        showPost.setRotation(270.0f);
        showPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(postLayout.getVisibility() != View.GONE){
                    showPost.animate().rotation(90.0f);
                    postLayout.animate()
                            .translationX(postLayout.getWidth())
                            .alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    postLayout.setVisibility(View.GONE);
                                }
                            });
                }else{
                    showPost.animate().rotation(270.0f);
                    postLayout.animate()
                            .translationXBy(-postLayout.getWidth())
                            .alpha(1.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    postLayout.setVisibility(View.VISIBLE);
                                }
                            });
                }

            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),GroupInfo.class).putExtra("group",group));
            }
        });
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
            case R.id.group_attachment :
                if(fileAttachement != null || fileRequestAttachement != null || imageAttachement !=null){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                    intent.setType("*/*");
                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                                    try {
                                        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"),FILE_INTENT);
                                    } catch (android.content.ActivityNotFoundException ex) {
                                        Toast.makeText(getApplicationContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("You've already selected an item,Are you sure you want to replace it?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }else{
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    try {
                        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"),FILE_INTENT);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.group_photo :
                if(fileAttachement != null || fileRequestAttachement != null || imageAttachement !=null){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    Intent intent = new Intent();
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    try {
                                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_INTENT);
                                    } catch (android.content.ActivityNotFoundException ex) {
                                        Toast.makeText(getApplicationContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("You've already selected an item,Are you sure you want to replace it?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }else{
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_INTENT);
                }

                break;
            case R.id.group_upload :
                if(fileAttachement != null || fileRequestAttachement != null || imageAttachement !=null){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                    intent.setType("*/*");
                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                                    try {
                                        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"),FILE_REQUEST_INTENT);
                                    } catch (android.content.ActivityNotFoundException ex) {
                                        Toast.makeText(getApplicationContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("You've already selected an item,Are you sure you want to replace it?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }else{
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    try {
                        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"),FILE_REQUEST_INTENT);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.group_poll :
                startActivityForResult(new Intent(getApplicationContext(),group_poll.class).putExtra("group",group).putExtra("account",account).putExtra("content",content.getText().toString()),POLL_INTENT);
                break;
            case R.id.group_post :
                Map map = new HashMap<String, String>();
                map.put("Type",Integer.toString(getType()));
                map.put("Content",content.getText().toString());
                if(fileAttachement == null && fileRequestAttachement != null){
                    map.put("File",Base64.encodeToString(fileRequestAttachement, Base64.DEFAULT));
                }else if(fileAttachement != null && fileRequestAttachement == null){
                    map.put("File",Base64.encodeToString(fileAttachement, Base64.DEFAULT));
                }
                if(imageAttachement !=null){
                    map.put("Image", Base64.encodeToString(imageAttachement, Base64.DEFAULT));
                }
                map.put("Account_id",Long.toString(account.getId()));
                map.put("Grp_id",Long.toString(group.getId()));
                map.put("postingDate",DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date()).toString());
                postInGroup(context,map,Utility.getProperty("API_URL",context)+"Posts/");
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            final Uri imageUri;
            switch (requestCode) {
                case IMAGE_INTENT:
                    imageUri = data.getData();
                    try {
                        final InputStream stream = getContentResolver().openInputStream(imageUri);
                        imageAttachement = IOUtils.toByteArray(stream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fileAttachement = null;
                    fileRequestAttachement = null;
                    toggleButtons(Color.argb(255,255,64,129));

                    break;
                case FILE_INTENT:
                    imageUri = data.getData();
                    try {
                        final InputStream stream = getContentResolver().openInputStream(imageUri);
                        fileAttachement = IOUtils.toByteArray(stream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageAttachement = null;
                    fileRequestAttachement = null;
                    toggleButtons(Color.argb(255,255,64,129));
                    break;
                case FILE_REQUEST_INTENT:
                    imageUri = data.getData();
                    try {
                        final InputStream stream = getContentResolver().openInputStream(imageUri);
                        fileRequestAttachement = IOUtils.toByteArray(stream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageAttachement = null;
                    fileAttachement = null;
                    toggleButtons(Color.argb(255,255,64,129));
                    break;
                case POLL_INTENT:
                    Post post = (Post) data.getSerializableExtra("post");
                    if(post != null)
                        posts.add(0,post);
                    content.setText(data.getExtras().getString("content"));
                    postsArrayAdapter.notifyDataSetChanged();
                    postsLv.post(new Runnable() {
                        @Override
                        public void run() {
                            postsLv.smoothScrollToPosition(0);
                        }
                    });
                    imageAttachement = null;
                    fileAttachement = null;
                    fileRequestAttachement = null;
                    toggleButtons(Color.argb(255,255,64,129));
                    break;
            }
        }
    }
    public void toggleButtons(int color){
        if(fileRequestAttachement == null){
            requestFile.clearColorFilter();
        }else{
            requestFile.setColorFilter(color);
        }
        if(fileAttachement == null){
            file.clearColorFilter();
        }else{
            file.setColorFilter(color);
        }
        if(imageAttachement == null){
            imageFile.clearColorFilter();
        }else{
            imageFile.setColorFilter(color);
        }
    }
    public void postInGroup(final Context context, final Map map, final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Post post = Post.mapJson(new JSONObject(response));
                    post.setGroup(group);
                    post.setAccount(account);
                    posts.add(0,post);
                    postsLv.post(new Runnable() {
                        @Override
                        public void run() {
                            postsLv.smoothScrollToPosition(0);
                        }
                    });
                    postsArrayAdapter.notifyDataSetChanged();
                    content.setText("");
                    fileAttachement = null;
                    fileRequestAttachement = null;
                    imageAttachement = null;
                    toggleButtons(Color.argb(255,255,64,129));
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
    public int getType(){
        if(fileAttachement != null)
            return 1;
        if(imageAttachement != null)
            return 2;
        if(fileRequestAttachement != null)
            return 3;
        return 0;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }
}
