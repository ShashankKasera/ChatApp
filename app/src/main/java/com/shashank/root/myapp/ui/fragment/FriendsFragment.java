package com.shashank.root.myapp.ui.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shashank.root.myapp.R;
import com.shashank.root.myapp.common.FirebaseConstants;
import com.shashank.root.myapp.common.Profiledialog;
import com.shashank.root.myapp.model.Friend;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.root.myapp.ui.activity.ChatActivity;
import com.shashank.root.myapp.ui.activity.ProfaileActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {


    private String current_User_Id;

    private RecyclerView frind_List;

    private View mainView;

    private DatabaseReference frienddataRef;
    private FirebaseRecyclerAdapter adapter;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mainView = inflater.inflate(R.layout.fragment_friends, container, false);

         init();

         return mainView;


    }

    private void init(){

        current_User_Id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        frienddataRef = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.Friend).child(current_User_Id);

        frind_List = mainView.findViewById(R.id.Friend_recyceler);
        frind_List.setHasFixedSize(true);
        frind_List.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Friend> options =
                new FirebaseRecyclerOptions.Builder<Friend>()
                        .setQuery(frienddataRef, Friend.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Friend, FriendHolder>(options) {
            @Override
            public FriendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.singluser, parent, false);

                return new FriendHolder(view);
            }

            @Override
            protected void onBindViewHolder(final FriendHolder holder, int position, Friend model) {
                // Bind the User object to the FriendHolder
                // ...

                final String id  = getRef(position).getKey();
                FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseConstants.User)
                        .child(id)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                String name = dataSnapshot.child(FirebaseConstants.name).getValue(String.class);
                                holder.name.setText(name);
                                Picasso.get().load(dataSnapshot.child(FirebaseConstants.compressor_image).getValue(String.class)).placeholder(R.drawable.userimage).into(holder.profileImage);


                                holder.profileImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Profiledialog profiledialog = new Profiledialog(getContext()
                                                ,dataSnapshot.child(FirebaseConstants.profile).getValue(String.class)
                                                ,dataSnapshot.child(FirebaseConstants.name).getValue(String.class));

                                        profiledialog.show();
                                    }
                                });

                                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        CharSequence option[] = new CharSequence[]{"Open Profile","Send Massege"};

                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                        builder.setTitle("Select Option");
                                        builder.setItems(option, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                if (which==0){

                                                    Intent Profaileintent = new Intent(getContext(), ProfaileActivity.class);
                                                    Profaileintent.putExtra("userID", id);
                                                    startActivity(Profaileintent);

                                                }

                                                if (which==1){

                                                    Intent Chatintent = new Intent(getContext(), ChatActivity.class);
                                                    Chatintent.putExtra("userID", id);
                                                    startActivity(Chatintent);
                                                }



                                            }
                                        });

                                        builder.show();

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                holder.date.setText(model.getDate());


            }
        };

        frind_List.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.stopListening();
    }

    class FriendHolder extends RecyclerView.ViewHolder{

        TextView name,date;
        CircleImageView profileImage;
        LinearLayout linearLayout;

        public FriendHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.status);
            profileImage = itemView.findViewById(R.id.profile_image);
            linearLayout = itemView.findViewById(R.id.labeled_profail);
        }
    }


}





