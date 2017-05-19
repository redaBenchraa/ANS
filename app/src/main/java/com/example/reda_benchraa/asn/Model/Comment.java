package com.example.reda_benchraa.asn.Model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class Comment implements Serializable {

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Date postingDate) {
        this.postingDate = postingDate;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public LinkedList<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(LinkedList<Reaction> reactions) {
        this.reactions = reactions;
    }

    long id;
    String content;
    byte[] file;
    int type;
    Date postingDate;
    int popularity;

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    String Publisher;
    int post_id;


    LinkedList<Reaction> reactions = new LinkedList<>();


    public static Comment mapJson(JSONObject object) throws JSONException, ParseException {
        Comment comment = new Comment();

        // attributes(based on the attribute that are ready to test, in the api documentation)

        comment.id = object.getInt("id");
        comment.content = object.getString("Content");
        if(!object.isNull("File")){
            comment.file = (byte[]) object.get("File");
        }
        comment.type = object.getInt("Type");
        comment.postingDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(object.getString("created_at"));
        comment.popularity = object.getInt("Popularity");
        if(object.has("Publisher")){
            comment.Publisher = object.getString("Publisher");
        }
        comment.post_id = object.getInt("Post_id");
        Log.v("Comment",object.getJSONArray("Reactions").toString());
        if(object.has("Reactions")){
            JSONArray reactionsArray = object.getJSONArray("Reactions");
            for (int i=0;i<reactionsArray.length();i++){
                JSONObject reactionObject = (JSONObject) reactionsArray.get(i);
                Reaction reaction = Reaction.mapJson(reactionObject);
                comment.reactions.addLast(reaction);
            }
        }
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
