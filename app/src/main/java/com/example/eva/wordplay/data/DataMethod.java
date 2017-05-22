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
        final Cursor cursor = database.rawQuery("SELECT * FROM " + SET_TABLE + " WHERE name = '"
                + name + "';",  null);
        if(cursor.getCount()>0){
            cursor.close();
            throw new RuntimeException("Set with that name already exists");
        } else {
            cursor.close();
        }
        database.execSQL("INSERT INTO " + SET_TABLE
                + "(name) VALUES('" + name + "')");
    }

    public void addNewWord(final Word word){
        Log.d("WPLogs","Try to add new word in DB");
        final Cursor cursor = database.rawQuery("SELECT * FROM " + WORD_TABLE + " WHERE word = '"
                + word.getWord() + "';",  null);
        if(cursor.getCount()>0){
            cursor.close();
            throw new RuntimeException("This word already exists");
        } else {
            cursor.close();
        }
        final String values = "'" + word.getWord() + "','" + word.getTranslation() + "'";
        database.execSQL("INSERT INTO " + WORD_TABLE
                + " (word, translation) VALUES( " + values + " )");
    }

    public void insertWord(final String word, final String translation){
        String values = "'" + word + "','" + translation + "'";
        database.execSQL("INSERT INTO " + WORD_TABLE
                + " (word, translation) VALUES( " + values + " )");
    }

    public void createWordInSet(final Word word, final String setName){
        if(word.getId()==null) {
            final Cursor cursor = database.rawQuery("SELECT * FROM " + WORD_TABLE + " WHERE word = '"
                    + word.getWord() + "';", null);
            try {
                while (cursor.moveToNext()) {
                    word.setId(cursor.getInt(cursor.getColumnIndex("id")));
                }
            } finally {
                cursor.close();
            }
        }
        final String values = "'" + setName + "'," + word.getId() + ",'"+word.getWord() + "', 0";
        database.execSQL(" INSERT INTO " + WORDS_TO_SETS_TABLE + " (setName, wordId, word, isCorrect) "
                + "VALUES ( " + values + " );");
    }

    public void makeWordCorrect(final String setName, final String word){
        //TODO - следует использовать id слова и вообще всю логику работы со словами вести через
        //TODO классы Word и WordSet
        //FIXME - если пользователь создаст два перевода одного слова и добавит их в один сет, то
        //FIXME все они будут отмечаться как корректные по одному запросу
        database.execSQL("UPDATE " + WORDS_TO_SETS_TABLE + " SET isCorrect = 1 WHERE word = '"
                + word + "' AND setName = '"+setName+"';");
    }

    public ArrayList<Word> getAllWords(){
        Cursor cursor = database.rawQuery("Select * FROM Words;", null);
        if(cursor == null) {
            return null;
        }

        ArrayList<Word> tmp = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                String word = cursor.getString(cursor.getColumnIndex("word"));
                String translation = cursor.getString(cursor.getColumnIndex("translation"));
                Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                tmp.add(new Word(word, translation, id));
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
        Log.d("WPLogs", "Processing set with name " + name);
        try {
            while (cursor.moveToNext()) {
                String word, translation;
                int correct;
                word = cursor.getString(cursor.getColumnIndex("word"));
                translation = cursor.getString(cursor.getColumnIndex("translation"));
                tmp.addWord(word, translation);
                correct = cursor.getInt(cursor.getColumnIndex("isCorrect"));
                Log.d("WPLogs", "Current word is " + word + " and it is " + correct);
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
