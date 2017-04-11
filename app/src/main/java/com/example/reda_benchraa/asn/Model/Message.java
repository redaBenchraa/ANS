package com.example.reda_benchraa.asn.Model;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class Message {
    Conversation conversation; // Isn't this recursive ?

    public Message() {
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
}
