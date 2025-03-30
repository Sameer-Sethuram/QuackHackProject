package com.example.billsplitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.billsplitter.R;
import com.example.billsplitter.databases.TabDatabase;
import com.example.billsplitter.entities.User;
import com.example.billsplitter.ui.UserAdapter;

import java.util.List;

public class ViewUsersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private static final String TAG = ViewUsersActivity.class.getCanonicalName();

    private UserAdapter userAdapter;
    private TabDatabase tabdb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_users);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.view_users), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tabdb = TabDatabase.getInstance(getApplicationContext());
        List<User> users = tabdb.userDao().GIMMEALLTHEFUCKINGUSERS();

        for (int i = 0; i < users.size(); i++) {
            List<Double> negatives = tabdb.itemDao().fetchAmountsFromOwingUserId(users.get(i).userId);
            for (Double currentVal: negatives) {
                users.get(i).balance -= currentVal;
            }
            List<Double> positives = tabdb.itemDao().fetchAmountsFromOwedUserId(users.get(i).userId);
            for (Double currentVal: positives) {
                users.get(i).balance += currentVal;
            }
            tabdb.userDao().upsert(users.get(i));
        }

        userAdapter = new UserAdapter(this);
        ListView userList = findViewById(R.id.user_list);
        userList.setAdapter(userAdapter);




        tabdb.userDao().fetchAllUsers().observe(this, usr ->{
            Log.d(TAG, usr.toString());
            userAdapter.setElements(usr);
            userAdapter.notifyDataSetChanged();
        });
        userList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id){
        User user = userAdapter.getItem(position);

        Intent intent = new Intent(this, ViewUserActivity.class);
        intent.putExtra(ViewUserActivity.VIEW_USER_KEY, user.userId);
        startActivity(intent);
    }

}
