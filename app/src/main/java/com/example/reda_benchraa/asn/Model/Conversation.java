package com.example.reda_benchraa.asn.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class Conversation {

    // TODO fix webserivce, the include "containAccount" and "containMessage" do not work
    // TODO also, last_message shouldn't be String, instead it should be JSONObject : {Message
    // TODO so that it will be parsed using Message.mapJson(message);

    // TODO it should have a json Array containing all the messages

    long id;
    String lastMessage;


    public static Conversation mapJson(JSONObject object) throws JSONException, ParseException {
        Conversation conversation = new Conversation();

        // attributes

        conversation.id = object.getLong("id");
        conversation.lastMessage = object.getString("lastMessage");


        // includes
        // TODO fix the includes, more info above


        return conversation;
    }



    public Conversation() {
    }

//    public LinkedList<Account> getParticipants() {
//        return participants;
//    }
//
//    public void setParticipants(LinkedList<Account> participants) {
//        this.participants = participants;
//    }
//
//    public LinkedList<Message> getMessages() {
//        return messages;
//    }
//
//    public void setMessages(LinkedList<Message> messages) {
//        this.messages = messages;
//    }
}
