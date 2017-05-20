package com.example.eva.wordplay.data;

import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;


public class DataProcessor {

    public static void insertSet(final WordSet newSet){
        String setName = newSet.getName();
        DataMethod.getInstance().insertSet(setName);
        HashMap<String, String> setWords = newSet.getWords();
        for(String word : setWords.keySet()){
            DataMethod.getInstance().insertWord(word, setWords.get(word), newSet.isCorrect(word));

        }
    }

    public static void createNewSet(final ArrayList<Word> words, final String setName){
        DataMethod.getInstance().insertSet(setName);
        Log.d("WPLogs","Set is successfully inserted.");
        for(Word word: words){
            Log.d("WPLogs","Inserting word "+word.getWord());
            DataMethod.getInstance().createWordInSet(word, setName);
        }
        Log.d("WPLogs","All words are successfully inserted.");
    }

    public static WordSet getSetInformation(final String name){
        WordSet result = DataMethod.getInstance().getSetInfo(name);
        return result;
    }

    public static ArrayList<WordSet> getLastSavedSets(){
        return DataMethod.getInstance().getLastSavedSets();
    }

    public static void makeWordCorrect(String setName, String word) {
        DataMethod.getInstance().makeWordCorrect(setName, word);
    }

    public static ArrayList<Word> getAllWords(){
        return DataMethod.getInstance().getAllWords();
    }
}

