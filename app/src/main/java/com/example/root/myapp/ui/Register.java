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
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private FirebaseAuth auth;
   private ImageButton singupbutton;

   private EditText nameEdit;
   private EditText emailEdit;
   private EditText passwordEdit;
   private EditText addressEdit;
   private TextView textViewRegister;


   private String name;
   private String email;
   private String password;
   private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        singupbutton = findViewById(R.id.signup);

        nameEdit = findViewById(R.id.name);
        emailEdit = findViewById(R.id.email);
        passwordEdit = findViewById(R.id.Password);
        addressEdit = findViewById(R.id.address);
        textViewRegister = findViewById(R.id.login);


        auth = FirebaseAuth.getInstance();


        singupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = nameEdit.getText().toString();
                email = emailEdit.getText().toString();
                password = passwordEdit.getText().toString();
                address = addressEdit.getText().toString();



                if(name.length()==0){
                    Toast.makeText(Register.this, "Enter Name ", Toast.LENGTH_SHORT).show();
                }
                else if(email.length()==0){
                    Toast.makeText(Register.this, "Enter Corect Email", Toast.LENGTH_SHORT).show();
                }
                else if(password.length()<6){
                    Toast.makeText(Register.this, "Enter Corect Password", Toast.LENGTH_SHORT).show();
                }
                else if(address.length()==0){
                    Toast.makeText(Register.this, "Enter Corect Address", Toast.LENGTH_SHORT).show();
                }
                else {

                    register();

                }



            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Login.class));
                finish();
            }
        });



    }
    private void register() {

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("fnmvdvndfnmv", "createUserWithEmail:success");
                            Toast.makeText(Register.this, "Registration Succesfull", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = auth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                            Log.w("fnmvdvndfnmv", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
