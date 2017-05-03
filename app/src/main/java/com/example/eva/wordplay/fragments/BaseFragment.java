package com.example.eva.wordplay.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eva.wordplay.R;
import com.example.eva.wordplay.data.DataHelper;
import com.example.eva.wordplay.data.Word;
import com.example.eva.wordplay.data.WordSet;

import java.util.ArrayList;


public class BaseFragment extends Fragment implements DataHelper.ResultListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    View view;
    CheckBeginListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.base_fragment, null);

        int requestId = DataHelper.getInstance(getActivity()).getLastSavedSets(getActivity(), this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity()) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View view, int position) {
                prepareDeck(recyclerView, view, position);
            }
        });
        return view;
    }

    private void prepareDeck(RecyclerView recyclerView, View view, int position) {
        RecyclerAdapter.ViewHolder viewHolder = (RecyclerAdapter.ViewHolder) recyclerView.getChildViewHolder(view);
        String deckName = (String) viewHolder.deckName.getText();
        DataHelper.getInstance(getActivity()).get(getActivity(),deckName, this);
    }

    @Override
    public void onStringResult(boolean success, String result) {

    }

    public interface CheckBeginListener {
        void onStartDeckCheck(WordSet set);
    }

    public void registerWPCallback(CheckBeginListener listener){
        this.listener = listener;
    }

    @Override
    public void onSetResult(boolean success, WordSet result) {
        listener.onStartDeckCheck(result);
    }

    @Override
    public void onArraySetResult(boolean success, ArrayList<WordSet> result) {
        adapter = new RecyclerAdapter(result);
        recyclerView.setAdapter(adapter);
    }

}
