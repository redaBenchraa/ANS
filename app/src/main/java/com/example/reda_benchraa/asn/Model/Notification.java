package com.example.reda_benchraa.asn.Model;

import java.util.Date;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class Notification {
    long id;
    long postId;
    String text;
    Date date;
    boolean seen;
    // Link to where the notification leads

    public Notification() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
