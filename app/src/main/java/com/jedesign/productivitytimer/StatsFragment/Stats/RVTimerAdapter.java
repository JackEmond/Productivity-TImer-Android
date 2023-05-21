package com.jedesign.productivitytimer.StatsFragment.Stats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.jedesign.productivitytimer.DataHelper;
import com.jedesign.productivitytimer.R;
import com.jedesign.productivitytimer.TimerClass;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class RVTimerAdapter extends RecyclerView.Adapter<RVTimerAdapter.MyViewHolder>{
private ArrayList<TimerClass> timers;

public RVTimerAdapter(ArrayList<TimerClass> timers){
        this.timers = timers;
        }


public class MyViewHolder extends RecyclerView.ViewHolder {
    private TextView tvTimerDuration, tvTaskName, tvLocationName, tvDay;
    private Button btnDelete;

    public MyViewHolder(final View view) {
        super(view);
        tvDay = view.findViewById(R.id.tvDay);
        tvTimerDuration = view.findViewById(R.id.tvTimerDuration);
        tvTaskName = view.findViewById(R.id.tvTaskName);
        tvLocationName = view.findViewById(R.id.tvLocationName);
        btnDelete = view.findViewById(R.id.btnDeleteTimer);
    }
}

    @NonNull
    @Override
    public RVTimerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_timers, parent, false);
        return new RVTimerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RVTimerAdapter.MyViewHolder holder, int position) {
        //Get the class
        TimerClass t = timers.get(position);

        //Get duration
        int durationInSeconds = t.getDuration();
        String duration = formatDuration(durationInSeconds);

        //get location
        String location = t.getLocation();
        location = (location.equals(""))? "Not Set" : location;

        //get task
        String task = t.getTask();
        task = (task.equals(""))? "Not Set" : task;

        long startingTime = t.getStartingTime();
        String day = getDay(startingTime);
        int p = position;

        holder.tvDay.setText(day);
        holder.tvTimerDuration.setText(duration + "");
        holder.tvLocationName.setText(location);
        holder.tvTaskName.setText(task);


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete Item from database
                DataHelper dh = new DataHelper(holder.tvDay.getContext()); //Grabbing the context from a random item in holder
                dh.DeleteTimer(startingTime);
                removeLocationFromRV(p);
            }
            private void removeLocationFromRV(int p) {
                timers.remove(p);
                notifyItemRemoved(p);
                notifyItemRangeChanged(p, timers.size());

            }
        });

    }

    private String formatDuration(int durationInSeconds) {
        //Convert time into hours. 3600 seconds in an hour. Int automatically removes decimals
        long hours = durationInSeconds / 3600;

        //Subtract total time in seconds converted to minutes minus  the amount of time in hours converted to minute
        long minutes = ((durationInSeconds/60) - (hours*60));

        //Take total time remove hours and minutes (converted to seconds) to get seconds.
        long seconds = durationInSeconds - (hours*3600) - (minutes*60);

        if(hours != 0){
            return hours + "hrs " + minutes  + " min " + seconds + " sec";
        }
        else if(minutes != 0){
            return minutes  + " min " + seconds + " sec";
        }
        else{
            return seconds + " sec";
        }
    }

    private String getDay(long startingTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startingTime);

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int monthNum = cal.get(Calendar.MONTH);
        String month = monthFromNumberToText(monthNum);
        int year = cal.get(Calendar.YEAR);

        return month + "-" + day + "-" + year;
    }

    private String monthFromNumberToText(int month) {
    String fullDate = new DateFormatSymbols().getMonths()[month];
    String date = fullDate.substring(0,3);
    return date;
    }

    @Override
    public int getItemCount() {
        return timers.size();
    }
}