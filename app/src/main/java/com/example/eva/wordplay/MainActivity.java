package com.example.eva.wordplay;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.app.FragmentTransaction;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.eva.wordplay.fragments.AboutFragment;
import com.example.eva.wordplay.fragments.BaseFragment;
import com.example.eva.wordplay.fragments.CreationFragment;
import com.example.eva.wordplay.fragments.ImportFragment;
import com.example.eva.wordplay.network.NetworkHelper;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements  NetworkHelper.ResultListener{

    private final String TAG = MainActivity.class.getSimpleName();

    private String[] navigationItems;
    private DrawerLayout drawerLayout;
    private ListView listView;

    private Integer fragmentContainer;

    private CreationFragment creationPage = new CreationFragment();
    private BaseFragment basePage = new BaseFragment();
    private ImportFragment importPage = new ImportFragment();
    private AboutFragment aboutPage = new AboutFragment();

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

        setPage(basePage);
        currentFragmentIndex = 0;

    }

    private void setPage(Fragment fragment){
        FragmentTransaction fTransaction = getFragmentManager().beginTransaction();
        fTransaction.add(fragmentContainer, fragment, fragment.getTag());
        fTransaction.commit();
    }

    private void clearContainer(Fragment fragment){
        FragmentTransaction fTransaction = getFragmentManager().beginTransaction();
        fTransaction.remove(fragment);
        fTransaction.commit();
    }

    public void deckListImport() {
        int mRequestId = NetworkHelper.getInstance(this).viewAllDecksRequest(this, this);
        Log.d(TAG, "We want to draw list of external decks ");
    }

    @Override
    public void onResult(final boolean success, final String result) {
        /*if (success) {
            resultTextView.setText(String.format("OK: %s", result));
        } else {
            resultTextView.setText(String.format("FAIL: %s", result));
        }*/
        Log.d(TAG, "Activity get result " + result);
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
                if(pages[position].equals(importPage)){
                    deckListImport();
                }

                clearContainer(pages[currentFragmentIndex]);
                
                setPage(pages[position]);
                currentFragmentIndex = position;
            }

            listView.setItemChecked(position, true);
            setTitle(navigationItems[position]);
            drawerLayout.closeDrawer(listView);

        }
    }



}
