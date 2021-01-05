package com.example.who_am_i;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.who_am_i.Model.User;
import com.example.who_am_i.Model.UserDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    MaterialEditText edtNewUser,edtNewPassword,edtNewEmail;
    MaterialEditText edtUser,edtPassword;

    Button btnSignUp,btnSignIn;

    FirebaseDatabase database;
    DatabaseReference users;
    User User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        User = User.getInstance(null);


        edtUser = (MaterialEditText)findViewById(R.id.edtUser);
        edtPassword =(MaterialEditText)findViewById(R.id.edtPassword);

        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignUp = findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(edtUser.getText().toString(),edtPassword.getText().toString());
            }
        });
    }

    private void showSignUpDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sing_up,null);

        edtNewUser = (MaterialEditText)sign_up_layout.findViewById((R.id.edtNewUserName));
        edtNewPassword = (MaterialEditText)sign_up_layout.findViewById((R.id.edtNewPassword));
        edtNewEmail = (MaterialEditText)sign_up_layout.findViewById((R.id.edtNewEmail));

        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                final UserDB user = new UserDB(edtNewUser.getText().toString(),
                        edtNewPassword.getText().toString(),
                        edtNewEmail.getText().toString());
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child((user.getUsername())).exists())
                            Toast.makeText(MainActivity.this,"User already exists!",Toast.LENGTH_SHORT).show();
                        else{
                            users.child((user.getUsername()))
                                    .setValue(user);
                            Toast.makeText(MainActivity.this,"User registrated success!",Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void signIn(final String login, final String password) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(login).exists())
                {
                    if(!login.isEmpty())
                    {
                        UserDB userDB = snapshot.child(login).getValue(UserDB.class);
                        if(userDB.getPassword().equals(password)) {
                            Toast.makeText(MainActivity.this, "Loged in", Toast.LENGTH_SHORT).show();
                            User.reset(new User(login));
                            Intent intent = new Intent(MainActivity.this,MenuActivity.class);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(MainActivity.this,"Wrong password",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"Please enter user name",Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this,"User is not exists !",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
