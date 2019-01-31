package com.shashank.root.myapp.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.shashank.root.myapp.R;
import com.squareup.picasso.Picasso;

public class ShowImageActivity extends AppCompatActivity {

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showimagecativity);

        image = findViewById(R.id.Image);
        Picasso.get().load(getIntent().getStringExtra("image"))
                .placeholder(R.drawable.userimage).into(image);
    }
}
