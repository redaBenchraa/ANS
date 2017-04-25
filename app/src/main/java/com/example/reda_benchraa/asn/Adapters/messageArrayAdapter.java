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

import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.Model.Conversation;
import com.example.reda_benchraa.asn.R;

import java.util.List;

public class messageArrayAdapter extends ArrayAdapter<Conversation> {
    public messageArrayAdapter(Context context, int resource, List<Conversation> items) {
        super(context, resource, items);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.my_message_item, null);
        }
        Conversation conversation = getItem(position);
        if (conversation != null) {
            StringBuilder name = new StringBuilder();
            TextView names = (TextView) v.findViewById(R.id.message_item_fullNames);
            TextView lastMessage = (TextView) v.findViewById(R.id.message_item_lastMessage);
            ImageView image = (ImageView) v.findViewById(R.id.message_item_image);
            if(conversation.getAccounts().getLast().getProfilePicture() != null){
                image.setImageBitmap(BitmapFactory.decodeByteArray(conversation.getAccounts().getLast().getProfilePicture(),0,conversation.getAccounts().getLast().getProfilePicture().length));
            }
            for (Account account : conversation.getAccounts())
                name.append(" "+account.getFirstName());
            names.setText(name);
            if(conversation.getMessages().isEmpty()){
                lastMessage.setText(" ");
            }else{
                lastMessage.setText(conversation.getMessages().getLast().getContent());
            }
        }

        return v;
    }

}