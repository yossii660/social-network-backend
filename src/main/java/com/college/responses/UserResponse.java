package com.college.responses;

public class UserResponse extends BasicResponse {
    private Long id;
    private String username;
    private String profileImageUrl;
    private Integer followersCount;
    private Integer followingCount;
    private Integer postCount;

    public UserResponse(boolean success, Integer errorCode) {
        super(success, errorCode);
    }

    public UserResponse(boolean success, Integer errorCode, Long id, String username, String profileImageUrl,
                        Integer followersCount, Integer followingCount, Integer postCount) {
        super(success, errorCode);
        this.id = id;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.postCount = postCount;
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

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }

    public Integer getPostCount() {
        return postCount;
    }

    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }
}