
package com.example.kspartner;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewAdaptor extends RecyclerView.Adapter<ReviewAdaptor.ReviewViewHolder> {

    RecyclerView recyclerView;
    Context context;
    ArrayList<String> name ;
    ArrayList<String> stars ;


    public ReviewAdaptor(RecyclerView recyclerView, Context context, ArrayList<String> name, ArrayList<String> stars) {

        this.context = context;
        this.name = name;
        this.stars = stars;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.review_display_layout,parent,false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        holder.fname.setText(name.get(position));
        //Toast.makeText(context, "value= "+stars.get(position) , Toast.LENGTH_SHORT).show();
        holder.ratingBar.setRating(Float.parseFloat(stars.get(position)));


    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        TextView fname;
        RatingBar ratingBar;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            fname = itemView.findViewById(R.id.fname);
            ratingBar = itemView.findViewById(R.id.ratingbar);
        }
    }
}
