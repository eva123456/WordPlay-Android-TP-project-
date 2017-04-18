package com.example.eva.wordplay.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;


public class DataHelper {

    private int mIdCounter = 1;
    private final Map<Integer, ResultListener> mListeners = new Hashtable<>();

    private static DataHelper instance = new DataHelper();
    private static SQLiteDatabase database;

    private static final int VERSION = 1;
    private static final String DB_NAME = "WordPlay.db";
    private static final String SET_TABLE = "Sets";
    private static final String WORD_TABLE = "Words";

    Context context;

    private DataHelper() {
    }

    public synchronized static DataHelper getInstance(final Context context) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            throw new IllegalStateException();
        }
        instance.context = context;

        if(instance.database == null){
            SQLiteOpenHelper dbHelper = new SQLiteOpenHelper(context, DB_NAME, null, VERSION) {
                @Override
                public void onCreate(SQLiteDatabase db) {
                    instance.createDB(db);
                }

                @Override
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                }
            };

            instance.database = dbHelper.getWritableDatabase();
        }
        instance.initBroadcastReceiver(context);
        return instance;
    }

    public static SQLiteDatabase getWritableDataBase(){
        return database;
    }

    private void createDB(SQLiteDatabase db){
        createSetTable(db);
        createWordTable(db);
    }

    private void createSetTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + SET_TABLE + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name text" + ");");
    }

    private void createWordTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + WORD_TABLE + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "setName text,"
                + "word text,"
                + "translation text,"
                + "isCorrect integer,"
                + "FOREIGN KEY(setName) REFERENCES " + SET_TABLE + "(name)"
                + ");");
    }

    private void initBroadcastReceiver(Context context) {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(DataService.ACTION_SUCCESS);
        filter.addAction(DataService.ACTION_FAIL);

        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                final int requestId = intent.getIntExtra(DataService.EXTRA_REQUEST_ID, -1);
                final ResultListener listener = mListeners.remove(requestId);

                if (listener != null) {
                    if(intent.getStringExtra(DataService.EXTRA_RESULT_TYPE)
                            .equals(DataService.EXTRA_INSERT_RESULT)){
                        final boolean success = intent.getAction().equals(DataService.ACTION_SUCCESS);
                        final String res = success ? "OK" : "FAIL";
                        listener.onStringResult(success, res);
                    }

                    if(intent.getStringExtra(DataService.EXTRA_RESULT_TYPE)
                            .equals(DataService.EXTRA_ALL_SETS_RESULT)){
                        final boolean success = intent.getAction().equals(DataService.ACTION_SUCCESS);
                        final ArrayList<WordSet> result = (ArrayList<WordSet>)
                                intent.getSerializableExtra(DataService.EXTRA_ALL_SETS_RESULT);
                        listener.onArraySetResult(success, result);
                    }

                    if(intent.getStringExtra(DataService.EXTRA_RESULT_TYPE)
                            .equals(DataService.EXTRA_SET_INFO_RESULT)){
                        final boolean success = intent.getAction().equals(DataService.ACTION_SUCCESS);
                        final WordSet result = (WordSet) intent.getSerializableExtra(DataService.EXTRA_SET_INFO_RESULT);
                        listener.onSetResult(success, result);
                    }
                }
            }
        }, filter);
    }

    public int add(Context context, WordSet newSet, final ResultListener listener){
        mListeners.put(mIdCounter, listener);
        Intent intent = new Intent(context, DataService.class);
        intent.setAction(DataService.ACTION_INSERT_SET);
        intent.putExtra(DataService.EXTRA_SET_OBJECT, newSet);
        intent.putExtra(DataService.EXTRA_REQUEST_ID, mIdCounter);
        context.startService(intent);
        return mIdCounter++;
    }

    public int getLastSavedSets(Context context, final ResultListener listener){
        mListeners.put(mIdCounter, listener);
        Intent intent = new Intent(context, DataService.class);
        intent.setAction(DataService.ACTION_GET_ALL_SETS);
        intent.putExtra(DataService.EXTRA_REQUEST_ID, mIdCounter);
        context.startService(intent);
        return mIdCounter++;
    }

    public int get(Context context, final String setName, final ResultListener listener){
        mListeners.put(mIdCounter, listener);
        Intent intent = new Intent(context, DataService.class);
        intent.setAction(DataService.ACTION_GET_SET_INFO);
        intent.putExtra(DataService.EXTRA_SET_NAME, setName);
        intent.putExtra(DataService.EXTRA_REQUEST_ID, mIdCounter);
        context.startService(intent);
        return mIdCounter++;
    }

    public interface ResultListener {
        void onStringResult(final boolean success, final String result);
        void onSetResult(final boolean success, final WordSet result);
        void onArraySetResult(final boolean success, final ArrayList<WordSet> result);
    }
    
}
