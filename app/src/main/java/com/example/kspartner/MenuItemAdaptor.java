package com.example.kspartner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuItemAdaptor extends RecyclerView.Adapter<MenuItemAdaptor.MenuItemViewHolder> {

    RecyclerView recyclerView;
    Context context;
     ArrayList<String> list;
     ArrayList<String> submenuitem_list ;
     ArrayList<String> submenuitem_pricelist;
    ArrayList<String> submenuitem_list1 ;
     ArrayList<String> submenuitem_pricelist1 ;
     DatabaseReference reference;
     String id;
    int submenu_item_index ;
    int submenu_item_index1 ;
    SubMenuItemAdaptor subMenuItemAdaptor;
    DatabaseReference reference1;
    LinearLayout linearLayout;




    public MenuItemAdaptor(Context context, ArrayList<String> list, String id, RecyclerView recyclerView)
    {
        this.context = context;
        this.list = list;
        this.id = id;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.menu_item_list,parent,false);
        return new MenuItemAdaptor.MenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuItemViewHolder holder, final int position) {

        holder.item_name.setText(list.get(position));
        linearLayout = holder.menu_item;

        holder.menu_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setBackgroundResource(R.drawable.rec_box_rounded_corner);
                //holder.menu_item.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                holder.menu_item.setBackgroundResource(R.drawable.menu_item_pressed);
                linearLayout = holder.menu_item;
                reference = FirebaseDatabase.getInstance().getReference("Menu").child(id).child(list.get(position));

                //when new item is pressed the string array should be initialized
                //again to new list of strings
                //and also the index should start from 0
                submenuitem_list = new ArrayList<>();
                submenuitem_pricelist = new ArrayList<>();
                submenu_item_index = 0;
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                                String subitem = dataSnapshot.child("name" + submenu_item_index).child("name").getValue(String.class);
                                String subitem_price = dataSnapshot.child("name" + submenu_item_index).child("price").getValue(String.class);


                                submenu_item_index++;

                                submenuitem_list.add(subitem);
                                submenuitem_pricelist.add(subitem_price);

                                subMenuItemAdaptor = new SubMenuItemAdaptor(context, submenuitem_list, submenuitem_pricelist);

                                recyclerView.setAdapter(subMenuItemAdaptor);

                                recyclerView.setHasFixedSize(true);

                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                                recyclerView.setLayoutManager(mLayoutManager);
                            }
                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MenuItemViewHolder extends RecyclerView.ViewHolder {
        TextView item_name;
        LinearLayout menu_item;

        public MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.food_name);
            menu_item = itemView.findViewById(R.id.menu_item);
        }
    }
}
