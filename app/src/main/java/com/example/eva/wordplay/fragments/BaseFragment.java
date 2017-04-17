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

import java.util.ArrayList;


/**
 * Created by eva on 12.04.17.
 */

public class BaseFragment extends Fragment implements DataHelper.ResultListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_fragment, null);
        Log.d("WPLogs", "BaseFragment:onCreateView");

        int requestId = DataHelper.getInstance(getActivity()).getLastSavedSets(getActivity(), this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity()) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View view, int position) {
                Toast.makeText(getActivity(), "Сlick on the card" , Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onStringResult(boolean success, String result) {

    }

    @Override
    public void onSetResult(boolean success, WordSet result) {

    }

    @Override
    public void onArraySetResult(boolean success, ArrayList<WordSet> result) {

        adapter = new RecyclerAdapter(result);
        recyclerView.setAdapter(adapter);

    }
}
