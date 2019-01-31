package com.shashank.root.myapp.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.shashank.root.myapp.R;
import com.shashank.root.myapp.common.FirebaseConstants;
import com.shashank.root.myapp.common.Loader;
import com.shashank.root.myapp.model.Message;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    private String userId;

    private Toolbar toolbar;

    private DatabaseReference rootRef;

    private TextView titelView;
    private TextView lastSeen;
    private ImageView backbtn;
    private ImageView profileImage;

    private String userName;

    private String current_User_ID;

    private ImageView buttonadd;
    private ImageView buttonsend;
    private EditText editmessege;

    private RecyclerView messageList;
    private SwipeRefreshLayout refresh;

    private FirebaseRecyclerAdapter adapter;

    private ImagePicker imagePicker;
    private Loader loader;

    private static final  int  TOTEL_ITEMS_TO_LODE = 10;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);








        init();

        setupToolbar();

        MessageSendList();

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                messageList.smoothScrollToPosition(adapter.getItemCount());
            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                currentPage++;
                Log.d("kjtn", "onRefresh: "+currentPage);
                MessageSendList();
                Log.d("kjtn", "onRefresh: ");
                refresh.setRefreshing(false);

            }
        });




        buttonsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        buttonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ktjrhnk", "buttonaddonClick: ");
                imagePicker.choosePicture(true /*show camera intents*/);
            }
        });




    }

    public void init() {


        imagePicker = new ImagePicker(this, /* activity non null*/
                null,
                new OnImagePickedListener(){
                    @Override
                    public void onImagePicked(Uri imageUri) {

                        Log.d("ktjrhnk", "onImagePicked: ");
                        imageUplode(imageUri);
                    }
                });

        loader = new Loader(this);

        userId = getIntent().getStringExtra("userID");

        toolbar = findViewById(R.id.toolbar);

        titelView =  toolbar.findViewById(R.id.chat_custom_name);
        profileImage = toolbar.findViewById(R.id.chat_custom_image);
        backbtn = toolbar.findViewById(R.id.back);

        buttonadd = findViewById(R.id.btn_add);
        buttonsend = findViewById(R.id.btn_send);
        editmessege = findViewById(R.id.edit_typing);
        current_User_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        rootRef = FirebaseDatabase.getInstance().getReference();

        messageList = findViewById(R.id.messagesList);
        refresh = findViewById(R.id.swiperefreshLayout);
        messageList.setLayoutManager(new LinearLayoutManager(this));


    }

    private void setupToolbar() {


        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rootRef.child(FirebaseConstants.User).child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        userName = dataSnapshot.child(FirebaseConstants.name).getValue(String.class);

                        getSupportActionBar().setTitle(userName);
                        titelView.setText(userName);
                        Picasso.get().load(dataSnapshot.child(FirebaseConstants.compressor_image).getValue(String.class)).placeholder(R.drawable.userimage).into(profileImage);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    private void imageUplode(Uri imageUri) {

        Log.d("ktjrhnk", "imageUplode: ");
        loader.show();

        final String current_User_ref = FirebaseConstants.Message + "/" + current_User_ID + "/" + userId;
        final String user_ID_ref = FirebaseConstants.Message + "/" + userId + "/" +current_User_ID;

        DatabaseReference user_message_push = rootRef.child(FirebaseConstants.Message)
                .child(current_User_ID).child(userId).push();

        final String push_id = user_message_push.getKey();

        final StorageReference Image_message = FirebaseStorage.getInstance().getReference()
                .child("Image_message").child(push_id+".png");

        Image_message.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.d("ktjrhnk", "onSuccess: ");
                Image_message .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("ktjrhnk", "onSuccess: "+uri);

                        String DownloadUrl = uri.toString();

                        HashMap messageMap = new HashMap();
                        Log.d("ktjrhnk", "hashmaponSuccess: ");

                        messageMap.put(FirebaseConstants.message,DownloadUrl);
                        messageMap.put(FirebaseConstants.from,current_User_ID);
                        messageMap.put(FirebaseConstants.type,"image");

                        HashMap messageUserMap = new HashMap();

                        messageUserMap.put(current_User_ref + "/" + push_id,messageMap);
                        messageUserMap.put(user_ID_ref + "/" + push_id,messageMap);

                        editmessege.setText("");

                       rootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                Log.d("ktjrhnk", "onComplete: ");
                                loader.dismiss();

                                if (databaseError != null){

                                    Log.d("ktjrhnk", "onComplete: "+databaseError.getMessage().toString());

                                }

                            }
                        });


                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                               loader.dismiss();
                            }
                        });

            }
        });

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



        DatabaseReference messageRef = rootRef.child(FirebaseConstants.Message).child(current_User_ID).child(userId);

        Query messageQuery = messageRef.limitToLast(currentPage * TOTEL_ITEMS_TO_LODE);

        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(messageQuery, Message.class)
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
                //holder.userMessage.setText(model.getMessage());

                if (model.getType().equals("text")){



                    if (model.getFrom().equals(current_User_ID)){

                        holder.currentUserMessage.setVisibility(View.VISIBLE);
                        holder.currentUserimage.setVisibility(View.GONE);
                        holder.userimage.setVisibility(View.GONE);
                        holder.userMessage.setVisibility(View.GONE);
                        holder.currentUserMessage.setText(model.getMessage());

                    }else{

                        holder.userMessage.setVisibility(View.VISIBLE);
                        holder.currentUserMessage.setVisibility(View.GONE);
                        holder.currentUserimage.setVisibility(View.GONE);
                        holder.userimage.setVisibility(View.GONE);
                        holder.userMessage.setText(model.getMessage());


                    }

                }else{

                    if (model.getFrom().equals(current_User_ID)){

                        holder.currentUserimage.setVisibility(View.VISIBLE);
                        holder.userimage.setVisibility(View.GONE);
                        holder.currentUserMessage.setVisibility(View.GONE);
                        holder.userMessage.setVisibility(View.GONE);
                        Picasso.get().load(model.getMessage()).into(holder.currentUserimage);
                       // holder.currentUserimage.setBackgroundResource(R.drawable.rounded_typing);

                    }else{
                        holder.userimage.setVisibility(View.VISIBLE);
                        holder.currentUserimage.setVisibility(View.GONE);
                        holder.currentUserMessage.setVisibility(View.GONE);
                        holder.userMessage.setVisibility(View.GONE);
                        Picasso.get().load(model.getMessage()).into(holder.userimage);
                       // holder.userMessage.setBackgroundResource(R.drawable.message_backgraund);


                    }


                }




            }
        };

        messageList.setAdapter(adapter);
        adapter.startListening();

    }

    class MessageHolder extends RecyclerView.ViewHolder{


        TextView currentUserMessage,userMessage;
        ImageView currentUserimage,userimage;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);

            currentUserMessage = itemView.findViewById(R.id.currentuserTextV);
            userMessage = itemView.findViewById(R.id.userTextV);

            currentUserimage = itemView.findViewById(R.id.currentuserImage);
            userimage = itemView.findViewById(R.id.userImage);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode,requestCode, data);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
    }


}
