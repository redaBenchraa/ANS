package com.example.reda_benchraa.asn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.reda_benchraa.asn.Model.Group;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddGroup extends AppCompatActivity {

    static int PICK_PHOTO_FOR_AVATAR = 1;
    EditText name,about;
    ImageView image;
    TextView nameTv;
    Button add;
    private Toolbar toolbar;
    Account account;
    static SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "Account";
    Group group;
    Context context;
    byte[] photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //nameTv = (TextView) toolbar.findViewById(R.id.name);
        name = (EditText) findViewById(R.id.addGroup_name);
        about = (EditText) findViewById(R.id.addGroup_about);
        image = (ImageView) findViewById(R.id.addGroup_image);
        add = (Button) findViewById(R.id.addGroup_addButton);
        //nameTv.setText("Add Group");

        context = this;

        /*Gson gson = new Gson();
        String json = sharedpreferences.getString("myAccount", "");
        account = gson.fromJson(json, Account.class);*/
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
            }
        });
        if(getIntent().hasExtra("group")){
            group = (Group) getIntent().getSerializableExtra("group");
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String encodedImage = Base64.encodeToString(photo, Base64.DEFAULT);
                    Map map = new HashMap<String, String>();
                    map.put("Name",name.getText().toString());
                    map.put("About",about.getText().toString());
                    map.put("Account_id",account.getId());
                    map.put("Grp_id",Long.toString(group.getId()));
                    createGroup(context,map,Utility.getProperty("API_URL",context)+"Groups/");
                }
            });
        }else{
            Toast.makeText(this, "Error getting the super group", Toast.LENGTH_SHORT).show();
        }
    }

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

                photo = IOUtils.toByteArray(inputStream);
                image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createGroup(final Context context, final Map map, final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(context,updateGroup.class);
                intent.putExtra("group",group);
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
