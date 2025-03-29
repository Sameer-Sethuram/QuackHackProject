package com.example.billsplitter.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.billsplitter.entities.Bank;
import com.example.billsplitter.entities.Bill;
import com.example.billsplitter.entities.Cost;
import com.example.billsplitter.entities.Item;
import com.example.billsplitter.entities.User;


@Database(entities = {Bank.class, Bill.class, Cost.class, Item.class, User.class}, version=1)
public abstract class TabDatabase extends RoomDatabase{
    private static final String DATABASE_NAME = "bills.db";
    private static TabDatabase instance;
    public abstract BankDao bankDao();
    public abstract BillDao billDao();
    public abstract CostDao costDao();
    public abstract ItemDao itemDao();
    public abstract UserDao userDao();

    public static TabDatabase getInstance(Context context){
        if(instance == null){
            //allowing main thread queries just for prototype
            instance = Room.databaseBuilder(context, TabDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        }
        return instance;
    }
}
