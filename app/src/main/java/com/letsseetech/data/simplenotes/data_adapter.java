package com.letsseetech.data.simplenotes;

import android.app.Activity;
import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class data_adapter extends RecyclerView.Adapter<data_adapter.MyViewHolder>  {
    Context context;
    Activity activity;
    List<Data> notesList;
    List<Data> newList;

    public data_adapter(Context context, Activity activity, List<Data> notesList) {
        this.context = context;
        this.activity = activity;
        this.notesList = notesList;
        newList = new ArrayList<>(notesList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.title.setText(notesList.get(position).getTitle());
        holder.description.setText(notesList.get(position).getDescription());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("title", notesList.get(position).getTitle());
                bundle.putString("description", notesList.get(position).getDescription());
                bundle.putString("id", notesList.get(position).getId());
                bundle.putString("userId", notesList.get(position).getUserId());

                // Set Fragmentclass Arguments
                EditFragment sampleFragment = new EditFragment();
                sampleFragment.setArguments(bundle);

                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                ;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //EditFragment sampleFragment=new EditFragment();
                fragmentTransaction.replace(R.id.fragmentContainer, sampleFragment);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        LinearLayout layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            layout = itemView.findViewById(R.id.note_layout);
        }
    }


    public List<Data> getList() {
        return notesList;
    }

    public void removeItem(int position) {
        notesList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Data item, int position) {
        notesList.add(position, item);
        notifyItemInserted(position);
    }
}
