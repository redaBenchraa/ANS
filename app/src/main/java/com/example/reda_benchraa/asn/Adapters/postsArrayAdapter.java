package com.example.reda_benchraa.asn.Adapters;
/*
 * Created by reda-benchraa on 23/04/17.
 */

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.reda_benchraa.asn.Model.Post;
import com.example.reda_benchraa.asn.R;

import java.util.List;

public class postsArrayAdapter extends ArrayAdapter<Post> {
    List<Post>posts ;
    static Context context;
    static int listViewTouchAction = 0;

    public postsArrayAdapter(Context context, int resource, List<Post> items) {
        super(context, resource, items);
        posts = items;
        this.context = context;
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
    static class postHeader{
        TextView group,publisher,time,content,popularity;
        ImageView groupImage,up,down,postImage;
        ListView pollsLv;
        pollArrayAdapter pollAdapter;
        postHeader(View view,Post post){
            group = (TextView) view.findViewById(R.id.groupName);
            publisher = (TextView) view.findViewById(R.id.post_item_publisher);
            time = (TextView) view.findViewById(R.id.post_item_date);
            content = (TextView) view.findViewById(R.id.textView2);
            popularity = (TextView) view.findViewById(R.id.textView3);
            up = (ImageView) view.findViewById(R.id.imageView);
            down = (ImageView) view.findViewById(R.id.imageView7);
            groupImage = (ImageView) view.findViewById(R.id.post_item_groupImage);
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
}