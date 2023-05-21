package com.jedesign.productivitytimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bnv;

    SettingsFragment settingsFragment = new SettingsFragment();
    TimerFragment timerFragment = new TimerFragment();
    StatsFragment statsFragment = new StatsFragment();
    private static String APP_FEATURE = "featureParam";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnv = findViewById(R.id.bottomNavigationView);

        setBottomNavigationView();
        setActiveFragment();

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
                        setCurrentFragment(timerFragment);
                        return true;
                }
                return false;
            }
        });
    }


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