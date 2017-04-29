package com.example.reda_benchraa.asn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
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
import com.example.reda_benchraa.asn.Adapters.newMessageAddContactArrayAdapter;
import com.example.reda_benchraa.asn.DAO.Utility;
import com.example.reda_benchraa.asn.Model.Account;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class add_contact_message extends AppCompatActivity {
    private Toolbar toolbar;
    ArrayList<Account> accounts;
    ArrayList<Account> accountsList;
    EditText search;
    Context context;
    ListView account_lv;
    ImageButton addAccouts;
    newMessageAddContactArrayAdapter accountAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact_message);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        accounts = new ArrayList<>();
        context = this;
        search = (EditText) findViewById(R.id.search);
        account_lv = (ListView) findViewById(R.id.accounts_listview);
        account_lv.setItemsCanFocus(false);
        addAccouts = (ImageButton) findViewById(R.id.addAccouts);
        accountsList = new ArrayList<>();
        ((TextView) toolbar.findViewById(R.id.name)).setText(getResources().getString(R.string.new_Message_account));
        if(getIntent().hasExtra("accounts")){
            accounts = (ArrayList<Account>) getIntent().getSerializableExtra("accounts");
        }else{
            accounts = new ArrayList<>();
        }
        final LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup insertPoint = (ViewGroup) findViewById(R.id.accounts);
        for(final Account account : accounts){
            View v = vi.inflate(R.layout.new_message_contact, null);
            TextView name = (TextView) v.findViewById(R.id.contact_name);
            ImageView image = (ImageView) v.findViewById(R.id.contact_image);
            name.setText(account.getFirstName());
            if(account.getProfilePicture() != null)
                image.setImageBitmap(BitmapFactory.decodeByteArray(account.getProfilePicture(),0,account.getProfilePicture().length));
            else
                image.setImageResource(R.mipmap.avatar);
            insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getContacts(context,new HashMap(),Utility.getProperty("API_URL",context)+"Accounts/searchMembers/"+search.getText().toString());
                accountsList.clear();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        account_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBoxv = (CheckBox) view.findViewById(R.id.checkBox);
                TextView emailv = (TextView) view.findViewById(R.id.email);
                TextView namev = (TextView) view.findViewById(R.id.contact_name);
                TextView idv = (TextView) view.findViewById(R.id.id);
                ImageView imagev = (ImageView) view.findViewById(R.id.contact_image);
                Account account = new Account();
                account.setFirstName(namev.getText().toString());
                account.setLastName("");
                account.setEmail(emailv.getText().toString());
                account.setId(Long.parseLong(idv.getText().toString()));
                new ByteArrayOutputStream().toByteArray();
                Bitmap bitm = ((BitmapDrawable)imagev.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                account.setProfilePicture(stream.toByteArray());
                Log.v("addContactMessage","Index " +accounts.indexOf(account));
                if(checkBoxv.isChecked()){
                    insertPoint.removeViewAt(accounts.indexOf(account));
                    accounts.remove(account);
                }else{
                    accounts.add(account);
                    View v = vi.inflate(R.layout.new_message_contact, null);
                    TextView name = (TextView) v.findViewById(R.id.contact_name);
                    ImageView image = (ImageView) v.findViewById(R.id.contact_image);
                    name.setText(namev.getText().toString());
                    image.setImageDrawable(imagev.getDrawable());
                    insertPoint.addView(v, insertPoint.getChildCount(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
                checkBoxv.toggle();
            }
        });
        addAccouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(accounts);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("accounts", accounts);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
    public void getContacts(final Context context, final Map map, final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.v("addContactMessage",url);
                    Log.v("addContactMessage",response);
                    JSONArray accountJsonArray = new JSONArray(response);
                    for(int i=0;i<accountJsonArray.length();i++){
                        Account account = Account.mapJson((JSONObject) new JSONArray(response).get(i));
                        accountsList.add(account);
                    }
                    accountAdapter = new newMessageAddContactArrayAdapter(context, R.layout.new_message_add_contact,accountsList,accounts);
                    account_lv.setAdapter(accountAdapter);
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("accounts", accounts);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
