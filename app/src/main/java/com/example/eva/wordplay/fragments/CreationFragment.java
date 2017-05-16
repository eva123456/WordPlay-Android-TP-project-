package com.example.eva.wordplay.fragments;

import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.logging.Logger;

public class CreationFragment extends Fragment implements View.OnClickListener, DataHelper.ResultListener{

    private Button btnSave;
    private View view;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private EditText deckNameView;

    private ArrayList<PickableWord> allWords = new ArrayList<>();

    class PickableWord{
        private Word word;
        private Boolean isPicked = false;

        private PickableWord(Word word){
            this.word = word;
        }

        public String getWord(){
            return word.getWord();
        }

        public String getTranslation(){
            return word.getTranslation();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.create_edit_deck_fragment, null);
        btnSave = (Button) view.findViewById(R.id.createDeck);
        btnSave.setOnClickListener(this);
        deckNameView = (EditText) view.findViewById(R.id.deckName);

        DataHelper.getInstance(getActivity()).getAllWords(getActivity(), this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        allWords.add(new PickableWord(new Word("Word","Слово")));
        allWords.add(new PickableWord(new Word("Word 1","Слово 1")));
        allWords.add(new PickableWord(new Word("Word 2","Слово 2")));
        allWords.add(new PickableWord(new Word("Word 3","Слово 3")));
        allWords.add(new PickableWord(new Word("Word 4","Слово 4")));
        allWords.add(new PickableWord(new Word("Word 5","Слово 5")));
        allWords.add(new PickableWord(new Word("Word 6","Слово 6")));
        adapter = new WordRecyclerAdapter(allWords);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity()) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View view, int position) {
                prepareDeck(recyclerView, view, position);
            }
        });
        return view;

    }

    private void prepareDeck(RecyclerView recyclerView, View view, int position) {
        WordRecyclerAdapter.ViewHolder viewHolder = (WordRecyclerAdapter.ViewHolder) recyclerView.getChildViewHolder(view);
        if(!allWords.get(position).isPicked) {
            allWords.get(position).isPicked = true;
            viewHolder.pickWord();
        } else {
            allWords.get(position).isPicked = false;
            viewHolder.unpickWord();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createDeck:
                if(deckNameView.getText().toString()!=null){
                    Toast.makeText(getContext(),"You try to save deck with name " +
                            deckNameView.getText().toString(),Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(),"You try to save without name",Toast.LENGTH_SHORT).show();
                }
                //wordSet.setName(nameView.getText().toString());
                //int mRequestId = DataHelper.getInstance(getActivity()).add(getActivity(),wordSet, this);
                break;
        }
    }

    @Override
    public void onWordArrayResult(boolean success, ArrayList<Word> result) {
        Log.d("WPLogs", "Got all words " + result.size());
        for(Word word :result){
            Log.d("WPLogs",word.getWord() + " "+ word.getTranslation());
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
