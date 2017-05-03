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

    public ArrayList<String> getCorrectWords(){
        return correctWords;
    }

    public int getPercent(){
        double all = words.size();
        double correct = correctWords.size();
        return (all == 0) ? 0 : (int) (100 * correct/all);
    }

    public HashMap<String, String> getWordsForCheck(){
        HashMap<String, String> result = new HashMap<>();
        for(String key : words.keySet()){
            if(!correctWords.contains(key)){
                result.put(key, words.get(key));
            }
        }
        return result;
    }

    public void show(){
        final String TAG = "WPLogs";
        Log.d(TAG, " SET : NAME = " + name);
        for(String word : words.keySet()){
            Log.d(TAG, " WORD : " + word + ", TRANSLATION : " + words.get(word));
        }
    }

}
