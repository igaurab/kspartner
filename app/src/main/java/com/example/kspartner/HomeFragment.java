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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView, recyclerview_submenu_item;
    ArrayList<String> namelist;
    DatabaseReference reference;
    String rid;
    int foodlist_index = 0;
    MenuItemAdaptor adaptor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        namelist = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view_container_home);
        recyclerview_submenu_item = view.findViewById(R.id.submenu_item_rv); SharedPreferences preferences = this.getActivity().getSharedPreferences("Restaurant_Pref",0);
        rid = preferences.getString("rid",null);
        reference = FirebaseDatabase.getInstance().getReference("Foodlist").child(rid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int i=0; i< dataSnapshot.getChildrenCount();i++)
                {
                    String name = dataSnapshot.child("name"+foodlist_index).getValue(String.class);
                    foodlist_index++;

                    namelist.add(name);
                    adaptor = new MenuItemAdaptor(getActivity(), namelist, rid, recyclerview_submenu_item);

                    recyclerView.setAdapter(adaptor);

                    recyclerView.setHasFixedSize(true);

                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(mLayoutManager);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
}
