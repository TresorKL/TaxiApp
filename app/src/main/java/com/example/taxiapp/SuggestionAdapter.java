package com.example.taxiapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//------------------------------------------------------------------------------------------------
//Adapter using ViewHolder
//------------------------------------------------------------------------------------------------
public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder> {

String [] topDirections;
    public SuggestionAdapter(String [] topDirections){
        this.topDirections =topDirections;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_suggestion, null);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.direction.setText(topDirections[position]);
    }


    @Override
    public int getItemCount() {
        return topDirections.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout row;
        TextView direction;
        public ViewHolder(View view) {
            super(view);
            direction =view.findViewById(R.id.topDirection);
            row= view.findViewById(R.id.directionRow);
        }
    }
}
