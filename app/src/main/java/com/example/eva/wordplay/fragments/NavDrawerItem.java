package com.example.eva.wordplay.fragments;

/**
 * Created by eva on 23.05.17.
 */

public class NavDrawerItem {

    private boolean notify;
    private String title;

    public NavDrawerItem(){

    }

    public NavDrawerItem(boolean notify, String title){
        this.notify = notify;
        this.title = title;
    }

    public boolean isNotify(){
        return notify;
    }

    public void setNotify(boolean notify){
        this.notify = notify;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }
}
