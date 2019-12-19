package com.example.all4cars;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewServiceAdapter extends RecyclerView.Adapter<ViewServiceAdapter.ViewHolder> {
    ArrayList<AddServiceAttr> addServiceAttrs;
    private Context context;
    public ViewServiceAdapter(ArrayList<AddServiceAttr> addServiceAttrs, Context context){
        this.context=context;
        this.addServiceAttrs = addServiceAttrs;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Picasso.get().load(addServiceAttrs.get(position).getImage_url()).into(holder.serviceImage);
        holder.service.setText(addServiceAttrs.get(position).getService());
        holder.company.setText(addServiceAttrs.get(position).getCompanyName());
        holder.location.setText(addServiceAttrs.get(position).getLocation());
        holder.close.setText(addServiceAttrs.get(position).getCloseTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceId = null;
                serviceId = addServiceAttrs.get(position).getId();

                Intent i = new Intent(context , ServiceDetail.class);
                i.putExtra("Id" , serviceId);
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return addServiceAttrs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView serviceImage;
        TextView service,location,company,close;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceImage = itemView.findViewById(R.id.imgService);
            service = itemView.findViewById(R.id.txtService);
            company = itemView.findViewById(R.id.txtCompany);
            location = itemView.findViewById(R.id.txtLocation);
            close = itemView.findViewById(R.id.txtCloseTime);
        }
    }
}
