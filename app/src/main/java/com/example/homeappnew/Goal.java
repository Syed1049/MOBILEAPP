package com.example.homeappnew;
public class Goal {
    private long id;
    private String name;
    private double amount;
    private int userId;

    public Goal(long id, String name, double amount, int userId) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return name + " - $" + amount;
    }
}
