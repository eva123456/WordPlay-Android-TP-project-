package com.example.eva.wordplay.network;

import android.util.Log;

import com.example.eva.wordplay.data.Word;
import com.example.eva.wordplay.data.WordSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class NetworkRest {
    private final static String TAG = NetworkRest.class.getSimpleName();

    private final OkHttpClient httpClient = new OkHttpClient();

    private final static String BACK_URL = "https://glacial-everglades-25374.herokuapp.com/";


    NetworkRest() {
    }


    ArrayList<WordSet> processAllDecks() throws IOException {
        Request request = (new Request.Builder()).url(BACK_URL+"decks_list/").build();
        Response response = this.httpClient.newCall(request).execute();
        final String result = response.body().string();
        Log.d(TAG, result);
        final ArrayList<WordSet> serverSets;
        Gson gson = new Gson();
        serverSets = gson.fromJson(result,new TypeToken<List<WordSet>>(){}.getType());
        for(WordSet set : serverSets) {
            Log.d(TAG, set.getName());
        }
        return serverSets;
    }

    WordSet loadSet(WordSet targetSet) throws IOException{
        Request request = (new Request.Builder()).url(BACK_URL+"decks_content?deck_name="+targetSet.getName()).build();
        Response response = this.httpClient.newCall(request).execute();
        final String result = response.body().string();
        Log.d(TAG, result);
        Gson gson = new Gson();
        ArrayList<Word> words = gson.fromJson(result,new TypeToken<List<Word>>(){}.getType());
        for(Word word : words) {
            Log.d(TAG, word.getWord());
            targetSet.addWord(word.getWord(), word.getTranslation());
        }
        return targetSet;
    }

}
