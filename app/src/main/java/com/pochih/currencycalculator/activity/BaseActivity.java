package com.pochih.currencycalculator.activity;

import android.app.ActivityOptions;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pochih.currencycalculator.R;

/**
 * Created by PoChih on 2017/12/26.
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean mToolbarInitialized;
    private int mItemToOpenWhenDrawerCloses = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mToolbarInitialized) {
            throw new IllegalStateException("You must run super.initializeToolbar at " +
                    "the end of your onCreate method");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getFragmentManager().addOnBackStackChangedListener(backStackChangedListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        getFragmentManager().removeOnBackStackChangedListener(backStackChangedListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item != null && item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private final FragmentManager.OnBackStackChangedListener backStackChangedListener =
            new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    updateDrawerToggle();
                }
            };

    protected void updateDrawerToggle() {
        if (mDrawerToggle == null) {
            return;
        }
        boolean isRoot = getFragmentManager().getBackStackEntryCount() == 0;
        mDrawerToggle.setDrawerIndicatorEnabled(isRoot);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(!isRoot);
            getSupportActionBar().setDisplayHomeAsUpEnabled(!isRoot);
            getSupportActionBar().setHomeButtonEnabled(!isRoot);
        }
        if (isRoot) {
            mDrawerToggle.syncState();
        }
    }

    protected void initializeToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar == null) {
            throw new IllegalStateException("Layout is required to include a Toolbar with id " +
                    "'toolbar'");
        }

        TextView mTitle = mToolbar.findViewById(R.id.toolbar_title);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        if (mDrawerLayout != null) {
            NavigationView navigationView = findViewById(R.id.nav_view);
            if (navigationView == null) {
                throw new IllegalStateException("Layout requires a NavigationView " +
                        "with id 'nav_view'");
            }

            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                    mToolbar, R.string.open_content_drawer, R.string.close_content_drawer);
            mDrawerLayout.setDrawerListener(drawerListener);
            populateDrawerItems(navigationView);

            setSupportActionBar(mToolbar);
            mTitle.setText(mToolbar.getTitle());
            mToolbar.setTitle("");
            updateDrawerToggle();
        } else {

            setSupportActionBar(mToolbar);
            mTitle.setText(mToolbar.getTitle());
            mToolbar.setTitle("");
        }

        mToolbarInitialized = true;
    }

    private final DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            if (mDrawerToggle != null) mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            if (mDrawerToggle != null) mDrawerToggle.onDrawerOpened(drawerView);
            if (getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.app_name);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            if (mDrawerToggle != null) mDrawerToggle.onDrawerClosed(drawerView);
            if (mItemToOpenWhenDrawerCloses >= 0) {
                Bundle extras = ActivityOptions.makeCustomAnimation(
                        BaseActivity.this, R.anim.fade_in, R.anim.fade_out).toBundle();
                Class activityClass = null;
                switch (mItemToOpenWhenDrawerCloses) {
                    case R.id.nav_currency:
                        activityClass = MainActivity.class;
                        break;
                    case R.id.nav_settings:
                        activityClass = SettingActivity.class;
                        break;
                }
                if (activityClass != null) {
                    startActivity(new Intent(BaseActivity.this, activityClass), extras);
                    finish();
                }
            }
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            if (mDrawerToggle != null) mDrawerToggle.onDrawerStateChanged(newState);
        }
    };

    private void populateDrawerItems(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mItemToOpenWhenDrawerCloses = menuItem.getItemId();
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
        if (MainActivity.class.isAssignableFrom(getClass())) {
            navigationView.setCheckedItem(R.id.nav_currency);

        } else if (SettingActivity.class.isAssignableFrom(getClass())) {
            navigationView.setCheckedItem(R.id.nav_settings);
        }
    }
}