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

    // GET ALL THE BILLS IN THE DATABASE
    @Query("SELECT * FROM bill")
    public abstract LiveData<List<Bill>> fetchAllBills();

    // GET THE BILL USING THE BILLID
    @Query("SELECT * FROM bill WHERE billId = :billId")
    public abstract Bill fetchBillUsingBillId(int billId);

    @Query("SELECT * FROM bill WHERE name = :billName")
    public abstract Bill fetchBillFromName(String billName);

    // GET PURCHASER ID USING THE BILL ID
    @Query("SELECT purchaserId FROM bill WHERE billId = :billId")
    public abstract int fetchPurchaserIdFromBillId(int billId);

    // GET BILL NAME USING THE BILL ID
    @Query("SELECT name FROM bill WHERE billId = :billId")
    public abstract String fetchBillName(int billId);

    // GET SUBTOTAL USING THE BILL ID
    @Query("SELECT subtotal FROM bill WHERE billId = :billId")
    public abstract double fetchSubtotal(int billId);

    // GET TOTAL USING THE BILL ID
    @Query("SELECT total FROM bill WHERE billId = :billId")
    public abstract double fetchTotal(int billId);

    // GET TIP USING THE BILL ID
    @Query("SELECT tip FROM bill WHERE billId = :billId")
    public abstract double fetchTip(int billId);

    // GET TAX USING THE BILL ID
    @Query("SELECT tax FROM bill WHERE billId = :billId")
    public abstract double fetchTax(int billId);


    // INSERT A NEW BILL INTO THE DATABASE
    @Insert
    protected abstract void insert(Bill bill);

    // UPDATE AN EXISTING BILL IN THE DATABASE
    @Update
    protected abstract void update(Bill bill);

    @Transaction
    public void upsert(Bill bill) {
        int id = bill.billId;
        if (id == 0) {
            insert(bill);
        }
        else {
            bill.billId = id;
            update(bill);
        }
    }
}
