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
    public double base_amount;
    public double total_amount;
    public int purchaserId;
    public int owerId;
    public boolean splitEvenly = false;
    public String displayName;

    public Item(){

    }
    public Item(int itemBillId, double base_amount, int purchaserId, String displayName){
        this.itemBillId = itemBillId;
        this.base_amount = base_amount;
        this.total_amount = -1;
        this.purchaserId = purchaserId;
        this.displayName = displayName;
    }

    public Item(int itemBillId, double base_amount, String displayName) {
        this.itemBillId = itemBillId;
        this.base_amount = base_amount;
        this.displayName = displayName;
        this.purchaserId = -1;
    }
}
