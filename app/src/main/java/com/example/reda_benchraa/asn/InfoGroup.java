package com.example.reda_benchraa.asn;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class InfoGroup extends AppCompatActivity {
    private Toolbar toolbar;
    ImageView image;
    TextView name,about;
    ListView owner,admins,members;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_group);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        context = this;
        image = (ImageView) findViewById(R.id.info_group_imageGroup);
        name = (TextView) findViewById(R.id.infoGroup_groupName);
        about = (TextView) findViewById(R.id.infoGroup_about);
        owner = (ListView) findViewById(R.id.listView_infoGroupOwner);
        admins = (ListView) findViewById(R.id.listView_infoGroupAdmins);
        members = (ListView) findViewById(R.id.listView_infoGroupMembers);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }
}
