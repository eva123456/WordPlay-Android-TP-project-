package com.example.eva.wordplay.network;

import android.util.Log;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class NetworkProcessor {
    private final static String LOG_TAG = NetworkProcessor.class.getSimpleName();

    private final static Map<String, String> mCache = new Hashtable<>();

    private static String allDeckCache;

    public static String processText(final String text) throws IOException {
        if (mCache.containsKey(text)) {
            Log.i(LOG_TAG, "Text was found in cache. Returning.");
            return mCache.get(text);
        } else {
            Log.i(LOG_TAG, "Text was not found in cache. Making server request.");
            final String result = new NetworkRest().processText(text);
            if (result != null && !result.isEmpty()) {
                mCache.put(text, result);
            }
            return result;
        }
    }

    public static String processAllDecks() throws IOException {
        if (allDeckCache!=null) {
            Log.i(LOG_TAG, "Text was found in cache. Returning.");
            return allDeckCache;
        } else {
            Log.i(LOG_TAG, "Text was not found in cache. Making server request.");
            final String result = new NetworkRest().processAllDecks();
            if (result != null && !result.isEmpty()) {
                allDeckCache = result;
            }
            return result;
        }
    }
}
