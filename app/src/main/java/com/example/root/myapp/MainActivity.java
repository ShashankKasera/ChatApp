package com.example.root.myapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.root.myapp.ui.Dashboard;
import com.example.root.myapp.ui.Register;
import com.example.root.myapp.ui.StartActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private int PERMISSION_ALL = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String[] PERMISSIONS = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA

        };

        if(!hasPermissions(this, PERMISSIONS)){
            Log.i("dsbfjsd", "not PERMISSIONS: ");
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        else {
            Log.i("dsbfjsd", "PERMISSIONS: ");
            chechLogin();
        }



    }

    private void chechLogin() {
        Log.i("dsbfjsd", "chechLogin: ");
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Log.i("dsbfjsd", "Dashboard: ");
            startActivity(new Intent(this, Dashboard.class));
            finish();
        }
        else {
            Log.i("dsbfjsd", "StartActivity: ");
            startActivity(new Intent(this, StartActivity.class));
            finish();

        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                Log.i("dsbfjsd", "hasPermissions: "+permission);
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.i("jdscuidsuiv", "1");
        if (requestCode == PERMISSION_ALL) {
            if (grantResults.length > 0) {
                boolean flag = true;
                for (String per : permissions) {
                    Log.i("jdscuidsuiv", "2");
                    if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                        flag = false;
                        Log.i("jdscuidsuiv", "3"+grantResults[0]);
                    }
                }
                if(flag){
                    Log.i("jdscuidsuiv", "4");
                    chechLogin();
                }

            } else {
                Log.i("jdscuidsuiv", "else onRequestPermissionsResult: ");
            }
        }
    }



}
