package com.example.billsplitter.processes;
import java.util.ArrayList;
import Item.java;

/*
Calculates what everyone owes, tax and gratuity, based on if the bill is split equally or not.
We are given a list of items, the total, and the subtotal.
 */

public class Algorithm {

    public static Item[] algorithm(boolean equalSplit, double subtotal, double total, int num_people, Item[] billItems) {
        Item[] return_array = billItems;
        Item currentItem;
        if (equalSplit) {
            for (int i = 0; i < billItems.length; i++) {
                currentItem = billItems[i];
                currentItem.total_amount = currentItem.base_amount + Math.ceil((total - subtotal) / num_people * 100) / 100.0;
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
        } else {
            Log.e(Algorithm.java, "Calculation failure!"); // Prints "Calculation failure!" if the calculation are not correct.
            return [];
        }
    }

}
