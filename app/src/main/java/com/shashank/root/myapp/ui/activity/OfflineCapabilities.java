package com.shashank.root.myapp.ui.activity;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class OfflineCapabilities extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}