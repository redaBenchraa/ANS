package com.example.reda_benchraa.asn.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class Poll {
//    LinkedList<PollOption> pollOptions = new LinkedList<>();
//    PollPrivacy pollPrivacy;
//
//    class PollOption {
//        String text;
//        LinkedList<Account> voters;
//    }
//
//    enum PollPrivacy { VISIBLE_VOTERS, ANONYMOUS_VOTERS }
//
//    enum PollType { ONLY_ONE_CHOICE, MANY_CHOICES}
//
//

    long id;
    String content;
    int vote;
    long post_id;


    public static Poll mapJson(JSONObject object) throws JSONException, ParseException {
        Poll poll = new Poll();

        // attributes(based on the attribute that are ready to test, in the api documentation)

        poll.id = object.getLong("id");
        poll.content = object.getString("Content");
        poll.vote = object.getInt("Vote");
        poll.post_id = object.getLong("Post");


        // TODO fix includes in the webservice because they give an ErorException in PollService.php line 50

        return poll;
    }



    public Poll() {
    }

}
