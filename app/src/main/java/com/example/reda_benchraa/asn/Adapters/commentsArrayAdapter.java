package com.example.reda_benchraa.asn.Adapters;
/*
 * Created by reda-benchraa on 23/04/17.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.reda_benchraa.asn.DAO.Utility;
import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.Model.Comment;
import com.example.reda_benchraa.asn.Model.Comment;
import com.example.reda_benchraa.asn.Model.Reaction;
import com.example.reda_benchraa.asn.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class commentsArrayAdapter extends ArrayAdapter<Comment> {
    List<Comment> comments ;
    static Context context;
    Account account;
    public commentsArrayAdapter(Context context, int resource, List<Comment> items, Account account) {
        super(context, resource, items);
        comments = items;
        this.context = context;
        this.account = account;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater vi = LayoutInflater.from(getContext());
        if (v == null) {
            v = vi.inflate(R.layout.comment_item, null);
        }
        Comment comment = getItem(position);
        commentHeader header = new commentHeader(v,comment);
        switch (comment.getType()){

        }
        return v;
    }
    class commentHeader{
        TextView publisher,content,popularity;
        Button up,down;
        ImageView download;
        int reaction = 0;
        commentHeader(View view, final Comment comment){
            for(Reaction react : comment.getReactions()){
                if(react.getAccount_id() == account.getId()){
                    reaction = react.getType();
                }
            }
            publisher = (TextView) view.findViewById(R.id.textView10);
            content = (TextView) view.findViewById(R.id.textView11);
            popularity = (TextView) view.findViewById(R.id.textView3);
            up = (Button) view.findViewById(R.id.up);
            down = (Button) view.findViewById(R.id.down);
            if(reaction == 1){
                up.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
            }else if(reaction == 2){
                down.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
            }else{
                up.setTextColor(getContext().getResources().getColor(R.color.grey));
                down.setTextColor(getContext().getResources().getColor(R.color.grey));
            }
            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map map = new HashMap<String, String>();
                    map.put("accountId", Long.toString(account.getId()));
                    map.put("Type", "1");
                    if(reaction !=1){
                        up.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                        down.setTextColor(getContext().getResources().getColor(R.color.grey));
                        reactInComment(context, map, Utility.getProperty("API_URL", context) + "Comments/" + comment.getId() + "/removeReact");
                        reactInComment(context,map,Utility.getProperty("API_URL",context)+"Comments/"+comment.getId()+"/reactedToBy");
                        if(reaction == 0){
                            popularity.setText(Integer.parseInt(popularity.getText().toString())+1 +"");
                        }else {
                            popularity.setText(Integer.parseInt(popularity.getText().toString())+2 +"");
                        }
                        reaction = 1;
                    }else{
                        map.put("delete", "0");
                        popularity.setText(Integer.parseInt(popularity.getText().toString()) - 1 +"");
                        up.setTextColor(getContext().getResources().getColor(R.color.grey));
                        reactInComment(context, map, Utility.getProperty("API_URL", context) + "Comments/" + comment.getId() + "/removeReact");
                        reaction = 0;
                    }
                }
            });
            down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map map = new HashMap<String, String>();
                    map.put("accountId", Long.toString(account.getId()));
                    map.put("Type", "2");
                    if(reaction !=2) {
                        up.setTextColor(getContext().getResources().getColor(R.color.grey));
                        down.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                        reactInComment(context, map, Utility.getProperty("API_URL", context) + "Comments/" + comment.getId() + "/removeReact");
                        reactInComment(context, map, Utility.getProperty("API_URL", context) + "Comments/" + comment.getId() + "/reactedToBy");
                        if(reaction == 0){
                            popularity.setText(Integer.parseInt(popularity.getText().toString()) - 1 + "");
                        }else {
                            popularity.setText(Integer.parseInt(popularity.getText().toString()) - 2 + "");
                        }
                        reaction = 2;
                    }else{
                        map.put("delete", "0");
                        popularity.setText(Integer.parseInt(popularity.getText().toString())  + 1 + "");
                        down.setTextColor(getContext().getResources().getColor(R.color.grey));
                        reactInComment(context, map, Utility.getProperty("API_URL", context) + "Comments/" + comment.getId() + "/removeReact");
                        reaction = 0;
                    }
                }
            });
            if(comment.getType() == 2) {
                download  = (ImageView) view.findViewById(R.id.imageView9);
            }
            map(comment);
        }
        void map(Comment comment){
            //time.setText(comment.getCommentingDate().toString());
            publisher.setText(comment.getPublisher());
            content.setText(comment.getContent());
            popularity.setText(comment.getPopularity()+"");
            if(comment.getType() == 2) {
                    //downaload
            }
        }
    }
    public void reactInComment(final Context context, final Map map, final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.v("Comment",response);
                }catch (Exception e){
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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