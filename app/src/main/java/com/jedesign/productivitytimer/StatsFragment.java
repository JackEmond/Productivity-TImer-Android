package com.jedesign.productivitytimer;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.jedesign.productivitytimer.Stats.AllLocationsFragment;
import com.jedesign.productivitytimer.Stats.AllTasksFragment;
import com.jedesign.productivitytimer.Stats.AllTimersFragment;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.Arrays;
import java.util.HashMap;

public class StatsFragment extends Fragment {

    public StatsFragment() {}

    GraphView graph;
    TextView graphName;
    Spinner spinnerGraph;
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
        spinnerGraph = v.findViewById(R.id.spinnerGraphSelection);
        initializeSpinnerGraph();

        //Set the graph and its name
        graph = (GraphView) v.findViewById(R.id.graph);
        graphName = v.findViewById(R.id.tvGraphTitle);

        initializeButtonClicks(v);

        return v;
    }


    public void openNewFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity()
                .getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, fragment);
        fragmentTransaction.commit();
    }

    private void initializeSpinnerGraph() {
        ArrayAdapter<CharSequence> spinnerAdapter =ArrayAdapter.createFromResource(getActivity(), R.array.DropDownGraph, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGraph.setAdapter(spinnerAdapter);

        spinnerGraph.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String graphText = spinnerGraph.getSelectedItem().toString();
                if(graphText.contains("Productivity by Location")){
                    createTimeInEachLocationGraph();
                }
                else if(graphText.contains("Productivity by Day of the Week")){
                    createTimeProductiveEachDayGraph();
                }
                else if(graphText.contains("Productivity by Task")){
                    createTimeCompletingEachTaskGraph();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private DataPoint[] getDataPoints(HashMap<String, Integer> timeAtEachLocation){
        DataPoint[] arrDataPoints = new DataPoint[timeAtEachLocation.size()+2];
        arrDataPoints[0] = new DataPoint(0,0);
        int j = 1;
        for(Integer duration : timeAtEachLocation.values()) {
            arrDataPoints[j] = new DataPoint(j, duration);
            j++;
        }
        arrDataPoints[j] = new DataPoint(j,0);
        return arrDataPoints;
    }



    private void createTimeCompletingEachTaskGraph(){
        graph.removeAllSeries();

        //Get time at  each location
        DataHelper dh = new DataHelper(getActivity());
        HashMap<String, Integer> timeAtEachLocation = dh.getTimeCompletingEachTask();
        dh.close();

        //Get Data Points
        DataPoint[] arrDataPoints = getDataPoints(timeAtEachLocation);

        //Set List of Locations
        String[] listOfLocations = getListOfLocations(timeAtEachLocation);


        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(listOfLocations);

        //Set GridLabelRenderer
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        glr.setLabelFormatter(staticLabelsFormatter);

        //This is Different
        glr.setHorizontalAxisTitle("Task");
        glr.setVerticalAxisTitle("Minutes Timer Ran");
        graphName.setText("Productivity During Each Task");

        // set Viewport
        setViewPort(graph, timeAtEachLocation.values().toArray(new Integer[0]));

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(arrDataPoints);
        series.setAnimated(true);
        //Change Bars to Black
        setSeriesColour(series);
        series.setSpacing(5);

        graph.addSeries(series);

    }

    private void setSeriesColour(BarGraphSeries<DataPoint> series) {
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {return Color.rgb(0, 0, 0);}
        });

    }

    private void setViewPort(GraphView graph, Integer[] dataPoints) {
        Viewport vp = graph.getViewport();
        vp.setYAxisBoundsManual(true);
        vp.setMinY(0); vp.setMinX(0);
        vp.setMaxY(getMaxYValue(dataPoints));
    }

    private double getMaxYValue(Integer[] dataPoints) {
        int maxYValue = 0 ;
        for(int i=0; i< dataPoints.length; i++){ //For Each Day
            int timeSpentWorking = dataPoints[i];  // Time spent working that day
            if(maxYValue < timeSpentWorking) maxYValue = timeSpentWorking;
        }
        return maxYValue + 10;
    }

    private String[] getListOfLocations(HashMap<String, Integer> timeAtEachLocation) {
        String[] listOfLocations = new String[timeAtEachLocation.size()+2];
        listOfLocations[0] = "";
        int j=1;
        for(String location : timeAtEachLocation.keySet()){
            listOfLocations[j++] = location;
        }
        listOfLocations[j] = "";
        return listOfLocations;
    }

    private void createTimeInEachLocationGraph() {
        graph.removeAllSeries();
        DataHelper dh = new DataHelper(getActivity());
        HashMap<String, Integer> timeAtEachLocation = dh.getTimeAtEachLocation();
        dh.close();

        //Get Data Points
        DataPoint[] arrDataPoints = getDataPoints(timeAtEachLocation);

        //Set List of Locations
        String[] listOfLocations = getListOfLocations(timeAtEachLocation);


        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(arrDataPoints);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(listOfLocations);

        //Set GridLabelRenderer
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        glr.setLabelFormatter(staticLabelsFormatter);
        glr.setHorizontalAxisTitle("Locations");
        glr.setVerticalAxisTitle("Minutes Timer Ran");
        graphName.setText("Productivity at Each Location");


        // set Viewport
        setViewPort(graph, timeAtEachLocation.values().toArray(new Integer[0]));

        series.setAnimated(true);

        //Change Bars to Black
        setSeriesColour(series);


        series.setSpacing(5);
        graph.addSeries(series);
    }



    private void createTimeProductiveEachDayGraph() {
        graph.removeAllSeries();
        //Get Total time spent working each day from database
        // [0] = Sunday, [6] = Saturday
        DataHelper dh = new DataHelper(getActivity());
        int[] timeSpentWorkingEachDay = dh.getAverageTimeSpentWorkingEachDay();
        dh.close();

        //Set each day as the datapoint
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0), // First and Last have to be empty so the graph displays correctly
                new DataPoint(1, timeSpentWorkingEachDay[0]),
                new DataPoint(2, timeSpentWorkingEachDay[1]),
                new DataPoint(3, timeSpentWorkingEachDay[2]),
                new DataPoint(4, timeSpentWorkingEachDay[3]),
                new DataPoint(5, timeSpentWorkingEachDay[4]),
                new DataPoint(6, timeSpentWorkingEachDay[5]),
                new DataPoint(7, timeSpentWorkingEachDay[6]),
                new DataPoint(8, 0), // First and Last have to be empty so the graph displays correctly

        });
        String[] daysOfWeekArray = new String[] {"","S", "M", "T", "W", "T", "F", "S",""};
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(daysOfWeekArray);

        //Set GridLabelRenderer
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        glr.setLabelFormatter(staticLabelsFormatter);
        glr.setHorizontalAxisTitle("Day of Week");
        glr.setVerticalAxisTitle("Minutes Timer Ran");

        series.setAnimated(true);
        graphName.setText("Time Productive Each Day");

        // set Viewport
        setViewPort(graph, convertIntArrayToIntegerArray(timeSpentWorkingEachDay));

        //Change Bars to Black
        setSeriesColour(series);
        series.setSpacing(15);
        graph.addSeries(series);
    }

    private Integer[] convertIntArrayToIntegerArray(int[] intArray) {
        return Arrays.stream(intArray).boxed().toArray( Integer[]::new );
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