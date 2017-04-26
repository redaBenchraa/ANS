package com.example.reda_benchraa.asn.Adapters;
/*
 * Created by reda-benchraa on 23/04/17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.reda_benchraa.asn.Model.Group;
import com.example.reda_benchraa.asn.Model.Notification;
import com.example.reda_benchraa.asn.Model.Post;
import com.example.reda_benchraa.asn.R;

import java.util.List;

public class postsArrayAdapter extends ArrayAdapter<Post> {
    List<Post>posts ;
    public postsArrayAdapter(Context context, int resource, List<Post> items) {
        super(context, resource, items);
        posts = items;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater vi = LayoutInflater.from(getContext());
        if (v == null) {
            v = vi.inflate(R.layout.notification_item_seen, null);
        }
        Post post = getItem(position);
        TextView name = (TextView) v.findViewById(R.id.notification);
        name.setText(post.getContent());
        return v;
    }

}