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

    // GET ALL USERS
    @Query("SELECT * FROM user")
    public abstract LiveData<List<User>> fetchAllUsers();

    // GET A USER USING THEIR USERID
    @Query("SELECT * FROM user WHERE userId = :userId")
    public abstract LiveData<User> fetchUserById(int userId);

    // GET A USER USING THEIR USERNAME
    @Query("SELECT * FROM user WHERE userName = :userName")
    public abstract LiveData<User> fetchUserByName(String userName);

    // GET THE USERS WHO OWE SOME OTHER USER SOME AMOUNT OF MONEY GRAEATER THAN 0
    @Query("SELECT * FROM user WHERE amountOwed > 0")
    public abstract LiveData<List<User>> usersWhoOwe();

    // GET THE USERS WHO ARE OWED MONEY BY OTHER USERS
    @Query("SELECT * FROM user WHERE amountOwing > 0")
    public abstract LiveData<List<User>> usersWhoAreOwed();

    // GET THE BALANACE OF A SPECIFIC USER USING THEIR USERID
    @Query("SELECT balance FROM user WHERE userId = :userId")
    public abstract LiveData<double> fetchBalanceOfUser(int userId);

    // GET THE BALANCE OF A SPECIFIC USER USING THEIR USERNAME
    @Query("SELECT balance FROM user WHERE userName = :userName")
    public abstract LiveData<double> fetchBalanceOfUserName(String userName);

    // INSERT A NEW USER FROM THE DATABASE
    @Insert
    protected abstract void insert(User user);

    // UPDATE THE DATA OF AN EXISTING USER IN THE DATABASE
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
        if (id == -1) {
            // TODO
            insert(user);
        } else {
            // TODO
            user.userId = id;
            update(user);
        }
    }


}
