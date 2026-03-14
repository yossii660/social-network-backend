package com.college.responses;

public class PostItem {
    private Long id;
    private Long userId;
    private String username;
    private String profileImageUrl;
    private String content;
    private String createdAt;
    private Integer likesCount;
    private boolean likedByMe;
    private boolean ownedByMe;

    public PostItem() {
    }

    public PostItem(Long id, Long userId, String username, String profileImageUrl,
                    String content, String createdAt, Integer likesCount,
                    boolean likedByMe, boolean ownedByMe) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.content = content;
        this.createdAt = createdAt;
        this.likesCount = likesCount;
        this.likedByMe = likedByMe;
        this.ownedByMe = ownedByMe;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public boolean isLikedByMe() {
        return likedByMe;
    }

    public void setLikedByMe(boolean likedByMe) {
        this.likedByMe = likedByMe;
    }

    public boolean isOwnedByMe() {
        return ownedByMe;
    }

    public void setOwnedByMe(boolean ownedByMe) {
        this.ownedByMe = ownedByMe;
    }
}