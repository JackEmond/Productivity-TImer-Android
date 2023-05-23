package com.jedesign.productivitytimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.jedesign.productivitytimer.StatsFragment.StatsFragment;
import com.jedesign.productivitytimer.TimerFragment.CreateTimerFragment;
import com.jedesign.productivitytimer.TimerFragment.TimerFragment;
import com.jedesign.productivitytimer.TimerFragment.TimerPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bnv;

    SettingsFragment settingsFragment = new SettingsFragment();
    TimerFragment timerFragment = new TimerFragment();
    StatsFragment statsFragment = new StatsFragment();
    CreateTimerFragment createTimerFragment = new CreateTimerFragment();
    private static String APP_FEATURE = "featureParam";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnv = findViewById(R.id.bottomNavigationView);
        startTimerFragment();
        setBottomNavigationView();
        setActiveFragment();

    }

    private void startTimerFragment() {
        if(timerIsActive()){
            setCurrentFragment(timerFragment);
        }
        else{
            setCurrentFragment(createTimerFragment);
        }
    }


    private void setBottomNavigationView(){

        //Change Fragments based on Click
        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_account:
                        setCurrentFragment(settingsFragment);
                        return true;
                    case R.id.menu_stats:
                        setCurrentFragment(statsFragment);
                        return true;
                    case R.id.menu_timer:
                        startTimerFragment();
                        return true;
                }
                return false;
            }
        });
    }

    private boolean timerIsActive() {
        long timeCalendarStarted = TimerPreferences.getTimeCalendarStartedFromPreferences(this);
        if(timerStarted(timeCalendarStarted)) {
            return true;
        }
        return false;
    }

    private boolean timerStarted(long timeCalendarStarted) { return (timeCalendarStarted != -1);}

    private void setActiveFragment() {
        String getAppFeature = "";

        if(this.getIntent().hasExtra(APP_FEATURE))
            getAppFeature = this.getIntent().getExtras().getString(APP_FEATURE);

        if(getAppFeature.equals("stats")){
            bnv.getMenu().findItem(R.id.menu_stats).setChecked(true);
            setCurrentFragment(statsFragment);
        }
        else{
            bnv.getMenu().findItem(R.id.menu_timer).setChecked(true);
        }

    }


    private void setCurrentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, fragment, null)
                .setReorderingAllowed(true)
                .addToBackStack(null) // name can be null
                .commit();
    }
}