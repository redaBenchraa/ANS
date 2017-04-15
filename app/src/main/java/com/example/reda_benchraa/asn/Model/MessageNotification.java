package com.example.reda_benchraa.asn.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;




public class MessageNotification {
    long id;
    String content;
    boolean seen;
    Account receiver;
    Message message;


    public static MessageNotification mapJson(JSONObject object) throws JSONException, ParseException {
        MessageNotification messageNotification = new MessageNotification();

        // attributes(

        messageNotification.id = object.getInt("id");
        messageNotification.content = object.getString("Content");
        messageNotification.seen = object.getBoolean("Seen");


        // includes


        if(object.has("message")){
            JSONObject messageObject = object.getJSONObject("message");
            messageNotification.message = Message.mapJson(messageObject);
        }
        if(object.has("receiver")){
            JSONObject receiverObject = object.getJSONObject("receiver");
            messageNotification.receiver = Account.mapJson(receiverObject);
        }


        return messageNotification;
    }


    public MessageNotification() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
