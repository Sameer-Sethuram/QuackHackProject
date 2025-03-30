package com.example.billsplitter.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/*
Defines class structure for items in the bill.
 */

@Entity(foreignKeys = @ForeignKey(entity = Bill.class, onDelete = ForeignKey.CASCADE, parentColumns = "billId", childColumns = "itemBillId"), indices = @Index("itemBillId"))
public class Item {
    @PrimaryKey(autoGenerate = true)
    public int itemId;
    public int itemBillId;
    public double amount;
    public int purchaserId;
    public String displayName;

    public Item(){

    }
    public Item(int itemBillId, double amount, int purchaserId, String displayName){
        this.itemBillId = itemBillId;
        this.amount = amount;
        this.purchaserId = purchaserId;
        this.displayName = displayName;
    }

    public Item(int itemBillId, double amount, String displayName) {
        this.itemBillId = itemBillId;
        this.amount = amount;
        this.displayName = displayName;
        this.purchaserId = -1;
    }
}
