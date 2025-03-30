package com.example.billsplitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.billsplitter.R;
import com.example.billsplitter.databases.TabDatabase;
import com.example.billsplitter.entities.Bill;
import com.example.billsplitter.entities.Item;
import com.example.billsplitter.entities.User;

import java.util.ArrayList;

public class AddItemsActivity extends AppCompatActivity implements View.OnClickListener{
    public final static String ADD_ITEMS_KEY = "additems";
    private TabDatabase tabdb;

    private EditText nametb;
    private EditText costtb;

    private String name;
    private double cost;

    private Bill bill;

    private ArrayList<Item> items;

    private final static String TAG = AddItemsActivity.class.getCanonicalName();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_items), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tabdb = TabDatabase.getInstance(getApplicationContext());

        bill = tabdb.billDao().fetchBillFromName(getIntent().getExtras().getString(ADD_ITEMS_KEY));

        Log.d(TAG, "Intent Bill Name: " + getIntent().getExtras().getString(ADD_ITEMS_KEY));

        nametb = findViewById(R.id.add_item_name_text);
        costtb = findViewById(R.id.add_item_cost_text);

        Button button = findViewById(R.id.add_item_button);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        Log.d(TAG, "button pressed");
        if(id==R.id.add_item_button){
            name = nametb.getText().toString();
            cost = Double.parseDouble(costtb.getText().toString());
            Log.d(TAG, "item attempting to add");
            Log.d(TAG, "COST: " + String.valueOf(cost));
            Log.d(TAG, "Bill id: " + String.valueOf(bill.billId));
            Log.d(TAG, "Bill purchaser: " + String.valueOf(tabdb.userDao().fetchUserById(bill.purchaserId)).toString());
            Item item = new Item(bill.billId, cost, bill.purchaserId);

            tabdb.itemDao().upsert(item);

            bill.subtotal+=item.amount;

            nametb.setText("");
            costtb.setText("");

        }else if(id==R.id.add_tax_tip_button){
            Intent intent = new Intent(this, AddTaxTipActivity.class);
            tabdb.billDao().upsert(bill);
            intent.putExtra(AddTaxTipActivity.ADD_TAX_TIP_KEY, bill.name);
            startActivity(intent);
        }else{
            return;
        }
    }
}
