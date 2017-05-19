package com.example.eva.wordplay.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;


public class DataMethod {

    private static final String SET_TABLE = "Sets";
    private static final String WORD_TABLE = "Words";
    private static final String WORDS_TO_SETS_TABLE = "WordsToSets";

    private SQLiteDatabase database = DataHelper.getWritableDataBase();
    private static DataMethod instance = new DataMethod();

    DataMethod(){}

    public synchronized static DataMethod getInstance() {
        return instance;
    }

    public void insertSet(final String name){
        database.execSQL("INSERT INTO " + SET_TABLE
                + "(name) VALUES('" + name + "')");
    }

    public void insertWord(final String word, final String translation, final boolean correct){
        String values = "'" + word + "','" + translation + "'," + Integer.toString(correct ? 1 : 0);
        database.execSQL("INSERT INTO " + WORD_TABLE
                + " (word, translation, isCorrect) VALUES( " + values + " )");
        //TODO сделать два метода - один про создание нового слова, второй - для добавления свзяи
    }

    public void makeWordCorrect(final String setName, final String word){
        database.execSQL("UPDATE " + WORDS_TO_SETS_TABLE + " SET isCorrect = 1 WHERE word = '"
                + word + "' AND setName = '"+setName+"';");
    }

    public ArrayList<Word> getAllWords(){
        Cursor cursor = database.rawQuery("Select * FROM Words;", null);
        if(cursor == null) {
            return null;
        }

        ArrayList<Word> tmp = new ArrayList<>();
        Log.d("WPLogs","Let's see all words");
        try {
            while (cursor.moveToNext()) {
                Log.d("WPLogs", "Some word was " + cursor.getString(cursor.getColumnIndex("word")));
                String word = cursor.getString(cursor.getColumnIndex("word"));
                String translation = cursor.getString(cursor.getColumnIndex("translation"));
                tmp.add(new Word(word, translation));
            }
        } finally {
            cursor.close();
        }
        return tmp;


    }

    public WordSet getSetInfo(final String name) {
        Cursor cursor = database.rawQuery("SELECT * FROM  WordsToSets WTS LEFT OUTER JOIN Words W" +
                " ON WTS.wordId = W.id WHERE WTS.setName = '" + name + "';", null);
        if(cursor == null){
            return null;
        }

        WordSet tmp = new WordSet();
        tmp.setName(name);
        String word, translation;
        int correct = 0;
        try {
            while (cursor.moveToNext()) {
                word = cursor.getString(cursor.getColumnIndex("word"));
                translation = cursor.getString(cursor.getColumnIndex("translation"));
                tmp.addWord(word, translation);
                correct = cursor.getInt(cursor.getColumnIndex("isCorrect"));
                if (correct != 0) {
                    tmp.markWordCorrect(word);
                }
            }
        } finally {
            cursor.close();
        }
        return tmp;
    }

    public ArrayList<WordSet> getLastSavedSets() {
        Cursor cursor = database.rawQuery("SELECT name FROM " + SET_TABLE + ";", null);
        if(cursor == null){
            return null;
        }

        ArrayList<WordSet> tmp = new ArrayList<>();
        String name;
        try {
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex("name"));
                tmp.add(getSetInfo(name));
            }
        } finally {
            cursor.close();
        }
        return tmp;
    }

    public void deleteSetFromDb(String name){
        database.execSQL("DELETE FROM " + SET_TABLE
                + " WHERE name = '" + name + "';");
    }



}
