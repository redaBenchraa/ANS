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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.R;
import java.util.ArrayList;
import java.util.List;

public class newMessageAddContactArrayAdapter extends ArrayAdapter<Account> {
    ArrayList<Account> checkedAccounts;
    List<Account> accounts;
    public newMessageAddContactArrayAdapter(Context context, int resource, List<Account> items, ArrayList<Account> accounts) {
        super(context, resource, items);
        this.accounts = items;
        checkedAccounts = accounts;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater vi = LayoutInflater.from(getContext());
        if (v == null) {
            v = vi.inflate(R.layout.new_message_add_contact, null);
        }
        Account account = getItem(position);
        if(account != null){
            TextView name = (TextView) v.findViewById(R.id.contact_name);
            TextView email = (TextView) v.findViewById(R.id.email);
            TextView id = (TextView) v.findViewById(R.id.id);
            ImageView image = (ImageView) v.findViewById(R.id.contact_image);
            CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox);
            if(account.getProfilePicture() != null){
                image.setImageBitmap(BitmapFactory.decodeByteArray(account.getProfilePicture(),0,account.getProfilePicture().length));
            }else{
                image.setImageResource(R.mipmap.avatar);
            }
            if(checkedAccounts.contains(account)){
                checkBox.setChecked(true);
            }else{
                checkBox.setChecked(false);
            }
            name.setText(account.getFirstName() + " " + account.getLastName());
            email.setText(account.getEmail());
            id.setText(account.getId()+"");
            checkBox.setEnabled(false);
        }

        return v;
    }

}