package com.example.reda_benchraa.asn.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class Notification implements Serializable {
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Account getReceiver() {
        return receiver;
    }

    public void setReceiver(Account receiver) {
        this.receiver = receiver;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

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
        notification.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(object.getJSONObject("date").getString("date"));
        notification.seen = (object.getInt("seen") != 0 );


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
