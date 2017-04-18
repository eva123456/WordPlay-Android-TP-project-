package com.example.eva.wordplay.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;


public class DataMethod {

    private static final String SET_TABLE = "Sets";
    private static final String WORD_TABLE = "Words";

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

    public void insertWord(final String setName, final String word, final String translation, final boolean correct){
        String values = "'" + setName + "','" + word + "','" + translation + "'," + Integer.toString(correct ? 1 : 0);
        database.execSQL("INSERT INTO " + WORD_TABLE
                + " (setName, word, translation, isCorrect) VALUES( " + values + " )");
    }

    public WordSet getSetInfo(final String name) {

        Cursor cursor = database.rawQuery("SELECT * FROM Words WHERE setName = '" + name + "';", null);
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
