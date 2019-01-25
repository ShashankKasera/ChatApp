package com.example.root.myapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.root.myapp.R;
import com.example.root.myapp.common.FirebaseConstants;
import com.example.root.myapp.model.Message;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    private String userId;

    private Toolbar toolbar;

    private DatabaseReference rootRef;

    private TextView titelView;
    private TextView lastSeen;

    private String userName;

    private String current_User_ID;

    private ImageButton buttonadd;
    private ImageButton buttonsend;
    private EditText editmessege;

    private RecyclerView messageList;

    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        init();



    }

    public void init() {

        userId = getIntent().getStringExtra("userID");


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_Bar_View = inflater.inflate(R.layout.chat_custom_bar, null);

        titelView = action_Bar_View.findViewById(R.id.chat_custom_name);

        buttonadd = findViewById(R.id.btn_add);
        buttonsend = findViewById(R.id.btn_send);
        editmessege = findViewById(R.id.edit_typing);





        actionBar.setCustomView(action_Bar_View);


        current_User_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.child(FirebaseConstants.User).child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        userName = dataSnapshot.child(FirebaseConstants.name).getValue(String.class);

                        getSupportActionBar().setTitle(userName);
                        titelView.setText(userName);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        buttonsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessage();


            }
        });


        messageList = findViewById(R.id.messagesList);
        messageList.setLayoutManager(new LinearLayoutManager(this));
        MessageSendList();
        messageList.setAdapter(adapter);
        adapter.startListening();


    }

    private void sendMessage() {

        String message = editmessege.getText().toString();

        if (!TextUtils.isEmpty(message)){

            String current_User_ref = FirebaseConstants.Message + "/" + current_User_ID + "/" + userId;
            String user_ID_ref = FirebaseConstants.Message + "/" + userId + "/" +current_User_ID;

            DatabaseReference user_message_push = rootRef.child(FirebaseConstants.Message)
                    .child(current_User_ID).child(userId).push();

            String push_id = user_message_push.getKey();

            HashMap messageMap = new HashMap();

            messageMap.put(FirebaseConstants.message,message);
            messageMap.put(FirebaseConstants.from,current_User_ID);
            messageMap.put(FirebaseConstants.type,"text");

            HashMap messageUserMap = new HashMap();

            messageUserMap.put(current_User_ref + "/" + push_id,messageMap);
            messageUserMap.put(user_ID_ref + "/" + push_id,messageMap);

            editmessege.setText("");

            rootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                    if (databaseError != null){

                        Log.d("hkhk", "onComplete: "+databaseError.getMessage().toString());

                    }

                }
            });


        }

    }

    private void MessageSendList(){

        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(rootRef.child(FirebaseConstants.Message).child(current_User_ID).child(userId), Message.class)
                        .build();

         adapter = new FirebaseRecyclerAdapter<Message, MessageHolder>(options) {
            @Override
            public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.singl_message, parent, false);

                return new MessageHolder(view);
            }

            @Override
            protected void onBindViewHolder(final MessageHolder holder, int position, Message model) {
                // Bind the Message object to the MessageHolder
                // ...





                Log.d("bkge", "onBindViewHolder: "+holder.currentUserMessage);
                holder.userMessage.setText(model.getMessage());

                if (model.getFrom().equals(current_User_ID)){

                    holder.userMessage.setVisibility(View.VISIBLE);
                    holder.userMessage.setVisibility(View.GONE);
                    holder.currentUserMessage.setText(model.getMessage());
                    holder.currentUserMessage.setTextColor(getResources().getColor(R.color.white));
                    holder.currentUserMessage.setBackgroundResource(R.drawable.rounded_typing);

                }else{

                    holder.userMessage.setVisibility(View.VISIBLE);
                    holder.currentUserMessage.setVisibility(View.GONE);
                    holder.userMessage.setText(model.getMessage());
                    holder.userMessage.setTextColor(getResources().getColor(R.color.white));
                    holder.userMessage.setBackgroundResource(R.drawable.message_backgraund);


                }


            }
        };

    }

    class MessageHolder extends RecyclerView.ViewHolder{


        TextView currentUserMessage,userMessage;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);

            currentUserMessage = itemView.findViewById(R.id.currentuserTextV);
            userMessage = itemView.findViewById(R.id.userTextV);

        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}
