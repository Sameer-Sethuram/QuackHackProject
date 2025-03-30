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

    // GETS ALL THE COSTS IN THE DATABASE
    @Query("SELECT * FROM cost")
    public abstract LiveData<List<Cost>> fetchAllCosts();

    // GETS ALL THE COSTS FROM A CERTAIN BILL USING ITS BILLID
    @Query("SELECT * FROM cost WHERE billId = :receipt")
    public abstract LiveData<List<Cost>> fetchCostsFromBill(int receipt);

    // GETS THE OWINGUSERID USING A COSTID
    @Query("SELECT owingUserId FROM cost WHERE costId = :costId")
    public abstract int fetchOwingUserId(int costId);

    // GETS THE OWEDUSERID USING A COSTID
    @Query("SELECT owedUserId FROM cost WHERE costId = :costId")
    public abstract int fetchOwedUserId(int costId);

    // GETS THE ITEMID USING THE COSTID
    @Query("SELECT costItemId FROM cost WHERE costId = :costId")
    public abstract int fetchCostItemId(int costId);

    // GETS THE BILLID USING THE COSTID
    @Query("SELECT billId FROM cost WHERE costId = :costId")
    public abstract int fetchBillIdUsingCostId(int costId);

    // GETS THE AMOUNT USING THE COSTID
    @Query("SELECT amount FROM cost WHERE costId = :costId")
    public abstract double fetchAmountFromCostId(int costId);

    // GETS THE TYPE OF THE COST USING COSTID
    @Query("SELECT type FROM cost WHERE costId = :costId")
    public abstract String fetchTypeOfCost(int costId);

    // INSERTS A NEW COST INTO THE DATABASE
    @Insert
    protected abstract void insert(Cost cost);

    // UPDATES AN EXISTING COST INTO THE DATABASE
    @Update
    protected abstract void update(Cost cost);

    @Transaction
    public void upsert(Cost cost) {
        int id = cost.costId;
        if (id == 0) {
            insert(cost);
        }
        else {
            cost.costId = id;
            update(cost);
        }
    }

}
