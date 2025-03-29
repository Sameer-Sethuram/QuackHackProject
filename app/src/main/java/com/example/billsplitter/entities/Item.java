package com.example.billsplitter.entities;

public class Item {
    private String item_name;
    private double item_cost;
    private String user_id;

    public item(String in, double c, String uid) {
        this.item_name = in;
        this.item_cost = c;
        this.user_id = uid;
    }
}
