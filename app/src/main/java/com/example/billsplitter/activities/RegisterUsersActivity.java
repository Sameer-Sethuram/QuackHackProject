package com.example.billsplitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.billsplitter.R;
import com.example.billsplitter.databases.BankDao;
import com.example.billsplitter.databases.TabDatabase;
import com.example.billsplitter.databases.UserDao;
import com.example.billsplitter.entities.Bank;
import com.example.billsplitter.entities.User;

public class RegisterUsersActivity extends AppCompatActivity implements OnClickListener {

    final static private String TAG = RegisterUsersActivity.class.getCanonicalName();

    final static public String USER_NAME = "name";

    private TabDatabase tabdb;

    private EditText textbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_users);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_users), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tabdb = TabDatabase.getInstance(getApplicationContext());

        textbox = findViewById(R.id.register_user_text);
        Button button = findViewById(R.id.register_user_button);
        button.setOnClickListener(this);

    }
    @Override
    public void onClick(View v){
        String name = textbox.getText().toString();
        Log.d(TAG, name);

        UserDao userDao = tabdb.userDao();
        BankDao bankDao = tabdb.bankDao();

        User user = new User(name);
        int userId = (int) userDao.returnId(user);

        String routingStr = getIntent().getStringExtra("ROUTING_NUMBER");
        Log.d(TAG, "Routing Number recieved: " + routingStr);

        int routingNumber = 0;
        try {
            routingNumber = Integer.parseInt(routingStr);
        } catch(NumberFormatException e) {
            Log.e(TAG, "INVALID routing number format.");
        }

        Bank bank = new Bank();
        bank.setRoutingNumber(routingNumber);
        bank.bankerUserId = userId;
        bankDao.upsert(bank);

        Log.d(TAG, "User inserted: name=" + user.userName + ", id=" + userId);
        Log.d(TAG, "Bank inserted: routing=" + bank.getRoutingNumber() + ", bankerUserId=" + bank.bankerUserId);


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}