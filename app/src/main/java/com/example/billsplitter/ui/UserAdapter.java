package com.example.billsplitter.ui;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.billsplitter.R;
import com.example.billsplitter.entities.Bill;
import com.example.billsplitter.entities.User;

public class UserAdapter extends ExtendedAdapter<User>{
    public UserAdapter(Context context){
        super(context, android.R.layout.simple_list_item_1);
    }

    @Override
    public void setFields(User element, View view) {
        TextView userName = view.findViewById(R.id.header);
    }
}
