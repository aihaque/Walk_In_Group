package com.example.homepc.walkinggroupapp.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * To be used as needed.
 */

public class Authorizor {
    private List<User> users = new ArrayList<>();
    private String status;
    private List<User> whoApprovedOrDenied = new ArrayList<>();

    public Authorizor() {

    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<User> getWhoApprovedOrDenied() {
        return whoApprovedOrDenied;
    }

    public void setWhoApprovedOrDenied(List<User> whoApprovedOrDenied) {
        this.whoApprovedOrDenied = whoApprovedOrDenied;
    }
}
