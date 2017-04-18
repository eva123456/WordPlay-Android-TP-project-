package com.example.eva.wordplay.data;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by eva on 17.04.17.
 */

public class DataProcessor {

    private final static String LOG_TAG = DataProcessor.class.getSimpleName();
    private final static Map<String, String> mCache = new Hashtable<>();

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

}

