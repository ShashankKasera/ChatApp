package com.example.root.myapp.firebaseDatabaseex;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.root.myapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Insertsinglekeyvalue extends AppCompatActivity {

    public Button button1;

    public TextView a;

    public String A;

    public EditText NameEDT;
    public EditText EmailEDT;
    public EditText PasswordEDT;
    public EditText AddressEDT;

    public String name;
    public String email;
    public String password;
    public String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertsinglekeyvalue);

        button1 = findViewById(R.id.mainbutton);

        a = findViewById(R.id.namet);

        NameEDT = findViewById(R.id.nameEdt);
        EmailEDT = findViewById(R.id.emailEdt);
        PasswordEDT = findViewById(R.id.psswordEdt);
        AddressEDT = findViewById(R.id.addressEdt);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                name = NameEDT.getText().toString();
                email = EmailEDT.getText().toString();
                password = PasswordEDT.getText().toString();
                address = AddressEDT.getText().toString();



                Log.d("jhgj", "onClick:");

                HashMap<String, Object> map = new HashMap<>();
                map.put("Name", name);
                Log.d("jhgj", "onClick: xncbn" + name);

                map.put("Email", email);
                Log.d("jhgj", "onClick:dnsbv" + email);

                map.put("Password", password);
                Log.d("jhgj", "onClick:dsnv" + password);

                map.put("Address",address);
                Log.d("jhgj","onClick:,mdsnv,"+address);

                FirebaseDatabase.getInstance().getReference()

                    .child("User")
                        //.push()
                        //.child(FirebaseAuth.getInstance().getUid())
                        .setValue(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("yugug", "onComplete: ");
                            }
                    });


                //FirebaseDatabase.getInstance().getReference()
                        //.child("User")
                        //.child(FirebaseAuth.getInstance().getUid())
                        //.updateChildren(map)
                        //.addOnCompleteListener(new OnCompleteListener<Void>() {
                            //@Override
                            //public void onComplete(@NonNull Task<Void> task) {



                          //  }
                        //});

                FirebaseDatabase.getInstance().getReference()
                        .child("User")
                        //.child(FirebaseAuth.getInstance().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                a.setText(dataSnapshot.child("Name").getValue(String.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


            }
        });




    }
}
