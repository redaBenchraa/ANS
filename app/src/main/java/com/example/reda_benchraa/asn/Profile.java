package com.example.reda_benchraa.asn;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reda_benchraa.asn.Model.Account;

public class Profile extends AppCompatActivity {
    private Toolbar toolbar;
    Account account;
    Context context;
    TextView email,fullName,about;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        ((TextView) toolbar.findViewById(R.id.name)).setText(getResources().getString(R.string.myProfile));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),NewMessage.class).putExtra("account",account));
            }
        });
        account = (Account) getIntent().getSerializableExtra("account");
        fullName = (TextView) findViewById(R.id.profile_fullName);
        email = (TextView) findViewById(R.id.profile_email);
        about = (TextView) findViewById(R.id.profile_about);
        image = (ImageView) findViewById(R.id.profile_image);
        fullName.setText(account.getFirstName() + " " + account.getLastName());
        email.setText(account.getEmail());
        about.setText(account.getAbout());
        if(account.getProfilePicture() != null){
            image.setImageBitmap(BitmapFactory.decodeByteArray(account.getProfilePicture(),0,account.getProfilePicture().length));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

}
