package com.example.kspartner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SubMenuItemAdaptor extends RecyclerView.Adapter<SubMenuItemAdaptor.SubMenuItemViewHolder> {
    Context context;
    ArrayList<String> item_list;
    ArrayList<String> price_list;

    public SubMenuItemAdaptor(Context context, ArrayList<String> item_list, ArrayList<String> price_list) {
        this.context = context;
        this.item_list = item_list;
        this.price_list = price_list;
    }

    @NonNull
    @Override
    public SubMenuItemAdaptor.SubMenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.submenu_item_list,parent,false);
        return new SubMenuItemAdaptor.SubMenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubMenuItemAdaptor.SubMenuItemViewHolder holder, int position) {

        holder.item_name.setText(item_list.get(position));
        holder.item_price.setText(price_list.get(position));

    }

    @Override
    public int getItemCount() {
        return item_list.size();
    }

    public class SubMenuItemViewHolder extends RecyclerView.ViewHolder{
        TextView item_name, item_price;

        public SubMenuItemViewHolder(@NonNull View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.submenu_item_name);
            item_price = itemView.findViewById(R.id.submenu_item_price);
        }
    }
}
