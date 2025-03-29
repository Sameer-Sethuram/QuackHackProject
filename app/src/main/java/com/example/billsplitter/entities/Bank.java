package com.example.billsplitter.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = User.class, onDelete=ForeignKey.CASCADE, parentColumns="userId", childColumns="bankerUserId"), indices = @Index("bankerUserId"))
public class Bank {
    @PrimaryKey(autoGenerate = true)
    public int bankId;
    public int bankerUserId;
    private int bankAccount;
    private int routingNumber;

    public String toString() {return String.valueOf(bankId);}
    public int getBankAccount(){return bankAccount;}
    public int getRoutingNumber(){return routingNumber;}

    public void setBankAccount(int bankAccount){this.bankAccount = bankAccount;}
    public void setRoutingNumber(int routingNumber){this.routingNumber = routingNumber;}

    public Bank(){

    }

    public Bank(int bankId, int bankerUserId, int bankAccount, int routingNumber){
        this.bankId = bankId;
        this.bankerUserId = bankerUserId;
        this.bankAccount = bankAccount;
        this.routingNumber = routingNumber;
    }
}
