package com.example.billsplitter.databases;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.billsplitter.entities.Bill;

import java.util.List;

@Dao
public abstract class BillDao {
    /**
     * Get all the bills in the database.
     * @return
     */
    @Query("SELECT * FROM bill");
    public abstract LiveData<List<Bill>> fetchAllBills();

    @Insert
    protected abstract void insert(Bill bill);

    @Update
    protected abstract void update(Bill bill);

    @Transaction
    public void upsert(Bill bill) {
        long id = bill.billId;
        if (id == 0) {
            insert(bill);
        }
        else {
            bill.billId = id;
            update(bill);
        }
    }
}
