package com.example.eva.wordplay.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Looper;
import android.support.annotation.IntegerRes;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import com.example.eva.wordplay.network.NetworkHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;


public class DataHelper {

    private int mIdCounter = 1;
    private final Map<Integer, ResultListener> mListeners = new Hashtable<>();

    private static DataHelper instance = new DataHelper();
    private static SQLiteDatabase database;

    private static final int VERSION = 2;
    private static final String DB_NAME = "WordPlay.db";
    private static final String SET_TABLE = "Sets";
    private static final String WORD_TABLE = "Words";
    private static final String WORDS_TO_SETS_TABLE = "WordsToSets";

    Context context;

    private DataHelper() {
    }

    public synchronized static DataHelper getInstance(final Context context){
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
                    Log.d("WPLogs","Try to begin update " + oldVersion + " " + newVersion);
                    if(oldVersion==1&&newVersion==2){
                        migrateForm1To2(db);
                    }
                }
            };

            instance.database = dbHelper.getWritableDatabase();

            /*database.execSQL("DELETE FROM " + WORD_TABLE
                    +" WHERE word='word 5';");
            String values = "'word 5', 'слово 5', 0";
            database.execSQL("INSERT INTO " + WORD_TABLE
                    + " (word, translation, isCorrect) VALUES( " + values + " )");*/
            //Внешние ключи не работают в принципе, пам-пам
            //пока их не включишь, разумеется.

            /*database.execSQL("INSERT INTO " + SET_TABLE
                    + "(name) VALUES('deck2')");*/

            Log.d("WPLogs","--------Table content-------------");
            Log.d("WPLogs", "Sets");
            Cursor cursor = database.rawQuery("SELECT * FROM Sets;", null);
            try {
                while (cursor.moveToNext()) {
                    String name;
                    //String setName;
                    name = cursor.getString(cursor.getColumnIndex("name"));
                    //setName = cursor.getString(cursor.getColumnIndex("setName"));*/
                    //for(String tmp:cursor.getColumnNames())
                    Log.d("WPLogs",name);
                }
            } finally {
                cursor.close();
            }

            Log.d("WPLogs", "Words");

            cursor = database.rawQuery("SELECT * FROM Words;", null);
            try {
                while (cursor.moveToNext()) {
                    String word;
                    String translation;
                    word = cursor.getString(cursor.getColumnIndex("word"));
                    translation = cursor.getString(cursor.getColumnIndex("translation"));
                    if(word!=null) {
                        Log.d("WPLogs", word + " "+ translation);
                    } else {
                        Log.d("WPLogs", "Word was null, WTF");
                    }
                }
            } finally {
                cursor.close();
            }

            Log.d("WPLogs", "Now database version is " + database.getVersion());
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
        createWordsTOSetsTable(db);
    }

    private void createSetTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + SET_TABLE + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name text" + ");");
    }

    private void createWordTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + WORD_TABLE + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "word text,"
                + "translation text"
                + ");");
    }

    private void createWordsTOSetsTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + WORDS_TO_SETS_TABLE + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "setName text, "
                + "wordId integer, "
                + "word text"
                + "isCorrect integer" + ");");
    }

    private static void migrateForm1To2(SQLiteDatabase db){
        db.beginTransaction();
        try{
            db.setForeignKeyConstraintsEnabled(false);
            db.execSQL("CREATE TABLE " + WORDS_TO_SETS_TABLE + " ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "setName text, "
                    + "word text, "
                    + "wordId integer, "
                    + "isCorrect integer" + ");");
            Log.d("WPLogs","Новая таблица создана, но мы не спешим коммитить транзакцию.");
            Log.d("WPLogs","Заполняем новую таблицу");
            Cursor cursor = db.rawQuery("SELECT * FROM " + WORD_TABLE+";", null);
            try {
                while (cursor.moveToNext()) {
                    String setName;
                    Integer id;
                    Integer isCorrect;
                    String word;
                    setName = cursor.getString(cursor.getColumnIndex("setName"));
                    id = cursor.getInt(cursor.getColumnIndex("id"));
                    isCorrect = cursor.getInt(cursor.getColumnIndex("isCorrect"));
                    word = cursor.getString(cursor.getColumnIndex("word"));
                    if(setName!=null) {
                        Log.d("WPLogs", setName + " " + word);
                        String values = "'"+setName+"', '"+word +"', " + id +", "+ isCorrect;
                        db.execSQL("INSERT INTO " + WORDS_TO_SETS_TABLE + " (setName, word, wordId, isCorrect)"
                                + "VALUES (" + values + ");");
                    }
                }
            } finally {
                cursor.close();
            }
            Log.d("WPLogs","Таблица заполнилась. Теперь меняем таблицу со словами");

            db.execSQL("CREATE temporary TABLE tmp ("
                    + "id INTEGER,"
                    + "word text,"
                    + "translation text"
                    + ");");
            db.execSQL("INSERT INTO tmp SELECT id, word, translation FROM "
                    + WORD_TABLE + ";");

            db.execSQL("DROP TABLE " + WORD_TABLE + ";");
            db.execSQL("CREATE TABLE Words ("
                    + "id integer primary key autoincrement,"
                    + "word text,"
                    + "translation text"
                    + ");");
            Log.d("WPLogs","Изменили таблицу со словами. Проверяем, что она изменилась");
            //db.execSQL("INSERT INTO Words (id, word, translation) "
            //        + "SELECT id, word, translation FROM tmp"); - не работает
            //А может и работает. Но отображается все равно криво
            cursor = db.rawQuery("SELECT * FROM tmp;", null);
            try {
                while (cursor.moveToNext()) {
                    Integer id;
                    Integer isCorrect;
                    String word;
                    String translation;
                    word = cursor.getString(cursor.getColumnIndex("word"));
                    translation = cursor.getString(cursor.getColumnIndex("translation"));
                    id = cursor.getInt(cursor.getColumnIndex("id"));
                    Log.d("WPLogs", id + " " + word + " " + translation);
                    db.execSQL("INSERT INTO Words (id, word, translation) VALUES ("
                            + id+", '"+word+"','"+ translation+"');");
                }
            } finally {
                cursor.close();
            }
            db.execSQL("DROP TABLE tmp;");
            db.setTransactionSuccessful();
            //А внешних ключей у нас не будет. Они и так не работали раньше
        } finally {
            Log.d("WPLogs","Миграция успешно примененаю");
            db.endTransaction();
        }
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
                Log.d("WPLogs","Get intent with request id = " + requestId);
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
                            .equals(DataService.EXTRA_ALL_WORDS_RESULT)){
                        final boolean success = intent.getAction().equals(DataService.ACTION_SUCCESS);
                        final ArrayList<Word> result = (ArrayList<Word>)
                                intent.getSerializableExtra(DataService.EXTRA_ALL_WORDS_RESULT);
                        listener.onWordArrayResult(success, result);
                    }

                    if(intent.getStringExtra(DataService.EXTRA_RESULT_TYPE)
                            .equals(DataService.EXTRA_SET_INFO_RESULT)){
                        final boolean success = intent.getAction().equals(DataService.ACTION_SUCCESS);
                        final WordSet result = (WordSet) intent.getSerializableExtra(DataService.EXTRA_SET_INFO_RESULT);
                        listener.onSetResult(success, result);
                    }

                    if(intent.getStringExtra(DataService.EXTRA_RESULT_TYPE)
                            .equals(DataService.EXTRA_UPDATE_WORD_RESULT)){
                        final boolean success = intent.getAction().equals(DataService.ACTION_SUCCESS);
                        final String res = success ? "OK" : "FAIL";
                        listener.onStringResult(success, res);
                    }

                    if(intent.getStringExtra(DataService.EXTRA_RESULT_TYPE)
                            .equals(DataService.EXTRA_CREATE_SET_RESULT)){
                        final boolean success = intent.getAction().equals(DataService.ACTION_SUCCESS);
                        final String res = success ? "Set created" : "FAIL";
                        listener.onStringResult(success, res);
                    }

                    if(intent.getStringExtra(DataService.EXTRA_RESULT_TYPE)
                            .equals(DataService.EXTRA_ADD_WORD_RESULT)){
                        Log.d("WPLogs","Get response for add word action");
                        final boolean success = intent.getAction().equals(DataService.ACTION_SUCCESS);
                        final String res = success ? "Word added" : "FAIL";
                        listener.onStringResult(success, res);
                    }
                }
            }
        }, filter);
    }

    public int addWord(Context context, Word word, final ResultListener listener){
        mListeners.put(mIdCounter, listener);
        Intent intent = new Intent(context, DataService.class);
        intent.setAction(DataService.ACTION_ADD_WORD);
        intent.putExtra(DataService.EXTRA_REQUEST_ID, mIdCounter);
        intent.putExtra(DataService.EXTRA_WORD_OBJECT, word);
        Log.d("WPLogs","Sending intent for new word creation");
        context.startService(intent);
        return mIdCounter++;
    }

    public int updateWord(Context context, String setName, String word, final ResultListener listener){
        mListeners.put(mIdCounter, listener);
        Intent intent = new Intent(context, DataService.class);
        intent.setAction(DataService.ACTION_UPDATE_WORD);
        intent.putExtra(DataService.EXTRA_SET_NAME, setName);
        intent.putExtra(DataService.EXTRA_WORD, word);
        intent.putExtra(DataService.EXTRA_REQUEST_ID, mIdCounter);
        context.startService(intent);
        return mIdCounter++;
    }

    //?? Возможно, теперь этот метод не нужен
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

    public int getAllWords(Context context, final ResultListener listener){
        mListeners.put(mIdCounter, listener);
        Intent intent = new Intent(context, DataService.class);
        intent.setAction(DataService.ACTION_GET_ALL_WORDS);
        intent.putExtra(DataService.EXTRA_REQUEST_ID, mIdCounter);
        context.startService(intent);
        return mIdCounter++;
    }

    public int createNewSet(Context context, final ArrayList<Word> words, final String setName,
                            final ResultListener listener){
        mListeners.put(mIdCounter, listener);
        Log.d("WPLogs","Creation set request id is " + mIdCounter);
        Intent intent = new Intent(context, DataService.class);
        intent.setAction(DataService.ACTION_CREATE_NEW_SET);
        intent.putExtra(DataService.EXTRA_REQUEST_ID, mIdCounter);
        intent.putExtra(DataService.EXTRA_WORDS_LIST, words);
        intent.putExtra(DataService.EXTRA_SET_NAME, setName);
        context.startService(intent);
        return  mIdCounter++;
    }

    public interface ResultListener {
        void onStringResult(final boolean success, final String result);
        void onSetResult(final boolean success, final WordSet result);
        void onArraySetResult(final boolean success, final ArrayList<WordSet> result);
        void onWordArrayResult(final boolean success, final ArrayList<Word> result);
    }
    
}
