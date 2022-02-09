package com.unipr.bookblog.Models;

import com.google.firebase.database.ServerValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private String content;
    private String uid;
    private String uImg;
    private String uname;
    private Object timestamp;

    public Comment(String content, String uid, String uimg, String uname) {
        this.content = content;
        this.uid = uid;
        this.uImg = uimg;
        this.uname = uname;
        this.timestamp = ServerValue.TIMESTAMP;
    }
}
