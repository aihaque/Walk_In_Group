package com.example.homepc.walkinggroupapp.Model;

import java.util.List;

/**
 * Singleton class for the current user user of the App.
 */
public class App {
    private static App instance;

    private String apiKey;
    private String token;
    private User currentUser;
    private double groupLatitude;
    private double groupLongitude;
    private boolean isUserLoggedIn;
    private Group currGroup;
    private Group startWalkingWithTheGroup;
    private double lat;
    private double lng;
    private int InitialNumberOfGroupMembers;
    private Group creatingGroup;
    private List<Message> messages;
    private App() {
        apiKey = "62B6DC1D-AE49-4CF4-9EA5-17FF243A9FE0";
        token = null;
        currentUser = null;
        isUserLoggedIn = false;
    }

    public static App getInstance() {
        if(instance == null) {
            instance = new App();
        }
        return instance;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getCurrentUser() {
        return currentUser;
    }


    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String getCurrentUserName() {
        return currentUser.getName();
    }

    public boolean getisUserLoggedIn() { return isUserLoggedIn; }

    public void setisUserLoggedIn(boolean userLoggenIn) { isUserLoggedIn = userLoggenIn; }

    public Long getCurrentUserId() {
        return currentUser.getId();
    }

    public String getCurrentUserEmail() {
        return currentUser.getEmail();
    }

    public String getCurrentUserCellPhone() {
        return currentUser.getCellPhone();
    }

    public String getCurrentUserGrade(){return currentUser.getGrade();}

    public String getCurrentUserTeacherName(){return currentUser.getTeacherName();}

    public String getCurrentUserEmergencyContactInfo(){return currentUser.getEmergencyContactInfo();}

    public String getCurrentUserAddress() {
        return currentUser.getAddress();
    }

    public String getCurrentUserHomePhone(){return currentUser.getHomePhone();}

    public String getCurrentUserBirthYear(){return currentUser.getBirthYear();}

    public String getCurrentUserBirthMonth(){return currentUser.getBirthMonth();}

    public List<User> getCurrentUserMonitor() {
        return currentUser.getMonitorsUsers();
    }

    public void setCurrentUserMonitor(List<User> userList) {
        currentUser.setMonitorsUsers(userList);
    }

    public List<User> getCurrentUserMonitoredBy() {
        return currentUser.getMonitoredByUsers();
    }

    public void setCurrentUserMonitoredBy(List<User> userList) {
        currentUser.setMonitoredByUsers(userList);
    }

    public void setCurrGroup(Group currGroup){this.currGroup=currGroup;}

    public Group getCurrGroup(){return this.currGroup;}

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Group getCreatingGroup() {
        return creatingGroup;
    }

    public void setCreatingGroup(Group creatingGroup) {
        this.creatingGroup = creatingGroup;
    }

    public int getInitialNumberOfGroupMembers() {
        return InitialNumberOfGroupMembers;
    }

    public void setInitialNumberOfGroupMembers(int InitialNumberOfGroupMembers) {
        this.InitialNumberOfGroupMembers = InitialNumberOfGroupMembers;
    }

    public double getGroupLatitude() {
        return groupLatitude;
    }

    public void setGroupLatitude(double groupLatitude) {
        this.groupLatitude = groupLatitude;
    }

    public double getGroupLongitude() {
        return groupLongitude;
    }

    public void setGroupLongitude(double groupLongitude) {
        this.groupLongitude = groupLongitude;
    }
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public boolean hasNoParents() {
        return currentUser.getMonitoredByUsers().size() == 0;
    }

    public boolean hasNoGroups() {
        return currentUser.getMemberOfGroups().size() == 0;
    }

    public Group getStartWalkingWithTheGroup() {
        return startWalkingWithTheGroup;
    }

    public void setStartWalkingWithTheGroup(Group startWalkingWithTheGroup) {
        this.startWalkingWithTheGroup = startWalkingWithTheGroup;
    }
}
