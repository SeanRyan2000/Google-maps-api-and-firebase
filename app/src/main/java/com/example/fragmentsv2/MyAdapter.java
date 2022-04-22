package com.example.fragmentsv2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Accommodation> list;
    private OnNoteListener mOnNoteListener;

    public MyAdapter(Context context, ArrayList<Accommodation> list, OnNoteListener onNoteListener) {

        this.context = context;
        this.list = list;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Accommodation accomodation = list.get(position);
        holder.address.setText("Address: " + accomodation.getAddress());
        holder.contact.setText("Contact number: " + accomodation.getContact());
        holder.price.setText("Price per month: €" + String.valueOf(accomodation.getPrice()));
        holder.totalBeds.setText("Total Beds: " + String.valueOf(accomodation.getNumOfBedrooms()));
        holder.availableBeds.setText("Available Beds: " +  String.valueOf(accomodation.getSpacesAvailable()));
    }

    @Override
    public int getItemCount() {
        Log.e(" List", " List Size is " + list.size());
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView address, contact, price, totalBeds, availableBeds;
        OnNoteListener onNoteListener;


        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            address = itemView.findViewById(R.id.textViewShowAddress);
            contact = itemView.findViewById(R.id.textViewShowContact);
            price = itemView.findViewById(R.id.textViewShowPrice);
            totalBeds = itemView.findViewById(R.id.textViewShowTotalBeds);
            availableBeds = itemView.findViewById(R.id.textViewShowBedsAvailable);

            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);

    }


}
