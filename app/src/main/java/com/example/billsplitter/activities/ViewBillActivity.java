package com.example.billsplitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import android.widget.AdapterView;

import com.example.billsplitter.R;
import com.example.billsplitter.databases.TabDatabase;
import com.example.billsplitter.entities.Bill;
import com.example.billsplitter.entities.Item;
import com.example.billsplitter.entities.User;
import com.example.billsplitter.ui.ItemAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class ViewBillActivity extends AppCompatActivity{

    public static final String BILL_KEY = "bill";
    private static final String TAG = ViewBillActivity.class.getCanonicalName();

    private TabDatabase tabdb;
    private Bill bill;

    public ItemAdapter itemAdapter;



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

        Log.d(TAG, "View Bill Activity created");

        tabdb = TabDatabase.getInstance(getApplicationContext());

        bill = tabdb.billDao().fetchBillFromName(getIntent().getExtras().getString(BILL_KEY));

        User user = tabdb.userDao().fetchUserById(bill.purchaserId);

        TextView name = findViewById(R.id.view_bill_name);
        name.setText(getString(R.string.view_bill_name, bill.name));
        TextView purchaser = findViewById(R.id.view_purchaser);
        purchaser.setText(getString(R.string.view_bill_purchaser_name, user.userName));
        TextView subtotal = findViewById(R.id.view_subtotal);
        subtotal.setText(getString(R.string.view_bill_subtotal, bill.subtotal));
        TextView tax = findViewById(R.id.view_tax);
        tax.setText(getString(R.string.view_bill_tax, bill.tax));
        TextView tip = findViewById(R.id.view_tip);
        tip.setText(getString(R.string.view_bill_tip, bill.tip));
        TextView total = findViewById(R.id.view_total);
        total.setText(getString(R.string.view_bill_total, bill.total));

        LiveData<List<Item>> items = tabdb.itemDao().fetchItemsUnderBill(bill.billId);
        itemAdapter = new ItemAdapter(this);
        ListView itemList = findViewById(R.id.item_user_list);
        itemList.setAdapter(itemAdapter);

        tabdb.itemDao().fetchItemsUnderBill(bill.billId).observe(this, itm -> {
            itemAdapter.setElements(itm);
            itemAdapter.notifyDataSetChanged();
        });

        //itemList.setOnItemClickListener(this);
    }

    /*@Override
    public void onClick(View v){

    }*/
}