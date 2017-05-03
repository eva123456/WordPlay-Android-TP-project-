package com.example.eva.wordplay.data;

import java.util.ArrayList;
import java.util.HashMap;


public class DataProcessor {

    public static void insertSet(final WordSet newSet){
        String setName = newSet.getName();
        DataMethod.getInstance().insertSet(setName);
        HashMap<String, String> setWords = newSet.getWords();
        for(String word : setWords.keySet()){
            DataMethod.getInstance().insertWord(setName, word, setWords.get(word), newSet.isCorrect(word));
        }
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
}

