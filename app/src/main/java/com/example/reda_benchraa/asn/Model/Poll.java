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

    LinkedList<Long> voters_id = new LinkedList<>();

    public static Poll mapJson(JSONObject object) throws JSONException, ParseException {
        Poll poll = new Poll();

        // attributes(based on the attribute that are ready to test, in the api documentation)

        poll.id = object.getLong("id");
        poll.content = object.getString("Content");
        poll.vote = object.getInt("Vote");
        poll.post_id = object.getLong("Post");


        // TODO fix "include=posts" gives an ErorException in PollService.php line 50

        // includes
        if(object.has("voters")){
            // TODO shouldn't a vote be { account_id, poll_id, chosen_option_id } ?
            JSONArray votersArray = object.getJSONArray("voters");
            for (int i=0;i<votersArray.length();i++){
                poll.voters_id.add(((JSONObject) votersArray.get(i)).getLong("account"));
            }
        }

        return poll;
    }



    public Poll() {
    }

}
