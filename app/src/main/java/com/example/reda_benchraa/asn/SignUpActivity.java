package com.example.reda_benchraa.asn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity{
    EditText fname,lname,email,password,repassword;
    Button signup;
    Context context;
    static Account account;
    static SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "Account" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        context = this;
        fname = (EditText) findViewById(R.id.signIn_firstName);
        lname = (EditText) findViewById(R.id.signIn_lastName);
        email = (EditText) findViewById(R.id.signIn_email);
        password = (EditText) findViewById(R.id.signIn_password);
        repassword = (EditText) findViewById(R.id.signIn_passwordRetyped);
        signup = (Button) findViewById(R.id.signUp_button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(repassword.getText().toString())){
                    Map map = new HashMap<String, String>();
                    map.put("firstName",fname.getText().toString());
                    map.put("lastName",lname.getText().toString());
                    map.put("Email",email.getText().toString());
                    map.put("Password",password.getText().toString());
                    map.put("showEmail","1");
                    map.put("About"," z");
                    map.put("xCoordinate","0");
                    map.put("yCoordinate","0");
                    addNewAccount(context,map,Utility.getProperty("API_URL",context)+"Accounts");
                }else{
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static void addNewAccount(final Context context,final Map map,final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    account = Account.mapJson(new JSONObject(response));
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(account);
                    editor.putString("myAccount", json);
                    editor.apply();
                    Intent intent = new Intent(context,MyProfile.class);
                    context.startActivity(intent);
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
}
