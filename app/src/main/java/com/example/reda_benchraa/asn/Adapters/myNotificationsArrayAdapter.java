package com.example.reda_benchraa.asn.Adapters;
/*
 * Created by reda-benchraa on 23/04/17.
 */

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reda_benchraa.asn.Model.Group;
import com.example.reda_benchraa.asn.Model.Notification;
import com.example.reda_benchraa.asn.R;

import java.util.List;

public class myNotificationsArrayAdapter extends ArrayAdapter<Notification> {
    List<Notification> notifications;
    public myNotificationsArrayAdapter(Context context, int resource, List<Notification> items) {
        super(context, resource, items);
        notifications = items;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater vi = LayoutInflater.from(getContext());
        if (v == null) {
            v = vi.inflate(R.layout.notification_item_seen, null);
        }
        Notification notification = getItem(position);
        if(notification.isSeen()){
            v = vi.inflate(R.layout.notification_item_seen, null);
        }else{
            v = vi.inflate(R.layout.notification_item, null);
        }
        TextView name = (TextView) v.findViewById(R.id.notification);
        TextView date = (TextView) v.findViewById(R.id.notification_sharedAt);
        name.setText(notification.getContent());
        return v;
    }

}