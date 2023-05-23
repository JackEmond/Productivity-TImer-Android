package com.jedesign.productivitytimer.StatsFragment.Stats;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.AdapterView;

import com.jedesign.productivitytimer.DataHelper;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Arrays;
import java.util.HashMap;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class GraphSelector {
    //GitHub for GraphView Library
    //https://github.com/jjoe64/GraphView

    public static void createTimeCompletingEachTaskGraph(GraphView graph, FragmentActivity activity){
        //Get time at  each location
        DataHelper dh = new DataHelper(activity);
        HashMap<String, Integer> timeAtEachLocation = dh.getTimeCompletingEachTask();
        dh.close();

        //Get Data Points
        DataPoint[] arrDataPoints = getDataPoints(timeAtEachLocation);

        //Set List of Locations
        String[] listOfLocations = getListOfLocations(timeAtEachLocation);
        Integer[] xAxisValues =timeAtEachLocation.values().toArray(new Integer[0]);
        String horizontalAxisTitle = "Task";
        String verticalAxisTitle = "Minutes Timer Ran";

        createGraph(graph, arrDataPoints, xAxisValues, listOfLocations,  horizontalAxisTitle, verticalAxisTitle);
    }


    public static void createTimeProductiveEachDayGraph(GraphView graph, FragmentActivity activity) {
        graph.removeAllSeries();
        //Get Total time spent working each day from database
        // [0] = Sunday, [6] = Saturday
        DataHelper dh = new DataHelper(activity);
        int[] timeSpentWorkingEachDay = dh.getAverageTimeSpentWorkingEachDay();
        dh.close();

        DataPoint[] dataPoints = new DataPoint[] {
                new DataPoint(0, 0), // First and Last have to be empty so the graph displays correctly
                new DataPoint(1, timeSpentWorkingEachDay[0]),
                new DataPoint(2, timeSpentWorkingEachDay[1]),
                new DataPoint(3, timeSpentWorkingEachDay[2]),
                new DataPoint(4, timeSpentWorkingEachDay[3]),
                new DataPoint(5, timeSpentWorkingEachDay[4]),
                new DataPoint(6, timeSpentWorkingEachDay[5]),
                new DataPoint(7, timeSpentWorkingEachDay[6]),
                new DataPoint(8, 0), // First and Last have to be empty so the graph displays correctly

        };
        String[] daysOfWeekArray = new String[] {"","S", "M", "T", "W", "T", "F", "S",""};
        Integer[] xAxisValues = convertIntArrayToIntegerArray(timeSpentWorkingEachDay);

        //Set List of Locations
        String horizontalAxisTitle = "Day of Week";
        String verticalAxisTitle = "Minutes Timer Ran";

        createGraph(graph, dataPoints, xAxisValues, daysOfWeekArray,  horizontalAxisTitle, verticalAxisTitle);
    }


    public static void createTimeInEachLocationGraph(GraphView graph, FragmentActivity activity) {
        graph.removeAllSeries();
        DataHelper dh = new DataHelper(activity);
        HashMap<String, Integer> timeAtEachLocation = dh.getTimeAtEachLocation();
        dh.close();

        //Get Data Points
        DataPoint[] arrDataPoints = getDataPoints(timeAtEachLocation);
        Integer[] xAxisValues =timeAtEachLocation.values().toArray(new Integer[0]);

        //Set List of Locations
        String[] listOfLocations = getListOfLocations(timeAtEachLocation);
        String horizontalAxisTitle = "Locations";
        String verticalAxisTitle = "Minutes Timer Ran";

        createGraph(graph, arrDataPoints, xAxisValues, listOfLocations,  horizontalAxisTitle, verticalAxisTitle);
    }


    private static Integer[] convertIntArrayToIntegerArray(int[] intArray) {
        return Arrays.stream(intArray).boxed().toArray( Integer[]::new );
    }



    private static DataPoint[] getDataPoints(HashMap<String, Integer> timeAtEachLocation){
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

    private static String[] getListOfLocations(HashMap<String, Integer> timeAtEachLocation) {
        String[] listOfLocations = new String[timeAtEachLocation.size()+2];
        listOfLocations[0] = "";
        int j=1;
        for(String location : timeAtEachLocation.keySet()){
            listOfLocations[j++] = location;
        }
        listOfLocations[j] = "";
        return listOfLocations;
    }


    private static void createGraph(GraphView graph, DataPoint[] arrDataPoints, Integer[] xAxisValues, String[] listOfLocations, String horizontalAxisTitle, String verticalAxisTitle) {
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(listOfLocations);

        //Set GridLabelRenderer
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        glr.setLabelFormatter(staticLabelsFormatter);
        glr.setHorizontalAxisTitle(horizontalAxisTitle);
        glr.setVerticalAxisTitle(verticalAxisTitle);


        ////////Series Data/////////
        // set Viewport
        setViewPort(graph, xAxisValues);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(arrDataPoints);
        series.setAnimated(true);
        //Change Bars to Black
        setSeriesColour(series);
        series.setSpacing(5);

        graph.addSeries(series);
    }

    private static void setSeriesColour(BarGraphSeries<DataPoint> series) {
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {return Color.rgb(0, 0, 0);}
        });

    }

    private static void setViewPort(GraphView graph, Integer[] dataPoints) {
        Viewport vp = graph.getViewport();
        vp.setYAxisBoundsManual(true);
        vp.setMinY(0); vp.setMinX(0);
        vp.setMaxY(getMaxYValue(dataPoints));
    }

    private static double getMaxYValue(Integer[] dataPoints) {
        int maxYValue = 0 ;
        for(int i=0; i< dataPoints.length; i++){ //For Each Day
            int timeSpentWorking = dataPoints[i];  // Time spent working that day
            if(maxYValue < timeSpentWorking) maxYValue = timeSpentWorking;
        }
        return maxYValue + 10;
    }


}
