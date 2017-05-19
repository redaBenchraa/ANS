package com.example.reda_benchraa.asn.Adapters;
/*
 * Created by reda-benchraa on 23/04/17.
 */

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.reda_benchraa.asn.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class groupsArrayAdapter extends ArrayAdapter<Group> {
    List<Group> groups;
    Account account;
    View v;
    public groupsArrayAdapter(Context context, int resource, List<Group> items, Account account) {
        super(context, resource, items);
        groups = items;
        this.account = account;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        v = convertView;
        LayoutInflater vi = LayoutInflater.from(getContext());
        if (v == null) {
            v = vi.inflate(R.layout.group_item_add, null);
        }
        final Group group = getItem(position);
        ImageView image = (ImageView) v.findViewById(R.id.group_item_image);
        TextView name = (TextView) v.findViewById(R.id.group_item_name);
        Button add = (Button) v.findViewById(R.id.group_item_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap<String,String>();
                map.put("memberId",account.getId());
                addToGroup(getContext(),new HashMap(), Utility.getProperty("API_URL",getContext())+"Groups/"+group.getId()+"/addMember" );
            }
        });
        name.setText(group.getName());
        if(group.getImage() != null)
            image.setImageBitmap(BitmapFactory.decodeByteArray(group.getImage(),0,group.getImage().length));
        else
            image.setImageResource(R.mipmap.avatar);
        return v;
    }
    public void addToGroup(final Context context, final Map map, final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ((Button) v.findViewById(R.id.group_item_add)).setVisibility(View.GONE);
                }catch (Exception e){
                    Toast.makeText(context, "Error parsing the groups", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error fetching groups", Toast.LENGTH_SHORT).show();
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