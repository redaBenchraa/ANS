package com.example.reda_benchraa.asn.Adapters;
/*
 * Created by reda-benchraa on 23/04/17.
 */

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.Model.Message;
import com.example.reda_benchraa.asn.R;
import java.util.LinkedList;
import java.util.List;

public class conversationArrayAdapter extends ArrayAdapter<Message> {
    float account_id;
    LinkedList<Account> accounts;
    List<Message> messages;
    public conversationArrayAdapter(Context context, int resource, List<Message> items, float account_id,LinkedList<Account> accounts) {
        super(context, resource, items);
        this.account_id = account_id;
        this.accounts = accounts;
        messages = items;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater vi = LayoutInflater.from(getContext());
        if (v == null) {
            v = vi.inflate(R.layout.conversation_item_left, null);
        }
        Message message = getItem(position);
        if(message != null){
            if(message.getAccount_id() == account_id){
                v = vi.inflate(R.layout.conversation_item_right, null);
            }else{
                v = vi.inflate(R.layout.conversation_item_left, null);
            }
            TextView messageText = (TextView) v.findViewById(R.id.message);
            TextView publisher = (TextView) v.findViewById(R.id.publisher);
            ImageView image = (ImageView) v.findViewById(R.id.image);
            messageText.setText(message.getContent());
            for(Account account : accounts){
                Log.v("ConversationAdapter","Assert " + message.getAccount_id() + " " + account.getId());
                if(message.getAccount_id() == account.getId()){
                    publisher.setText(account.getFirstName());
                    if(account.getProfilePicture() != null){
                        image.setImageBitmap(BitmapFactory.decodeByteArray(account.getProfilePicture(),0,account.getProfilePicture().length));
                    }else{
                        image.setImageResource(R.mipmap.avatar);
                    }
                    break;
                }
            }
        }

        return v;
    }

}