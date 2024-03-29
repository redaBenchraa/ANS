package com.example.reda_benchraa.asn;

import android.app.Activity;
import android.app.Dialog;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.reda_benchraa.asn.Model.Conversation;
import com.example.reda_benchraa.asn.Model.Message;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewMessage extends AppCompatActivity {
    static Conversation conversation;
    static Message message;
    ImageButton close,send;
    EditText content;
    Account account;
    static SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "Account" ;
    private Toolbar toolbar;
    ArrayList<Account> accounts;
    ImageView add;
    Context context;
    final int ADD_ACCOUNTS_ACTIVITY = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ((TextView) toolbar.findViewById(R.id.name)).setText(getResources().getString(R.string.new_Message));
        close = (ImageButton) findViewById(R.id.newMessage_cancelButton);
        send = (ImageButton) findViewById(R.id.newMessage_sendButton);
        add = (ImageView) findViewById(R.id.newmessage_add);
        content = (EditText) findViewById(R.id.newMessage_message);
        accounts = new ArrayList<>();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        account =  new Gson().fromJson(sharedpreferences.getString("myAccount", ""), Account.class);
        add = (ImageView) findViewById(R.id.newmessage_add);
        accounts = new ArrayList<>();
        if(getIntent().hasExtra("account")){
            accounts.add((Account)getIntent().getSerializableExtra("account"));
        }
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context = this;
        for(final Account account : accounts) {
            View v = vi.inflate(R.layout.new_message_contact, null);
            TextView name = (TextView) v.findViewById(R.id.contact_name);
            ImageView image = (ImageView) v.findViewById(R.id.contact_image);
            name.setText(accounts.get(0).getFirstName());
            if (accounts.get(0).getProfilePicture() != null)
                image.setImageBitmap(BitmapFactory.decodeByteArray(accounts.get(0).getProfilePicture(), 0, accounts.get(0).getProfilePicture().length));
            else
                image.setImageResource(R.mipmap.avatar);
            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.accounts);
            insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(NewMessage.this, "Item " + account.getFirstName(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(),add_contact_message.class).putExtra("accounts", accounts),ADD_ACCOUNTS_ACTIVITY);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateConversation(context,new HashMap<>(),Utility.getProperty("API_URL",context)+"Conversations/");
                startActivityForResult(new Intent(getApplicationContext(),add_contact_message.class).putExtra("accounts",accounts),ADD_ACCOUNTS_ACTIVITY);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (ADD_ACCOUNTS_ACTIVITY) : {
                if (resultCode == Activity.RESULT_OK) {
                    accounts = (ArrayList<Account>) data.getSerializableExtra("accounts");
                    LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final ViewGroup insertPoint = (ViewGroup) findViewById(R.id.accounts);
                    insertPoint.removeAllViews();
                    for(final Account account : accounts) {
                        View v = vi.inflate(R.layout.new_message_contact, null);
                        TextView name = (TextView) v.findViewById(R.id.contact_name);
                        ImageView image = (ImageView) v.findViewById(R.id.contact_image);
                        name.setText(account.getFirstName());
                        if (account.getProfilePicture() != null)
                            image.setImageBitmap(BitmapFactory.decodeByteArray(account.getProfilePicture(), 0, account.getProfilePicture().length));
                        else
                            image.setImageResource(R.mipmap.avatar);
                        insertPoint.addView(v, insertPoint.getChildCount(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        v.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                final Dialog dialog = new Dialog(context);
                                dialog.setContentView(R.layout.delete_layout);
                                TextView text = (TextView) dialog.findViewById(R.id.textView);
                                text.setText(account.getFirstName() + " " + account.getLastName() );
                                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                                Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNo);
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.accounts);
                                        insertPoint.removeViewAt(accounts.indexOf(account));
                                        accounts.remove(account);
                                        dialog.dismiss();
                                    }
                                });
                                dialogButtonNo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                                return true;
                            }
                        });
                    }
                }
                break;
            }
        }
    }

    public void CreateConversation(final Context context, final Map map, final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    conversation = Conversation.mapJson(new JSONObject(response));
                    String accountIds = "";
                    Map map = new HashMap<>();
                    accounts.add(account);
                    for (Account account:accounts)
                        accountIds = accountIds + "," + account.getId();
                    accounts.remove(account);
                    accountIds = accountIds.substring(1);
                    map.put("accountIds",accountIds);
                    addAccounts(context,map,Utility.getProperty("API_URL",context)+"Conversations/"+Long.toString(conversation.getId())+"/addAccounts");
                }catch (Exception e){
                    Toast.makeText(context, "Error sending the message", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error creating the conversation", Toast.LENGTH_SHORT).show();
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
    public void addAccounts(final Context context, final Map map, final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    Log.v("New Message",url);
                    Map map = new HashMap<>();
                    map.put("Content",content.getText().toString());
                    map.put("Account_id",account.getId()+"");
                    map.put("Conversation_id",conversation.getId()+"");
                    sendMessage(context,map,Utility.getProperty("API_URL",context)+"Messages/");
                }catch (Exception e){
                    Toast.makeText(context, "Error sending the message", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error adding accounts", Toast.LENGTH_SHORT).show();
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
    public void sendMessage(final Context context, final Map map, final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    Log.v("New Message",url);
                    message = Message.mapJson(new JSONObject(response));
                    Toast.makeText(context, "Message sent", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context,MyMessages.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    context.startActivity(intent);
                    finish();
                }catch (Exception e){
                    Toast.makeText(context, "Error sending the message", Toast.LENGTH_SHORT).show();
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

}
