package com.example.who_am_i;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.who_am_i.Model.User;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        user = User.getInstance("no");
        TextView username = findViewById(R.id.username);
        username.setText(user.getName());
        final Button log_out = findViewById(R.id.log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log_out();
            }
        });
        Button solo = findViewById(R.id.btn_solo);
        solo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,SoloModeActivity.class);
                startActivity(intent);
            }
        });

    }


    public void Log_out() {
        user.reset(new User(null));
        Intent intent = new Intent(MenuActivity.this,MainActivity.class);
        startActivity(intent);
        Toast.makeText(this,"LOGED_OUT",Toast.LENGTH_SHORT).show();
    }
}