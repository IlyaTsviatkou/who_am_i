package com.example.who_am_i;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.who_am_i.Model.Game;
import com.example.who_am_i.Model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Random;

public class MenuActivity extends AppCompatActivity {
    User user;
    FirebaseDatabase database;
    DatabaseReference result;
    ValueEventListener listener,listener2, listener3;
    public String answer,answerCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        user = User.getInstance("no");
        TextView username = findViewById(R.id.username);
        username.setText(user.getName());
        final Button log_out = findViewById(R.id.log_out);
        final Button multi = findViewById(R.id.btn_multiplayer);
        final Button join = findViewById(R.id.btn_join);

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
        multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,MultiplayerModeActivity.class);
                createGame(intent);

            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenuActivity.this,MultiplayerModeActivity.class);
                join(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }
    public void Log_out() {
        user.reset(new User(null));
        Intent intent = new Intent(MenuActivity.this,MainActivity.class);
        startActivity(intent);
        Toast.makeText(this,"LOGED_OUT",Toast.LENGTH_SHORT).show();
    }
   /* public ArrayList<String> getAnswer()
    {
        database = FirebaseDatabase.getInstance();
        result = database.getReference("Word");
      //  final String[] answer=new String[1],answerCode = new String[1];
        final ArrayList<String> list = new ArrayList<>();
        final ArrayList<String> list2 = new ArrayList<>();
        final ArrayList<String> list3 = new ArrayList<>();
        listener = new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildren()!=null)
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        list.add(ds.getKey().toString());
                        list2.add(ds.getValue(String.class));
                    }
                // Log.d("TAG", list.get(0));
                // Questions questions = snapshot.child().getValue(Questions.class);
                int a = new Random().nextInt(list.size()-1);
                answer = list.get(a);
                answerCode = list2.get(a);
                list3.add(answer);
                list3.add(answerCode);
            }


            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        result.addListenerForSingleValueEvent((ValueEventListener) listener);
        return list3;
    }*/
    public void createGame(final Intent intent)
    {
        final int a = new Random().nextInt(89999)+1000;
        final Game game= new Game();
        game.id=String.valueOf(a);
        game.p1=user.getName();
        game.turn=0;

        database = FirebaseDatabase.getInstance();
        result = database.getReference();
        listener2 = new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Word")!=null) {
                    final ArrayList<String> list = new ArrayList<>();
                    final ArrayList<String> list2 = new ArrayList<>();
                    for(DataSnapshot ds : snapshot.child("Word").getChildren()) {
                        list.add(ds.getKey().toString());
                        list2.add(ds.getValue(String.class));
                    }
                    int a = new Random().nextInt(list.size()-1);

                    game.answer = list.get(a);
                    game.answerCode = list2.get(a);
                }
                if(!snapshot.child("Games").child(game.id).exists()) {
                    result.child("Games").child(game.id)
                            .setValue(game);
                    intent.putExtra("gameId",String.valueOf(a));

                    result.removeEventListener(listener2);

                    startActivity(intent);
                }


            }


            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
       result.addListenerForSingleValueEvent((ValueEventListener) listener2);

    }
    public void join(final Intent intent)
    {
        final Game[] games = {new Game()};

        database = FirebaseDatabase.getInstance();
        result = database.getReference("Games");
        final TextView gameid = findViewById(R.id.textid);
        listener3 = new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(gameid.getText().toString()).exists()) {
                    games[0] = snapshot.child(gameid.getText().toString()).getValue(Game.class);
                    if(games[0].p2==null) {
                        games[0].p2 = user.getName();
                        result.child(gameid.getText().toString()).setValue(games[0]);
                        intent.putExtra("gameId", gameid.getText().toString());

                        result.removeEventListener(listener3);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(MenuActivity.this,"Game is full",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MenuActivity.this,"This id is incorrect",Toast.LENGTH_SHORT).show();
                }
            }


            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        result.addValueEventListener((ValueEventListener) listener3);

    }
}