package com.example.billsplitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.ArrayList;
import java.util.List;

public class ViewBillActivity extends AppCompatActivity implements View.OnClickListener {

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
        TextView total = findViewById(R.id.view_total);
        total.setText(getString(R.string.view_bill_total, bill.total));

        itemAdapter = new ItemAdapter(this);
        ListView itemList = findViewById(R.id.item_user_list);
        itemList.setAdapter(itemAdapter);

        List<String> users = new ArrayList<String>();

        tabdb.itemDao().fetchItemsUnderBill(bill.billId).observe(this, itm -> {
            itemAdapter.setElements(itm);
            itemAdapter.notifyDataSetChanged();
        });

        TextView taxEven = findViewById(R.id.tax_even);
        taxEven.setText(getString(R.string.view_bill_tax, bill.tax));
        TextView tipEven = findViewById(R.id.tip_even);
        tipEven.setText(getString(R.string.view_bill_tip, bill.tip));

        Button button = findViewById(R.id.confirm_button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        ListView lv = findViewById(R.id.item_user_list);
        View listicle;
        Item item;
        for(int i = 0; i < lv.getCount(); i++){
            listicle = lv.getChildAt(i);
            if(listicle!=null){
                String input = itemAdapter.getEditTextValue(listicle);
                User user = tabdb.userDao().fetchUserByName(input);
                if(user!=null){
                    item = itemAdapter.getItem(i);
                    item.owerId = user.userId;
                    tabdb.itemDao().upsert(item);
                }else{
                    item = itemAdapter.getItem(i);
                    item.splitEvenly = true;
                    tabdb.itemDao().upsert(item);
                }
            }
        }
        CheckBox tax_even_box = findViewById(R.id.tax_even);
        CheckBox tip_even_box = findViewById(R.id.tip_even);
        bill.taxEven = tax_even_box.isChecked();
        bill.tipEven = tip_even_box.isChecked();
        tabdb.billDao().upsert(bill);
    }
}