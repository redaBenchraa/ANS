package com.example.reda_benchraa.asn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.reda_benchraa.asn.DAO.Utility;
import com.example.reda_benchraa.asn.Model.Group;

import java.io.Serializable;
import java.util.HashMap;

import layout.ManageAdmins;
import layout.ManageMembers;
import layout.ManageRequests;
import layout.ModifyGroup;

public class updateGroup extends AppCompatActivity implements Serializable {

    private TextView mTextMessage,toolbarMessage;
    Group group;
    Toolbar toolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            if (selectedFragment == null) {
                selectedFragment = ModifyGroup.newInstance(Long.toString(group.getId()));
                getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment).addToBackStack(null).commit();
            }
            switch (item.getItemId()) {
                case R.id.menu_modify:
                    toolbarMessage.setText("Modify");
                    selectedFragment = ModifyGroup.newInstance(Long.toString(group.getId()));
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment).addToBackStack(null).commit();
                    return true;
                case R.id.menu_admins:
                    toolbarMessage.setText("Admins");
                    selectedFragment = ManageAdmins.newInstance(Long.toString(group.getId()));
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment).addToBackStack(null).commit();
                    return true;
                case R.id.menu_members:
                    toolbarMessage.setText("Members");
                    selectedFragment = ManageMembers.newInstance(Long.toString(group.getId()));
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment).addToBackStack(null).commit();
                    return true;
                case R.id.menu_request:
                    toolbarMessage.setText("Requests");
                    selectedFragment = ManageRequests.newInstance(Long.toString(group.getId()));
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment).addToBackStack(null).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_group);
        mTextMessage = (TextView) findViewById(R.id.message);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbarMessage = (TextView) toolbar.findViewById(R.id.name);
        if(getIntent().hasExtra("group")){
                group = (Group) getIntent().getSerializableExtra("group");
                BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),InfoGroup.class);
        startActivity(i);
    }
}
