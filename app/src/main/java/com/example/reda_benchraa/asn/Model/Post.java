package com.example.reda_benchraa.asn.Model;

import android.graphics.Bitmap;
import android.util.Base64;

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

public class Post implements Serializable {

    // fields based on the sql table defintions
    long id;
    String content;
    byte[] image;
    byte[] file;
    int type;
    Date postingDate;
    int popularity;
    Date created_at;
    Date updated_at;

    // list of includes based on the api documentation

    LinkedList<Comment> comments = new LinkedList<>();
    LinkedList<Poll> polls = new LinkedList<>();
    Group group;
    Account account;


    LinkedList<Reaction> reactions = new LinkedList<>();

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public LinkedList<Poll> getPolls() {
        return polls;
    }

    public void setPolls(LinkedList<Poll> polls) {
        this.polls = polls;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LinkedList<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(LinkedList<Reaction> reactions) {
        this.reactions = reactions;
    }




    public static Post mapJson(JSONObject object) throws JSONException, ParseException {
        Post post = new Post();

        // attributes(based on the attribute that are ready to test, in the api documentation)

        post.id = object.getInt("id");
        post.content = object.getString("Content");
        if(!object.isNull("file")){
            post.file = Base64.decode(object.getString("File"), Base64.DEFAULT);
        }
        if(!object.isNull("Image")){
            post.image = Base64.decode(object.getString("Image"), Base64.DEFAULT);
        }
        post.type = object.getInt("Type");
        post.postingDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(object.getString("postingDate"));
        post.popularity = object.getInt("popularity");
        if(object.has("poster"))
            post.account = Account.mapJson(object.getJSONObject("poster"));
        if(object.has("group"))
            post.group = Group.mapJson(object.getJSONObject("group"));


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
            // TODO should the reaction JSONObject have post_id or comment_id ?
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
