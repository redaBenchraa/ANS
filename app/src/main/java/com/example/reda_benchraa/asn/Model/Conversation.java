package com.example.reda_benchraa.asn.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class Conversation implements Serializable {

    // TODO also, last_message shouldn't be String, instead it should be JSONObject : {Message
    // TODO so that it will be parsed using Message.mapJson(message);
    // {"id":1,"lastMessage":"velit","href":"http:\/\/127.0.0.1:8000\/api\/v1\/Conversations\/1"}

    long id;
    LinkedList<Message> messages = new LinkedList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LinkedList<Message> getMessages() {
        return messages;
    }

    public void setMessages(LinkedList<Message> messages) {
        this.messages = messages;
    }

    public LinkedList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(LinkedList<Account> accounts) {
        this.accounts = accounts;
    }

    LinkedList<Account> accounts = new LinkedList<>();
    public static Conversation mapJson(JSONObject object) throws JSONException, ParseException {
        Conversation conversation = new Conversation();

        // attributes
        conversation.id = object.getLong("id");
        // includes
        if(object.has("messages")){
            JSONArray messagesArray = object.getJSONArray("messages");
            for (int i=0;i<messagesArray.length();i++){
                JSONObject messageObject = (JSONObject) messagesArray.get(i);
                Message message = Message.mapJson(messageObject);
                conversation.messages.add(message);
            }
        }
        if(object.has("accounts")){
            JSONArray accountsArray = object.getJSONArray("accounts");
            for (int i=0;i<accountsArray.length();i++){
                JSONObject accountObject = (JSONObject) accountsArray.get(i);
                Account account = Account.mapJson(accountObject);
                conversation.accounts.add(account);
            }
        }



        return conversation;
    }



    public Conversation() {
    }
}
