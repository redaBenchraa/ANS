package com.example.reda_benchraa.asn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reda_benchraa.asn.Adapters.conversationArrayAdapter;
import com.example.reda_benchraa.asn.Adapters.messageArrayAdapter;
import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.Model.Conversation;
import com.google.gson.Gson;

public class conversation extends AppCompatActivity {
    private Toolbar toolbar;
    Conversation conversation;
    ListView messagesLv;
    ListAdapter messagesAdapter;
    Account account;
    Context context;
    static SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "Account" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        conversation = (Conversation) getIntent().getSerializableExtra("conversation");
        messagesLv = (ListView) findViewById(R.id.listView_message);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        account =  new Gson().fromJson(sharedpreferences.getString("myAccount", ""), Account.class);
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
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }
    @Override
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
}
