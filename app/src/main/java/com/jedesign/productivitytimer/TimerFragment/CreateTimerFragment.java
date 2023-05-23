package com.jedesign.productivitytimer.TimerFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.jedesign.productivitytimer.DataHelper;
import com.jedesign.productivitytimer.R;
import com.jedesign.productivitytimer.TimerFragment.TimerFragment;

import java.util.Calendar;
import java.util.List;

public class CreateTimerFragment extends Fragment {
    public CreateTimerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Button timerButton;
    AutoCompleteTextView actvTask, actvLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_timer, container, false);
        initializeLayout(v);
        setTimerButtonOnClick(); //Start timer and change visibility

        setTasks();
        setLocations();


        return v;
    }

    private boolean timerStarted(long timeCalendarStarted) { return (timeCalendarStarted != -1);}

    private void initializeLayout(View v){
        timerButton = v.findViewById(R.id.btnTimer);
        actvLocation = v.findViewById(R.id.actvLocation);
        actvTask = v.findViewById(R.id.actvTask);
    }

    /*
    public long getTimeCalendarStartedFromPreferences(){
        SharedPreferences calendarPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        long calendarInMillis = calendarPref.getLong("TimerStartTime", -1);
        return calendarInMillis;
    }
     */

    private void setLocations() {
        DataHelper dh = new DataHelper(getActivity());
        List<String> allLocations = dh.getAllLocations();
        dh.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, allLocations);
        actvLocation.setAdapter(adapter);
    }

    private void setTasks(){
        DataHelper dh = new DataHelper(getActivity());
        List<String> allTasks = dh.getAllTasks();
        dh.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, allTasks);
        actvTask.setAdapter(adapter);
    }


    private void setTimerButtonOnClick() {
        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTimeStartedToSharedPreferences();
                saveTaskToSharedPreferences(actvTask.getText().toString());
                saveLocationToSharedPreferences(actvLocation.getText().toString());
                goToTimerFragment();

            }
        });
    }

    private void goToTimerFragment() {
        TimerFragment timerFragment = new TimerFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, timerFragment)
                .addToBackStack(null)
                .commit();
    }

    private void saveTimeStartedToSharedPreferences() {
        SharedPreferences calendarPref = getActivity().getPreferences(Context.MODE_PRIVATE );
        SharedPreferences.Editor editor= calendarPref.edit();
        editor.putLong("TimerStartTime", Calendar.getInstance().getTimeInMillis());
        editor.commit();
    }

    private void saveLocationToSharedPreferences(String location) {
        SharedPreferences calendarPref = getActivity().getPreferences(Context.MODE_PRIVATE );
        SharedPreferences.Editor editor= calendarPref.edit();
        editor.putString("Locations", location);
        editor.commit();
    }

    private void saveTaskToSharedPreferences(String task) {
        SharedPreferences calendarPref = getActivity().getPreferences(Context.MODE_PRIVATE );
        SharedPreferences.Editor editor= calendarPref.edit();
        editor.putString("Task", task);
        editor.commit();
    }




}