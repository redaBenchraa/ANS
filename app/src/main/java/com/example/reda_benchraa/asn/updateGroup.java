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
import layout.ManageNestedGroups;
import layout.ManageRequests;
import layout.ModifyGroup;

public class updateGroup extends AppCompatActivity implements Serializable {

    private TextView toolbarMessage;
    Group group;
    Toolbar toolbar;
    Fragment selectedFragment = null;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (selectedFragment == null) {
                selectedFragment = ModifyGroup.newInstance(Long.toString(group.getId()));
                getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment).addToBackStack(null).commit();
            }
            switch (item.getItemId()) {
                case R.id.menu_modify:
                    toolbarMessage.setText("General Info");
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
                case R.id.menu_groups:
                    toolbarMessage.setText("Nested Groups");
                    selectedFragment = ManageNestedGroups.newInstance(group);
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
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbarMessage = (TextView) toolbar.findViewById(R.id.name);

        if(getIntent().hasExtra("group")){
                group = (Group) getIntent().getSerializableExtra("group");
                toolbarMessage.setText("General Info");
                selectedFragment = ModifyGroup.newInstance(Long.toString(group.getId()));
                getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment).addToBackStack(null).commit();
                BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),GroupInfo.class);
        startActivity(i);
    }
}
