package com.example.eva.wordplay.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.eva.wordplay.R;

import java.io.Console;

/**
 * Created by eva on 12.04.17.
 */

public class BaseFragment extends Fragment{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_fragment, null);

        String[] dataSet = getDataSet();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(dataSet);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity()) {
            @Override
            public void onItemClick(RecyclerView recyclerView, View view, int position) {
                   /*String TAG = "WPLogs";
                   Log.d(TAG, "Deck card was clicked!!!");
                   Log.d(TAG, "VIEW = " + view.toString());
                   Log.d(TAG, "POSITION = " + String.valueOf(position));*/
            }
        });

        return view;
    }

    private String[] getDataSet() {

        String[] mDataSet = new String[100];
        for (int i = 0; i < 100; i++) {
            mDataSet[i] = "my_deck_item_" + i;
        }
        return mDataSet;
    }


}
