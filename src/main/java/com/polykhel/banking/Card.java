package com.polykhel.banking;

public class Card {
    private int id;
    private String number;
    private String pin;
    private long balance;

    public Card(String number, String pin) {
        this.number = number;
        this.pin = pin;
    }

    public Card(int id, String number, String pin, long balance) {
        this.id = id;
        this.number = number;
        this.pin = pin;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}