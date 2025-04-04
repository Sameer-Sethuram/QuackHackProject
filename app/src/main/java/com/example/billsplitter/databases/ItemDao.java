package com.example.billsplitter.databases;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.billsplitter.entities.Item;

import java.util.List;
@Dao
public abstract class ItemDao {

    // GETS ALL THE ITEMS IN THE DATABASE
    @Query("SELECT * FROM item")
    public abstract LiveData<List<Item>> fetchAllItems();

    // GETS AN ITEM BASED ON ITS ITEMID
    @Query("SELECT * FROM item WHERE itemId = :itemId")
    public abstract Item fetchItemById(int itemId);

    // GETS THE AMOUNT OF AN ITEM BASED ON ITS ITEMID
    @Query("SELECT base_amount FROM item WHERE itemId = :itemId")
    public abstract double fetchItemAmount(int itemId);

    @Query("SELECT total_amount FROM item WHERE owerId =:owingUserId")
    public abstract List<Double> fetchAmountsFromOwingUserId(int owingUserId);

    @Query("SELECT total_amount FROM item WHERE purchaserId =:owedUserId")
    public abstract List<Double> fetchAmountsFromOwedUserId(int owedUserId);

    // GETS THE ITEM BASED ON THE PURCHASER ID
    @Query("SELECT * FROM item WHERE purchaserId = :purchaserId")
    public abstract Item fetchItemWithPurchaseId(int purchaserId);

    // GETS THE PURCHASER ID BASED ON THE ITEMID
    @Query("SELECT purchaserId FROM item WHERE itemId = :itemId")
    public abstract int fetchPurchaserId(int itemId);

    // GETS ALL THE ITEMS UNDER A CERTAIN BILLID
    @Query("SELECT * FROM item WHERE itemBillId = :billId")
    public abstract LiveData<List<Item>> fetchItemsUnderBill(int billId);

    /**
     * Inserts an item if it isn't in the database.
     * @param item
     */
    @Insert
    protected abstract void insert(Item item);


    /**
     * Updates an item using the item as a parameter.
     * @param item
     */
    @Update
    protected abstract void update(Item item);

    /**
     * Add an item record if it doesn't exist yet.
     * Update the item record if it does exist.
     * @param item
     */
    @Transaction
    public void upsert(Item item) {
        int id = item.itemId;
        if (id == 0) {
            insert(item);
        } else {
            item.itemId = id;
            update(item);
        }
    }

}
