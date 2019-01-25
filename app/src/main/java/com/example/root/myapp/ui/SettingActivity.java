package com.example.root.myapp.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.myapp.R;
import com.example.root.myapp.common.FirebaseConstants;
import com.example.root.myapp.common.Loader;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private Loader loader;

    private ImageView nameedit;
    private ImageView statusedit;
    private ImageView addressedit;
    private ImageView editImage;

    private TextView nameUpdet;
    private TextView statusUpdet;
    private TextView addressUpdet;
    private ImagePicker imagePicker;
    private CircleImageView ProfileImage;
    private CircleImageView  compressor_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);



        loader = new Loader(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Uset Setting");


        nameUpdet = findViewById(R.id.Updet_name);
        statusUpdet = findViewById(R.id.Udet_status);
        addressUpdet = findViewById(R.id.Updet_Address);
        editImage = findViewById(R.id.edit_img);


        nameedit = findViewById(R.id.image_name_edit);
        statusedit = findViewById(R.id.image_status_edit);
        addressedit = findViewById(R.id.image_address_edit);
        ProfileImage = findViewById(R.id.profile_image);

        compressor_image = findViewById(R.id.profile_image);

        imagePicker = new ImagePicker(this, /* activity non null*/
                null,
                new OnImagePickedListener(){
                    @Override
                    public void onImagePicked(Uri imageUri) {
                        imageUpload(imageUri);
                    }
                });



        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.choosePicture(true /*show camera intents*/);
            }
        });

        nameedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loader.show();
                Intent intent = new Intent(SettingActivity.this, UpdateProfile.class);
                intent.putExtra("key", FirebaseConstants.name);
                startActivity(intent);
                finish();
            }
        });


        statusedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loader.show();
                Intent intent = new Intent(SettingActivity.this, UpdateProfile.class);
                intent.putExtra("key", FirebaseConstants.status);
                startActivity(intent);
                finish();
            }
        });


        addressedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loader.show();
                Intent intent = new Intent(SettingActivity.this, UpdateProfile.class);
                intent.putExtra("key", FirebaseConstants.address);
                startActivity(intent);
                finish();
            }
        });





        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseConstants.User)
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        nameUpdet.setText(dataSnapshot.child(FirebaseConstants.name).getValue(String.class));
                        Log.d("kjkh", "onDataChange: " + dataSnapshot.child(FirebaseConstants.name).getValue(String.class));

                        statusUpdet.setText(dataSnapshot.child(FirebaseConstants.status).getValue(String.class));
                        Log.d("kjkh", "onDataChange: " + dataSnapshot.child(FirebaseConstants.status).getValue(String.class));

                        addressUpdet.setText(dataSnapshot.child(FirebaseConstants.address).getValue(String.class));
                        Log.d("kjkh", "onDataChange: " + dataSnapshot.child(FirebaseConstants.address).getValue(String.class));

                        String image = dataSnapshot.child(FirebaseConstants.profile).getValue(String.class);

                        Log.d("enrg,", "onDataChange: "+image);

                        Picasso.get().load(image).placeholder(R.drawable.userimage).into(ProfileImage);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void imageUpload(final Uri file) {


        String s =  getRealPathFromUri(this,file);
        Log.i("dsbjfsdbkfb", "imageUpload: "+file+ " "+ s);

        loader.show();
        Uri uri = null;

        try {
            Bitmap bit = new Compressor(this)
                    .setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(60)
                    .compressToBitmap(new File(s));


            uri = getImageUri(this,bit);

        } catch (IOException e) {
            e.printStackTrace();
        }

        final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("profile").child(FirebaseAuth.getInstance().getUid()+".png");

        final StorageReference Compressor_filepath = FirebaseStorage.getInstance().getReference()
                .child("Compressor_profile").child(FirebaseAuth.getInstance().getUid()+".png");




        Compressor_filepath.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        Compressor_filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("fnm", "onSuccess: "+uri);

                                Map<String,Object> map = new HashMap<>();
                                map.put(FirebaseConstants.compressor_image,uri.toString());

                                FirebaseDatabase.getInstance().getReference()
                                        .child(FirebaseConstants.User)
                                        .child(FirebaseAuth.getInstance().getUid())
                                        .updateChildren(map)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                compressor_image.setImageURI(file);

                                                uploadProfileImage(reference,file);
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

    private void uploadProfileImage(final StorageReference reference, final Uri file) {
        reference.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("fnm", "onSuccess: "+uri);

                                Map<String,Object> map = new HashMap<>();
                                map.put(FirebaseConstants.profile,uri.toString());

                                FirebaseDatabase.getInstance().getReference()
                                        .child(FirebaseConstants.User)
                                        .child(FirebaseAuth.getInstance().getUid())
                                        .updateChildren(map)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                ProfileImage.setImageURI(file);
                                                loader.dismiss();
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
