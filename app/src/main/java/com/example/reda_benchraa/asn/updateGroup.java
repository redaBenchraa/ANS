package com.example.reda_benchraa.asn;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.reda_benchraa.asn.DAO.Utility;
import com.example.reda_benchraa.asn.Model.Group;

import java.io.Serializable;
import java.util.HashMap;

import layout.ManageMembers;
import layout.ModifyGroup;

public class updateGroup extends AppCompatActivity implements Serializable {

    private TextView mTextMessage;
    Group group;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.menu_modify:
                    getFragmentManager().popBackStack();
                    selectedFragment = ModifyGroup.newInstance(group);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment).addToBackStack(null).commit();
                    return true;
                case R.id.menu_admins:
                    getFragmentManager().popBackStack();
                    mTextMessage.setText("Admins");
                    return true;
                case R.id.menu_members:
                    getFragmentManager().popBackStack();
                    selectedFragment = ManageMembers.newInstance(group);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment).addToBackStack(null).commit();
                    return true;
                case R.id.menu_request:
                    getFragmentManager().popBackStack();
                    mTextMessage.setText("add members");
                    return true;
            }
            if (selectedFragment != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.detach(selectedFragment);
                fragmentTransaction.attach(selectedFragment);
                fragmentTransaction.commit();
            }
            else{
                selectedFragment = ModifyGroup.newInstance(group);
                getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment).addToBackStack(null).commit();
            }

            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_group);
        mTextMessage = (TextView) findViewById(R.id.message);
        if(getIntent().hasExtra("group")){
                group = (Group) getIntent().getSerializableExtra("group");
                BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }

    }

}
