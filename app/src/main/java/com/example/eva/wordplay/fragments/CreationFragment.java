package com.example.eva.wordplay.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eva.wordplay.R;
import com.example.eva.wordplay.data.DataHelper;
import com.example.eva.wordplay.data.WordSet;

import java.util.ArrayList;

public class CreationFragment extends Fragment implements View.OnClickListener, DataHelper.ResultListener{

    private Button btnAdd, btnCreate;
    private EditText wordView, transaltionView, nameView;
    private View view;
    private WordSet wordSet = new WordSet();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.creation_fragment, null);

        btnAdd = (Button) view.findViewById(R.id.btnAddWord);
        btnCreate = (Button) view.findViewById(R.id.createDeck);

        wordView = (EditText) view.findViewById(R.id.word);
        transaltionView = (EditText) view.findViewById(R.id.translation);
        nameView = (EditText) view.findViewById(R.id.deckName);

        btnAdd.setOnClickListener(this);
        btnCreate.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddWord:
                wordSet.addWord(wordView.getText().toString(), transaltionView.getText().toString());
                break;
            case R.id.createDeck:
                wordSet.setName(nameView.getText().toString());
                int mRequestId = DataHelper.getInstance(getActivity()).add(getActivity(),wordSet, this);
                break;
        }
    }

    @Override
    public void onStringResult(boolean success, String result) {
        Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSetResult(boolean success, WordSet result) {

    }

    @Override
    public void onArraySetResult(boolean success, ArrayList<WordSet> result) {

    }
}
