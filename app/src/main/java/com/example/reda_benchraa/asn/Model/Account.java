package com.example.reda_benchraa.asn.Model;

import android.support.annotation.NonNull;
import android.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.text.ParseException;
import java.util.LinkedList;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class Account implements Serializable,Comparable<Account>{
    long id;
    String firstName;
    String lastName;
    String email;
    String about;
    String password;

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    byte[] profilePicture;
    boolean showEmail;
    float xCoordinate;
    float yCoordinate;

    LinkedList<Group> groups = new LinkedList<>();
    LinkedList<Group> administratedGroups = new LinkedList<>();
    LinkedList<Group> myGroups = new LinkedList<>();
    LinkedList<Group> createdGroups =new LinkedList<>();
    LinkedList<Conversation> conversations = new LinkedList<>();

    // TODO this is wrong conceptually; the account should have conversations and not sentMessages
    // Messages should be only accessible from within a conversation

    LinkedList<Message> sentMessages = new LinkedList<>();
    LinkedList<Post> posts = new LinkedList<>();
    LinkedList<Notification> notifications = new LinkedList<>();
    LinkedList<MessageNotification> messageNotifications = new LinkedList<>();
    LinkedList<Long> voteInPoll = new LinkedList<>();
    public Account() {

    }

    public static Account mapJson(JSONObject object) throws JSONException, ParseException {
        Account account = new Account();
        account.id = object.getLong("id");
        account.firstName = object.getString("firstName");
        account.lastName  = object.getString("lastName");
        account.about  = object.getString("About");
        account.email  = object.getString("Email");
        if(!object.isNull("Image")){
            account.profilePicture = Base64.decode(object.getString("Image"), Base64.DEFAULT);
        }
        account.showEmail  = (object.getInt("showEmail") != 0 );
        if(object.has("xCoordinate") && object.has("yCoordinate")){
            if(!object.isNull("xCoordinate")){
                account.xCoordinate  = Float.parseFloat(object.getString("xCoordinate"));
            }
            if(!object.isNull("yCoordinate")) {
                account.yCoordinate = Float.parseFloat(object.getString("yCoordinate"));
            }
        }


        // includes
        if(object.has("notifications")){
            JSONArray notificationsArray = object.getJSONArray("notifications");
            for (int i=0;i<notificationsArray.length();i++){
                JSONObject notificationObject = (JSONObject) notificationsArray.get(i);
                Notification notification = Notification.mapJson(notificationObject);
                account.notifications.addLast(notification);
            }
        }
        if(object.has("messageNotifications")){
            JSONArray messageNotificationsArray = object.getJSONArray("messageNotifications");
            for (int i=0;i<messageNotificationsArray.length();i++){
                JSONObject messageNotificationObject = (JSONObject) messageNotificationsArray.get(i);
                MessageNotification messageNotification = MessageNotification.mapJson(messageNotificationObject);
                account.messageNotifications.addLast(messageNotification);
            }
        }
        if(object.has("conversations")){
            JSONArray conversationsArray = object.getJSONArray("conversations");
            for (int i=0;i<conversationsArray.length();i++){
                JSONObject conversationObject = (JSONObject) conversationsArray.get(i);
                Conversation conversation = Conversation.mapJson(conversationObject);
                account.conversations.addLast(conversation);
            }
        }

        if(object.has("sentMessages")){
            JSONArray sentMessagesArray = object.getJSONArray("sentMessages");
            for (int i=0;i<sentMessagesArray.length();i++){
                JSONObject messageObject = (JSONObject) sentMessagesArray.get(i);
                Message message = Message.mapJson(messageObject);
                account.sentMessages.addLast(message);
            }
        }
        if(object.has("posts")){
            JSONArray postsArray = object.getJSONArray("posts");
            for (int i=0;i<postsArray.length();i++){
                JSONObject postObject = (JSONObject) postsArray.get(i);
                Post post = Post.mapJson(postObject);
                account.posts.addLast(post);
            }
        }
        if(object.has("groups")){
            JSONArray groupsArray = object.getJSONArray("groups");
            for (int i=0;i<groupsArray.length();i++){
                JSONObject groupObject = (JSONObject) groupsArray.get(i);
                Group group = Group.mapJson(groupObject);
                account.groups.addLast(group);
            }
        }
        if(object.has("administratedGroups")){
            JSONArray groupsArray = object.getJSONArray("administratedGroups");
            for (int i=0;i<groupsArray.length();i++){
                JSONObject groupObject = (JSONObject) groupsArray.get(i);
                Group group = Group.mapJson(groupObject);
                account.administratedGroups.addLast(group);
            }
        }
        if(object.has("createdGroups")){
            JSONArray groupsArray = object.getJSONArray("createdGroups");
            for (int i=0;i<groupsArray.length();i++){
                JSONObject groupObject = (JSONObject) groupsArray.get(i);
                Group group = Group.mapJson(groupObject);
                account.createdGroups.addLast(group);
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
    @Override
    public boolean equals(Object obj) {
        return (this.getEmail().equals(((Account)obj).getEmail()));
    }

    @Override
    public int compareTo(@NonNull Account o) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;
        if(o.getId() == this.getId()) return EQUAL;
        else if(o.getId() < this.getId()) return  BEFORE;
        else return AFTER;
    }
}
