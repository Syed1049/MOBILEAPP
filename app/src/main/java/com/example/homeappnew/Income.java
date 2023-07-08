package com.example.homeappnew;

public class Income {
    private String name;
    private String amount;
    private int userId;
    private int incomeId;

    public Income(int incomeId,String name, String amount, int userId) {
        this.incomeId=incomeId;
        this.name = name;
        this.amount = amount;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setIncomeId(int incomeId){
        this.incomeId=incomeId;
    }
    public int getIncomeId() {
        return incomeId;
    }

}
