package com.example.reda_benchraa.asn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.reda_benchraa.asn.Adapters.commentsArrayAdapter;
import com.example.reda_benchraa.asn.Adapters.pollArrayAdapter;
import com.example.reda_benchraa.asn.DAO.Utility;
import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.Model.Comment;
import com.example.reda_benchraa.asn.Model.Post;
import com.example.reda_benchraa.asn.Model.Reaction;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class post extends AppCompatActivity {
    Post post;
    Account account;
    private Toolbar toolbar;
    TextView group,publisher,content,popularity;
    ImageView groupImage,up,down,postImage,viewMore;
    ListView pollsLv;
    pollArrayAdapter pollAdapter;
    commentsArrayAdapter commentAdapter;
    Context context;
    ImageButton commentButton;
    EditText commentContent;
    int reaction;
    static int listViewTouchAction = 0;
    View postLayoutView;
    int postLayout;
    ListView commentLv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        post = (Post)getIntent().getSerializableExtra("Post");
        postLayoutView = findViewById(R.id.post);
        postLayout = R.layout.post_item;
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.postLY);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainLayout.removeAllViews();
        commentLv = (ListView) findViewById(R.id.listView_comment);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        switch (post.getType()){
            case 0 :
                postLayout = R.layout.post_item;
                break;
            case 1 :
                postLayout = R.layout.post_attachement_item;
                break;
            case 2 :
                postLayout = R.layout.post_photo_item;
                break;
            case 3 :
                postLayout = R.layout.post_file_request_item;
                break;
            case 4 :
                postLayout = R.layout.post_poll_item;
                break;
            default:
                postLayout = R.layout.post_item;
                break;
        }
        postLayoutView = inflater.inflate(postLayout, null);
        mainLayout.addView(postLayoutView);
        account = (Account) getIntent().getSerializableExtra("Account");
        group = (TextView) findViewById(R.id.groupName);
        viewMore = (ImageView) findViewById(R.id.viewMore);
        publisher = (TextView) findViewById(R.id.post_item_publisher);
        content = (TextView) findViewById(R.id.textView2);
        popularity = (TextView) findViewById(R.id.textView3);
        up = (ImageView) findViewById(R.id.imageView);
        down = (ImageView) findViewById(R.id.imageView7);
        groupImage = (ImageView) findViewById(R.id.post_item_groupImage);
        commentButton = (ImageButton) findViewById(R.id.imageButton7);
        commentContent = (EditText) findViewById(R.id.editText5);
        commentAdapter = new commentsArrayAdapter(context,R.layout.comment_item,post.getComments(),account);
        commentLv.setAdapter(commentAdapter);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap<String, String>();
                map.put("Content", commentContent.getText().toString());
                map.put("Type", "1");
                map.put("Popularity", "0");
                map.put("Account_id", Long.toString(account.getId()));
                map.put("Post_id", Long.toString(post.getId()));
                comment(context,map, Utility.getProperty("API_URL",context)+"Comments");
            }
        });
        reaction = 0;
        for(Reaction react : post.getReactions()){
            if(react.getAccount_id() == account.getId()){
                reaction = react.getType();
            }
        }
        if(reaction == 1){
            up.setImageResource(R.drawable.ic_upactivated);
        }else if(reaction == 2){
            down.setImageResource(R.drawable.ic_downactivated);
        }else{
            up.setImageResource(R.drawable.ic_up);
            down.setImageResource(R.drawable.ic_down);
        }
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reaction !=1){
                    up.setImageResource(R.drawable.ic_upactivated);
                    down.setImageResource(R.drawable.ic_down);
                    Map map = new HashMap<String, String>();
                    map.put("accountId", Long.toString(account.getId()));
                    map.put("Type", "1");
                    reactInPost(context,map, Utility.getProperty("API_URL",context)+"Posts/"+post.getId()+"/addReactingAccount");
                    popularity.setText(Integer.parseInt(popularity.getText().toString())+1 +"");
                    reaction = 1;
                }
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reaction !=2) {
                    down.setImageResource(R.drawable.ic_downactivated);
                    up.setImageResource(R.drawable.ic_up);
                    Map map = new HashMap<String, String>();
                    map.put("accountId", Long.toString(account.getId()));
                    map.put("Type", "2");
                    reactInPost(context, map, Utility.getProperty("API_URL", context) + "Posts/" + post.getId() + "/addReactingAccount");
                    popularity.setText(Integer.parseInt(popularity.getText().toString()) - 1 + "");
                    reaction = 2;
                }
            }
        });
        switch (post.getType()){
            case 2 :
                Log.v("Post","image");
                postImage = (ImageView) postLayoutView.findViewById(R.id.photo_image);
                break;
            case 4 :
                Log.v("Post","poll");
                pollsLv = (ListView) postLayoutView.findViewById(R.id.listView_polls);
                break;
        }
        group.setText(post.getGroup().getName());
        publisher.setText(post.getAccount().getFirstName() + " " +post.getAccount().getLastName());
        content.setText(post.getContent());
        popularity.setText(post.getPopularity()+"");
        if(post.getAccount().getProfilePicture() != null)
            groupImage.setImageBitmap(BitmapFactory.decodeByteArray(post.getAccount().getProfilePicture(),0,post.getAccount().getProfilePicture().length));
        else groupImage.setImageResource(R.mipmap.avatar);
        switch (post.getType()){
            case 2 :
                if(post.getImage() != null)
                    postImage.setImageBitmap(BitmapFactory.decodeByteArray(post.getImage(), 0, post.getImage().length));
                break;
            case 4 :
                pollAdapter = new pollArrayAdapter(context, R.layout.post_poll_item_layout, post.getPolls());
                pollsLv.setAdapter(pollAdapter);
                pollsLv.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        listViewTouchAction = event.getAction();
                        if (listViewTouchAction == MotionEvent.ACTION_MOVE) {
                            pollsLv.scrollBy(0, 1);
                        }
                        return false;
                    }
                });
                pollsLv.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (listViewTouchAction == MotionEvent.ACTION_MOVE) {
                            pollsLv.scrollBy(0, -1);
                        }
                    }
                });
                break;
        }
    }
    public void reactInPost(final Context context, final Map map, final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.v("Post",response);
                }catch (Exception e){
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
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
    public void comment(final Context context, final Map map, final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Comment comment = Comment.mapJson(new JSONObject(response));
                    post.getComments().add(0,comment);
                    commentAdapter.notifyDataSetChanged();
                    commentContent.setText("");
                }catch (Exception e){
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
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
