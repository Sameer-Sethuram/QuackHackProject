package com.example.billsplitter.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.billsplitter.R;
import com.example.billsplitter.databases.TabDatabase;
import com.example.billsplitter.entities.Bill;
import com.google.android.material.tabs.TabLayout;

public class ViewBillActivity extends AppCompatActivity {

    public static final String BILL_KEY = "bill";
    private static final String TAG = ViewBillActivity.class.getCanonicalName();

    private TabDatabase tabdb;
    private Bill bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_bill);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.view_bill), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tabdb = TabDatabase.getInstance(getApplicationContext());

        bill = tabdb.billDao().fetchBillFromName(getIntent().getExtras().getString(BILL_KEY));

        TextView name = findViewById(R.id.view_bill_name);
        name.setText(getString(R.string.view_bill_name, bill.name));
        TextView purchaser = findViewById(R.id.view_purchaser);
        purchaser.setText(getString(R.string.view_bill_purchaser_name, tabdb.userDao().fetchUserById(bill.billId)));
        TextView subtotal = findViewById(R.id.view_subtotal);
        subtotal.setText(getString(R.string.view_bill_subtotal, bill.subtotal));
        TextView tax = findViewById(R.id.view_tax);
        tax.setText(getString(R.string.view_bill_tax, bill.tax));
        TextView tip = findViewById(R.id.view_tip);
        tip.setText(getString(R.string.view_bill_tip, bill.tip));
        TextView total = findViewById(R.id.view_total);
        total.setText(getString(R.string.view_bill_total, bill.total));
    }
}