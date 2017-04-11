package com.example.reda_benchraa.asn.Model;

import java.sql.Blob;
import java.util.LinkedList;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class Account {
    long id;
    String firstName;
    String secondName;
    String about;
    Blob profilePicture;
    String email;
    boolean showEmail;
    LinkedList<Group> createdGroups;
    LinkedList<Conversation> conversations;
    LinkedList<Notification> notifications;

    public Account() {
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

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
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
                ", secondName='" + secondName + '\'' +
                ", about='" + about + '\'' +
                ", profilePicture=" + profilePicture +
                ", email='" + email + '\'' +
                ", showEmail=" + showEmail +
                '}';
    }
}
