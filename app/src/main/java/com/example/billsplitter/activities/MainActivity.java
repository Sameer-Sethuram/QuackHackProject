package com.example.billsplitter.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.example.billsplitter.R;
import com.example.billsplitter.databases.TabDatabase;
import com.example.billsplitter.ui.BillAdapter;


public class MainActivity extends AppCompatActivity implements OnClickListener{

    private static final String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        BillAdapter billAdapter = new BillAdapter(this);
        ListView billList = findViewById(R.id.bill_list);
        billList.setAdapter(billAdapter);

        TabDatabase tabdb = TabDatabase.getInstance(getApplicationContext());

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        Log.d("main", "create options menU!");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        int itemId = item.getItemId();
        Intent intent;
        if(itemId==R.id.register){
            intent = new Intent(this, StripeConnect.class);
            startActivity(intent);
            return true;
        }else if(itemId==R.id.view_users){
            intent = new Intent(this, ViewUsersActivity.class);
            startActivity(intent);
            return true;
        }else if(itemId==R.id.add_bill) {
            intent = new Intent(this, AddItemsActivity.class);
            startActivity(intent);
            return true;
//        }else if(itemId==R.id.register_user){
//            intent = new Intent(this, RegisterUsersActivity.class);
//            startActivity(intent);
//            return true;
//        }
        }else{
            return false;
        }
    }
}