package com.example.eva.wordplay.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eva.wordplay.R;
import com.example.eva.wordplay.data.DataHelper;
import com.example.eva.wordplay.data.Word;
import com.example.eva.wordplay.data.WordSet;

import java.util.ArrayList;

public class AddWordFragment extends Fragment implements View.OnClickListener, DataHelper.ResultListener{

    private EditText wordEdit, translationEdit;
    private Button saveWord;
    private wordCreateListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.add_word_fragment, null);
        saveWord = (Button) view.findViewById(R.id.addWord);
        saveWord.setOnClickListener(this);
        wordEdit = (EditText) view.findViewById(R.id.wordInput);
        translationEdit = (EditText) view.findViewById(R.id.translationInput);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addWord:
                final String word = wordEdit.getText().toString();
                final String translation = translationEdit.getText().toString();
                if(word.equals("")||translation.equals("")){
                    Toast.makeText(getContext(),"One of word or translation was empty",
                            Toast.LENGTH_SHORT).show();
                } else {
                    DataHelper.getInstance(getActivity()).addWord(getActivity(),new Word(word, translation),this);
                }
        }
    }

    public interface wordCreateListener{
        void onWordCreated(Word w);
    }

    public void registerWordCreateListener(wordCreateListener listener){
        this.listener = listener;
    }

    @Override
    public void onStringResult(boolean success, String result) {
        if(success) {
            Toast.makeText(getActivity(), "Word successfully added.", Toast.LENGTH_SHORT).show();
            Word word = new Word(wordEdit.getText().toString(), translationEdit.getText().toString(), null);
            listener.onWordCreated(word);
        } else {
            Toast.makeText(getActivity(), "Probably, this word already exists in DB." +
                    " Try another.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSetResult(boolean success, WordSet result) {

    }

    @Override
    public void onArraySetResult(boolean success, ArrayList<WordSet> result) {

    }

    @Override
    public void onWordArrayResult(boolean success, ArrayList<Word> result) {

    }
}
