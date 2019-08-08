package com.example.kspartner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RestaurantDetail extends Fragment {
    Button logout;
    TextView res_name,res_description;
    TextView edit_profile, edit_description;
    String rid;
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

        fetchDataFromDataBase();

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
//                        Log.d("LOG", "onDataChange: "+des);
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
