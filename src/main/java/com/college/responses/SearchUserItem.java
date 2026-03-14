package com.college.responses;

public class SearchUserItem {
    private Long id;
    private String username;
    private String profileImageUrl;
    private boolean followedByMe;

    public SearchUserItem() {
    }

    public SearchUserItem(Long id, String username, String profileImageUrl, boolean followedByMe) {
        this.id = id;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.followedByMe = followedByMe;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public boolean isFollowedByMe() {
        return followedByMe;
    }

    public void setFollowedByMe(boolean followedByMe) {
        this.followedByMe = followedByMe;
    }
}