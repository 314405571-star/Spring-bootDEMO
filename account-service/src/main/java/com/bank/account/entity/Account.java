package com.bank.account.entity;
//银行账户
public class Account {
    private Long id;
    private String name;
    private double balance;
    public Account() {
    }
    public Account(Long id, String name, double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
