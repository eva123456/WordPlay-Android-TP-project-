package com.example.eva.wordplay.fragments;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eva.wordplay.R;

/**
 * Created by eva on 14.04.17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private String[] data;

    public RecyclerAdapter(String[] data){
        this.data = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView deckCard;
        TextView deckName;
        TextView percent;
        ImageView justForFun;

        public ViewHolder(View itemView) {
            super(itemView);
            deckCard = (CardView) itemView.findViewById(R.id.deckCard);
            deckName = (TextView) itemView.findViewById(R.id.deckName);
            percent = (TextView) itemView.findViewById(R.id.percent);
            justForFun = (ImageView) itemView.findViewById(R.id.picture);

        }
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View newView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(newView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        holder.deckName.setText(data[position]);
        holder.percent.setText("75% complete");
    }

    @Override
    public int getItemCount() {
        return data.length;
    }
}
