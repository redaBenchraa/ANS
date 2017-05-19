package com.example.reda_benchraa.asn.Adapters;
/*
 * Created by reda-benchraa on 23/04/17.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
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
import com.example.reda_benchraa.asn.Model.Post;
import com.example.reda_benchraa.asn.Model.Reaction;
import com.example.reda_benchraa.asn.R;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class postsArrayAdapter extends ArrayAdapter<Post> {
    List<Post>posts ;
    static Context context;
    static int listViewTouchAction = 0;
    Account account;
    public postsArrayAdapter(Context context, int resource, List<Post> items, Account account) {
        super(context, resource, items);
        posts = items;
        this.context = context;
        this.account = account;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater vi = LayoutInflater.from(getContext());
        if (v == null) {
            v = vi.inflate(R.layout.post_item, null);
        }
        Post post = getItem(position);
        postHeader header;
        switch (post.getType()){
            case 0 :
                v = vi.inflate(R.layout.post_item, null);
                postHeader postheader = new postHeader(v,post);
                break;
            case 1 :
                v = vi.inflate(R.layout.post_attachement_item, null);
                postHeader fileheader = new postHeader(v,post);
                break;
            case 2 :
                v = vi.inflate(R.layout.post_photo_item, null);
                postHeader imageheader = new postHeader(v,post);
                break;
            case 3 :
                v = vi.inflate(R.layout.post_file_request_item, null);
                postHeader filerequestheader = new postHeader(v,post);
                break;
            case 4 :
                v = vi.inflate(R.layout.post_poll_item, null);
                postHeader pollheader = new postHeader(v,post);
                break;
        }
        return v;
    }
    class postHeader{
        TextView group,publisher,content,popularity;
        ImageView groupImage,up,down,postImage,viewMore;
        ListView pollsLv;
        pollArrayAdapter pollAdapter;
        int reaction = 0;
        postHeader(View view, final Post post){
            for(Reaction react : post.getReactions()){
                if(react.getAccount_id() == account.getId()){
                    reaction = react.getType();
                }
            }
            group = (TextView) view.findViewById(R.id.groupName);
            viewMore = (ImageView) view.findViewById(R.id.viewMore);
            publisher = (TextView) view.findViewById(R.id.post_item_publisher);
            content = (TextView) view.findViewById(R.id.textView2);
            popularity = (TextView) view.findViewById(R.id.textView3);
            up = (ImageView) view.findViewById(R.id.imageView);
            down = (ImageView) view.findViewById(R.id.imageView7);
            groupImage = (ImageView) view.findViewById(R.id.post_item_groupImage);
            viewMore.setImageResource(R.drawable.ic_add);
            if(reaction == 1){
                up.setImageResource(R.drawable.ic_upactivated);
            }else if(reaction == 2){
                down.setImageResource(R.drawable.ic_downactivated);
            }else{
                up.setImageResource(R.drawable.ic_up);
                down.setImageResource(R.drawable.ic_down);
            }
            viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context.getApplicationContext(),com.example.reda_benchraa.asn.post.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("Post",post)
                            .putExtra("Account",account));
                }
            });
            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(reaction !=1){
                        up.setImageResource(R.drawable.ic_upactivated);
                        down.setImageResource(R.drawable.ic_down);
                        Map map = new HashMap<String, String>();
                        map.put("accountId", Long.toString(account.getId()));
                        map.put("Type", "1");
                        reactInPost(context,map,Utility.getProperty("API_URL",context)+"Posts/"+post.getId()+"/addReactingAccount");
                        popularity.setText(Integer.parseInt(popularity.getText().toString())+1 +"");
                        reaction = 1;
                    }
                }
            });
            down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(reaction !=2) {
                        down.setImageResource(R.drawable.ic_downactivated);
                        up.setImageResource(R.drawable.ic_up);
                        Map map = new HashMap<String, String>();
                        map.put("accountId", Long.toString(account.getId()));
                        map.put("Type", "2");
                        reactInPost(context, map, Utility.getProperty("API_URL", context) + "Posts/" + post.getId() + "/addReactingAccount");
                        popularity.setText(Integer.parseInt(popularity.getText().toString()) - 1 + "");
                        reaction = 2;
                    }
                }
            });
            switch (post.getType()){
                case 2 :
                    postImage = (ImageView) view.findViewById(R.id.photo_image);
                break;
                case 4 :
                    pollsLv = (ListView) view.findViewById(R.id.listView_polls);
                    break;
            }
            map(post);
        }
        void map(Post post){
            //time.setText(post.getPostingDate().toString());
            group.setText(post.getGroup().getName());
            publisher.setText(post.getAccount().getFirstName() + " " +post.getAccount().getLastName());
            content.setText(post.getContent());
            popularity.setText(post.getPopularity()+"");
            if(post.getAccount().getProfilePicture() != null)
                groupImage.setImageBitmap(BitmapFactory.decodeByteArray(post.getAccount().getProfilePicture(),0,post.getAccount().getProfilePicture().length));
            else groupImage.setImageResource(R.mipmap.avatar);
            switch (post.getType()){
                case 2 :
                    if(post.getImage() != null)
                        postImage.setImageBitmap(BitmapFactory.decodeByteArray(post.getImage(), 0, post.getImage().length));
                    break;
                case 4 :
                    pollAdapter = new pollArrayAdapter(context, R.layout.post_poll_item_layout, post.getPolls());
                    pollsLv.setAdapter(pollAdapter);
                    pollsLv.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            listViewTouchAction = event.getAction();
                            if (listViewTouchAction == MotionEvent.ACTION_MOVE) {
                                pollsLv.scrollBy(0, 1);
                            }
                            return false;
                        }
                    });

                    pollsLv.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {

                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                            if (listViewTouchAction == MotionEvent.ACTION_MOVE) {
                                pollsLv.scrollBy(0, -1);
                            }
                        }
                    });
                    break;
            }
        }
    }
    public void reactInPost(final Context context, final Map map, final String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.v("Post",response);
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