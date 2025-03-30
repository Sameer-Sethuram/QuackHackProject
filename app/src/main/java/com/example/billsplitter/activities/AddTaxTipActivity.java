package com.example.billsplitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.billsplitter.R;
import com.example.billsplitter.databases.TabDatabase;
import com.example.billsplitter.entities.Bill;

public class AddTaxTipActivity extends AppCompatActivity implements OnClickListener {
    private TabDatabase tabdb;

    private EditText taxtb;
    private EditText tiptb;

    private double tax;
    private double tip;

    private Bill bill;

    public final static String ADD_TAX_TIP_KEY = "taxtip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_tax_tip);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_tax_tip), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tabdb = TabDatabase.getInstance(getApplicationContext());

        bill = tabdb.billDao().fetchBillFromName(getIntent().getExtras().getString(ADD_TAX_TIP_KEY));

        taxtb = findViewById(R.id.tax_text);
        tiptb = findViewById(R.id.tip_text);

        Button button = findViewById(R.id.add_tax_tip_button);
        button.setOnClickListener(this);


    }
    @Override
    public void onClick(View v){
        tax = Double.parseDouble(taxtb.getText().toString());
        tip = Double.parseDouble(tiptb.getText().toString());

        bill.tax = tax;
        bill.tip = tip;

        tabdb.billDao().upsert(bill);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}