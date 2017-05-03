package com.example.eva.wordplay.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.eva.wordplay.R;
import com.example.eva.wordplay.data.WordSet;

import java.util.ArrayDeque;
import java.util.HashMap;


public class WordPlayFragment extends Fragment implements View.OnClickListener{

    View view;
    TextView wordView, hintView;
    CardView hintCard;
    private WordSet currentDeck;
    private ArrayDeque<String> words = new ArrayDeque<>();
    private DeckListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.word_play_fragment, null);
        wordView = (TextView) view.findViewById(R.id.wordDisplay);
        hintView = (TextView) view.findViewById(R.id.translationDisplay);
        hintCard = (CardView) view.findViewById(R.id.hintCard);

        Button btnOk, btnFail, btnHelp;
        btnOk = (Button) view.findViewById(R.id.wordOk);
        btnFail = (Button) view.findViewById(R.id.wordFail);
        btnHelp = (Button) view.findViewById(R.id.showTranslation);

        btnOk.setOnClickListener(this);
        btnFail.setOnClickListener(this);
        btnHelp.setOnClickListener(this);

        listener.onCheckStart();

        return view;
    }

    public interface DeckListener{
        void onCheckStart();
        void onCheckFinish();
    }

    public void registerDeckListener(DeckListener listener){
        this.listener = listener;
    }

    public void setDeck(WordSet currentDeck){
        this.currentDeck = currentDeck;
        HashMap<String, String> wordPack = currentDeck.getWords();//TODO exclude correct words
        for(String key : wordPack.keySet()){
            words.add(key);
        }
    }

    public void goNext(){
        if(words.isEmpty()){
            listener.onCheckFinish();
        }
        String word = words.poll();
        wordView.setText(word);
    }

    private void markWordAsCorrect(){
        String word = (String) wordView.getText();
        currentDeck.markWordCorrect(word);
        //TODO db update
    }

    private void getHelp(){
        hintCard.setVisibility(View.VISIBLE);
        String word = (String) wordView.getText();
        String translation = currentDeck.getTranslation(word);
        hintView.setText(translation);
    }

    @Override
    public void onClick(View v) {
        hintCard.setVisibility(View.INVISIBLE);
        switch (v.getId()){
            case R.id.wordOk:
                markWordAsCorrect();
                goNext();
                break;
            case R.id.wordFail:
                goNext();
                break;
            case R.id.showTranslation:
                getHelp();
                break;
        }
    }
}
