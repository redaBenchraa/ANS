package com.example.reda_benchraa.asn.Model;

import java.sql.Blob;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class Group {
    long id;
    String name;
    Account owner;
    Date creationDate;
    Blob groupPicture;
    String about;
    LinkedList<Account> admins;
    LinkedList<Account> members;
    LinkedList<Account> notifiedAccounts;// why this?
    LinkedList<Post> posts;
    LinkedList<Group> subGroups;
    Group superGroup;
    PublishingSettings publishingSettings;
    enum PublishingSettings { ADMINS_ONLY, ALL_MEMBERS }

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

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Blob getGroupPicture() {
        return groupPicture;
    }

    public void setGroupPicture(Blob groupPicture) {
        this.groupPicture = groupPicture;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
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

    public LinkedList<Account> getNotifiedAccounts() {
        return notifiedAccounts;
    }

    public void setNotifiedAccounts(LinkedList<Account> notifiedAccounts) {
        this.notifiedAccounts = notifiedAccounts;
    }

    public LinkedList<Post> getPosts() {
        return posts;
    }

    public void setPosts(LinkedList<Post> posts) {
        this.posts = posts;
    }

    public LinkedList<Group> getSubGroups() {
        return subGroups;
    }

    public void setSubGroups(LinkedList<Group> subGroups) {
        this.subGroups = subGroups;
    }

    public Group getSuperGroup() {
        return superGroup;
    }

    public void setSuperGroup(Group superGroup) {
        this.superGroup = superGroup;
    }

    public PublishingSettings getPublishingSettings() {
        return publishingSettings;
    }

    public void setPublishingSettings(PublishingSettings publishingSettings) {
        this.publishingSettings = publishingSettings;
    }
}
