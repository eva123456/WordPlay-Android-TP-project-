package com.example.eva.wordplay.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.eva.wordplay.data.WordSet;

import java.io.IOException;
import java.util.ArrayList;

public class NetworkIntentService extends IntentService{

    private final static String TAG = NetworkIntentService.class.getSimpleName();

    public static final String EXTRA_RESULT_TYPE = "extra.RESULT_TYPE";

    public final static String ACTION_VIEW_DECKS = "action.VIEW_ALL_DECKS";
    public final static String EXTRA_DECKS_RESULT = "extra.ALL_DECKS_RESULT";
    public static final String EXTRA_TYPE_ALL_DECKS_ = "extra.TYPE_LIST_DECKS_LOAD_RESULT";

    public final static String ACTION_LOAD_DECK = "action.LOAD_DECK_CONTENT";
    public final static String EXTRA_WORD_SET = "extra.LOAD_DECK_CONTENT_RESULT";
    public final static String EXTRA_TYPE_LOAD_DECK = "extra.TYPE_LOAD_DECK";


    public final static String ACTION_WEB = "action.WEB";
    public final static String EXTRA_WEB_TEXT = "extra.WEB_TEXT";
    public final static String EXTRA_REQUEST_ID = "extra.REQUEST_ID";

    public final static String ACTION_WEB_RESULT_SUCCESS = "action.ACTION_WEB_RESULT_SUCCESS";
    public final static String ACTION_WEB_RESULT_ERROR = "action.ACTION_WEB_RESULT_ERROR";
    public final static String EXTRA_WEB_RESULT = "extra.WEB_RESULT";

    public NetworkIntentService() {
        super("WebIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_WEB.equals(action)) {
                final String text = intent.getStringExtra(EXTRA_WEB_TEXT);
                final int requestId = intent.getIntExtra(EXTRA_REQUEST_ID, -1);
                handleActionWeb(text, requestId);
            } else if(ACTION_VIEW_DECKS.equals(action)){
                final int requestId = intent.getIntExtra(EXTRA_REQUEST_ID, -1);
                handleActionViewAllDecks(requestId);
            } else if(ACTION_LOAD_DECK.equals(action)){
                final int requestId = intent.getIntExtra(EXTRA_REQUEST_ID, -1);
                WordSet targetSet =(WordSet) intent.getSerializableExtra(EXTRA_WORD_SET);
                handleActionLoadDeck(targetSet, requestId);
            }

        }
    }

    private void handleActionLoadDeck(WordSet targetSet, int requestId){
        WordSet result = targetSet;
        Log.d(TAG, " Service get request for loading deck with name ");
        boolean success = true;
        try{
            NetworkProcessor.processSet(targetSet);
        }catch(IOException ex) {
            result = null;
            success = false;
        }
        final Intent intent = new Intent(success ? ACTION_WEB_RESULT_SUCCESS : ACTION_WEB_RESULT_ERROR);
        intent.putExtra(EXTRA_RESULT_TYPE, EXTRA_TYPE_LOAD_DECK);
        intent.putExtra(EXTRA_WORD_SET, targetSet);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void handleActionViewAllDecks(final int requestId){
        ArrayList<WordSet> result;
        Log.d(TAG, " Service get request for looking deck list");
        boolean success = true;
        try{
            result = NetworkProcessor.processAllDecks();
            if (result == null) {
                success = false;
            }
        } catch(IOException ex) {
            result = null;
            success = false;
        }

        final Intent intent = new Intent(success ? ACTION_WEB_RESULT_SUCCESS : ACTION_WEB_RESULT_ERROR);
        intent.putExtra(EXTRA_RESULT_TYPE, EXTRA_TYPE_ALL_DECKS_);
        intent.putExtra(EXTRA_DECKS_RESULT, result);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void handleActionWeb(final String text, final int requestId) {
        String result;

        Log.d(TAG, " Service get request " + text);
        boolean success = true;
        try {
            result = NetworkProcessor.processText(text);
            if (TextUtils.isEmpty(result)) {
                result = "result is null";
                success = false;
            }
        } catch (IOException ex) {
            result = ex.getMessage();
            success = false;
        }

        final Intent intent = new Intent(success ? ACTION_WEB_RESULT_SUCCESS : ACTION_WEB_RESULT_ERROR);
        intent.putExtra(EXTRA_WEB_RESULT, result);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
