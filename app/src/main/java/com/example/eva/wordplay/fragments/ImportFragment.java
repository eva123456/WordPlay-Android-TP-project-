package com.example.eva.wordplay.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eva.wordplay.R;
import com.example.eva.wordplay.data.DataHelper;
import com.example.eva.wordplay.data.Word;
import com.example.eva.wordplay.data.WordSet;
import com.example.eva.wordplay.network.NetworkHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import static com.example.eva.wordplay.R.attr.layoutManager;
import static com.example.eva.wordplay.R.id.recyclerView;
import static com.example.eva.wordplay.R.id.recyclerViewImport;

public class ImportFragment extends Fragment implements  NetworkHelper.ResultListener,
        DataHelper.ResultListener{

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.import_fragment, null);
        NetworkHelper.getInstance(getActivity()).viewAllDecksRequest(getActivity(), this);
        recyclerView = (RecyclerView) view.findViewById(recyclerViewImport);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity()) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View view, int position) {
                if(position < adapter.getItemCount()) {
                    Toast.makeText(getActivity(), "Сlick on the card " +
                            adapter.getWordSets().get(position).getName(), Toast.LENGTH_SHORT).show();
                    loadDeck(adapter.getWordSets().get(position).getName());
                }
            }
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        NetworkHelper.getInstance(getActivity()).removeListener(this);
    }

    private void loadDeck(String deckName){
        if(!deckName.isEmpty()) {
            WordSet targetSet = new WordSet();
            targetSet.setName(deckName);
            NetworkHelper.getInstance(getActivity()).loadDeck(getActivity(), targetSet, this);
        }
    }

    @Override
    public void onServerSetListResult(final ArrayList<WordSet> serverSets){
        if(serverSets==null){
            Toast.makeText(getActivity(), "No Internet connection =\\ ", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new RecyclerAdapter(serverSets);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onDeckLoadedResult(final boolean success, WordSet targetSet){
        targetSet.show();
        ArrayList<Word> words = new ArrayList<>();
        Iterator it = targetSet.getWords().entrySet().iterator();
        while(it.hasNext()){
            HashMap.Entry pair = (HashMap.Entry)it.next();
            Word wordObj = new Word((String)pair.getKey(), (String)pair.getValue());
            DataHelper.getInstance(getActivity()).addWord(getActivity(),wordObj,this);
            words.add(wordObj);
        }
        DataHelper.getInstance(getActivity()).createNewSet(getActivity(), words, targetSet.getName(), this);

    }

    @Override
    public void onWordArrayResult(boolean success, ArrayList<Word> result) {
    }

    @Override
    public void onStringResult(boolean success, String result) {
        if(success) {
            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSetResult(boolean success, WordSet result) {

    }

    @Override
    public void onArraySetResult(boolean success, ArrayList<WordSet> result) {

    }

}
