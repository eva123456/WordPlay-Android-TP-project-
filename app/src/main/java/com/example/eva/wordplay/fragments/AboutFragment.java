package com.example.eva.wordplay.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eva.wordplay.R;


public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(getLogTag(), "onCreate");
        return inflater.inflate(R.layout.about_fragment, null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.v(getLogTag(), "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(getLogTag(), "onCreate");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(getLogTag(), "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(getLogTag(), "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(getLogTag(), "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(getLogTag(), "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(getLogTag(), "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(getLogTag(), "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(getLogTag(), "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(getLogTag(), "onDetach");
    }

    protected String getLogTag() {
        return getClass().getSimpleName();
    }

}
