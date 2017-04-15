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


public class Group {


//    Account owner;
//    Date creationDate;
//    Blob groupPicture;
//    String about;
//    LinkedList<Account> admins = new LinkedList<>();
//    LinkedList<Account> members = new LinkedList<>();
//    LinkedList<Account> notifiedAccounts = new LinkedList<>();// why this?
//    LinkedList<Post> posts = new LinkedList<>();
//    LinkedList<Group> subGroups = new LinkedList<>();
//    Group superGroup;
//    PublishingSettings publishingSettings;
//    enum PublishingSettings { ADMINS_ONLY, ALL_MEMBERS }




    long id;
    String name;
    byte[] image;
    String about;
    Date creationDate;


    LinkedList<Post> posts = new LinkedList<>();
    LinkedList<Account> admins = new LinkedList<>();
    LinkedList<Account> members = new LinkedList<>();
    Account owner;


    // TODO superGroup column is missing from the database
    Group superGroup;

    // TODO "subs" include mentioned in the doc doesn't exists in the webservice nor in the database


    public static Group mapJson(JSONObject object) throws JSONException, ParseException {
        Group group = new Group();

        // attributes(based on the attribute that are ready to test, in the api documentation)

        group.id = object.getInt("id");
        group.name = object.getString("Name");
        if(!object.isNull("Image")){
            group.image = (byte[]) object.get("Image");
        }
        group.about = object.getString("About");
        group.creationDate = new SimpleDateFormat("yyyy-MM-dd").parse(object.getString("createdDate"));


        // includes as mentioned in the api doc


        if(object.has("owner")){
            JSONObject owner = object.getJSONObject("owner");
            group.owner = Account.mapJson(owner);
        }
        if(object.has("admins")){
            JSONArray adminsArray = object.getJSONArray("admins");
            for (int i=0;i<adminsArray.length();i++){
                JSONObject adminObject = (JSONObject) adminsArray.get(i);
                Account admin = Account.mapJson(adminObject);
                group.admins.add(admin);
            }
        }
        if(object.has("members")){
            JSONArray membersArray = object.getJSONArray("members");
            for (int i=0;i<membersArray.length();i++){
                JSONObject memberObject = (JSONObject) membersArray.get(i);
                Account member = Account.mapJson(memberObject);
                group.members.add(member);
            }
        }

        if(object.has("posts")){
            JSONArray postsArray = object.getJSONArray("posts");
            for (int i=0;i<postsArray.length();i++){
                JSONObject postObject = (JSONObject) postsArray.get(i);
                Post post = Post.mapJson(postObject);
                group.posts.add(post);
            }
        }

        return group;
    }



    public Group() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
