package ru.romanblack.test.ui.activity;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import ru.romanblack.test.R;
import ru.romanblack.test.ui.fragment.DrawerFragment;
import ru.romanblack.test.ui.fragment.ListFragment;
import ru.romanblack.test.ui.fragment.MainActivityFragment;
import ru.romanblack.test.ui.fragment.MapFragment;
import ru.romanblack.test.ui.fragment.ScalingFragment;
import ru.romanblack.test.ui.fragment.ServiceFragment;

public class MainActivity extends BaseActivity {

    private static final String STATE_CURRENT_FRAGMENT_TAG = "current_fragment_tag";
    private static final String STATE_CURRENT_SELECTION = "current_selection";

    private DrawerLayout drawerLayout;
    private DrawerFragment drawerFragment;

    private int currentSelection = 0;

    private String currentFragmentTag;

    private MainActivityFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            currentFragmentTag = savedInstanceState.getString(STATE_CURRENT_FRAGMENT_TAG);
            currentSelection = savedInstanceState.getInt(STATE_CURRENT_SELECTION, 0);
        }

        initializeUi();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.drawer_fragment, DrawerFragment.crate(currentSelection))
                .commit();

        initializeFragment(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (currentFragmentTag != null) {
            outState.putString(STATE_CURRENT_FRAGMENT_TAG, currentFragmentTag);
            outState.putInt(STATE_CURRENT_SELECTION, currentSelection);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.change_lang:
                String current = getResources().getConfiguration().locale.toString();
                if (current.contains("en")) {
                    setLocale("ru");
                } else {
                    setLocale("en");
                }

                setContentView(R.layout.activity_main);

                currentFragment = null;
                currentFragmentTag = null;

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.drawer_fragment, DrawerFragment.crate(currentSelection))
                        .commit();

                initializeUi();

                if (currentSelection >= 0) {
                    onSelected(currentSelection);
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeUi() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
    }

    private void initializeFragment(Bundle state) {
        if (state == null) {
            return;
        }

        currentFragmentTag = state.getString(STATE_CURRENT_FRAGMENT_TAG, null);

        if (!TextUtils.isEmpty(currentFragmentTag)) {
            currentFragment = (MainActivityFragment) getSupportFragmentManager()
                    .findFragmentByTag(currentFragmentTag);
        }
    }

    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void onSelected(int item) {
        currentSelection = item;

        MainActivityFragment newFragment = null;

        String fragmentTag = "fragment" + item;

        switch (item) {
            case DrawerFragment.MENU_LIST:
                if (currentFragment instanceof ListFragment) {
                } else {
                    newFragment = new ListFragment();
                }
                break;
            case DrawerFragment.MENU_SCALING:
                if (currentFragment instanceof ScalingFragment) {
                } else {
                    newFragment = new ScalingFragment();
                }
                break;
            case DrawerFragment.MENU_SERVICE:
                if (currentFragment instanceof ServiceFragment) {
                } else {
                    newFragment = new ServiceFragment();
                }
                break;
            case DrawerFragment.MENU_MAP:
                if (currentFragment instanceof MapFragment) {
                } else {
                    newFragment = new MapFragment();
                }
                break;
        }

        if (newFragment != null) {
            setupFragment(newFragment, fragmentTag);
        }

        closeDrawer();
    }

    private void removeFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(currentFragment)
                .commit();
    }

    private void setupFragment(MainActivityFragment fragment, String fragmentTag) {
        currentFragment = fragment;
        currentFragmentTag = fragmentTag;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, fragmentTag)
                .commit();
    }
}
