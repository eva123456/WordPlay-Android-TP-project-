package com.example.eva.wordplay.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by eva on 15.04.17.
 */

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

    public boolean isCorrect(String word){
        return correctWords.indexOf(word) != -1;
    }

    public void markWordCorrect(String word){
        correctWords.add(word);
    }

    public int getPercent(){
        /*int all = words.size();
        int correct = correctWords.size();
        return 100*(correct/all);*/
        return 70;
    }

    public void mixWords(){

    }

    public void show(){
        Log.d("WPLogs", "----------------------------------------------------------------------------------");
        Log.d("WPLogs", " SET :: NAME = " + name);
        for (String key : words.keySet()){
            Log.d("WPLogs", " WORD : " + key + ", TRANSLATION : " + words.get(key));
        }
        Log.d("WPLogs", "----------------------------------------------------------------------------------");
    }


}
