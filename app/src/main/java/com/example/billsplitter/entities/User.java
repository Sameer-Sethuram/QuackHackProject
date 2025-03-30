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
    public int owedUserId;

    public User(){
        this.amountOwed = 0;
        this.amountOwing = 0;
        this.balance = 0;
    }

    public User(String userName, double amountOwed, double amountOwing, double balance){
        this.userName = userName;
        this.amountOwed = amountOwed;
        this.amountOwing = amountOwing;
        this.balance = balance;
    }

    //CONSTRUCTOR MOST USED
    public User(String userName){
        this.userName = userName;
        this.amountOwed = 0;
        this.amountOwing = 0;
        this.balance = 0;
    }

}
