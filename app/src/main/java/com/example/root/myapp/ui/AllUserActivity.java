package com.example.root.myapp.ui;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.root.myapp.R;
import com.example.root.myapp.common.FirebaseConstants;
import com.example.root.myapp.common.Loader;
import com.example.root.myapp.model.User;
import com.example.root.myapp.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUserActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView alluserlist;

    private Loader loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);

        init();



        alluserlist.setAdapter(adapter);
        adapter.startListening();


    }



    FirebaseRecyclerOptions<User> options =
            new FirebaseRecyclerOptions.Builder<User>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.User),
                            User.class)
                    .build();

    FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<User, UserHolder>(options) {
        @Override
        public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Create a new instance of the ViewHolder, in this case we are using a custom
            // layout called R.layout.message for each item
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.singluser, parent, false);

            return new UserHolder(view);
        }

        @Override
        protected void onBindViewHolder(UserHolder holder, int position, User model) {
            // Bind the User object to the UserHolder
            // ...


            final String userID = getRef(position).getKey();

            holder.name.setText(model.getName());
            holder.status.setText(model.getStatus());

            Picasso.get().load(model.getCompressor_image()).placeholder(R.drawable.userimage).into(holder.profileImage);

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(AllUserActivity.this, ProfaileActivity.class);
                    intent.putExtra("userID", userID);
                    startActivity(intent);


                }
            });

        }
    };

    public void init(){

        loader = new Loader(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All User");

        alluserlist = findViewById(R.id.AllUser_recyclerView);
        alluserlist.setHasFixedSize(true);
        alluserlist.setLayoutManager(new LinearLayoutManager(this));

    }

    class UserHolder extends RecyclerView.ViewHolder{


        TextView name,status;
        CircleImageView profileImage;
        LinearLayout linearLayout;

        public UserHolder(@NonNull View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            profileImage = itemView.findViewById(R.id.profile_image);
            linearLayout = itemView.findViewById(R.id.labeled_profail);
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
