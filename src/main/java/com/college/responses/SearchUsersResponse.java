package com.college.responses;

import java.util.List;

public class SearchUsersResponse extends BasicResponse {
    private List<SearchUserItem> users;

    public SearchUsersResponse(boolean success, Integer errorCode, List<SearchUserItem> users) {
        super(success, errorCode);
        this.users = users;
    }

    public List<SearchUserItem> getUsers() {
        return users;
    }

    public void setUsers(List<SearchUserItem> users) {
        this.users = users;
    }
}