package com.jedesign.productivitytimer.StatsFragment.Stats;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jedesign.productivitytimer.DataHelper;
import com.jedesign.productivitytimer.R;
import com.jedesign.productivitytimer.TimerClass;

import java.util.ArrayList;

public class AllTimersFragment extends Fragment {


    public AllTimersFragment() {
        // Required empty public constructor
    }

    public static AllTimersFragment newInstance(String param1, String param2) {
        AllTimersFragment fragment = new AllTimersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }
    RecyclerView rvTimers;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_all_timers, container, false);
        rvTimers = v.findViewById(R.id.rvTimers);
        setRVAdapter();
         return v;
    }

    private void setRVAdapter() {
        // Get List of Timers
        DataHelper dh = new DataHelper(getActivity());
        ArrayList<TimerClass> list = dh.getAllTimers();
        dh.close();

        //Set Recycler View and Layout Manager
        RVTimerAdapter adapter = new RVTimerAdapter(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                //if (adapter.getItemCount() <= 0) showNoMeetingsScheduledLayout();
            }
        });

        //if (adapter.getItemCount() <= 0) showNoMeetingsScheduledLayout();

        //Add recycler view stuff
        rvTimers.setLayoutManager(layoutManager);
        rvTimers.setItemAnimator(new DefaultItemAnimator());
        rvTimers.setAdapter(adapter);
    }


}