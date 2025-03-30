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

    @Query("SELECT * FROM item")
    public abstract LiveData<List<Item>> fetchAllItems();

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
        long id = item.itemId;
        if (id == 0) {
            // TODO
            insert(item);
        } else {
            // TODO
            item.itemId = id;
            update(item);
        }
    }

}

}
