package com.jedesign.productivitytimer.TimerFragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.jedesign.productivitytimer.DataHelper;
import com.jedesign.productivitytimer.R;
import com.jedesign.productivitytimer.TimerClass;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class TimerFragment extends Fragment {

    public TimerFragment(){
        // Required empty public constructor
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Button cancelButton, pauseButton, saveButton;
    TextView timerText;
    LinearLayout ll_pause_and_stop_button;
    long time = 0;
    boolean paused = false;
    boolean timerRunning = false;
    Timer timer;
    TimerTask timerTask;
    private static String PAUSE_APP = "exercise.name";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timer, container, false);
        initializeLayout(v);
        setTimerIfAlreadyStarted(v);
        setButtonOnClicks();

        return v;
    }

    private void setButtonOnClicks() {
        setCancelButtonOnClick();
        setSaveButtonOnClick();
        setPauseButtonOnClick();
    }


    private void initializeLayout(View v) {
        timerText = v.findViewById(R.id.txtTimer);
        ll_pause_and_stop_button = v.findViewById(R.id.ll_pause_and_stop_button);
        pauseButton = v.findViewById(R.id.btnPause);
        saveButton = v.findViewById(R.id.btnSave);
        cancelButton = v.findViewById(R.id.btnCancel);
    }


    private void setTimerIfAlreadyStarted(View v) {
        long timeCalendarStarted = TimerPreferences.getTimeCalendarStartedFromPreferences(getActivity());

        if(timerStarted(timeCalendarStarted)){
            //Set Timer to Current time minus when the Calendar started
            long totalTimePaused = TimerPreferences.getTotalTimePausedFromPreferences(getActivity());
            long currTimeInSeconds = Calendar.getInstance().getTimeInMillis()/1000;
            time = currTimeInSeconds - (timeCalendarStarted/1000) - totalTimePaused;

            long timePaused = TimerPreferences.getCurrTimePausedFromPreferences(getActivity());
            if(timePaused != -1){ // Timer should be paused
                long timeElapsedSincePause = currTimeInSeconds - (timePaused/1000);
                time = time - timeElapsedSincePause;
                pauseButton.setText("RESUME");
                paused = true;
                timerText.setText(getTimerText());
            }
            else{ //Timer should be running
                if(!timerRunning) startTimer();
            }

        }

    }

    private boolean timerStarted(long timeCalendarStarted) { return (timeCalendarStarted != -1);}

    private void setPauseButtonOnClick() {
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!paused)
                    pauseTimer();
                else
                   resumeTimer();
            }
        });
    }

   private void pauseTimer(){
        pauseButton.setText("RESUME");
        timer.cancel();
        TimerPreferences.saveCurrTimePausedToSharedPreferences(Calendar.getInstance().getTimeInMillis(), getActivity());
        paused = true;
    }

    private void resumeTimer(){
        paused =false;
        pauseButton.setText("PAUSE");
        startTimer();
        TimerPreferences.updateTotalTimePaused(getActivity());
    }

    private void setSaveButtonOnClick() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!paused) timer.cancel();
                insertTimerIntoDatabase();
                resetData(v);
                popupSuccessScreen();
                //Implement Timer Save in Database
            }

            private void insertTimerIntoDatabase() {
                //Get Data to be inserted
                String location = TimerPreferences.getLocationFromPreferences(getActivity());
                String task = TimerPreferences.getTaskFromPreferences(getActivity());
                int duration = Math.round(time);
                TimerClass timer = new TimerClass(TimerPreferences.getTimeCalendarStartedFromPreferences(getActivity()), duration,location, task);

                //Insert Timer
                DataHelper dh = new DataHelper(getActivity());
                long taskId = dh.addOrFindTask(task);
                long locationId = dh.addOrFindLocation(location);
                dh.addTimer(timer, taskId, locationId);
            }
        });
    }

    private void resetTimerInfo(View v) {
        paused = true;
        time = 0;
    }




    private void setCancelButtonOnClick() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetData(v);
                openCreateTimerFragment();
            }
        });
    }

    private void openCreateTimerFragment() {
        CreateTimerFragment createTimerFragment = new CreateTimerFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, createTimerFragment)
                .addToBackStack(null)
                .commit();
    }

    private void resetData(View v) {
        if(!paused) timer.cancel();
        resetTimerPreferences();
        TimerPreferences.resetTotalTimePaused(getActivity());
        TimerPreferences.saveCurrTimePausedToSharedPreferences(-1,getActivity());
        resetTimerInfo(v);
        pauseButton.setText("PAUSE");
    }


    private void resetTimerPreferences() {
        SharedPreferences calendarPref = getActivity().getPreferences(Context.MODE_PRIVATE );
        SharedPreferences.Editor editor= calendarPref.edit();
        editor.putLong("TimerStartTime", -1);
        editor.commit();
    }


    private void startTimer() {
        paused = false;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if(isAdded()){
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            time++;
                            timerText.setText(getTimerText());
                            timerRunning = true;
                        }
                    });
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private SpannableStringBuilder getTimerText() {
        int rounded = Math.round(time);
        int seconds = ((rounded % 86400) %3600)%60;
        int minutes = ((rounded % 86400) %3600)/60;
        int hours = (rounded % 86400) /3600;
        return formatTime(seconds, minutes, hours);
    }

    private SpannableStringBuilder formatTime(int seconds, int minutes, int hours) {
        //Format Time
        String formattedTime =  String.format("%02d",hours)+"hrs "+String.format("%02d",minutes)+"min "+String.format("%02d",seconds) + "sec";

        //Set not timer text to lower case
        SpannableStringBuilder spannable = new SpannableStringBuilder(formattedTime);
        spannable.setSpan( new RelativeSizeSpan(0.4f), 14, 17, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannable.setSpan( new RelativeSizeSpan(0.4f),  2,5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannable.setSpan( new RelativeSizeSpan(0.4f), 8, 12, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannable;

    }


    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    Button btnClosePopup;

    private void popupSuccessScreen(){
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_timer_uploaded, null);
        btnClosePopup = contactPopupView.findViewById(R.id.btnClosePopup);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openCreateTimerFragment();
            }
        });


    }


}