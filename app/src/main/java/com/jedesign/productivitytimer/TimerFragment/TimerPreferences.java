package com.jedesign.productivitytimer.TimerFragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

import androidx.fragment.app.FragmentActivity;

public class TimerPreferences {

    public static long getTimeCalendarStartedFromPreferences(Activity activity){
        SharedPreferences calendarPref = activity.getPreferences(Context.MODE_PRIVATE);
        long calendarInMillis = calendarPref.getLong("TimerStartTime", -1);
        return calendarInMillis;
    }

    public static void resetTotalTimePaused(Activity activity) {
        SharedPreferences pref = activity.getPreferences(Context.MODE_PRIVATE );
        SharedPreferences.Editor editor= pref.edit();
        editor.putLong("TotalTimePaused", 0);
        editor.commit();
    }

    public static long getTotalTimePausedFromPreferences(Activity activity) {
        SharedPreferences pref = activity.getPreferences(Context.MODE_PRIVATE);
        long totalTimePaused = pref.getLong("TotalTimePaused", 0);
        return totalTimePaused;
    }


    public static void saveCurrTimePausedToSharedPreferences(long time, Activity activity) {
        SharedPreferences pref = activity.getPreferences(Context.MODE_PRIVATE );
        SharedPreferences.Editor editor= pref.edit();
        editor.putLong("TimerPaused", time);
        editor.commit();
    }


    public static long getCurrTimePausedFromPreferences(Activity activity){
        SharedPreferences calendarPref = activity.getPreferences(Context.MODE_PRIVATE);
        long calendarInMillis = calendarPref.getLong("TimerPaused", -1);
        return calendarInMillis;
    }

    public static String getTaskFromPreferences(Activity activity) {
        SharedPreferences pref = activity.getPreferences(Context.MODE_PRIVATE);
        return pref.getString("Task", "");
    }

    public static String getLocationFromPreferences(Activity activity) {
        SharedPreferences pref = activity.getPreferences(Context.MODE_PRIVATE);
        return pref.getString("Locations", "");
    }


    public static void updateTotalTimePaused(Activity activity) {
        long initialTimePaused = TimerPreferences.getCurrTimePausedFromPreferences(activity);
        long currTimePaused  = (Calendar.getInstance().getTimeInMillis() - initialTimePaused) / 1000;
        long totalTimePaused = TimerPreferences.getTotalTimePausedFromPreferences(activity);

        SharedPreferences pref = activity.getPreferences(Context.MODE_PRIVATE );
        SharedPreferences.Editor editor= pref.edit();
        editor.putLong("TotalTimePaused", currTimePaused + totalTimePaused);
        editor.commit();

        TimerPreferences.saveCurrTimePausedToSharedPreferences(-1, activity);
    }


}
