package com.example.kspartner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class RestaurantDetail extends Fragment {
    Button logout;
    TextView res_name,res_description;
    TextView edit_profile, edit_description;
    CircleImageView photo;
    String rid;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    private final int PICK_IMAGE_REQUEST = 71;
    Uri download_url;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_detail, null);
        SharedPreferences preferences = getActivity().getSharedPreferences("Restaurant_Pref",0);
        rid = preferences.getString("rid",null);
        logout = view.findViewById(R.id.logout);
        res_name = view.findViewById(R.id.name_of_restaurant);
        res_description = view.findViewById(R.id.profile_description);
        edit_profile = view.findViewById(R.id.edit_restaurant_profile);
        edit_description = view.findViewById(R.id.edit_description);
        photo = view.findViewById(R.id.image);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        fetchDataFromDataBase();
        setrestaurant_picture();

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent_login = new Intent(getActivity(), Login.class);
                startActivity(intent_login);

            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateProfile.class);
                startActivity(intent);
            }
        });

        edit_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateDescription.class);
                startActivity(intent);
            }
        });

        return view;
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                photo.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        uploadImage();
    }

    private void uploadImage() {
        final DatabaseReference imageUpload = FirebaseDatabase.getInstance().getReference("Images");
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref;
            ref = storageReference.child( UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    download_url = uri;

                                    Log.d("LOG", "onSuccess: "+ download_url);
                                    Log.d("LOG",""+ imageUpload);
                                    imageUpload.child(rid).child("imgpath").setValue(download_url.toString());
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
    public void setrestaurant_picture()
    {

        DatabaseReference reference_img;
        reference_img = FirebaseDatabase.getInstance().getReference("Images").child(rid);
        reference_img.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String imgpath = dataSnapshot.child("imgpath").getValue(String.class);
                if (imgpath == null) {
                    imgpath = "https://firebasestorage.googleapis.com/v0/b/khajasangram-c54ca.appspot.com/o/food.png?alt=media&token=afec85ea-f5d6-436b-b3e8-174c039005fb";
                    Picasso.get().load(imgpath).into(photo);
                } else {
                    Picasso.get().load(imgpath).into(photo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void fetchDataFromDataBase() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Restaurants");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Description");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    String id = snapshot.child("id").getValue(String.class);
                    //Toast.makeText(getApplicationContext(), "database id= "+id+" and pased id= "+intent_id, Toast.LENGTH_SHORT).show();

                    if(rid.equals(id))
                    {
                        //Toast.makeText(getApplicationContext(), "id= "+id, Toast.LENGTH_SHORT).show();
                        String dname = snapshot.child("name").getValue(String.class);
                        String daddress = snapshot.child("address").getValue(String.class);
                        String dcontact = snapshot.child("contact").getValue(String.class);

                        res_name.setText(dname);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String id = snapshot.child("rid").getValue(String.class);
                    Log.d("LOG", "onDataChanges: "+id);
                    if(rid.equals(id))
                    {
//                        Log.d("LOG", "onDataChange: "+id);
                        //Toast.makeText(getApplicationContext(), "id= "+id, Toast.LENGTH_SHORT).show();
                        String des = snapshot.child("description").getValue(String.class);
                        Log.d("DB", "onDataChange: "+des);
                        res_description.setText(des);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
