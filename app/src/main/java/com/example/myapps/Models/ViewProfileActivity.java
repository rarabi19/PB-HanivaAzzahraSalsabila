package com.example.myapps.Models;

public class ViewProfileActivity {

    private String userId, username, userEmail, userPassword, userNIM;

    public ViewProfileActivity(){
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserNIM() {
        return userNIM;
    }

    public void setUserNIM(String userNIM) {
        this.userNIM = userNIM;
    }

    public ViewProfileActivity(String userId, String username, String userEmail, String userPassword, String userNIM) {
        this.userId = userId;
        this.username = username;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userNIM = userNIM;
    }


}