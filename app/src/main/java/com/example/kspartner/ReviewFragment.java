package com.example.kspartner;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ReviewFragment extends Fragment {
    String rid;
    ReviewAdaptor reviewAdaptor;
    RecyclerView recyclerView;
    Integer avg_rating = 0;
    TextView ratingsBold;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view =  inflater.inflate(R.layout.fragment_review, null);
        recyclerView = view.findViewById(R.id.recycler_view_container);
        ratingsBold = view.findViewById(R.id.ratingsBold);

        final ArrayList<String> list_name = new ArrayList<>();
        final ArrayList<String> list_star = new ArrayList<>();
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Restaurant_Pref",0);
        rid = preferences.getString("rid",null);
        DatabaseReference review_ref = FirebaseDatabase.getInstance().getReference("Rating").child(rid);
        Toast.makeText(getContext(),""+review_ref,Toast.LENGTH_LONG).show();

            review_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null ) {
                        ratingsBold.setText("0.00");
                        TextView textView = view.findViewById(R.id.txt_review);
                        textView.setText("No Ratings");

                    }else{
                        for(DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            String name = snapshot.child("name").getValue(String.class);
                            String star = snapshot.child("stars").getValue(String.class);

                            list_name.add(name);
                            list_star.add(star);
                            avg_rating += Integer.parseInt(star);
                            reviewAdaptor = new ReviewAdaptor(recyclerView, getActivity(),list_name,list_star);
                            recyclerView.setAdapter(reviewAdaptor);

                            recyclerView.setHasFixedSize(true);
                            // use a linear layout manager
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(mLayoutManager);

                        }
                        ratingsBold.setText(String.valueOf(avg_rating));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        return view;
    }
}
