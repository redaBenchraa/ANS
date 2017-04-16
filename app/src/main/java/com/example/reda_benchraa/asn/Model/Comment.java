package com.example.reda_benchraa.asn.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class Comment {

    long id;
    String content;
    byte[] file;
    int type;
    Date postingDate;       // TODO why is this date an JSONObject and not a String like the date in Pot ?
    int popularity;
    int account_id;
    int post_id;


    LinkedList<Reaction> reactions = new LinkedList<>();


    public static Comment mapJson(JSONObject object) throws JSONException, ParseException {
        Comment comment = new Comment();

        // attributes(based on the attribute that are ready to test, in the api documentation)

        comment.id = object.getInt("id");
        comment.content = object.getString("content");
        comment.file = (byte[]) object.get("File");        // TODO check file and image type, is it really byte[] or Bitmap or Blob ? something else ?
        comment.type = object.getInt("Type");
        comment.postingDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(object.getJSONObject("postingDate").getString("date"));
        comment.popularity = object.getInt("popularity");
        comment.account_id = object.getInt("Account");
        comment.post_id = object.getInt("Post");

        // TODO fix includes because they are not woking in the webservice
        // ErrorException in CommentService.php line 72:
        return comment;
    }
    public Comment() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
