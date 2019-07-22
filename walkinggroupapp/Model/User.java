package com.example.homepc.walkinggroupapp.Model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

/**
 * Using the given code by Dr. Fraser such that JSON serialization will work.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String birthYear;
    private String birthMonth;
    private String address;
    private String cellPhone;
    private String homePhone;
    private String grade;
    private String teacherName;
    private String emergencyContactInfo;

    private LastGpsLocation lastGpsLocation;

    private List<User> monitoredByUsers = new ArrayList<>();
    private List<User> monitorsUsers = new ArrayList<>();
    private List<Group> memberOfGroups = new ArrayList<>();
    private List<Group> leadsGroups = new ArrayList<>();

    private List<Message> unreadMessages = new ArrayList<>();
    private List<Message> readMessages = new ArrayList<>();
    private List<Permission> permissionStatus=new ArrayList<>();

    private Integer currentPoints;
    private Integer totalPointsEarned;
    private String customJson;

    private String href;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {this.id = id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getBirthMonth() {return birthMonth;}

    public void setBirthMonth(String birthMonth) {this.birthMonth = birthMonth;}

    public String getAddress() {return address;}

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getEmergencyContactInfo() {
        return emergencyContactInfo;
    }

    public void setEmergencyContactInfo(String emergencyContactInfo) { this.emergencyContactInfo = emergencyContactInfo; }

    public List<User> getMonitoredByUsers() { return monitoredByUsers; }

    public void setMonitoredByUsers(List<User> monitoredByUsers) { this.monitoredByUsers = monitoredByUsers; }

    public List<User> getMonitorsUsers() {
        return monitorsUsers;
    }

    public void setMonitorsUsers(List<User> monitorsUsers) {
        this.monitorsUsers = monitorsUsers;
    }

    public List<Group> getMemberOfGroups() {
        return memberOfGroups;
    }

    public void setMemberOfGroups(List<Group> memberOfGroups) { this.memberOfGroups = memberOfGroups; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public List<Group> getLeadsGroups() {
        return leadsGroups;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public void setLeadsGroups(List<Group> leadsGroups) {
        this.leadsGroups = leadsGroups;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public boolean searchId(long id) {
        boolean found = false;
        int index = 0;
        while(found == false && index < monitorsUsers.size()) {
            if(monitorsUsers.get(index).getId() == id) {
                found = true;
            }
            index++;
        }
        return found;
    }

    public List<Message> getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(List<Message> unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public List<Message> getReadMessages() {
        return readMessages;
    }

    public void setReadMessages(List<Message> readMessages) {
        this.readMessages = readMessages;
    }

    public LastGpsLocation getLastGpsLocation() {
        return lastGpsLocation;
    }

    public void setLastGpsLocation(LastGpsLocation lastGpsLocation) { this.lastGpsLocation = lastGpsLocation; }

    public Integer getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(Integer currentPoints) {
        this.currentPoints = currentPoints;
    }

    public Integer getTotalPointsEarned() {
        return totalPointsEarned;
    }

    public void setTotalPointsEarned(Integer totalPointsEarned) { this.totalPointsEarned = totalPointsEarned; }

    public String getCustomJson() { return customJson; }

    public void setCustomJson(String customJson) { this.customJson = customJson; }

    public List<Permission> getPermissionStatus() {
        return permissionStatus;
    }

    public void setPermissionStatus(List<Permission> permissionStatus) {
        this.permissionStatus = permissionStatus;
    }



    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birthYear='"+ birthYear + '\''+
                ", birthMonth='"+ birthMonth + '\''+
                ", address='"+ address + '\''+
                ", cellPhone='"+ cellPhone + '\''+
                ", grade='"+ grade + '\''+
                ", teacherName='"+ teacherName + '\''+
                ", emergencyContactInfo='"+ emergencyContactInfo + '\''+
                ", monitoredByUsers=" + monitoredByUsers +
                ", monitorsUsers=" + monitorsUsers +
                ", memberOfGroups=" + memberOfGroups +
                ", lastGpsLocation=" + lastGpsLocation +
                '}';
    }
}