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

    public Item(){

    }
    public Item(int itemId, int itemBillId, double amount, int purchaserId){
        this.itemId = itemId;
        this.itemBillId = itemBillId;
        this.amount = amount;
        this.purchaserId = purchaserId;
    }

    public Item(int itemId, int itemBillId, double amount) {
        this.itemId = itemId;
        this.itemBillId = itemBillId;
        this.amount = amount;
        this.purchaserId = -1;
    }
}
