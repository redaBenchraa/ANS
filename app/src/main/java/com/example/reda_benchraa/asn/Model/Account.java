package com.example.reda_benchraa.asn.Model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Blob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class Account {
    long id;
    String firstName;
    String lastName;
    String about;
    Blob profilePicture;
    String email;
    boolean showEmail;
<<<<<<< HEAD
    float xCoordinate;
    float yCoordinate;
    LinkedList<Group> groups;
    LinkedList<Group> createdGroups;
    LinkedList<Conversation> conversations;
    LinkedList<Notification> notifications;
=======
    LinkedList<Group> createdGroups =new LinkedList<>();
    LinkedList<Conversation> conversations = new LinkedList<>();
    LinkedList<Notification> notifications = new LinkedList<>();
>>>>>>> master

    public Account() {
        groups = new LinkedList<>();
        createdGroups = new LinkedList<>();
        conversations = new LinkedList<>();
        notifications = new LinkedList<>();
    }
    public static Account mapJson(JSONObject object) throws JSONException, ParseException {
        Account account = new Account();
        account.id = Integer.parseInt(object.getString("id"));
        account.firstName = object.getString("firstName");
        account.lastName  = object.getString("lastName");
        account.about  = object.getString("About");
        account.email  = object.getString("Email");
//        account.showEmail  = (Integer.parseInt(object.getString("Email")) != 0);
        account.xCoordinate  = Float.parseFloat(object.getString("xCoordinate"));
        account.yCoordinate  = Float.parseFloat(object.getString("yCoordinate"));
        if(object.has("groups")){
            JSONArray groupArray = object.getJSONArray("groups");
            for (int i=0;i<groupArray.length();i++){
                JSONObject groupObject = (JSONObject) groupArray.get(i);
                // You can replace this with a better group mapper from the group model in the future
                Group group = new Group();
                group.id = Integer.parseInt(groupObject.getString("id"));
                group.name = groupObject.getString("Name");
                group.about = groupObject.getString("About");
              //  group.creationDate =  new SimpleDateFormat("yyyy/MM/dd").parse(groupObject.getString("createdDate"));
                account.groups.add(group);
            }
        }
        return account;
    }
    public LinkedList<Group> getGroups() {
        return groups;
    }

    public void setGroups(LinkedList<Group> groups) {
        this.groups = groups;
    }

    public float getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(float xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public float getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(float yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String secondName) {
        this.lastName = secondName;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Blob getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Blob profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LinkedList<Group> getCreatedGroups() {
        return createdGroups;
    }

    public void setCreatedGroups(LinkedList<Group> createdGroups) {
        this.createdGroups = createdGroups;
    }

    public boolean isShowEmail() {
        return showEmail;
    }

    public void setShowEmail(boolean showEmail) {
        this.showEmail = showEmail;
    }

    public LinkedList<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(LinkedList<Conversation> conversations) {
        this.conversations = conversations;
    }

    public LinkedList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(LinkedList<Notification> notifications) {
        this.notifications = notifications;
    }

    @Override
    public String toString() {
        return "Account{" +
                "firstName='" + firstName + '\'' +
                ", secondName='" + lastName + '\'' +
                ", about='" + about + '\'' +
                ", profilePicture=" + profilePicture +
                ", email='" + email + '\'' +
                ", showEmail=" + showEmail +
                '}';
    }
}
