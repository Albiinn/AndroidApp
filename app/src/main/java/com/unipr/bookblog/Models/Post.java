package com.unipr.bookblog.Models;

import com.google.firebase.database.ServerValue;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Post {
    private String postKey;
    private String title;
    private String description;
    private String picture;
    private String userId;
    private String userPhoto;
    private Object timeStamp;

    public Post(String title, String description, String picture, String userId, String userPhoto) {
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.timeStamp = ServerValue.TIMESTAMP;
    }
}
