package com.taboola.sdk4example;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


public class SDKClassicMenuActivity extends AppCompatActivity implements SDKClassicMenuFragment.OnFragmentInteractionListener {
    private Toolbar mToolbar;
    private ActionBar mSupportActionBar;
    private FragmentManager mSupportFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mSupportActionBar = getSupportActionBar();
        if (mSupportActionBar != null) {
            mSupportActionBar.setLogo(R.drawable.ic_taboola);
            mSupportActionBar.setTitle(R.string.toolbar_title_classic);
        }
        // Code to handle toolbar title and back arrow
        mSupportFragmentManager = getSupportFragmentManager();
        mSupportFragmentManager.addOnBackStackChangedListener(() -> {
            int lastBackStackEntryCount = getSupportFragmentManager().getBackStackEntryCount() - 1;

            if (lastBackStackEntryCount < 0) {
                resetToolbarTitle();
                showBackArrow(false);
            } else {
                if (mSupportActionBar != null) {
                    mSupportActionBar.setTitle(getSupportFragmentManager().getBackStackEntryAt(lastBackStackEntryCount).getName());
                }
                showBackArrow(true);
            }
        });

        mSupportFragmentManager.beginTransaction()
                .replace(R.id.container, new SDKClassicMenuFragment()).commit();
        prepareTaboolaLogoRotation();
    }

    private void showBackArrow(boolean shouldShowBackButton) {
        if (mSupportActionBar != null) {
            mSupportActionBar.setDisplayHomeAsUpEnabled(shouldShowBackButton);
            mSupportActionBar.setDisplayShowHomeEnabled(!shouldShowBackButton);
        }
    }

    private void resetToolbarTitle() {
        if (mSupportActionBar != null) {
            mSupportActionBar.setTitle(R.string.toolbar_title_classic);
        }
    }

    @Override
    public void onMenuItemClicked(Fragment fragmentToOpen, String screenName) {
        try {
            mSupportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragmentToOpen)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(screenName)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void prepareTaboolaLogoRotation() {
        try {
            Animation rotateAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
            View toolbarTaboolaLogo = mToolbar.getChildAt(1);
            toolbarTaboolaLogo.setOnClickListener(v -> toolbarTaboolaLogo.startAnimation(rotateAnim));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}