package com.example.reda_benchraa.asn.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Reaction {

    long account_id;
    int type;

    // TODO : could not find reaction in database. Though, they show up in the json returned


    public static Reaction mapJson(JSONObject object) throws JSONException, ParseException {
        Reaction reaction = new Reaction();

        // attributes from returned json
        reaction.account_id = object.getLong("Account_id");
        reaction.type = object.getInt("type");

        return null;
    }
}
