package com.example.billsplitter.activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.billsplitter.R;
import com.example.billsplitter.entities.User;

public class RegisterUsersActivity extends AppCompatActivity implements OnClickListener {

    final static private String TAG = RegisterUsersActivity.class.getCanonicalName();

    final static public String USER_NAME = "name";

    private EditText textbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_users);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_users), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textbox = findViewById(R.id.register_user_text);
        Button button = findViewById(R.id.register_user_button);
        button.setOnClickListener(this);


    }
    @Override
    public void onClick(View v){
        String name = textbox.getText().toString();
        User user = new User(name);
    }
}