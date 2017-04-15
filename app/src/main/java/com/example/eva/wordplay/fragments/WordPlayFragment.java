package com.example.eva.wordplay.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eva.wordplay.R;

/**
 * Created by eva on 15.04.17.
 */

public class WordPlayFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.word_play_fragment, null);
    }
}
