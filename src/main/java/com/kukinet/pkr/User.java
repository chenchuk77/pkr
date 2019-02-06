package com.kukinet.pkr;

public class User {

    private String name;
    private String password;
    private int money;


    public User(){}

    public User(String name, String password, int money){
        this.name=name;
        this.password=password;
        this.money=money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}