package com.example.eva.wordplay;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.v4.app.FragmentTransaction;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.eva.wordplay.fragments.AboutFragment;
import com.example.eva.wordplay.fragments.BaseFragment;
import com.example.eva.wordplay.fragments.CreationFragment;
import com.example.eva.wordplay.fragments.ImportFragment;
import com.example.eva.wordplay.fragments.WordPlayFragment;
import com.example.eva.wordplay.network.NetworkHelper;

import java.security.spec.ECField;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity{

    private final String TAG = MainActivity.class.getSimpleName();

    private String[] navigationItems;
    private DrawerLayout drawerLayout;
    private ListView listView;

    private Integer fragmentContainer;

    private CreationFragment creationPage;
    private BaseFragment basePage;
    private ImportFragment importPage;
    private AboutFragment aboutPage;

    private static int currentFragmentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationItems = getResources().getStringArray(R.array.navigationItems);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        listView = (ListView) findViewById(R.id.navigationDrawer);

        fragmentContainer = R.id.content;

        listView.setAdapter(new ArrayAdapter<>(this, R.layout.navigation_item, navigationItems));
        listView.setOnItemClickListener(new NavigationDrawerListener());


        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment =fragmentManager.findFragmentById(R.id.content);

        if(currentFragment==null) {
            basePage = new BaseFragment();
            setPage(basePage);
            currentFragmentIndex = 0;
        } else {
            checkExistingFragment();
        }


    }

    private void checkExistingFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.content);
        String existingFragmentClass = fragment.getClass().getSimpleName();

        AboutFragment aboutFragment = AboutFragment.class.getSimpleName().equals(existingFragmentClass)
                ? (AboutFragment) fragmentManager.findFragmentById(R.id.content):null;

        BaseFragment baseFragment = BaseFragment.class.getSimpleName().equals(existingFragmentClass)
                ? (BaseFragment) fragmentManager.findFragmentById(R.id.content):null;

        ImportFragment importFragment = ImportFragment.class.getSimpleName().equals(existingFragmentClass)
                ? (ImportFragment) fragmentManager.findFragmentById(R.id.content):null;

        CreationFragment creationFragment = CreationFragment.class.getSimpleName().equals(existingFragmentClass)
                ? (CreationFragment) fragmentManager.findFragmentById(R.id.content):null;

        if(aboutFragment!=null){
            aboutPage = aboutFragment;
            currentFragmentIndex = 3;
        } else if(baseFragment!=null){
            basePage = baseFragment;
            currentFragmentIndex = 0;
        } else if(importFragment!=null) {
            importPage = importFragment;
            currentFragmentIndex = 2;
        } else if(creationFragment!=null){
            creationPage = creationFragment;
            currentFragmentIndex = 1;
        }

    }

    private void setPage(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        fTransaction.add(fragmentContainer, fragment, fragment.getTag());
        fTransaction.commit();
    }

    private void clearContainer(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        fTransaction.remove(fragment);
        fTransaction.commit();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    public class NavigationDrawerListener implements AdapterView.OnItemClickListener {
        private final String TAG = NavigationDrawerListener.class.getSimpleName();
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        private void selectItem(int position) {

            Fragment[] pages = {basePage, creationPage, importPage, aboutPage};

            if(currentFragmentIndex != position) {
                if(pages[currentFragmentIndex]!=null) {
                    clearContainer(pages[currentFragmentIndex]);
                }
                switch(position){
                    case 0:
                        basePage = basePage == null ? new BaseFragment() : basePage;
                        setPage(basePage);
                        break;
                    case 1:
                        creationPage = creationPage == null ? new CreationFragment() : creationPage;
                        setPage(creationPage);
                        break;
                    case 2:
                        importPage = importPage == null ? new ImportFragment() : importPage;
                        setPage(importPage);
                        break;
                    case 3:
                        aboutPage = aboutPage == null ? new AboutFragment() : aboutPage;
                        setPage(aboutPage);
                        break;
                }
                currentFragmentIndex = position;
            }

            listView.setItemChecked(position, true);
            setTitle(navigationItems[position]);
            drawerLayout.closeDrawer(listView);

        }
    }



}
