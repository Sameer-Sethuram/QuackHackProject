package com.example.billsplitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.billsplitter.R;
import com.example.billsplitter.databases.TabDatabase;
import com.example.billsplitter.entities.User;

public class ViewUserActivity extends AppCompatActivity {

    public static final String VIEW_USER_KEY = "user_view";

    private TabDatabase tabdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.view_user), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tabdb = TabDatabase.getInstance(getApplicationContext());

        String username = getIntent().getExtras().getString("VIEW_USER_KEY");
        User user = tabdb.userDao().fetchUserByName(username);

        TextView name = findViewById(R.id.view_user_name);
        name.setText(getString(R.string.view_user_name, user.userName));
        TextView total = findViewById(R.id.view_user_balance);
        total.setText(getString(R.string.view_user_balance, user.balance));

    }



}