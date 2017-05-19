package com.example.reda_benchraa.asn.Model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Reaction implements Serializable {
    private long account_id;
    private int type;

    public Reaction(){

    }
    public Reaction(long account_id, int type) {
        this.account_id = account_id;
        this.type = type;
    }
    public Reaction(long account_id) {
        this.account_id = account_id;
    }


    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public static Reaction mapJson(JSONObject object) throws JSONException, ParseException {
        Reaction reaction = new Reaction();

        // attributes from returned json
        reaction.account_id = object.getLong("Account_id");
        reaction.type = object.getInt("Type");

        return reaction;
    }
}
