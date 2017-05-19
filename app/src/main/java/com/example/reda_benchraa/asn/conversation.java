package com.example.reda_benchraa.asn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.reda_benchraa.asn.Adapters.conversationArrayAdapter;
import com.example.reda_benchraa.asn.DAO.Utility;
import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.Model.Conversation;
import com.example.reda_benchraa.asn.Model.Message;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class conversation extends AppCompatActivity {
    private Toolbar toolbar;
    Conversation conversation;
    ListView messagesLv;
    ImageButton send;
    EditText message;
    private Timer timer;
    Map map;
    conversationArrayAdapter messagesAdapter;
    Account account;
    Context context;
    static SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "Account" ;
    View postLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Map  map = new HashMap<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        send = (ImageButton) findViewById(R.id.sendButton);
        message = (EditText) findViewById(R.id.message_text);
        conversation = (Conversation) getIntent().getSerializableExtra("conversation");
        messagesLv = (ListView) findViewById(R.id.listView_message);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        account =  new Gson().fromJson(sharedpreferences.getString("myAccount", ""), Account.class);
        postLayout = findViewById(R.id.post_layout);
        ((TextView) toolbar.findViewById(R.id.name)).setText(getResources().getString(R.string.conversation));
        context = this;
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(final Account account : conversation.getAccounts()){
            View v = vi.inflate(R.layout.conversation_contact, null);
            TextView name = (TextView) v.findViewById(R.id.contact_name);
            ImageView image = (ImageView) v.findViewById(R.id.contact_image);
            name.setText(account.getFirstName());
            if(account.getProfilePicture() != null)
                image.setImageBitmap(BitmapFactory.decodeByteArray(account.getProfilePicture(),0,account.getProfilePicture().length));
            else
                image.setImageResource(R.mipmap.avatar);
            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.accounts);
            insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),Profile.class).putExtra("account",account));
                }
            });
        }
        messagesAdapter = new conversationArrayAdapter(getApplicationContext(), R.layout.conversation_item_left, conversation.getMessages(),account.getId(),conversation.getAccounts());
        messagesLv .setAdapter(messagesAdapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.put("Content",message.getText().toString());
                map.put("Account_id",account.getId()+"");
                map.put("Conversation_id",conversation.getId()+"");
                sendMessage(context,map,Utility.getProperty("API_URL",context)+"Messages/");
            }
        });
        startTimer();
    }
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            getConversation(context,new HashMap(),Utility.getProperty("API_URL",context)+"Conversations/"+conversation.getId()+"?include=messages");
        }
    };
    public void startTimer() {
        if(timer != null)
            return;
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 5000);
    }

    public void stopTimer() {
        timer.cancel();
        timer = null;
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
    public void sendMessage(final Context context, final Map map, final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    conversation.getMessages().add(Message.mapJson(new JSONObject(response)));
                    messagesAdapter.notifyDataSetChanged();
                    message.setText("");
                    scrollListview();
                }catch (Exception e){
                    Toast.makeText(context, "Error parsing the message", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error sending the message", Toast.LENGTH_SHORT).show();
                Log.v("New Message",error.getMessage());            }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getConversation(context,new HashMap(),Utility.getProperty("API_URL",context)+"Conversations/"+conversation.getId());
    }
    public void getConversation(final Context context, final Map map, final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    for(Message message : Conversation.mapJson((JSONObject) new JSONArray(response).get(0)).getMessages()){
                        if(!conversation.getMessages().contains(message)){
                            conversation.getMessages().add(message);
                        }
                    }
                    messagesAdapter.notifyDataSetChanged();
                    scrollListview();
                }catch (Exception e){
                    Toast.makeText(context, "Error retrieving the conversation", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error retrieving the conversation", Toast.LENGTH_SHORT).show();
                Log.v("New Message",error.getMessage());
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

    private void scrollListview() {
        messagesLv.post(new Runnable() {
            @Override
            public void run() {
                if(messagesLv.getCount() > 0)
                    messagesLv.setSelection(messagesLv.getCount() - 1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

}
