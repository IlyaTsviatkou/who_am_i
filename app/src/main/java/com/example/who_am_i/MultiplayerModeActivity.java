package com.example.who_am_i;

import android.content.Intent;
import android.graphics.Color;
import android.media.midi.MidiDeviceService;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.who_am_i.Model.Game;
import com.example.who_am_i.Model.Questions;
import com.example.who_am_i.Model.User;
import com.example.who_am_i.Model.UserDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MultiplayerModeActivity extends AppCompatActivity {
    public static int radioAorQ = 0;
    public static int radioTurn = 0;
    public  int qPos=-1;
    public String answer,answerCode;

    public Game game;
    FirebaseDatabase database;
    DatabaseReference result;
    User user;
    ValueEventListener listener,listener2,listener3;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayermode);
        Intent intent = getIntent();
        final ArrayList<String> questionsList,answerList;
        final ArrayList<String> messagesList = new ArrayList<>();
        questionsList = getQuestions();
        answerList = getAnswerStrings();
        final RecyclerAdapter adapter= new RecyclerAdapter(this,messagesList);
        getGame(intent.getStringExtra("gameId"),adapter,messagesList);
        user = User.getInstance("no");

        //adapter = new RecyclerAdapter(this, messagesList);

        RecyclerView recyclerView = findViewById(R.id.chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Button delete = findViewById(R.id.btn_delete);
        Button yes = findViewById(R.id.btn_yes);
        final Button answerbtn = findViewById(R.id.btn_answ);
        final Button questionbtn = findViewById(R.id.btn_quest);

        answerbtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent2));

        final DelayAutoCompleteTextView text = (DelayAutoCompleteTextView) findViewById(R.id.text);
        text.setThreshold(4);
        text.setAdapter(new AutoCompleteAdapter(MultiplayerModeActivity.this,answerList));
        text.setLoadingIndicator((ProgressBar) findViewById(R.id.progress_bar));
        text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String question = (String) adapterView.getItemAtPosition(position);
                qPos=position;
                text.setText(question);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // database = FirebaseDatabase.getInstance();
              //  database.getReference("Games").child(game.id).removeValue();
                result.removeEventListener(listener3);
                finish();

            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioTurn==game.turn) {
                    if (!text.getText().toString().equals("")) {
                        messagesList.add(text.getText().toString());
                        if (radioAorQ == 1) {
                            if (qPos >= 0) {
                                if (answerCode.charAt(qPos) == '1')
                                    messagesList.add("YES");
                                else
                                    messagesList.add("NO");
                                qPos = -1;
                            } else {
                                int index = messagesList.indexOf(text.getText().toString());
                                if (index >= 0) {
                                    if (answerCode.charAt(index) == '1')
                                        messagesList.add("YES");
                                    else
                                        messagesList.add("NO");
                                } else {
                                    messagesList.add("NO");
                                }
                            }
                        } else {

                            if (text.getText().toString().equals(answer)) {
                                Toast.makeText(MultiplayerModeActivity.this, "Correct, u won", Toast.LENGTH_LONG).show();
                                game.winner= user.getName();

                                finish();
                                //messagesList.add("Correct");
                            } else
                                messagesList.add("Answer is incorrect");

                        }
                        game.lastMessage=messagesList.get(messagesList.size()-2);
                        game.lastAnswer=messagesList.get(messagesList.size()-1);
                        text.setText("");
                        adapter.notifyDataSetChanged();
                        giveTurn();
                    }
                }
                else
                {
                    Toast.makeText(MultiplayerModeActivity.this,"Not ur turn",Toast.LENGTH_LONG).show();
                }
            }
        });

        answerbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                questionbtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                answerbtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent2));
                text.setAdapter(new AutoCompleteAdapter(MultiplayerModeActivity.this,answerList));
                radioAorQ = 0;
            }
        });

        questionbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                questionbtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent2));
                answerbtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                text.setAdapter(new AutoCompleteAdapter(MultiplayerModeActivity.this,questionsList));
                radioAorQ = 1;
            }
        });
    }

    ArrayList<String> getQuestions()
    {
        database = FirebaseDatabase.getInstance();
        result = database.getReference("Questions");
        final ArrayList<String> list = new ArrayList<>();
        listener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildren()!=null)
                    for(DataSnapshot ds : snapshot.getChildren()) {

                        list.add(ds.getValue(String.class));

                    }
                Log.d("TAG", list.get(0));
                    result.removeEventListener(listener);
                // Questions questions = snapshot.child().getValue(Questions.class);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        result.addListenerForSingleValueEvent(listener);

        return list;
    }
    ArrayList<String> getAnswerStrings()
    {
        database = FirebaseDatabase.getInstance();
        result = database.getReference("Word");
        final ArrayList<String> list = new ArrayList<>();
        listener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildren()!=null)
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        list.add(ds.getKey().toString());

                    }
                result.removeEventListener(listener);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        result.addListenerForSingleValueEvent(listener);

        return list;
    }

    void getGame(final String a, final RecyclerAdapter adapter, final ArrayList<String> messagesList){
        final Game[] games = {new Game()};

        database = FirebaseDatabase.getInstance();
        result = database.getReference("Games");
        listener3=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(a).exists()) {
                    games[0] = snapshot.child(a).getValue(Game.class);
                    if(games[0].winner!=null)
                    {
                        Toast.makeText(MultiplayerModeActivity.this,games[0].winner+" won, answer is "+ games[0].answer,Toast.LENGTH_LONG).show();
                       result.removeEventListener(listener3);
                        finish();
                    }
                    TextView t = findViewById(R.id.gameid);
                    t.setText("Game#"+games[0].id);
                    game = games[0];
                    answer=game.answer;
                    answerCode=game.answerCode;

                    if(game.lastMessage!=null) {
                        if (messagesList.size() > 0) {
                            if (!messagesList.get(messagesList.size() - 2).equals(game.lastMessage)) {
                                messagesList.add(game.lastMessage);
                                messagesList.add(game.lastAnswer);
                                adapter.notifyDataSetChanged();
                            }

                        }
                        else
                        {
                            messagesList.add(game.lastMessage);
                            messagesList.add(game.lastAnswer);
                            adapter.notifyDataSetChanged();

                        }

                    }
                    if (game.p1.equals(user.getName())) {
                        radioTurn = 0;
                    } else {
                        radioTurn = 1;
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        result.addValueEventListener(listener3);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }

    void giveTurn() {
        final Game[] games = {new Game()};

        database = FirebaseDatabase.getInstance();
        result = database.getReference("Games");
        listener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                game.turn = (radioTurn + 1) % 2;
                result.child(game.id).setValue(game);
                result.removeEventListener(listener2);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        result.addListenerForSingleValueEvent(listener2);
    }


}
