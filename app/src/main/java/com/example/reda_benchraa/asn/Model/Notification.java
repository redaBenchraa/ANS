package com.example.reda_benchraa.asn.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class Notification {
    long id;
    String content;
    Date date;
    boolean seen;
    Account receiver;
    Post post;

//    long postId;
//    String text;
//    Date date;
//    boolean seen;
    // Link to where the notification leads



    public static Notification mapJson(JSONObject object) throws JSONException, ParseException {
        Notification notification = new Notification();

        // attributes(

        notification.id = object.getInt("id");
        notification.content = object.getString("Content");
        notification.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(object.getString("dateAndTime"));
        notification.seen = object.getBoolean("Seen");


        // includes


        if(object.has("post")){
            JSONObject postObject = object.getJSONObject("post");
            notification.post = Post.mapJson(postObject);
        }
        if(object.has("receiver")){
            JSONObject receiverObject = object.getJSONObject("receiver");
            notification.receiver = Account.mapJson(receiverObject);
        }


        return notification;
    }


    public Notification() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
