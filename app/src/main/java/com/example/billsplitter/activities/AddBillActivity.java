package com.example.billsplitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.billsplitter.R;
import com.example.billsplitter.databases.TabDatabase;
import com.example.billsplitter.entities.Bill;
import com.example.billsplitter.entities.User;

public class AddBillActivity extends AppCompatActivity implements OnClickListener {

    private TabDatabase tabdb;

    EditText billNametb;
    EditText userNametb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_bill);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_bill), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tabdb = TabDatabase.getInstance(getApplicationContext());

        billNametb = findViewById(R.id.add_bill_name_text);
        userNametb = findViewById(R.id.add_user_name_text);

        Button button = findViewById(R.id.add_bill_button);
        button.setOnClickListener(this);

    }
    @Override
    public void onClick(View v){
        User user = tabdb.userDao().fetchUserByName(userNametb.getText().toString());
        if(user!=null){
            Bill bill = new Bill(user.userId, billNametb.getText().toString(), 0, 0, 0);
            Intent intent = new Intent(this, AddItemsActivity.class);
            intent.putExtra(AddItemsActivity.ADD_ITEMS_KEY, bill.billId);
            startActivity(intent);
        }
        userNametb.setText("");
    }
}