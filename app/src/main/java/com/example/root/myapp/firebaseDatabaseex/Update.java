package com.example.root.myapp.firebaseDatabaseex;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.root.myapp.R;
import com.example.root.myapp.common.FirebaseConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Update extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        HashMap<String, Object> map = new HashMap<>();
        map.put("dnmsdmv", "3");
        map.put("dbncdjbcjsd", "2");



        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseConstants.User)
                .child(FirebaseAuth.getInstance().getUid())
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {



                    }
                });





    }
}
