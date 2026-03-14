package com.college.responses;

import java.util.List;

public class PostsResponse extends BasicResponse {
    private List<PostItem> posts;

    public PostsResponse(boolean success, Integer errorCode, List<PostItem> posts) {
        super(success, errorCode);
        this.posts = posts;
    }

    public List<PostItem> getPosts() {
        return posts;
    }

    public void setPosts(List<PostItem> posts) {
        this.posts = posts;
    }
}