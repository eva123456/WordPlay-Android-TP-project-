package com.example.eva.wordplay.data;

import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;


class DataProcessor {

    static void addNewWord(final Word word){
        DataMethod.getInstance().addNewWord(word);
    }

    static void insertSet(final WordSet newSet){
        String setName = newSet.getName();
        DataMethod.getInstance().insertSet(setName);
        HashMap<String, String> setWords = newSet.getWords();
        for(String word : setWords.keySet()){ 
            DataMethod.getInstance().insertWord(word, setWords.get(word));

        }
    }

    static void createNewSet(final ArrayList<Word> words, final String setName){
        DataMethod.getInstance().insertSet(setName);
        for(Word word: words){
            DataMethod.getInstance().createWordInSet(word, setName);
        }
    }

    static void makeWordCorrect(String setName, String word) {
        DataMethod.getInstance().makeWordCorrect(setName, word);
    }

    static WordSet getSetInformation(final String name){
        return DataMethod.getInstance().getSetInfo(name);
    }

    static ArrayList<WordSet> getLastSavedSets(){
        return DataMethod.getInstance().getLastSavedSets();
    }

    public static ArrayList<Word> getAllWords(){
        return DataMethod.getInstance().getAllWords();
    }
}

