package com.college.responses;

public class LoginResponse extends BasicResponse {

    private Long userId;
    private String username;

    public LoginResponse(boolean success, Integer errorCode, Long userId, String username) {
        super(success, errorCode);
        this.userId = userId;
        this.username = username;
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
}