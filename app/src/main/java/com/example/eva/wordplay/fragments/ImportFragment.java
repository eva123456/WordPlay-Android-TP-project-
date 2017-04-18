package com.example.eva.wordplay.fragments;

import android.app.Fragment;
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
import com.example.eva.wordplay.data.WordSet;
import com.example.eva.wordplay.network.NetworkHelper;

import java.util.ArrayList;

import static com.example.eva.wordplay.R.attr.layoutManager;
import static com.example.eva.wordplay.R.id.recyclerView;
import static com.example.eva.wordplay.R.id.recyclerViewImport;

/**
 * Created by eva on 15.04.17.
 */

public class ImportFragment extends Fragment implements  NetworkHelper.ResultListener{

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.import_fragment, null);
        Log.d("WPLogs", "BaseFragment:onCreateView");

        //int requestId = DataHelper.getInstance(getActivity()).getLastSavedSets(getActivity(), this);
        int req = NetworkHelper.getInstance(getActivity()).viewAllDecksRequest(getActivity(), this);

        //WordSet targetSet = new WordSet();
        //targetSet.setName("Animals");
        //getActivity();
        //NetworkHelper.getInstance(getActivity()).loadDeck(getActivity(), targetSet, this);


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

    private void loadDeck(String deckName){
        if(!deckName.isEmpty()) {
            WordSet targetSet = new WordSet();
            targetSet.setName(deckName);
            //getActivity();
            NetworkHelper.getInstance(getActivity()).loadDeck(getActivity(), targetSet, this);
        }
    }

    @Override
    public void onResult(final boolean success, final String result){

    }

    @Override
    public void onServerSetListResult(final ArrayList<WordSet> serverSets){
        adapter = new RecyclerAdapter(serverSets);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDeckLoadedResult(final boolean success, WordSet targetSet){
        targetSet.show();
    }


}
