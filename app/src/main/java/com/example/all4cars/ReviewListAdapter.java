package com.example.all4cars;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {
    ArrayList<Rating_Attr> rating_attrs;
    private Context context;
    public ReviewListAdapter(ArrayList<Rating_Attr> rating_attrs, Context context){
        this.context=context;
        this.rating_attrs = rating_attrs;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.review,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.comnt.setText(rating_attrs.get(position).getComment());
        holder.user.setText(String.valueOf(rating_attrs.get(position).getTotal()));
    }

    @Override
    public int getItemCount() {
        return rating_attrs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView comnt,user;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            comnt = itemView.findViewById(R.id.comnt);
            user = itemView.findViewById(R.id.user);
        }
    }
}
