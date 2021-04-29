package com.example.cybank;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cybank.Fragments.AccountsFragment;
import com.example.cybank.Fragments.ChatFragment;
import com.example.cybank.Fragments.LoanFragment;
import com.example.cybank.Fragments.PlannerFragment;
import com.example.cybank.Fragments.SettingsFragment;
import com.example.cybank.Fragments.SupportFragment;
import com.example.cybank.Logic.FragmentsManager;
import com.example.cybank.Logic.Helper;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

/**
 * This is the full screen that contain bottom-top navigation, drawer and gragments.
 */
public class UIActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    public static Activity c = null;
    static ProgressBar simpleProgressBar;

    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.
    private ActionBarDrawerToggle drawerToggle;

    /**
     * This creates a page bottom-top navigation, drawer and fragments.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_ui);
        c = this;

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // This will display an Up icon (<-), we will replace it with hamburger later

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        simpleProgressBar = (ProgressBar)findViewById(R.id.progressBar); // initiate the progress bar
        simpleProgressBar.setVisibility(View.GONE);


        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        View hView = nvDrawer.inflateHeaderView(R.layout.nav_header);
//        ImageView imgvw = (ImageView)hView.findViewById(R.id.imageView);
        TextView tv = (TextView) hView.findViewById(R.id.txtheader);
//        imgvw .setImageResource();
        tv.setText("new text");
        tv.setText("Welcome " + Helper.getName() + "!");
        // Setup drawer view
        setupDrawerContent(nvDrawer);
        if (Helper.getIsAdmin() && !Helper.getCang()) {
            try {
                FragmentsManager.nextFragment(SupportFragment.class, false);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Helper.getAccountDataRequest();
                Helper.getLoanDataRequest();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    /**
     * The action bar home/up action should open or close the drawer.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * sets up the options in the drawer
     *
     * @param navigationView
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    /**
     * Create a new fragment and specify the fragment to show based on nav item clicked
     *
     * @param menuItem
     */
    public void selectDrawerItem(MenuItem menuItem) {
        if(menuItem.getItemId()==R.id.userReport){
            Intent intent = new Intent(getApplicationContext(),UserReport.class);
            startActivity(intent);
        }
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = AccountsFragment.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = PlannerFragment.class;
                break;
            case R.id.nav_fourth_fragment:
                fragmentClass = LoanFragment.class;
                break;
            case R.id.nav_fifth_fragment:
                fragmentClass = ChatFragment.class;
                break;
            case R.id.nav_sixth_fragment:
                fragmentClass = SettingsFragment.class;
                break;
            default:
                fragmentClass = AccountsFragment.class;
        }
            try {
                FragmentsManager.nextFragment(fragmentClass, true);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();

    }


    /**
     * gets UI Context
     * @return
     */
    public static Context getUIContext(){
        return c;
    }

    public static void setProgressBarVisible(boolean isVisible){
        if (isVisible){
            simpleProgressBar.setVisibility(View.VISIBLE);
        }else{
            simpleProgressBar.setVisibility(View.GONE);
        }
    }

    public static void setProgressBar(int progress){
        simpleProgressBar.setProgress(progress);
        if (progress == 100){
            setProgressBarVisible(false);
            simpleProgressBar.setProgress(0);
        } else if (progress <= 99){
            setProgressBarVisible(true);
        }
    }
    public static void goBack(){
        c.onBackPressed();
    }


}