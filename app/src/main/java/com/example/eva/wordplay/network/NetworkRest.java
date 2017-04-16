package com.example.eva.wordplay.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class NetworkRest {
    private final static String TAG = NetworkRest.class.getSimpleName();

    private final OkHttpClient httpClient = new OkHttpClient();

    NetworkRest() {
    }

    public String processText(final String text) throws IOException {
        Request request = (new Request.Builder()).url("https://glacial-everglades-25374.herokuapp.com/test/").build();
        Response response = this.httpClient.newCall(request).execute();
        Log.d(TAG, response.body().string());
        return "asd";
    }

    public String processAllDecks() throws IOException {
        Request request = (new Request.Builder()).url("https://glacial-everglades-25374.herokuapp.com/decks_list/").build();
        Response response = this.httpClient.newCall(request).execute();
        Log.d(TAG, response.body().string());
        return response.body().string();
    }

}
