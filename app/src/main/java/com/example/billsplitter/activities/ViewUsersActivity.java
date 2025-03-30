package com.example.billsplitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.billsplitter.R;
import com.example.billsplitter.entities.User;
import com.example.billsplitter.ui.UserAdapter;

public class ViewUsersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private UserAdapter userAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_users);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.view_users), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userAdapter = new UserAdapter(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id){
        User user = userAdapter.getItem(position);

        Intent intent = new Intent(this, ViewUserActivity.class);
        intent.putExtra(ViewUserActivity.VIEW_USER_KEY, user.userId);
        startActivity(intent);
    }

}
