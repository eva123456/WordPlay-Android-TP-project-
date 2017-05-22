package com.example.eva.wordplay.data;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Executor;


public class DataService extends IntentService {

    public final static String EXTRA_REQUEST_ID = "extra.REQUEST_ID";
    public static final String EXTRA_RESULT_TYPE = "extra.RESULT_TYPE";

    public final static String ACTION_INSERT_SET = "action.INSERT_SET";
    public final static String ACTION_GET_SET_INFO = "action.GET_SET_INFO";
    public final static String ACTION_GET_ALL_SETS = "action.GET_ALL_SETS";
    public final static String ACTION_UPDATE_WORD = "action.UPDATE_WORD";
    public final static String ACTION_GET_ALL_WORDS = "action.GET_ALL_WORDS";
    public final static String ACTION_CREATE_NEW_SET = "action.CREATE_NEW_SET";
    public final static String ACTION_ADD_WORD = "action.ADD_NEW_WORD";

    public final static String EXTRA_SET_OBJECT = "extra.SET_OBJECT";
    public final static String EXTRA_SET_NAME = "extra.SET_NAME";
    public final static String EXTRA_WORD = "extra.WORD";
    public final static String EXTRA_WORDS_LIST = "extra.WORDS_LIST";
    public final static String EXTRA_WORD_OBJECT = "extra.WORD_OBJECT";

    public final static String EXTRA_ADD_WORD_RESULT = "extra.ADD_WORD_RESULT";
    public final static String EXTRA_CREATE_SET_RESULT = "extra.CREATE_SET_RESULT";
    public final static String EXTRA_SET_INFO_RESULT = "extra.SET_INFO_RESULT";
    public final static String EXTRA_ALL_SETS_RESULT = "extra.ALL_SETS_RESULT";
    public final static String EXTRA_INSERT_RESULT = "extra.INSERT_RESULT";
    public final static String EXTRA_UPDATE_WORD_RESULT = "extra.UPDATE_WORD_RESULT";

    public final static String EXTRA_ALL_WORDS_RESULT = "extra.ALL_WORDS_RESULT";

    public final static String ACTION_SUCCESS = "action.SUCCESS";
    public final static String ACTION_FAIL = "action.FAIL";


    public DataService() {
        super("DataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INSERT_SET.equals(action)) {
                final WordSet newSet = (WordSet) intent.getSerializableExtra(EXTRA_SET_OBJECT);
                final int requestId = intent.getIntExtra(EXTRA_REQUEST_ID, -1);
                handleInsertAction(newSet, requestId);
            }

            if(ACTION_GET_SET_INFO.equals(action)){
                final String setName = intent.getStringExtra(EXTRA_SET_NAME);
                final int requestId = intent.getIntExtra(EXTRA_REQUEST_ID, -1);
                handleSetInfoAction(setName, requestId);
            }

            if(ACTION_GET_ALL_SETS.equals(action)){
                final int requestId = intent.getIntExtra(EXTRA_REQUEST_ID, -1);
                handleAllSetsAction(requestId);
            }

            if(ACTION_UPDATE_WORD.equals(action)){
                final String setName = intent.getStringExtra(EXTRA_SET_NAME);
                final String word = intent.getStringExtra(EXTRA_WORD);
                final int requestId = intent.getIntExtra(EXTRA_REQUEST_ID, -1);
                handleUpdateWordAction(setName, word, requestId);
            }

            if(ACTION_GET_ALL_WORDS.equals(action)){
                final int requestId = intent.getIntExtra(EXTRA_REQUEST_ID, -1);
                handleGetAllWordAction(requestId);
            }

            if(ACTION_CREATE_NEW_SET.equals(action)){
                final int requestId = intent.getIntExtra(EXTRA_REQUEST_ID, -1);
                final ArrayList<Word> words = (ArrayList<Word>) intent.getSerializableExtra(EXTRA_WORDS_LIST);
                final String setName = intent.getStringExtra(EXTRA_SET_NAME);
                handleCreateNewSetAction(requestId, setName, words);
            }

            if(ACTION_ADD_WORD.equals(action)){
                final int requestId = intent.getIntExtra(EXTRA_REQUEST_ID, -1);
                final Word word = (Word)intent.getSerializableExtra(EXTRA_WORD_OBJECT);
                handleAddWordAction(requestId, word);
            }
        }
    }

    private void handleAddWordAction(final int rId, final Word word){
        boolean success = true;
        try{
            DataProcessor.addNewWord(word);
        } catch (Exception e){
            Log.d("WPLogs","While trying to save word get exception.");
            success = false;
        }
        Intent intent = new Intent(success ? ACTION_SUCCESS : ACTION_FAIL);
        intent.putExtra(EXTRA_REQUEST_ID, rId);

        intent.putExtra(EXTRA_RESULT_TYPE, EXTRA_ADD_WORD_RESULT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void handleCreateNewSetAction(final int rId, final String setName,
                                          final ArrayList<Word> words){
        boolean success = true;
        try{
            DataProcessor.createNewSet(words,setName);
        } catch (Exception e){
            Log.d("WPLogs","While trying to save set get exception.");
            success = false;
        }
        Intent intent = new Intent(success ? ACTION_SUCCESS : ACTION_FAIL);
        intent.putExtra(EXTRA_REQUEST_ID, rId);

        intent.putExtra(EXTRA_RESULT_TYPE, EXTRA_CREATE_SET_RESULT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void handleUpdateWordAction(String setName, String word, final int rId){
        boolean success = true;
        try {
            DataProcessor.makeWordCorrect(setName, word);
        } catch (Exception e){
            success = false;
        }

        Intent intent = new Intent(success ? ACTION_SUCCESS : ACTION_FAIL);
        intent.putExtra(EXTRA_REQUEST_ID, rId);
        intent.putExtra(EXTRA_RESULT_TYPE, EXTRA_UPDATE_WORD_RESULT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void handleGetAllWordAction(final int rId){
        boolean success = true;
        ArrayList<Word> result;
        try{
            result = DataMethod.getInstance().getAllWords();
        } catch (Exception e){
            success = false;
            result = null;
        }

        Intent intent = new Intent(success ? ACTION_SUCCESS : ACTION_FAIL);

        intent.putExtra(EXTRA_REQUEST_ID, rId);
        if(result != null){
            intent.putExtra(EXTRA_ALL_WORDS_RESULT, result);
        }
        intent.putExtra(EXTRA_RESULT_TYPE, EXTRA_ALL_WORDS_RESULT);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }


    private void handleAllSetsAction(final int rId) {
        boolean success = true;
        ArrayList<WordSet> result;
        try{
            result = DataMethod.getInstance().getLastSavedSets();
        } catch (Exception e){
            success = false;
            result = null;
        }

        Intent intent = new Intent(success ? ACTION_SUCCESS : ACTION_FAIL);
        intent.putExtra(EXTRA_REQUEST_ID, rId);
        if(result != null){
            intent.putExtra(EXTRA_ALL_SETS_RESULT, result);
        }
        intent.putExtra(EXTRA_RESULT_TYPE, EXTRA_ALL_SETS_RESULT);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void handleSetInfoAction(String setName, final int rId) {
        boolean success = true;
        WordSet result;
        try{
            result = DataMethod.getInstance().getSetInfo(setName);
        } catch (Exception e){
            success = false;
            result = null;
        }
        Intent intent = new Intent(success ? ACTION_SUCCESS : ACTION_FAIL);
        intent.putExtra(EXTRA_REQUEST_ID, rId);
        if(result != null){
            intent.putExtra(EXTRA_SET_INFO_RESULT, result);
        }
        intent.putExtra(EXTRA_RESULT_TYPE, EXTRA_SET_INFO_RESULT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void handleInsertAction(WordSet newSet, final int rId) {
        boolean success = true;
        try {
            DataProcessor.insertSet(newSet);
        } catch (Exception e){
            success = false;
        }

        Intent intent = new Intent(success ? ACTION_SUCCESS : ACTION_FAIL);
        intent.putExtra(EXTRA_REQUEST_ID, rId);
        intent.putExtra(EXTRA_RESULT_TYPE, EXTRA_INSERT_RESULT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
