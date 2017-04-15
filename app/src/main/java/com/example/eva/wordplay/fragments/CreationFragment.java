package com.example.eva.wordplay.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eva.wordplay.R;

/**
 * Created by eva on 12.04.17.
 */

public class CreationFragment extends Fragment implements View.OnClickListener{

    final String TAG = "WPLogs";
    Button btnAdd, btnCreate;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.creation_fragment, null);

        btnAdd = (Button) view.findViewById(R.id.btnAddWord);
        btnCreate = (Button) view.findViewById(R.id.createDeck);

        btnAdd.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddWord:
                Log.d(TAG, "You pressed btn Add in CreationFragment!");
                break;
            case R.id.createDeck:
                Log.d(TAG, "You pressed btn Create in CreationFragment!");
                break;
        }
    }
}
