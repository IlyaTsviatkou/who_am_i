package com.example.who_am_i;

import android.os.Bundle;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class SoloModeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solomode);

        final ArrayList<String> messagesList = new ArrayList<>();
       /* for (int i=0;i<10;i++) {
            messagesList.add(""+i);
        }*/

        final RecyclerAdapter adapter = new RecyclerAdapter(this, messagesList);

        RecyclerView recyclerView = findViewById(R.id.chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Button no = findViewById(R.id.btn_no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagesList.add("BAD MOOOOOVE");
                messagesList.add("NOOO");
                adapter.notifyDataSetChanged();
            }
        });
        Button yes = findViewById(R.id.btn_yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagesList.add("GOOOD");
                messagesList.add("YEEES");
                adapter.notifyDataSetChanged();
            }
        });
    }
}
