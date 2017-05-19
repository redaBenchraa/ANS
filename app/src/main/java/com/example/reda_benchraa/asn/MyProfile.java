package com.example.reda_benchraa.asn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.reda_benchraa.asn.DAO.JsonFetcher;
import com.example.reda_benchraa.asn.DAO.Utility;
import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.Model.Group;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.json.*;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class MyProfile extends AppCompatActivity {
    static int PICK_PHOTO_FOR_AVATAR = 1;
    private Toolbar toolbar;
    EditText firstNameEt;
    EditText lastNameEt;
    EditText emailEt;
    ToggleButton hideEmail;
    Button submit;
    ImageView image;
    Context context;
    Account account;
    TextView nameTv;
    EditText aboutTv;
    static SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "Account" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        nameTv = (TextView) toolbar.findViewById(R.id.name);
        context = this;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        hideEmail = (ToggleButton) findViewById(R.id.myProfile_hideEmail);
        firstNameEt = (EditText) findViewById(R.id.myProfile_firstName);
        lastNameEt = (EditText) findViewById(R.id.myProfile_lastName);
        emailEt = (EditText) findViewById(R.id.myProfile_email);
        submit = (Button) findViewById(R.id.myProfile_save);
        image = (ImageView) findViewById(R.id.myProfile_image);
        aboutTv = (EditText) findViewById(R.id.myProfile_about);
        nameTv.setText(getResources().getString(R.string.myProfile));
        Gson gson = new Gson();
        String json = sharedpreferences.getString("myAccount", "");
        account = gson.fromJson(json, Account.class);
        try {
            emailEt.setText(account.getEmail());
            lastNameEt.setText(account.getLastName());
            firstNameEt.setText(account.getFirstName());
            hideEmail.setChecked(account.isShowEmail());
            if(account.getProfilePicture() != null){
                image.setImageBitmap(BitmapFactory.decodeByteArray(account.getProfilePicture(),0,account.getProfilePicture().length));
            }
            aboutTv.setText(account.getAbout());
        } catch (Exception e) {
            e.printStackTrace();
            startActivity(new Intent(getApplicationContext(), login.class));
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int showEmail = (hideEmail.isChecked()) ? 1 : 0;
                String encodedImage = Base64.encodeToString(account.getProfilePicture(), Base64.DEFAULT);
                Map map = new HashMap<String, String>();
                map.put("firstName",firstNameEt.getText().toString());
                map.put("lastName",lastNameEt.getText().toString());
                map.put("Email",emailEt.getText().toString());
                map.put("showEmail",Integer.toString(showEmail));
                map.put("About",aboutTv.getText().toString());
                map.put("Image",encodedImage);
                account.setFirstName(firstNameEt.getText().toString());
                account.setLastName(lastNameEt.getText().toString());
                account.setEmail(emailEt.getText().toString());
                account.setShowEmail(hideEmail.isChecked());
                account.setAbout(aboutTv.getText().toString());
                SharedPreferences.Editor editor = sharedpreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(account);
                editor.putString("myAccount", json);
                editor.apply();
                updateAccount(context,map,Utility.getProperty("API_URL",context)+"Accounts/"+account.getId());
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(context, "No photo selected", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                account.setProfilePicture(IOUtils.toByteArray(inputStream));
                image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
            case R.id.action_help:startActivity(new Intent(getApplicationContext(),search.class));break;
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
    public static void updateAccount(final Context context, final Map map, final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.PUT,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    Intent intent = new Intent(context,MyProfile.class);
                    context.startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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


}
