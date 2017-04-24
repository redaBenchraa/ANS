package com.example.reda_benchraa.asn.Model;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class Message implements Serializable {

    long id;
    String content;
    Date date;
    long account_id;
    long conversation_id;
    Conversation conversation;
    Account account;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public long getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(long conversation_id) {
        this.conversation_id = conversation_id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    public static Message mapJson(JSONObject object) throws JSONException, ParseException {
        Message message = new Message();

        // attributes
        message.id = object.getLong("id");
        message.content = object.getString("Content");
        message.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(object.getString("created_at"));
        message.account_id = object.getLong("Account_id");
        message.conversation_id = object.getLong("Conversation_id");
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
