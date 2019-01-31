package com.shashank.root.myapp.ui.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shashank.root.myapp.R;
import com.shashank.root.myapp.common.FirebaseConstants;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;

public class VerificationCodeActivity extends AppCompatActivity {

    private EditText codeEdit;
    private Button sing_in_btn;
    private TextView resendCode;

    private String verificationId, mobileNo;



    private String code;

    private FirebaseAuth mAuth;


    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);

        codeEdit = findViewById(R.id.editVerificationCode);
        sing_in_btn = findViewById(R.id.login);
        resendCode = findViewById(R.id.resend);



        mAuth = FirebaseAuth.getInstance();

        mobileNo = getIntent().getStringExtra("mobile_no");
        verificationId = getIntent().getStringExtra("verificationId");

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                resendCode.setText("" + millisUntilFinished / 1000);
                resendCode.setEnabled(false);
            }

            public void onFinish() {
                resendCode.setText("RESEND CODE");
                resendCode.setEnabled(true);
            }
        }.start();





        sing_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = codeEdit.getText().toString();

                Log.d("gfgri", "singin: "+code);

                if (code.isEmpty()){

                    Toast.makeText(VerificationCodeActivity.this,"enter number",Toast.LENGTH_LONG).show();

                }


                else if (code.length()<6){

                    Toast.makeText(VerificationCodeActivity.this,"enter carect number",Toast.LENGTH_LONG).show();

                }

                else {


                    singin();

                }

            }
        });

        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+mobileNo

                            , 60
                            , TimeUnit.SECONDS
                            , VerificationCodeActivity.this
                            , new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


                                private PhoneAuthProvider.ForceResendingToken mResendToken;

                                @Override
                                public void onVerificationCompleted(PhoneAuthCredential credential) {
                                    // This callback will be invoked in two situations:
                                    // 1 - Instant verification. In some cases the phone number can be instantly
                                    //     verified without needing to send or enter a verification code.
                                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
                                    //     detect the incoming verification SMS and perform verification without
                                    //     user action.
                                    Log.d("gfgri", "onVerificationCompleted:" + credential);


                                }

                                @Override
                                public void onVerificationFailed(FirebaseException e) {
                                    // This callback is invoked in an invalid request for verification is made,
                                    // for instance if the the phone number format is not valid.
                                    Log.w("gfgri", "onVerificationFailed", e);

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
                                public void onCodeSent(String verificationIdresend,PhoneAuthProvider.ForceResendingToken token) {
                                    // The SMS verification code has been sent to the provided phone number, we
                                    // now need to ask the user to enter the code and then construct a credential
                                    // by combining the code with a verification ID.
                                    Log.d("gfgri", "onCodeSent:" + verificationId);

                                    Toast.makeText(getBaseContext(), "We Have Sent To You Another Otp ", Toast.LENGTH_SHORT).show();


                                    // Save verification ID and resending token so we can use them later
                                    verificationId = verificationIdresend;
                                    new CountDownTimer(60000, 1000) {

                                        public void onTick(long millisUntilFinished) {
                                            resendCode.setText("" + millisUntilFinished / 1000);
                                            resendCode.setEnabled(false);
                                            Log.d("gfgri", "onTick: ");
                                        }

                                        public void onFinish() {
                                            resendCode.setText("RESEND CODE");
                                            resendCode.setEnabled(true);
                                            Log.d("gfgri", "onFinish: ");
                                        }


                                    }.start();

                                    // ...
                                }
                            });



            }
        });

    }

    private void singin() {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        Log.d("gfgri", "singin: "+verificationId);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("gfgri", "signInWithCredential:success");



                            final String id = task.getResult().getUser().getUid();

                            FirebaseDatabase.getInstance().getReference()
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@android.support.annotation.NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.hasChild(FirebaseConstants.User)){

                                                if (dataSnapshot.child(FirebaseConstants.User).hasChild(id)){

                                                    startActivity(new Intent(VerificationCodeActivity.this, Dashboard.class));
                                                    finish();

                                                }else {

                                                    Toast.makeText(VerificationCodeActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(VerificationCodeActivity.this, Register.class);
                                                    i.putExtra("mobile_no",mobileNo);
                                                    startActivity(i);
                                                    finish();

                                                }

                                            } else {

                                                Toast.makeText(VerificationCodeActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(VerificationCodeActivity.this, Register.class);
                                                i.putExtra("mobile_no",mobileNo);
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

                            // Sign in failed, display a message and update the UI
                            Log.w("gfgri", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}
