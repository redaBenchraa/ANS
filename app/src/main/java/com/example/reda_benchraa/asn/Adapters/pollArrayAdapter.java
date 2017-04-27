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
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.Model.Poll;
import com.example.reda_benchraa.asn.R;

import java.util.ArrayList;
import java.util.List;

public class pollArrayAdapter extends ArrayAdapter<Poll> {
    List<Poll> Polls;
    public pollArrayAdapter(Context context, int resource, List<Poll> items) {
        super(context, resource, items);
        this.Polls = items;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater vi = LayoutInflater.from(getContext());
        if (v == null) {
            v = vi.inflate(R.layout.post_poll_item_layout, null);
        }
        Poll poll = getItem(position);
        TextView pollPop = (TextView) v.findViewById(R.id.textView_vote);
        CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox_name);
        if(poll != null){
            checkBox.setText(poll.getContent());
            pollPop.setText(Integer.toString(poll.getVote()));
        }
        return v;
    }

}