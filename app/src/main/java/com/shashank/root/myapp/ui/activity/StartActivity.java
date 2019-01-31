package com.shashank.root.myapp.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.gjiazhe.panoramaimageview.GyroscopeObserver;
import com.gjiazhe.panoramaimageview.PanoramaImageView;
import com.google.firebase.auth.FacebookAuthProvider;
import com.shashank.root.myapp.R;
import com.shashank.root.myapp.common.FirebaseConstants;
import com.shashank.root.myapp.common.Loader;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.annotations.NonNull;

public class StartActivity extends AppCompatActivity {


    private static final String TAG = "cdmvfnv";
    public CircleImageView loginNumber;
    private CircleImageView loginGoogle;
    private CircleImageView loginFacebook;
    private FirebaseAuth mAuth;
    private Loader loader;

    private final static int RC_SIGN_IN = 2;
    private GoogleSignInClient mGoogleSignInClient;
    private Intent data;

    LoginButton button;
    private CallbackManager callbackManager;
    private GyroscopeObserver gyroscopeObserver;
    private PanoramaImageView panoramaImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);



        init();

        PanoramaImage();



        LoginManager.getInstance().logOut();
        button = findViewById(R.id.login_button);
        panoramaImageView = findViewById(R.id.panorama_image_view);


        callbackManager = CallbackManager.Factory.create();
        final LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }
            

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
// ...


        loginFacebook = findViewById(R.id.login_facebook);
        loginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });


    }

    private void PanoramaImage() {


        setContentView(R.layout.activity_start);
        // Initialize GyroscopeObserver.
        gyroscopeObserver = new GyroscopeObserver();
        // Set the maximum radian the device should rotate to show image's bounds.
        // It should be set between 0 and π/2.
        // The default value is π/9.
        gyroscopeObserver.setMaxRotateRadian(Math.PI/7);

        PanoramaImageView panoramaImageView = (PanoramaImageView) findViewById(R.id.panorama_image_view);
        // Set GyroscopeObserver for PanoramaImageView.
        panoramaImageView.setGyroscopeObserver(gyroscopeObserver);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register GyroscopeObserver.
        gyroscopeObserver.register(this);
        panoramaImageView.setEnablePanoramaMode(true);
        panoramaImageView.setEnableScrollbar(true);
        panoramaImageView.setInvertScrollDirection(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister GyroscopeObserver.
        gyroscopeObserver.unregister();
        panoramaImageView.setEnablePanoramaMode(true);
        panoramaImageView.setEnableScrollbar(true);
        panoramaImageView.setInvertScrollDirection(false);
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            Log.d(TAG, "signInWithCredential:success"+user.getDisplayName());



                          //  updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }



    private void init() {

      //  loader = new Loader(this);



        loader = new Loader(this);
        mAuth = FirebaseAuth.getInstance();
        loginGoogle = findViewById(R.id.login_google);
        loginNumber = findViewById(R.id.logon_number);
        Log.d("jhbj", "init: ");




        loginNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("jhbj", "init: ");
                startActivity(new Intent(StartActivity.this, LoginNumber.class));

            }
        });


        googleSignInSetUp();



    }



    private void googleSignInSetUp() {


        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.w("TAG", "btn");
                loader.show();
                signIn();

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1072620794821-595f0n90fgm6avmu82vas7j7pbag1njq.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }


    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                loader.dismiss();
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                loader.dismiss();
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            final String id = task.getResult().getUser().getUid();

                            FirebaseDatabase.getInstance().getReference()
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@android.support.annotation.NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.hasChild(FirebaseConstants.User)){

                                                if (dataSnapshot.child(FirebaseConstants.User).hasChild(id)){
                                                  loader.dismiss();
                                                    Toast.makeText(StartActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(StartActivity.this, Dashboard.class));
                                                    Log.d("jhbj", "1: ");
                                                    finish();

                                                }else {
                                                     loader.dismiss();
                                                    Toast.makeText(StartActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                                                   startActivity(new Intent(StartActivity.this, Register.class));
                                                    finish();

                                                }

                                            } else {
                                                loader.dismiss();
                                                Toast.makeText(StartActivity.this, "Log in failed", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(StartActivity.this,Register.class));
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@android.support.annotation.NonNull DatabaseError databaseError) {

                                        }
                                    });


                            //updateUI(user);
                        } else {
                            loader.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                           // Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }


}
