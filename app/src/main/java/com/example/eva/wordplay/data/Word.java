package com.example.eva.wordplay.data;

import java.io.Serializable;

public class Word implements Serializable{
    private Integer id;

    private String word;

    private String translation;

    public Word(String word, String translation, Integer id) {
        this.id = id;
        this.word = word;
        this.translation = translation;
    }

    public Word(String word, String translation){
        this.word = word;
        this.translation = translation;
    }

    public Integer getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
