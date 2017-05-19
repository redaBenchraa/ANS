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

import com.example.reda_benchraa.asn.Model.Group;
import com.example.reda_benchraa.asn.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rabab Chahboune on 5/19/2017.
 */

public class deleteGroupArrayAdapter  extends ArrayAdapter {

    private LayoutInflater inflater;
    private List<Group> groups = new ArrayList<>();
    String id;


    public deleteGroupArrayAdapter(@NonNull Context context,int resource, List<Group> groups) {
        super(context, resource, groups);
        this.inflater = inflater;
        this.groups.addAll(groups);
    }
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null)
        {
            holder = new ViewHolder();
            inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.remove_group_item, null);
            holder.name = (TextView)convertView.findViewById(R.id.removeGroup_name);
            holder.image = (ImageView) convertView.findViewById(R.id.ramoveGroup_image);
            holder.delete = (ImageButton) convertView.findViewById(R.id.removeGroup_remove);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(holder.name!=null){
            holder.name.setText(groups.get(position).getName());
        }
        if(holder.image!=null && groups.get(position).getImage()!=null){
            Bitmap bm = BitmapFactory.decodeByteArray(groups.get(position).getImage(), 0 ,groups.get(position).getImage().length);
            holder.image.setImageBitmap(bm);
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(groups.get(position));
            }
        });


        return convertView;
    }
    static class ViewHolder
    {
        public TextView name ;
        public ImageView image;
        public ImageButton delete;
    }

    @Override
    public void remove(@Nullable Object object) {
        super.remove(object);
        if(groups != null){
            groups.remove((Group) object);
        }
    }
}
