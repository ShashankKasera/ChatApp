package com.shashank.root.myapp.ui.activity;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;

import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;


import com.shashank.root.myapp.R;
import com.shashank.root.myapp.common.Loader;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Dashboard extends AppCompatActivity {


    private Toolbar toolbar;

    private Loader loader;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;
    private TextView a, b, c;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private DatabaseReference rootRef;
    private String  current_User_ID;
    private String Userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        //Tabs
        init();



    }

    private void init() {

        loader = new Loader(this);

        rootRef = FirebaseDatabase.getInstance().getReference();
        current_User_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Chat App");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.mainpager);
        viewPager.setAdapter(new Tabadapter(getSupportFragmentManager()));

        tabLayout = findViewById(R.id.maintab);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawerLayout = findViewById(R.id.drawerlayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //recyclerView = findViewById(R.id.recycler);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        navigationView = findViewById(R.id.NavDro);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.Logout) {

                    Log.i("kknkjn", "onOptionsItemSelected: ");
                    FirebaseAuth.getInstance().signOut();


                    startActivity(new Intent(Dashboard.this, StartActivity.class));


                }

                if (menuItem.getItemId() == R.id.Setting) {
                    loader.dismiss();
                    Log.i("kknkjn", "onOptionsItemSelected: ");

                    startActivity(new Intent(Dashboard.this, SettingActivity.class));


                }

                if (menuItem.getItemId() == R.id.AllUser) {

                    Log.i("kknkjn", "onOptionsItemSelected: ");


                    startActivity(new Intent(Dashboard.this, AllUserActivity.class));


                }


                return true;

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




}

