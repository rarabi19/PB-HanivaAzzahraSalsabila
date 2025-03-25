package com.example.myapps.Models;

public class UserDetails {

    private String userId;
    private String username;
    private String userEmail;
    private String userPassword;
    private String userNIM;

    public UserDetails() {
    }

    public UserDetails(String userId, String username, String userEmail, String userPassword, String userNIM) {
        this.userId = userId;
        this.username = username;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userNIM = userNIM;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserNIM() {
        return userNIM;
    }

    public void setUserNIM(String userNIM) {
        this.userNIM = userNIM;
    }
}
