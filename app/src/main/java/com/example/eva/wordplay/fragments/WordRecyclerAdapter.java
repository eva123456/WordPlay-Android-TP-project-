package com.example.eva.wordplay.fragments;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eva.wordplay.R;
import com.example.eva.wordplay.data.Word;

import java.util.ArrayList;


public class WordRecyclerAdapter extends RecyclerView.Adapter<WordRecyclerAdapter.ViewHolder> {

    private ArrayList<Word> words;

    public WordRecyclerAdapter(ArrayList<Word> words){
        this.words = words;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView word;
        TextView translation;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            word = (TextView) itemView.findViewById(R.id.word);
            translation = (TextView) itemView.findViewById(R.id.translation);
            layout = (LinearLayout)itemView.findViewById(R.id.word_layout);
            //unpickWord();
        }

        public void pickWord(){
            layout.setBackgroundColor(Color.parseColor("#FFFF88"));
        }

        public void unpickWord(){
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
        holder.word.setText(words.get(position).getWord());
        holder.translation.setText(words.get(position).getTranslation());
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

}
