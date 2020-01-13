package com.bitc502.grapemarket.model;

public class CurrentUserInfo {

    private User user;

    private String jSessionId;

    private CurrentUserInfo(){
        user = new User();
    }

    private static CurrentUserInfo instance = new CurrentUserInfo();

    public static CurrentUserInfo getInstance(){
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setJSessionId(String jSessionId){
        this.jSessionId = jSessionId;
    }

    public String getJSessionId(){
        return jSessionId;
    }
}
