package com.example.billsplitter.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Item.class, onDelete = ForeignKey.CASCADE, parentColumns = "itemId", childColumns = "costItemId"), indices = @Index("costItemId"))
public class Cost {
    @PrimaryKey(autoGenerate = true)
    public int costId;
    public int owingUserId;
    public int owedUserId;
    public int costItemId;
    public int billId;
    public double amount;
    public String type;
//    private String[] types = {"bill", "item", "tax", "tip"};

    public Cost(){

    }
    //Part of cost for non item
    public Cost(int owingUserId, int owedUserId, int billId, double amount, String type){
        this.owingUserId = owingUserId;
        this.owedUserId = owedUserId;
        this.billId = billId;
        this.amount = amount;
        this.type = type;
    }

    public Cost(int owingUserId, int owedUserId, int costItemId, int billId, double amount, String type){
        this.owingUserId = owingUserId;
        this.owedUserId = owedUserId;
        this.costItemId = costItemId;
        this.billId = billId;
        this.amount = amount;
        this.type = type;
    }

}
