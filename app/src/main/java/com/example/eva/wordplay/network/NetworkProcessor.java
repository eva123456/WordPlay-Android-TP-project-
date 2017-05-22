package com.example.eva.wordplay.network;

import android.util.Log;

import com.example.eva.wordplay.data.WordSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

class NetworkProcessor {
    private final static String LOG_TAG = NetworkProcessor.class.getSimpleName();

    private static ArrayList<WordSet> allDeckCache;


    static ArrayList<WordSet> processAllDecks() throws IOException {
        if (allDeckCache!=null) {
            Log.i(LOG_TAG, "Text was found in cache. Returning.");
            return allDeckCache;
        } else {
            Log.i(LOG_TAG, "Text was not found in cache. Making server request.");
            final ArrayList<WordSet> result = new NetworkRest().processAllDecks();
            if (result != null && !result.isEmpty()) {
                allDeckCache = result;
            }
            return result;
        }
    }

    static WordSet processSet(WordSet targetSet) throws IOException{
        return new NetworkRest().loadSet(targetSet);
    }
}
