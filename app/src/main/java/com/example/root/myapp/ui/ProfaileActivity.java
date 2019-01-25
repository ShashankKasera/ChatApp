package com.example.root.myapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.myapp.MainActivity;
import com.example.root.myapp.R;
import com.example.root.myapp.common.FirebaseConstants;
import com.example.root.myapp.common.Loader;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfaileActivity extends AppCompatActivity {


    private Loader loader;
    private TextView name;
    private TextView status;
    private Button BTNSendRequest, decline;

    private CircleImageView profileImage;

    private String curent_state;

    private FirebaseUser curent_User;

    private DatabaseReference userDatabaseRefrence;
    private DatabaseReference friendRequestRefrence;
    private DatabaseReference frienddataRefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profaile);

        loader = new Loader(this);



        final String UserID = getIntent().getStringExtra("userID");

        Log.d("ehjgkebr", "onCreate: " + UserID);

        userDatabaseRefrence = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.User).child(UserID);
        friendRequestRefrence = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.Friend_Request);
        frienddataRefrence = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.Friend);

        curent_User = FirebaseAuth.getInstance().getCurrentUser();


        curent_state = "not_friend";


        name = findViewById(R.id.name_profaile);
        status = findViewById(R.id.status_profaile);
        BTNSendRequest = findViewById(R.id.friend_request_send);
        decline = findViewById(R.id.friend_request_decline);
        profileImage = findViewById(R.id.profile_image);

        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseConstants.User)
                .child(UserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        name.setText(dataSnapshot.child("name").getValue(String.class));
                        Log.d("sbnk", "onDataChange: " + name);

                        status.setText(dataSnapshot.child("status").getValue(String.class));
                        Log.d("sbnk", "onDataChange: " + status);

                        Picasso.get().load(dataSnapshot.child(FirebaseConstants.profile).getValue(String.class))
                                .placeholder(R.drawable.userimage).into(profileImage);


                        decline.setVisibility(View.INVISIBLE);
                        decline.setEnabled(false);

                        //------------FRIEND LIST / REQUEST FEATURE-----------//


                        friendRequestRefrence.child(curent_User.getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        if (dataSnapshot.hasChild(UserID)) {

                                            String Request_Type = dataSnapshot.child(UserID)
                                                    .child(FirebaseConstants.requesr_type).getValue().toString();

                                            if (Request_Type.equals("received")) {

                                                curent_state = "reqest_received";
                                                BTNSendRequest.setText("Accept Friend Request");

                                                decline.setVisibility(View.VISIBLE);
                                                decline.setEnabled(true);


                                            } else if (Request_Type.equals("send")) {

                                                curent_state = "reqest_send";
                                                BTNSendRequest.setText("Cancel Friend Request");

                                                decline.setVisibility(View.INVISIBLE);
                                                decline.setEnabled(false);

                                            }

                                        } else {

                                            frienddataRefrence.child(curent_User.getUid())
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                            if (dataSnapshot.hasChild(UserID)) {

                                                                curent_state = "friend";
                                                                BTNSendRequest.setText("UnFriend this Porcen");
                                                                decline.setVisibility(View.INVISIBLE);
                                                                decline.setEnabled(false);

                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        BTNSendRequest.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                BTNSendRequest.setEnabled(false);
                //BTNSendRequest.setEnabled(false);

                //----------------NOT FRIEND STATE-------------//

                if (curent_state.equals("not_friend")) {


                    friendRequestRefrence.child(curent_User.getUid())
                            .child(UserID)
                            .child(FirebaseConstants.requesr_type)
                            .setValue(FirebaseConstants.send)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        friendRequestRefrence.child(UserID)
                                                .child(curent_User.getUid())
                                                .child(FirebaseConstants.requesr_type)
                                                .setValue(FirebaseConstants.received)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        curent_state = "reqest_send";
                                                        BTNSendRequest.setText("Cancel Friend Request");

                                                        decline.setVisibility(View.INVISIBLE);
                                                        decline.setEnabled(false);

                                                        //Toast.makeText(ProfaileActivity.this,"Request Send Success",Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    } else {

                                        Toast.makeText(ProfaileActivity.this, "Feiled Request Sending", Toast.LENGTH_LONG).show();
                                    }
                                    BTNSendRequest.setEnabled(true);
                                }
                            });


                }

                //----------------CANCEL REQUEST STATE-------------//


                if (curent_state.equals("reqest_send")) {

                    friendRequestRefrence.child(curent_User.getUid()).child(UserID).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    friendRequestRefrence.child(UserID).child(curent_User.getUid()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    BTNSendRequest.setEnabled(true);
                                                    curent_state = "not_friend";
                                                    BTNSendRequest.setText("Send Friend Request");

                                                    decline.setVisibility(View.INVISIBLE);
                                                    decline.setEnabled(false);

                                                }
                                            });

                                }
                            });
                }
                //------------REQUEST RECEIVED STATE------------//


                if (curent_state.equals("reqest_received")) {

                    final String currentDate = DateFormat.getDateInstance().format(new Date());

                    frienddataRefrence.child(curent_User.getUid()).child(UserID).child(FirebaseConstants.date).setValue(currentDate)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    frienddataRefrence.child(UserID).child(curent_User.getUid()).child(FirebaseConstants.date).setValue(currentDate)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    friendRequestRefrence.child(curent_User.getUid()).child(UserID).removeValue()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                    friendRequestRefrence.child(UserID).child(curent_User.getUid()).removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                    BTNSendRequest.setEnabled(true);
                                                                                    curent_state = "friend";
                                                                                    BTNSendRequest.setText("UnFriend this Porcen");

                                                                                    decline.setVisibility(View.INVISIBLE);
                                                                                    decline.setEnabled(false);

                                                                                }
                                                                            });

                                                                }
                                                            });

                                                }
                                            });

                                }
                            });

                }

                //------------------UNFRIEND-----------//

                if (curent_state.equals("friend")) {


                    frienddataRefrence.child(curent_User.getUid()).child(UserID).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    frienddataRefrence.child(UserID).child(curent_User.getUid()).removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    BTNSendRequest.setEnabled(true);
                                                    curent_state = "not_friend";
                                                    BTNSendRequest.setText("Send Friend Request");

                                                    decline.setVisibility(View.INVISIBLE);
                                                    decline.setEnabled(false);

                                                }
                                            });

                                }
                            });

                }


            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                friendRequestRefrence.child(curent_User.getUid()).child(UserID).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                friendRequestRefrence.child(UserID).child(curent_User.getUid()).removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                curent_state = "not_friend";
                                                BTNSendRequest.setText("Send Friend Request");

                                                decline.setVisibility(View.INVISIBLE);
                                                decline.setEnabled(false);

                                            }
                                        });

                            }
                        });


            }
        });


    }
}