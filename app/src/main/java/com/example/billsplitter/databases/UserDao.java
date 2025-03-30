package com.example.billsplitter.databases;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.billsplitter.entities.User;

import java.util.List;

@Dao
public abstract class UserDao {

    @Query("SELECT * FROM user")
    public abstract LiveData<List<User>> fetchAllUsers();

    @Query("SELECT * FROM user WHERE userId = :userId")
    public abstract LiveData<List<User>> fetchUserById(int userId);

    @Query("SELECT * FROM user WHERE userName = :userName")
    public abstract LiveData<List<User>> fetchUserByName(String userName);

    @Query("SELECT * FROM user WHERE amountOwed > 0")
    public abstract LiveData<List<User>> usersWhoOwe();

    @Query("SELECT * FROM user WHERE amountOwing > 0")
    public abstract LiveData<List<User>> usersWhoAreOwed();

    @Query("UPDATE User SET amountOwed = :amountOwed, amountOwing = :amountOwing, balance = :balance WHERE userId = :userId")
    void updateUserFinance(int userId, double amountOwed, double amountOwing, double balance);

    // DELETE A USER FROM THE DATABASE
    @Query("DELETE FROM User WHERE userId = :userId")
    void deleteUserById(int userId);


    @Insert
    protected abstract void insert(User user);

    /**
     * Updates an item using the item as a parameter.
     * @param item
     */
    @Update
    protected abstract void update(User user);

    /**
     * Add an item record if it doesn't exist yet.
     * Update the item record if it does exist.
     * @param item
     */

    @Transaction
    public void upsert(User user) {
        int id = user.userId;
        if (id == 0) {
            // TODO
            insert(user);
        } else {
            // TODO
            user.userId = id;
            update(user);
        }
    }


}
