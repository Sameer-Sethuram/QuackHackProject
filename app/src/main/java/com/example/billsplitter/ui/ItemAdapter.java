package com.example.billsplitter.ui;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.billsplitter.R;
import com.example.billsplitter.entities.Item;


public class ItemAdapter extends ExtendedAdapter<Item> {
    public ItemAdapter(Context context){
        super(context, R.layout.item_set_user);
    }

    @Override
    public void setFields(Item element, View view) {
        TextView itemName = view.findViewById(R.id.item_name);
        itemName.setText(element.displayName);
        TextView balance = view.findViewById(R.id.item_cost);
        balance.setText(String.valueOf(element.base_amount));
    }

    public String getEditTextValue(View view){
        EditText et = view.findViewById(R.id.item_user_text);
        return et.getText().toString();
    }
}
