package com.shashank.root.myapp.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shashank.root.myapp.R;
import com.shashank.root.myapp.common.FirebaseConstants;
import com.shashank.root.myapp.common.Loader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;

public class LoginNumber extends AppCompatActivity {

    private Button btnnext;
    private EditText editnumber;

    private FirebaseAuth mAuth;
    private String number;
    private Loader loader;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_number);

        loader = new Loader(this);

        mAuth = FirebaseAuth.getInstance();
        btnnext = findViewById(R.id.next);
        editnumber = findViewById(R.id.numberEdit);

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("kj", "sendvrificationcode: ");

                number = editnumber.getText().toString();
                Log.d("kj", "sendvrificationcode: "+number);

                if (number.isEmpty()){

                    Toast.makeText(LoginNumber.this,"enter number",Toast.LENGTH_LONG).show();

                }


                else if (number.length()<10){

                    Toast.makeText(LoginNumber.this,"enter carect number",Toast.LENGTH_LONG).show();

                }

                else {

                    loader.show();
                    sendvrificationcode();

                }
                Log.d("kj", "onClick: ");



                Log.d("kj", "onClick: ");

            }
        });

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("kj", "signInWithCredential:success");

                            final String id = task.getResult().getUser().getUid();

                            FirebaseDatabase.getInstance().getReference()
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@android.support.annotation.NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.hasChild(FirebaseConstants.User)){

                                                if (dataSnapshot.child(FirebaseConstants.User).hasChild(id)){

                                                    Toast.makeText(LoginNumber.this, "Successfull", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(LoginNumber.this, Dashboard.class));
                                                    Log.d("jhbj", "1: ");
                                                    finish();

                                                }else {

                                                    Toast.makeText(LoginNumber.this, "User not found", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(LoginNumber.this, Register.class);
                                                    i.putExtra("mobile_no", number );
                                                    Log.d("jhbj", "2: ");
                                                    startActivity(i);
                                                    finish();

                                                }

                                            } else {
                                                Toast.makeText(LoginNumber.this, "Log in failed", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(LoginNumber.this, Register.class);
                                                i.putExtra("mobile_no", number );
                                                Log.d("jhbj", "3: ");
                                                startActivity(i);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@android.support.annotation.NonNull DatabaseError databaseError) {

                                        }
                                    });


                            // ...
                        } else {
                            //loader.dismiss();

                            Toast.makeText(LoginNumber.this, "Log in failed", Toast.LENGTH_SHORT).show();
                            // Sign in failed, display a message and update the UI
                            Log.w("kj", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                        Log.d("kj", "onComplete: ");
                    }
                });
    }



    private void sendvrificationcode() {


        Log.d("kj", "sendvrificationcode: ");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {



                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        // This callback will be invoked in two situations:
                        // 1 - Instant verification. In some cases the phone number can be instantly
                        //     verified without needing to send or enter a verification code.
                        // 2 - Auto-retrieval. On some devices Google Play services can automatically
                        //     detect the incoming verification SMS and perform verification without
                        //     user action.
                        Log.d("kj", "onVerificationCompleted:" + credential);

                        signInWithPhoneAuthCredential(credential);

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // This callback is invoked in an invalid request for verification is made,
                        // for instance if the the phone number format is not valid.
                        Log.w("kj", "onVerificationFailed", e);

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            // ...
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // The SMS quota for the project has been exceeded
                            // ...
                        }

                        // Show a message and update the UI
                        // ...
                    }

                    @Override
                    public void onCodeSent(String verificationId,PhoneAuthProvider.ForceResendingToken token) {
                        // The SMS verification code has been sent to the provided phone number, we
                        // now need to ask the user to enter the code and then construct a credential
                        // by combining the code with a verification ID.
                        Log.d("kj", "sendvrificationcode: "+verificationId);
                        // Save verification ID and resending token so we can use them later
                        mVerificationId = verificationId;
                        Log.d("kj", "Singin: "+mVerificationId);
                        mResendToken = token;

                        Intent i = new Intent(LoginNumber.this, VerificationCodeActivity.class);
                        i.putExtra("verificationId", verificationId);
                        i.putExtra("resendToken", token);
                        i.putExtra("mobile_no", number );

                        Log.d("kj", "onCodeSent: "+number+verificationId+token);

                        startActivity(i);
                        finish();



                        // ...
                    }
                });        // OnVerificationStateChangedCallbacks



    }





}
