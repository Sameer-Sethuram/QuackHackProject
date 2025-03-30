package com.example.billsplitter.databases;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.billsplitter.entities.Bank;

import java.util.List;

import com.example.billsplitter.entities.Bank;

import java.util.List;
@Dao
public abstract class BankDao {

    // SELECT ALL BANKING INFORMATION IN THE DATABASE
    @Query("SELECT * FROM bank")
    public abstract LiveData<List<Bank>> fetchAllBankingInfo();

    // SELECT A USERS BANKING INFORMATION BASED ON THEIR BANKID
    @Query("SELECT * FROM bank WHERE bankId = :bankId")
    public abstract Bank fetchBankingWithBankId(int bankId);

    // SELECT A USERS BANKING INFORMATION BASED ON THEIR USERID
    @Query("SELECT * FROM bank WHERE bankerUserId = :userId")
    public abstract Bank fetchBankingWithUserId(int userId);

    // INSERT NEW USER BANKING INFORMATION INTO THE DATABASE
    @Insert
    protected abstract void insert(Bank bank);

    // UPDATE EXISTING USER BANKING INFORMATINO IN THE DATABASE
    @Update
    protected abstract void update(Bank bank);

    /**
     * INSERTS THE BANKING INFORMATION TO THE DATABASE IF ID IS NOT VALID
     * UPDATES THE BAKNING INFORMATINO IN THE DATABASE IF ID IS VALID
     * @param bank
     */
    @Transaction
    public void upsert(Bank bank) {
        int id = bank.bankId;
        if (id == 0) {
            insert(bank);
        }
        else {
            bank.bankId = id;
            update(bank);
        }

    }


}
