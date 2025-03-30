package com.example.billsplitter.processes;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.billsplitter.databases.TabDatabase;
import com.example.billsplitter.entities.Bill;
import com.example.billsplitter.entities.Cost;
import com.example.billsplitter.entities.Item;

import java.util.ArrayList;
import java.util.List;


/*
Calculates what everyone owes, tax and gratuity, based on if the bill is split equally or not.
We are given a list of items, the total, and the subtotal.
 */

public class Algorithm {

    public static Item[] algorithm(boolean equalSplit, double subtotal, double total, Item[] billItems) {
        Item[] return_array = new Item[billItems.length];
        Item currentItem;
        if (equalSplit) {
            for (int i = 0; i < billItems.length; i++) {
                currentItem = billItems[i];
                currentItem.total_amount = currentItem.base_amount + Math.ceil((total - subtotal) / billItems.length * 100) / 100.0;
                return_array[i] = currentItem;
            }
        } else {
            for (int i = 0; i < billItems.length; i++) {
                currentItem = billItems[i];
                currentItem.total_amount = currentItem.base_amount + Math.ceil((currentItem.base_amount / subtotal) * (total - subtotal) * 100) / 100.0;
                return_array[i] = currentItem;
            }
        }

        // Check if the total of the individual item costs matches the total of the bill
        double currTotal = 0;
        for (int i = 0; i < billItems.length; i++) {
            currTotal += return_array[i].total_amount;
        }
        if (currTotal >= total) {
            return return_array;
        }
        else {
            return new Item[0];
        }
    }

    public void costCalculator(boolean equalSplit, Bill bill, Context context){
        TabDatabase tabdb = TabDatabase.getInstance(context);

        List<Cost> costList = new ArrayList<Cost>();

        /*tabdb.itemDao().fetchItemsUnderBill(bill.billId).observe(this, itm -> {
            costList.add(new Cost())
        });*/
        LiveData<List<Item>> itemList = tabdb.itemDao().fetchItemsUnderBill(bill.billId);


    }

}

