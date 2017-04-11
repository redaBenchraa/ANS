package com.example.reda_benchraa.asn.Model;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class Comment {
    long id;
    Content content;
    Post post; // Isn't this recursive ?

    public Comment() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
