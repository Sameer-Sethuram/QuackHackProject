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
    public int owerId = -1;

    public String itemName;

    public boolean splitEvenly = false;

    public Item(){

    }

    public Item(int itemBillId, double base_amount, int purchaserId, String itemName){
        this.itemBillId = itemBillId;
        this.base_amount = base_amount;
        this.total_amount = -1;
        this.purchaserId = purchaserId;
        this.itemName = itemName;
    }

    /*public Item(int itemBillId, double amount) {
        this.itemBillId = itemBillId;
        this.amount = amount;
        this.purchaserId = -1;
    }*/
}
