package com.example.eva.wordplay.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by eva on 15.04.17.
 */

public abstract class RecyclerClickListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }
    };

    public RecyclerClickListener(Context context) {
        gestureDetector = new GestureDetector(context, gestureListener);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (gestureDetector.onTouchEvent(e)) {
            View itemView = rv.findChildViewUnder(e.getX(), e.getY());
            if (itemView != null && !itemView.dispatchTouchEvent(e)) {
                int itemPosition = rv.getChildAdapterPosition(itemView);
                if (itemPosition != RecyclerView.NO_POSITION) {
                    onItemClick(rv, itemView, itemPosition);
                    return true;
                }
            }
        }
        return false;
    }

    public abstract void onItemClick(RecyclerView recyclerView, View view, int position);

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
}
