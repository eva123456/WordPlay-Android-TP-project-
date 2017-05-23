package com.example.eva.wordplay;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.FragmentTransaction;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eva.wordplay.data.WordSet;
import com.example.eva.wordplay.fragments.AboutFragment;
import com.example.eva.wordplay.fragments.AddWordFragment;
import com.example.eva.wordplay.fragments.BaseFragment;
import com.example.eva.wordplay.fragments.CreationFragment;
import com.example.eva.wordplay.fragments.ImportFragment;
import com.example.eva.wordplay.fragments.NavigationFragment;
import com.example.eva.wordplay.fragments.WordPlayFragment;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;

import static com.example.eva.wordplay.R.array.navigationItems;
import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements BaseFragment.CheckBeginListener,
        WordPlayFragment.DeckListener, NavigationFragment.FragmentDrawerListener{

    private String[] navigationItems;
    private Integer fragmentContainer;

    private CreationFragment creationPage;
    private BaseFragment basePage;
    private ImportFragment importPage;
    private AboutFragment aboutPage;
    private WordPlayFragment checkPage;
<<<<<<< HEAD

    private Toolbar toolbar;
    private NavigationFragment navigationFragment;
    private DrawerLayout drawerLayout;
=======
>>>>>>> ec4fa8d05bcb65d52cc2d115bc9d5cd111e0606a

    private static int currentFragmentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationFragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        navigationFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationFragment.setDrawerListener(this);
        navigationItems = getResources().getStringArray(R.array.navigationItems);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        fragmentContainer = R.id.content;
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.content);

        if(currentFragment==null) {
            basePage = new BaseFragment();
            setPage(basePage);
            currentFragmentIndex = 0;
        } else {
            checkExistingFragment();
        }

        if(basePage != null){
            basePage.registerWPCallback(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        } else if(creationFragment!=null) {
            creationPage = creationFragment;
            currentFragmentIndex = 1;
        }
    }

    private void setPage(Fragment fragment){
        if(fragment.isAdded()){
            return;
        }
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

    @Override
    public void onStartDeckCheck(WordSet set) {
        if(basePage != null){
            clearContainer(basePage);
            basePage = null;
        }
        checkPage = new WordPlayFragment();
        checkPage.registerDeckListener(this);
        checkPage.setDeck(set);
        setPage(checkPage);
    }

    @Override
    public void onCheckStart() {
        checkPage.goNext();
    }

    @Override
    public void onCheckFinish() {
        clearContainer(checkPage);
        if(basePage == null){
            basePage = new BaseFragment();
        }
        basePage.registerWPCallback(this);
        setPage(basePage);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        itemSelected(position);
    }

<<<<<<< HEAD
    private void itemSelected(int position){
=======
    public class NavigationDrawerListener implements AdapterView.OnItemClickListener {
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
                        basePage.registerWPCallback(MainActivity.this);
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
>>>>>>> ec4fa8d05bcb65d52cc2d115bc9d5cd111e0606a

        Fragment[] pages = {basePage, creationPage, importPage, aboutPage};

        if(currentFragmentIndex != position) {
            if(pages[currentFragmentIndex]!=null) {
                clearContainer(pages[currentFragmentIndex]);
            }
            switch(position){
                case 0:
                    basePage = basePage == null ? new BaseFragment() : basePage;
                    basePage.registerWPCallback(MainActivity.this);
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

        getSupportActionBar().setTitle(navigationItems[position]);
    }
}
