package com.example.eva.wordplay.fragments;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.support.annotation.BoolRes;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.eva.wordplay.R;
import com.example.eva.wordplay.data.DataHelper;
import com.example.eva.wordplay.data.Word;
import com.example.eva.wordplay.data.WordSet;

import java.util.ArrayList;
import java.util.Collections;

public class CreationFragment extends Fragment implements View.OnClickListener, DataHelper.ResultListener, AddWordFragment.wordCreateListener{

    private Button btnSave, btnWordCreate;
    private FrameLayout wordFormContainer;
    private View view;

    private RecyclerView recyclerView;
    private WordRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private EditText deckNameView;
    private AddWordFragment addWordFragment;

    private ArrayList<Word> allWords = new ArrayList<>();
    private ArrayList<Boolean> isPicked = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_edit_deck_fragment, null);
        btnSave = (Button) view.findViewById(R.id.createDeck);
        btnWordCreate = (Button) view.findViewById(R.id.new_Word);
        wordFormContainer = (FrameLayout) view.findViewById(R.id.new_word_fragment);

        btnWordCreate.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        deckNameView = (EditText) view.findViewById(R.id.deckName);

        addWordFragment = new AddWordFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.new_word_fragment, addWordFragment);
        fragmentTransaction.commit();

        addWordFragment.registerWordCreateListener(this);

        DataHelper.getInstance(getActivity()).getAllWords(getActivity(), this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        return view;

    }

    private void wordClick(RecyclerView recyclerView, View view, int position) {
        WordRecyclerAdapter.ViewHolder viewHolder = (WordRecyclerAdapter.ViewHolder) recyclerView.getChildViewHolder(view);
        if(!isPicked.get(position)) {
            isPicked.set(position, true);
            viewHolder.pickWord();
        } else {
            isPicked.set(position, false);
            viewHolder.unpickWord();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createDeck:
                if(!deckNameView.getText().toString().equals("")){
                    Toast.makeText(getContext(),"You try to save deck with name " +
                            deckNameView.getText().toString(),Toast.LENGTH_SHORT).show();
                    final String setName = deckNameView.getText().toString();
                    final ArrayList<Word> words = new ArrayList<>();
                    for(Integer i = 0; i < allWords.size(); i++){
                        if(isPicked.get(i)){
                            words.add(allWords.get(i));
                        }
                    }
                    if(words.isEmpty()){
                        Toast.makeText(getContext(),"You pick no words, you really need empty set?",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        DataHelper.getInstance(getActivity()).createNewSet(getActivity(),words,
                                setName,this);
                    }
                } else {
                    Toast.makeText(getContext(),"You try to save without name",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.new_Word:
                btnWordCreate.setVisibility(View.GONE);
                wordFormContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onWordArrayResult(boolean success, ArrayList<Word> result) {
        if(success){
            allWords = result;
            isPicked = new ArrayList<>(Collections.nCopies(result.size(), false));
            adapter = new WordRecyclerAdapter(allWords, isPicked);
            recyclerView.setAdapter(adapter);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.addOnItemTouchListener(new RecyclerClickListener(getActivity()) {
                @Override
                public void onItemClick(RecyclerView recyclerView, View view, int position) {
                    wordClick(recyclerView, view, position);
                }
            });

        }
    }

    @Override
    public void onStringResult(boolean success, String result) {
        if(success) {
            Toast.makeText(getActivity(), "Set successfully saved", Toast.LENGTH_SHORT).show();
            //TODO - по идее, после этого надо перебросить юзера на экран со списокм сетов
        } else {
            Toast.makeText(getActivity(), "Probably, this name for set is busy. Try another.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSetResult(boolean success, WordSet result) {

    }

    @Override
    public void onArraySetResult(boolean success, ArrayList<WordSet> result) {

    }

    private void setWordInFront(Word element){
        Word tmp = allWords.get(0);
        allWords.set(0, element);
        allWords.add(tmp);
        Boolean firstPick = isPicked.get(0);
        isPicked.set(0, false);
        isPicked.add(firstPick);
    }


    @Override
    public void onWordCreated(Word word) {
        wordFormContainer.setVisibility(View.GONE);
        btnWordCreate.setVisibility(View.VISIBLE);
        setWordInFront(word);
        adapter.notifyDataSetChanged();
        adapter.setWordCreated();
    }
}
