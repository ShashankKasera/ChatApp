package com.example.root.myapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {


    private View mainView;

    private RecyclerView chatslist;

    private String userid;

    private FirebaseRecyclerAdapter adapter;

    private DatabaseReference reference;
    private String current_user_id;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mainView = inflater.inflate(R.layout.fragment_chats, container, false);

        init();
        chatslist.setAdapter(adapter);
        adapter.startListening();

        return mainView;
    }

    private void init(){

        current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference();

        chatslist = mainView.findViewById(R.id.chatlist);


        chatslist.setHasFixedSize(true);
        chatslist.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = reference.child(FirebaseConstants.Message)
                .child(current_user_id);




        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(query, Message.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Message, ChatHolder>(options) {
            @Override
            public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.singluser, parent, false);

                return new ChatHolder(view);
            }

            @Override
            protected void onBindViewHolder(final ChatHolder holder, int position, final Message model) {
                // Bind the Chat object to the ChatHolder
                // ...

                Query query1 = reference.child(FirebaseConstants.Message).child(current_user_id)
                        .child(getRef(position).getKey())
                        .limitToLast(1);

                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                holder.lastChat.setText(dataSnapshot.getChildren().iterator().next().child("message").getValue().toString());

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                Log.d("kdhk", "onBindViewHolder: "+model.getFrom());
                final String id = getRef(position).getKey();


                Log.i("dshgcjadg", "onBindViewHolder: "+getRef(position).getKey());

                FirebaseDatabase.getInstance().getReference().child("User")
                        .child(getRef(position).getKey())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        holder.name.setText(dataSnapshot.child(FirebaseConstants.name).getValue(String.class));
                        Picasso.get().load(dataSnapshot.child(FirebaseConstants.compressor_image).getValue(String.class))
                                .placeholder(R.drawable.userimage).into(holder.profileImage);
                        Log.d("ekjrgk", "onDataChange: "+dataSnapshot.child(FirebaseConstants.compressor_image).getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent Chatileintent = new Intent(getContext(), ChatActivity.class);
                        Chatileintent.putExtra("userID", id);
                        startActivity(Chatileintent);
                    }
                });
            }
        };


    }

    class ChatHolder extends RecyclerView.ViewHolder {

        TextView name,lastChat;
        CircleImageView profileImage;
        LinearLayout linearLayout;
        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            lastChat = itemView.findViewById(R.id.status);
            profileImage = itemView.findViewById(R.id.profile_image);
            linearLayout = itemView.findViewById(R.id.labeled_profail);
        }
    }


}