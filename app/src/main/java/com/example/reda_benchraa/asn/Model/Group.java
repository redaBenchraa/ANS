package com.example.reda_benchraa.asn.Model;

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


public class Group implements Serializable {


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


    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public LinkedList<Post> getPosts() {
        return posts;
    }

    public void setPosts(LinkedList<Post> posts) {
        this.posts = posts;
    }

    public LinkedList<Account> getAdmins() {
        return admins;
    }

    public void setAdmins(LinkedList<Account> admins) {
        this.admins = admins;
    }

    public LinkedList<Account> getMembers() {
        return members;
    }

    public void setMembers(LinkedList<Account> members) {
        this.members = members;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public Group getSuperGroup() {
        return superGroup;
    }

    public void setSuperGroup(Group superGroup) {
        this.superGroup = superGroup;
    }

    long id;
    String name;
    byte[] image;
    String about;
    Date creationDate;


    LinkedList<Post> posts = new LinkedList<>();
    LinkedList<Account> admins = new LinkedList<>();
    LinkedList<Account> members = new LinkedList<>();
    Account owner;


    // TODO superGroup column is missing from the JSONObject
    Group superGroup;

    // TODO "subs" include is NOT showing in the JSONObject, check example:
    // TODO super missing from JSONObject
    //http://127.0.0.1:8000/api/v1/Groups/1?include=super,subs
    //[{"id":1,"Name":"Miss Caleigh Heathcote PhD","Image":null,"About":"vero","CreationDate":"2012-04-06","href":"http:\/\/127.0.0.1:8000\/api\/v1\/Groups\/1"}]

    // TODO is subs = subgroups ? why then the subs JSONObject doesn't have same structure as a group ? (it has Accout_id and Grp_id)
    // shown in the photo on the report

    // TODO in attribute it should have Grp_super_id = "1" and Grp_super = {Group} (both of them are missing)
    public static Group mapJson(JSONObject object) throws JSONException, ParseException {
        Group group = new Group();

        // attributes(based on the attribute that are ready to test, in the api documentation)

        group.id = object.getInt("id");
        group.name = object.getString("Name");
        if(!object.isNull("Image")){
            group.image = Base64.decode(object.getString("Image"), Base64.DEFAULT);
        }
        group.about = object.getString("About");
        group.creationDate = new SimpleDateFormat("yyyy-MM-dd").parse(object.getString("creationDate"));


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
