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
import com.example.root.myapp.model.Friend;
import com.example.root.myapp.model.FriendRequest;
import com.example.root.myapp.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {

    private View mainView;

    private String current_User_Id;

    private RecyclerView requestslist;

    private DatabaseReference frienddataRef;
    private FirebaseRecyclerAdapter adapter;

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_request, container, false);

        init();

        requestslist.setAdapter(adapter);
        adapter.startListening();

        return mainView;
    }

    private void init(){

        requestslist = mainView.findViewById(R.id.requestlist);

        frienddataRef = FirebaseDatabase.getInstance().getReference();
        current_User_Id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        requestslist.setHasFixedSize(true);
        requestslist.setLayoutManager(new LinearLayoutManager(getContext()));


        FirebaseRecyclerOptions<FriendRequest> options =
                new FirebaseRecyclerOptions.Builder<FriendRequest>()
                        .setQuery(frienddataRef.child(FirebaseConstants.Friend_Request).child(current_User_Id), FriendRequest.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<FriendRequest, RequestHolder>(options) {
            @Override
            public RequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.singluser, parent, false);

                return new RequestHolder(view);
            }

            @Override
            protected void onBindViewHolder(final RequestHolder holder, int position, FriendRequest model) {
                // Bind the FriendRequest object to the ChatHolder
                // ...


                holder.status.setText(model.getRequesr_type());
                Log.d("lekgn", "onBindViewHolder: "+model.getRequesr_type());

                final String id = getRef(position).getKey();

               frienddataRef.child(FirebaseConstants.User).child(id)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                holder.name.setText(dataSnapshot.child(FirebaseConstants.name).getValue().toString());
                                Picasso.get().load(dataSnapshot.child(FirebaseConstants.compressor_image).getValue(String.class)).placeholder(R.drawable.userimage).into(holder.profileImage);
                                //holder.status.setText(dataSnapshot.child(FirebaseConstants.status).getValue().toString());

                                Log.d("bdkjg", "onDataChange: "+dataSnapshot.child(FirebaseConstants.compressor_image).getValue(String.class));

                                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent Profaileintent = new Intent(getContext(), ProfaileActivity.class);
                                        Profaileintent.putExtra("userID", id);
                                        startActivity(Profaileintent);

                                    }
                                });

                                Log.d("kjwf", "onDataChange: "+holder.name);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

            }
        };

    }

    class RequestHolder extends RecyclerView.ViewHolder{

        TextView name,status;
        CircleImageView profileImage;
        LinearLayout linearLayout;

        public RequestHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            profileImage = itemView.findViewById(R.id.profile_image);
            linearLayout = itemView.findViewById(R.id.labeled_profail);
        }
    }

}
