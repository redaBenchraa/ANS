package com.example.reda_benchraa.asn.Model;

import java.util.LinkedList;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class Conversation {
    LinkedList<Account> participants = new LinkedList<>();
    LinkedList<Message> messages = new LinkedList<>();

    public Conversation() {
    }

    public LinkedList<Account> getParticipants() {
        return participants;
    }

    public void setParticipants(LinkedList<Account> participants) {
        this.participants = participants;
    }

    public LinkedList<Message> getMessages() {
        return messages;
    }

    public void setMessages(LinkedList<Message> messages) {
        this.messages = messages;
    }
}
