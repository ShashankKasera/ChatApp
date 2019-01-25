package com.example.root.myapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.root.myapp.R;
import com.example.root.myapp.common.Loader;

public class StartActivity extends AppCompatActivity {

    public Button StartButton;

    private Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        loader = new Loader(this);

        StartButton = findViewById(R.id.start_button);

        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loader.show();
                startActivity(new Intent(StartActivity.this,Register.class));
                finish();
            }
        });
    }
}
