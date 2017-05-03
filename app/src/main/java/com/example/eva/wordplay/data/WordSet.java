package com.example.eva.wordplay.data;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class WordSet implements Serializable{

    private String name;
    private HashMap<String, String> words = new HashMap<>();
    private ArrayList<String> correctWords = new ArrayList<>();

    public void setName(String name){
        this.name = name;
    }

    public void addWord(String word, String translation){
        words.put(word, translation);
    }

    public HashMap<String, String> getWords(){
        return words;
    }

    public String getName(){
        return name;
    }

    public String getTranslation(String key){
        return words.get(key);
    }

    public boolean isCorrect(String word){
        return correctWords.indexOf(word) != -1;
    }

    public void markWordCorrect(String word){
        correctWords.add(word);
    }

    public int getPercent(){
        int all = words.size();
        int correct = correctWords.size();
        return (all == 0) ? 0 : 100*(correct/all);
    }

    public void mixWords(){

    }

    public void show(){
        final String TAG = "WPLogs";
        Log.d(TAG, " SET : NAME = " + name);
        for(String word : words.keySet()){
            Log.d(TAG, " WORD : " + word + ", TRANSLATION : " + words.get(word));
        }
    }

}
