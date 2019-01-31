package com.shashank.root.myapp.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shashank.root.myapp.R;
import com.shashank.root.myapp.common.FirebaseConstants;
import com.shashank.root.myapp.common.Loader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import io.reactivex.annotations.NonNull;

public class UpdateProfile extends AppCompatActivity {

    public EditText Edit;

    private Toolbar toolbar;

    public String EditString;
    private TextView Text;

    public Button ButtonMain;

    private Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("UpdateProfile");

        loader = new Loader(this);

        Edit = findViewById(R.id.edit);


        Text =findViewById(R.id.text);
        final String s = getIntent().getStringExtra("key");

        String s0 = s;
        String s1 = s0.substring(0,1);
        String s2 = s0.substring(1,s.length());

        String s3 = s1.toUpperCase();
        String s4 = s2.toLowerCase();

        final String s5 = s3+s4;

        Text.setText(s5);

        ButtonMain = findViewById(R.id.btn_name_status);



        ButtonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loader.show();

                EditString = Edit.getText().toString();


                final HashMap<String, Object> map = new HashMap<>();
                map.put(s,EditString);



                FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseConstants.User)
                        .child(FirebaseAuth.getInstance().getUid())
                        .updateChildren(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loader.dismiss();
                                startActivity(new Intent(UpdateProfile.this, SettingActivity.class));
                                finish();

                            }
                        });



            }
        });

    }
}
