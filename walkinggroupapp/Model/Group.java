package com.example.homepc.walkinggroupapp.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Group class for Serializing JSON from the server.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Group {
    private long id;
    private String groupDescription;

    private List<Double> routeLatArray = new ArrayList<>();
    private List<Double> routeLngArray = new ArrayList<>();

    private User leader;

    private List<User> memberUsers = new ArrayList<>();

    private String href;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public List<Double> getRouteLatArray() {
        return routeLatArray;
    }

    public void addRouteLat(Double lat) {
        this.routeLatArray.add(lat);
    }

    public List<Double> getRouteLngArray() {
        return routeLngArray;
    }

    public void addRouteLng(Double lng) {
        this.routeLngArray.add(lng);
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public List<User> getMemberUsers() {
        return memberUsers;
    }

    public void setMemberUsers(List<User> memberUsers) {
        this.memberUsers = memberUsers;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

}
