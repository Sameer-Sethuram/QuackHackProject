package com.example.billsplitter.ui;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.billsplitter.R;
import com.example.billsplitter.entities.Bill;
import com.example.billsplitter.entities.User;



public class UserAdapter extends ExtendedAdapter<User>{
    final static public String TAG = UserAdapter.class.getCanonicalName();
    public UserAdapter(Context context){
        super(context, R.layout.user);
    }

    @Override
    public void setFields(User user, View view) {
        TextView userName = view.findViewById(R.id.user_header);
        userName.setText(user.userName);
        TextView balance = view.findViewById(R.id.user_balance);
        balance.setText(String.valueOf(user.balance));

    }
}
