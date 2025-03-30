package com.example.billsplitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.billsplitter.R;
import com.example.billsplitter.databases.TabDatabase;
import com.example.billsplitter.entities.Bill;

public class AddItemsActivity extends AppCompatActivity implements View.OnClickListener{
    public final static String ADD_ITEMS_KEY = "additems";
    private TabDatabase tabdb;

    private EditText nametb;
    private EditText costtb;

    private String name;
    private double cost;

    private Bill bill;

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
        

        nametb = findViewById(R.id.add_item_name_text);
        costtb = findViewById(R.id.add_item_cost_text);

    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        if(id==R.id.add_bill_next){
            name = nametb.getText().toString();
            cost = Double.parseDouble(costtb.getText().toString());

            nametb.setText("");
            costtb.setText("");

        }else if(id==R.id.add_item_button){
            Intent intent = new Intent(this, AddTaxTipActivity.class);
        }else{
            return;
        }
    }
}
