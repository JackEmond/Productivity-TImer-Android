package com.jedesign.productivitytimer.StatsFragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.jedesign.productivitytimer.R;
import com.jedesign.productivitytimer.StatsFragment.Stats.AllLocationsFragment;
import com.jedesign.productivitytimer.StatsFragment.Stats.AllTasksFragment;
import com.jedesign.productivitytimer.StatsFragment.Stats.AllTimersFragment;
import com.jedesign.productivitytimer.StatsFragment.Stats.GraphSelector;
import com.jedesign.productivitytimer.TimerFragment.TimerFragment;
import com.jjoe64.graphview.GraphView;

public class StatsFragment extends Fragment {

    public StatsFragment() {}

    GraphView graph;
    TextView graphName;
    Spinner dropDownGraphSelector;
    Button btnAllTimers, btnNewTimer, btnAllLocations, btnAlTasks, btnGenerateData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_stats, container, false);

        //Set Graph Selector
        dropDownGraphSelector = v.findViewById(R.id.spinnerGraphSelection);
        initializedDropDownGraphSelector();

        //Set the graph and its name
        graphName = v.findViewById(R.id.tvGraphTitle);
        graph = v.findViewById(R.id.graph);
        initializeButtonClicks(v);

        return v;
    }


    public void openNewFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity()
                .getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, fragment);
        fragmentTransaction.commit();
    }



    private void initializedDropDownGraphSelector() {
        ArrayAdapter<CharSequence> spinnerAdapter =ArrayAdapter.createFromResource(getActivity(), R.array.DropDownGraph, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDownGraphSelector.setAdapter(spinnerAdapter);

        dropDownGraphSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String graphText = dropDownGraphSelector.getSelectedItem().toString();
                if(graphText.contains("Productivity by Location")){
                    GraphSelector.createTimeInEachLocationGraph(graph, getActivity());
                    graphName.setText("Productivity at Each Location");

                }
                else if(graphText.contains("Productivity by Day of the Week")){
                    GraphSelector.createTimeProductiveEachDayGraph(graph, getActivity());
                    graphName.setText("Time Productive Each Day");

                }
                else if(graphText.contains("Productivity by Task")){
                    graph.removeAllSeries();
                    GraphSelector.createTimeCompletingEachTaskGraph(graph, getActivity());
                    graphName.setText("Productivity During Each Task");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }




    private void initializeButtonClicks(View v) {

        btnAllTimers = v.findViewById(R.id.btnAllTimers);
        initializeClick(btnAllTimers, new AllTimersFragment());

        btnNewTimer = v.findViewById(R.id.btnNewTimer);
        initializeClick(btnNewTimer, new TimerFragment());


        btnAllLocations = v.findViewById(R.id.btnAllLocations);
        initializeClick(btnAllLocations, new AllLocationsFragment());

        btnAlTasks = v.findViewById(R.id.btnAllTasks);
        initializeClick(btnAlTasks, new AllTasksFragment());

        //Generate Data Button for testing purposes
        //btnGenerateData = v.findViewById(R.id.btnGenerateData);
        //initializeBtnGenerateDataClick();
    }

    private void initializeClick(Button btnInitializing, Fragment fragment) {
        btnInitializing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openNewFragment(fragment);}
        });
    }

    private void initializeBtnGenerateDataClick() {
        btnGenerateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GenerateData(getActivity());
                refreshFragment();
            }
        });
    }

    private void refreshFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().detach(this).commit();
        //getActivity().getSupportFragmentManager().beginTransaction().attach(this).commit();
    }

}