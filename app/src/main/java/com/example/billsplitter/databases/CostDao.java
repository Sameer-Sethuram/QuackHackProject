package com.example.billsplitter.databases;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.billsplitter.entities.Cost;

import java.util.List;
@Dao
public abstract class CostDao {

    @Query("SELECT * FROM cost")
    public abstract LiveData<List<Cost>> fetchAllCosts();

    @Query("SELECT * FROM cost WHERE billId = :receipt")
    public abstract LiveData<List<Cost>> fetchCostsFromBill(int receipt);

}
