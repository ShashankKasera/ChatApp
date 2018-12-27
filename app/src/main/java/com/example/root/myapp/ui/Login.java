package com.example.root.myapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.myapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText EmailEdt;
    private ImageButton Login;
    private EditText UserPasswordEdt;
    private TextView textViewRegister;
    private FirebaseAuth auth;

    private String Email;
    private  String UserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EmailEdt = findViewById(R.id.email);
        Login = findViewById(R.id.login);
        UserPasswordEdt = findViewById(R.id.PasswordEdittext);
        textViewRegister = findViewById(R.id.register);

        auth = FirebaseAuth.getInstance();


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = EmailEdt.getText().toString();
                UserPassword = UserPasswordEdt.getText().toString();

                if(Email.length()==0){
                    Toast.makeText(Login.this, "Enter Email", Toast.LENGTH_SHORT).show();
                }
                else if(UserPassword.length()<6){
                    Toast.makeText(Login.this, "Enter Corect Password", Toast.LENGTH_SHORT).show();
                }
                else {
                    login();
                }


            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
                finish();
            }
        });



    }
    private void login() {
        auth.signInWithEmailAndPassword(Email,UserPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("nmfdvfd", "onComplete: "+"Successfull");
                            Toast.makeText(com.example.root.myapp.ui.Login.this, "Successfull", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.d("nmfdvfd", "onComplete: "+"User not found");
                            Toast.makeText(com.example.root.myapp.ui.Login.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
