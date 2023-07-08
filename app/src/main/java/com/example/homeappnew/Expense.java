package com.example.homeappnew;

public class Expense {
    private String title;
    private String amount;
    private int id;
    public Expense(String title, String amount ,int id) {
        this.title = title;
        this.amount = amount;
        this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getId(){
        return id;

    }

    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "Expense: " + title + " - Amount: " + amount;
    }
}

