package com.day.bean;

/**
 * @Author: day
 * @Date: Created in 2020/11/30 下午12:26
 * @Description: 作为账户类，用来存储用户的个人信息，包括用户名和密码两个属性
 * @Version: 1.0
 */
public class Account {

    private String user;
    private char[] password;

    public Account(String user, char[] password) {
        this.user = user;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }
}
