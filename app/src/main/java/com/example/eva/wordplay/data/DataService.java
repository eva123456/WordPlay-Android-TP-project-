package com.example.eva.wordplay.data;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;


public class DataService extends IntentService {

    public final static String EXTRA_REQUEST_ID = "extra.REQUEST_ID";
    public static final String EXTRA_RESULT_TYPE = "extra.RESULT_TYPE";

    public final static String ACTION_INSERT_SET = "action.INSERT_SET";
    public final static String ACTION_GET_SET_INFO = "action.GET_SET_INFO";
    public final static String ACTION_GET_ALL_SETS = "action.GET_ALL_SETS";

    public final static String EXTRA_SET_OBJECT = "extra.SET_OBJECT";
    public final static String EXTRA_SET_NAME = "extra.SET_NAME";
    public final static String EXTRA_SET_INFO_RESULT = "extra.SET_INFO_RESULT";
    public final static String EXTRA_ALL_SETS_RESULT = "extra.ALL_SETS_RESULT";
    public final static String EXTRA_INSERT_RESULT = "extra.INSERT_RESULT";

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
        }
    }

    private void handleAllSetsAction(final int rId) {
        boolean success = true;
        ArrayList<WordSet> result = new ArrayList<>();
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
        WordSet result = new WordSet();
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
