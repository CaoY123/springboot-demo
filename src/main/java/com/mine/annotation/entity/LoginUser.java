package com.mine.annotation.entity;

/**
 * Created by xuhu on 2017/11/3.
 */
public class LoginUser {
    private String userAccount;
    private String userName;

    public LoginUser(String userAccount, String userName) {
        this.userAccount = userAccount;
        this.userName = userName;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
