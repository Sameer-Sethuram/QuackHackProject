package com.example.billsplitter.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = @Index(value="userId", unique = true))
public class User {
    @PrimaryKey(autoGenerate = true)
    public int userId;
    public String userName;
    public double amountOwed;
    public double amountOwing;
    public double balance;

    public User(){

    }

    public User(int userId, String userName, double amountOwed, double amountOwing, double balance){
        this.userId = userId;
        this.userName = userName;
        this.amountOwed = amountOwed;
        this.amountOwing = amountOwing;
        this.balance = balance;
    }
}
