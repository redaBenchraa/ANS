package com.example.reda_benchraa.asn.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
import com.example.reda_benchraa.asn.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rabab Chahboune on 4/27/2017.
 */

public class requestArrayAdapter extends ArrayAdapter {

    private LayoutInflater inflater;
    private List<Account> Accounts = new ArrayList<>();
    String id;
    public requestArrayAdapter(Context context, int resource, List<Account> Accounts,String id) {
        super(context, resource, Accounts);
        this.Accounts.addAll(Accounts);
        this.id = id;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        requestArrayAdapter.ViewHolder holder = null;
        if(convertView == null)
        {
            holder = new requestArrayAdapter.ViewHolder();
            inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.join_group_request_item, null);
            holder.name = (TextView)convertView.findViewById(R.id.joinGroup_name);
            holder.email = (TextView) convertView.findViewById(R.id.joinGroup_email);
            holder.image = (ImageView) convertView.findViewById(R.id.joinGroup_image);
            holder.accepted = (ImageButton) convertView.findViewById(R.id.joinGroup_accepted);
            holder.rejected = (ImageButton) convertView.findViewById(R.id.joinGroup_rejected);
            convertView.setTag(holder);
        } else {
            holder = (requestArrayAdapter.ViewHolder) convertView.getTag();
        }
        if(holder.name!=null){
            holder.name.setText(Accounts.get(position).getFirstName()+" "+Accounts.get(position).getLastName());
        }
        if(holder.email!=null){
            holder.email.setText(Accounts.get(position).getEmail());
        }
        if(holder.image!=null && Accounts.get(position).getProfilePicture()!=null){
            Bitmap bm = BitmapFactory.decodeByteArray(Accounts.get(position).getProfilePicture(), 0 ,Accounts.get(position).getProfilePicture().length);
            holder.image.setImageBitmap(bm);
        }
        holder.accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap<String,String>();
                map.put("memberId",Long.toString(Accounts.get(position).getId()));
                updateMember(getContext(),map, Utility.getProperty("API_URL",getContext())+"Groups/"+id+"/updateMember");
                remove(Accounts.get(position));
            }
        });
        holder.rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap<String,String>();
                map.put("memberId",Long.toString(Accounts.get(position).getId()));
                deleteMember(getContext(),map, Utility.getProperty("API_URL",getContext())+"Groups/"+id+"/removeMember");
                remove(Accounts.get(position));
            }
        });

        return convertView;
    }
    static class ViewHolder
    {
        public TextView name ;
        public TextView email;
        public ImageView image;
        public ImageButton accepted;
        public ImageButton rejected;
    }

    @Override
    public void remove(@Nullable Object object) {
        super.remove(object);
        if(Accounts != null){
            Accounts.remove((Account) object);
            notifyDataSetChanged();
        }
    }
    private void updateMember(final Context context, final Map<String, String> map, final String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "Account added to group", Toast.LENGTH_LONG).show();
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
    private void deleteMember(final Context context, final Map<String, String> map, final String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "Request deleted", Toast.LENGTH_LONG).show();
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
