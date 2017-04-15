package com.example.reda_benchraa.asn.Model;

import android.graphics.Bitmap;

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

public class Post {

    // fields based on the sql table defintions

    long id;
    String content;
    byte[] image;
    byte[] file;
    int type;
    Date postingDate;
    int popularity;
    int account_id;
    int grp_id;
    Date created_at;
    Date updated_at;

    // list of includes based on the api documentation

    LinkedList<Comment> comments = new LinkedList<>();
    LinkedList<Poll> polls = new LinkedList<>();
    Group group;
    Account account;


//    TODO what is a reaction ?
    LinkedList<Reaction> reactions = new LinkedList<>();


    public static Post mapJson(JSONObject object) throws JSONException, ParseException {
        Post post = new Post();

        // attributes(based on the attribute that are ready to test, in the api documentation)

        post.id = object.getInt("id");
        post.content = object.getString("content");
        post.file = (byte[]) object.get("file");        // TODO check file and image type, is it really byte[] or Bitmap or Blob ? something else ?
        post.image = (byte[]) object.get("Image");
        post.type = object.getInt("Type");
        post.postingDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(object.getString("postingDate"));
        post.popularity = object.getInt("popularity");
        post.account_id = object.getInt("Account");
        post.grp_id = object.getInt("Group");       // TODO check why the webservice always returns posts with "Group":null


        // includes as mentioned in the api doc

        if(object.has("comments")){
            JSONArray commentsArray = object.getJSONArray("comments");
            for (int i=0;i<commentsArray.length();i++){
                JSONObject commentObject = (JSONObject) commentsArray.get(i);
                Comment comment = Comment.mapJson(commentObject);
                post.comments.add(comment);
            }
        }
        if(object.has("polls")){
            JSONArray pollsArray = object.getJSONArray("polls");
            for (int i=0;i<pollsArray.length();i++){
                JSONObject pollObject = (JSONObject) pollsArray.get(i);
                Poll poll = Poll.mapJson(pollObject);
                post.polls.add(poll);
            }
        }
        if(object.has("group")){
            JSONObject groupObject = object.getJSONObject("group");
            post.group = Group.mapJson(groupObject);
        }
        if(object.has("account")){
            JSONObject accountObject = object.getJSONObject("account");
            post.account = Account.mapJson(accountObject);
        }
        if(object.has("reactions")){
            JSONArray reactionsArray = object.getJSONArray("reactions");
            for (int i=0;i<reactionsArray.length();i++){
                JSONObject reactionObject = (JSONObject) reactionsArray.get(i);
                Reaction reaction = Reaction.mapJson(reactionObject);
                post.reactions.add(reaction);
            }
        }


        return post;
    }



    public Post() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public LinkedList<Comment> getComments() {
        return comments;
    }

    public void setComments(LinkedList<Comment> comments) {
        this.comments = comments;
    }
}
