package com.jedesign.productivitytimer.Stats;

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

import java.util.List;

public class AllTasksFragment extends Fragment {

    public AllTasksFragment() {
        // Required empty public constructor
    }

    public static AllTasksFragment newInstance(String param1, String param2) {
        AllTasksFragment fragment = new AllTasksFragment();
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

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_tasks, container, false);
        recyclerView = v.findViewById(R.id.rvTasks);
        setRVAdapter();
        return v;
    }

    private void setRVAdapter() {
        DataHelper dh = new DataHelper(getActivity());
        List<String> list = dh.getAllTasks();
        RecyclerViewBasic adapter = new RecyclerViewBasic(list, "Task");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }
}