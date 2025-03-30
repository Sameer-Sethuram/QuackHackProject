package com.example.billsplitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.billsplitter.databases.TabDatabase;
import com.example.billsplitter.entities.Bill;
import com.example.billsplitter.entities.Item;
import com.example.billsplitter.processes.imageProcessing.*;
import com.googlecode.tesseract.android.TessBaseAPI;

import com.example.billsplitter.R;

public class ScrapeImageActivity extends AppCompatActivity {

    public static final String SCRAPE_KEY = "scraper";
    private static final String TAG = ScrapeImageActivity.class.getCanonicalName();
    private TabDatabase tabdb;
    private Bill bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "scrape image activity created");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scrape_image);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Assets.extractAssets(getApplicationContext());
        ImageScraper ourScraper = new ImageScraper();
        ourScraper.initTesseract(Assets.getTessDataPath(getApplicationContext()),"eng", TessBaseAPI.OEM_LSTM_ONLY);
        tabdb = TabDatabase.getInstance(getApplicationContext());
        bill = tabdb.billDao().fetchBillFromName(getIntent().getExtras().getString(SCRAPE_KEY));
        ourScraper.getBillItemsFromImage(Assets.getImageFile(getApplicationContext(), "receipt4.jpg"), Assets.getIntermediaryProcessDir(getApplicationContext()), bill.billId);

        while(ourScraper.billItems==null){
        }

        Item[] items = ourScraper.billItems;
        for (int i = 0; i < items.length; i++) {
            tabdb.itemDao().upsert(items[i]);
            bill.subtotal+=items[i].base_amount;
        }
        bill.tax = ourScraper.tax;
        bill.total = bill.tax+bill.subtotal;
        tabdb.billDao().upsert(bill);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}