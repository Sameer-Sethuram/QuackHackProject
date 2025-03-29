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

@Dao
public abstract class BankDao {
}
