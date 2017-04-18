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
import com.example.eva.wordplay.data.WordSet;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<WordSet> wordSets;

    public RecyclerAdapter(ArrayList<WordSet> wordSets){
        this.wordSets = wordSets;
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
        holder.deckName.setText(wordSets.get(position).getName());
        holder.percent.setText(Integer.toString(wordSets.get(position).getPercent()) + "% completed");
    }

    @Override
    public int getItemCount() {
        return wordSets.size();
    }

    public ArrayList<WordSet> getWordSets() {
        if(wordSets!=null)
            return wordSets;
        else
            return null;
    }
}
