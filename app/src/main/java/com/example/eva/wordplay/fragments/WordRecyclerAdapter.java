package com.example.eva.wordplay.fragments;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eva.wordplay.R;
import com.example.eva.wordplay.data.Word;

import java.util.ArrayList;


class WordRecyclerAdapter extends RecyclerView.Adapter<WordRecyclerAdapter.ViewHolder> {

    private ArrayList<Word> words;
    private Boolean wordCreated;
    private ArrayList<Boolean> isPicked;

    WordRecyclerAdapter(ArrayList<Word> words, ArrayList<Boolean> isPicked) {
        this.wordCreated = false;
        this.words = words;
        this.isPicked = isPicked;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView word;
        TextView translation;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            word = (TextView) itemView.findViewById(R.id.word);
            translation = (TextView) itemView.findViewById(R.id.translation);
            layout = (LinearLayout)itemView.findViewById(R.id.word_layout);
        }

        void pickWord() {
            layout.setBackgroundColor(Color.parseColor("#ffff88"));
        }

        void unpickWord() {
            layout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Override
    public WordRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View newView = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item, parent, false);
        return new WordRecyclerAdapter.ViewHolder(newView);
    }

    @Override
    public void onBindViewHolder(WordRecyclerAdapter.ViewHolder holder, int position) {
        if(isPicked.get(position)){
            holder.pickWord();
        } else {
            holder.unpickWord();
        }
        holder.word.setText(words.get(position).getWord());
        holder.translation.setText(words.get(position).getTranslation());
        if((position == 0)&&wordCreated){
            holder.pickWord();
            wordCreated = false;
        }
    }

    public void setWordCreated(){
        wordCreated = true;
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

}


