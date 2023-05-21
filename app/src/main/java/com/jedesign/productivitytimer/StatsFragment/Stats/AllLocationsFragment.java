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
import java.util.List;

public class AllLocationsFragment extends Fragment {


    public AllLocationsFragment() {
        // Required empty public constructor
    }

    public static AllLocationsFragment newInstance(String param1, String param2) {
        AllLocationsFragment fragment = new AllLocationsFragment();
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
        View v = inflater.inflate(R.layout.fragment_all_locations, container, false);
        recyclerView = v.findViewById(R.id.rvLocations);
        setRVAdapter();
        return v;
    }

    private void setRVAdapter() {
        DataHelper dh = new DataHelper(getActivity());
        List<String> list = dh.getAllLocations();
        RecyclerViewBasic adapter = new RecyclerViewBasic(list, "Location");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }
}