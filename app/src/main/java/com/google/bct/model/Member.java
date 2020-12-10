package com.google.bct.model;

public class Member {


    private int activated;
    private String name;
    private String date;
    private String email;
    private String exp;
    private String phone;
    private String password;
    public String avatarUrl;
    public String carType;
    public String homeTown;
    public String introduceCode;
    public String myCode;
    public String rates;
    public String reserved;

    public Member() {
    }

    public Member(int activated, String name, String date, String email, String exp, String phone, String password) {
        this.activated = activated;
        this.name = name;
        this.date = date;
        this.email = email;
        this.exp = exp;
        this.phone = phone;
        this.password = password;
    }

    public int getActivated() {
        return activated;
    }

    public void setActivated(int activated) {
        this.activated = activated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}