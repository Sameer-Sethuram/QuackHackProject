package com.example.billsplitter.ui;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.billsplitter.R;
import com.example.billsplitter.entities.Bill;

public class BillAdapter extends ExtendedAdapter<Bill> {
    public BillAdapter(Context context){
        super(context, android.R.layout.simple_list_item_1);
    }

    @Override
    public void setFields(Bill element, View view) {
        TextView amount = view.findViewById(android.R.id.text1);
        amount.setText(element.toString());
    }
}
