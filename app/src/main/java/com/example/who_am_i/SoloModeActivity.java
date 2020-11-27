package com.example.who_am_i;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class SoloModeActivity extends AppCompatActivity {
    public static int radioAorQ = 0;
    public static int radioTurn = 0;
    public  int qPos=-1;
    public String answer,answerCode;
    FirebaseDatabase database;
    DatabaseReference result;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solomode);

        final ArrayList<String> messagesList = new ArrayList<>();
        final ArrayList<String> questionsList,answerList;
        questionsList = getQuestions();
        answerList = getAnswerStrings();


       /* for (int i=0;i<10;i++) {
            messagesList.add(""+i);
        }*/

        final RecyclerAdapter adapter = new RecyclerAdapter(this, messagesList);

        RecyclerView recyclerView = findViewById(R.id.chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Button no = findViewById(R.id.btn_no);
        Button yes = findViewById(R.id.btn_yes);
        final Button answerbtn = findViewById(R.id.btn_answ);
        final Button questionbtn = findViewById(R.id.btn_quest);

        answerbtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent2));

        final DelayAutoCompleteTextView text = (DelayAutoCompleteTextView) findViewById(R.id.text);
        text.setThreshold(4);
        text.setAdapter(new AutoCompleteAdapter(SoloModeActivity.this,answerList));
        text.setLoadingIndicator((ProgressBar) findViewById(R.id.progress_bar));
        text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String question = (String) adapterView.getItemAtPosition(position);
                qPos=position;
                text.setText(question);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //adapter.notifyDataSetChanged();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!text.getText().toString().equals(""))
                {
                messagesList.add(text.getText().toString());
                if(radioAorQ==1) {
                    if(qPos>=0) {
                        if (answerCode.charAt(qPos) == '1')
                            messagesList.add("YES");
                        else
                            messagesList.add("NO");
                        qPos=-1;
                    }
                    else
                    {
                        messagesList.add("NO");
                    }
                }
                else
                {

                    if(text.getText().toString().equals(answer)) {
                        Toast.makeText(SoloModeActivity.this,"Correct, u won",Toast.LENGTH_SHORT);
                        finish();
                        //messagesList.add("Correct");
                    }
                    else
                        messagesList.add("Answer is incorrect");

                }
                text.setText("");
                adapter.notifyDataSetChanged();}
            }
        });

        answerbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                questionbtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                answerbtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent2));
                text.setAdapter(new AutoCompleteAdapter(SoloModeActivity.this,answerList));
                radioAorQ = 0;
            }
        });

        questionbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                questionbtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent2));
                answerbtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                text.setAdapter(new AutoCompleteAdapter(SoloModeActivity.this,questionsList));
                radioAorQ = 1;
            }
        });
    }

    ArrayList<String> getQuestions()
    {
        database = FirebaseDatabase.getInstance();
        result = database.getReference("Questions");
        final ArrayList<String> list = new ArrayList<>();
        result.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildren()!=null)
                for(DataSnapshot ds : snapshot.getChildren()) {

                    list.add(ds.getValue(String.class));

                }
                Log.d("TAG", list.get(0));
               // Questions questions = snapshot.child().getValue(Questions.class);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return list;
    }
    ArrayList<String> getAnswerStrings()
    {
        database = FirebaseDatabase.getInstance();
        result = database.getReference("Word");
        final ArrayList<String> list = new ArrayList<>();
        final ArrayList<String> list2 = new ArrayList<>();
        result.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildren()!=null)
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        list.add(ds.getKey().toString());
                        list2.add(ds.getValue(String.class));
                    }
                Log.d("TAG", list.get(0));
                // Questions questions = snapshot.child().getValue(Questions.class);
                int a = new Random().nextInt(list.size()-1);
                answer = list.get(a);
                answerCode = list2.get(a);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return list;
    }

}
