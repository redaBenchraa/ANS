package com.example.reda_benchraa.asn.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class Content implements Serializable {
    Account publisher;
    String text;
    //Attachement attachement;
    Date date;

    public Content() {
    }

    public Account getPublisher() {
        return publisher;
    }

    public void setPublisher(Account publisher) {
        this.publisher = publisher;
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
}

