package com.unipr.bookblog.Models;

public class AboutUs {
    private String id;
    private String description;
    private String item;
    private String group;
    private String email;
    private String website;
    private String youtube;
    private String playStore;
    private String instagram;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getPlayStore() {
        return playStore;
    }

    public void setPlayStore(String playStore) {
        this.playStore = playStore;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }
}

//.setDescription(" Add descrition about your app")
//        .addItem(new Element().setTitle("Version 1.0"))
//        .addGroup("CONNECT WITH US!")
//        .addEmail("Your mail id ")
//        .addWebsite("Your website/")
//        .addYoutube("UCbekhhidkzkGryM7mi5Ys_w")   //Enter your youtube link here (replace with my channel link)
//        .addPlayStore("com.example.yourprojectname")   //Replace all this with your package name
//        .addInstagram("jarves.usaram")    //Your instagram id