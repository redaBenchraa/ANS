package com.example.reda_benchraa.asn.Model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class Message {

    long id;
    String content;     // TODO this is a problem, the content should be an object that can contain the same things as a post, to allow sending files in messages
    Date date;
    long account_id;
    long conversation_id;

    Conversation conversation; // Isn't this recursive ?
    Account account;


    public static Message mapJson(JSONObject object) throws JSONException, ParseException {
        Message message = new Message();

        // attributes

        message.id = object.getLong("id");
        message.content = object.getString("Content");
        // TODO why date is returned as an object in json ? in Posts it is returned as a string
        message.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(object.getJSONObject("date").getString("date"));
        message.account_id = object.getLong("account");
        message.conversation_id = object.getLong("conversation");

        // includes

        if(object.has("Account")){
            JSONObject accountObject = object.getJSONObject("Account");
            message.account = Account.mapJson(accountObject);
        }
        if(object.has("Conversation")){
            JSONObject conversationObject = object.getJSONObject("Conversation");
            message.conversation = Conversation.mapJson(conversationObject);
        }


        return message;
    }

    public Message() {
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
}
