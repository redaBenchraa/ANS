package com.example.reda_benchraa.asn.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rabab Chahboune on 4/23/2017.
 */

public class accountArrayAdapter extends ArrayAdapter {
    private LayoutInflater inflater;
    private List<Account> Accounts = new ArrayList<Account>();
    public accountArrayAdapter(Context context,int resource,List<Account> Accounts) {
        super(context, resource, Accounts);
        this.Accounts = Accounts;
    }

    @NonNull
    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.account_item, null);
            holder.name = (TextView)convertView.findViewById(R.id.account_item_name);
            holder.image = (ImageView) convertView.findViewById(R.id.account_item_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(holder.name!=null){
            holder.name.setText(Accounts.get(position).getFirstName()+" "+Accounts.get(position).getLastName());
        }
        if(holder.image!=null){
            Bitmap bm = BitmapFactory.decodeByteArray(Accounts.get(position).getProfilePicture(), 0 ,Accounts.get(position).getProfilePicture().length);
            holder.image.setImageBitmap(bm);
        }
        return convertView;
    }
    static class ViewHolder
    {
        public TextView name ;
        public ImageView image;
    }
}
