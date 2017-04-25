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
import com.example.reda_benchraa.asn.R;

import java.util.List;

public class myGroupsArrayAdapter extends ArrayAdapter<Group> {
    List<Group> groups;
    public myGroupsArrayAdapter(Context context, int resource, List<Group> items) {
        super(context, resource, items);
        groups = items;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater vi = LayoutInflater.from(getContext());
        if (v == null) {
            v = vi.inflate(R.layout.group_item, null);
        }
        Group group = getItem(position);
        ImageView image = (ImageView) v.findViewById(R.id.group_item_image);
        TextView name = (TextView) v.findViewById(R.id.group_item_name);
        name.setText(group.getName());
        if(group.getImage() != null)
            image.setImageBitmap(BitmapFactory.decodeByteArray(group.getImage(),0,group.getImage().length));
        else
            image.setImageResource(R.mipmap.avatar);
        return v;
    }

}