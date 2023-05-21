package com.jedesign.productivitytimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;


public class DataHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DB_NAME = "timer";

    public static final String TABLE_TIMER = "timers";
    public static final String TIMER_START_TIME = "startTime";
    public static final String TIMER_DURATION = "duration";
    public static final String TIMER_LOCATION_ID = "locaitonId";
    public static final String TIMER_TASK_ID = "taskId";
    public static final String TIMER_ID = "id";

    public static final String TABLE_LOCATIONS = "locations";
    private static final String LOCATION_NAME = "location";
    private static final String LOCATION_ID = "id";
    private static final String LOCATION_X_COORDINATE = "x";
    private static final String LOCATION_Y_COORDINATE = "y";

    public static final String TABLE_TASKS = "task";
    private static final String TASK_NAME = "name";
    private static final String TASK_ID = "id";

    public DataHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    // SELECT startTime, duration, location, name FROM timers INNER JOIN locations on timers.locationId = locations.id
    // "SELECT TIMER_START_TIME, TIMER_DURATION, LOCATION_NAME, TASK_NAME FROM TABLE_TIMER"


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_TIMER = "CREATE TABLE " + TABLE_TIMER + "(" +
                TIMER_ID + " INTEGER PRIMARY KEY, " +
                TIMER_LOCATION_ID + " INTEGER REFERENCES " + TABLE_LOCATIONS +"," +
                TIMER_TASK_ID + " INTEGER REFERENCES " + TABLE_LOCATIONS +"," +
                TIMER_START_TIME +" INTEGER," +
                TIMER_DURATION +" INTEGER" +
                ")";

        String CREATE_TABLE_LOCATIONS = "CREATE TABLE " + TABLE_LOCATIONS +
                " (" + LOCATION_ID + " INTEGER PRIMARY KEY, " +
                LOCATION_NAME + " TEXT" +
                ")";

        String CREATE_TABLE_TASKS = "CREATE TABLE " + TABLE_TASKS +
                " (" + TASK_ID + " INTEGER PRIMARY KEY, " +
                TASK_NAME + " TEXT" +
                ")";

        String ALL_MEETINGS_QUERY = "SELECT " +
                "location, date, meetings.id " +
                "FROM MEETINGS LEFT OUTER JOIN LOCATIONS ON meetings.locationId = locations.id " +
                "ORDER BY date ASC";
        db.execSQL(CREATE_TABLE_TIMER);
        db.execSQL(CREATE_TABLE_LOCATIONS);
        db.execSQL(CREATE_TABLE_TASKS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {        //How to migrate or reconstruct data from old version to new on upgrade    } }
    }

    public long addOrFindTask(String task) {
        SQLiteDatabase db = getWritableDatabase();

        //Determine if the location already exists
        String FIND_MATCHING_TASK_QUERY = "SELECT " + TASK_ID
                + " FROM " + TABLE_TASKS + " WHERE " + TASK_NAME + "= '" + task +"'";

        Cursor cursor = db.rawQuery(FIND_MATCHING_TASK_QUERY, null);
        //Item already exists return the id
        if(cursor.moveToFirst()){ return cursor.getLong(0);}

        //Item does not already exist. Insert into database and return the id
        ContentValues cvTask =new ContentValues();
        cvTask.put(TASK_NAME, task);
        long id = db.insert(DataHelper.TABLE_TASKS,null, cvTask);
        db.close();
        return id;
    }

    public long addOrFindLocation(String location) {
            SQLiteDatabase db = getWritableDatabase();

            //Determine if the location already exists
            String FIND_MATCHING_LOCATION_QUERY = "SELECT " + LOCATION_ID
                    + " FROM " + TABLE_LOCATIONS + " WHERE " + LOCATION_NAME + "= '" + location +"'";

            Cursor cursor = db.rawQuery(FIND_MATCHING_LOCATION_QUERY, null);
            //Item already exists return the id
            if(cursor.moveToFirst()){return cursor.getLong(0);}

            //Item does not already exist. Insert into database and return the id
            ContentValues cvLocation =new ContentValues();
            cvLocation.put(LOCATION_NAME, location);
            long id = db.insert(DataHelper.TABLE_LOCATIONS,null, cvLocation);
            db.close();
            return id;
    }

    public void openDatabase(){
        SQLiteDatabase db = getWritableDatabase();

    }
    public void addTimer(TimerClass timer, long taskId, long locationId) {
        //Get Content values to be inserted into database
        ContentValues cv =new ContentValues();
        cv.put(TIMER_DURATION, timer.getDuration());
        cv.put(TIMER_LOCATION_ID, locationId);
        cv.put(TIMER_TASK_ID, taskId);
        cv.put(TIMER_START_TIME, timer.getStartingTime());

        //Insert Data into Database
        SQLiteDatabase db = getWritableDatabase();
        db.insert(DataHelper.TABLE_TIMER,null, cv);
        db.close();
    }


    public ArrayList<TimerClass> getAllTimers() {
        ArrayList<TimerClass> timers = new ArrayList<>();

        String ALL_TIMERS_QUERY =
                " SELECT startTime, duration, location ,name FROM timers " +
                        "INNER JOIN locations on timers.locaitonId = locations.id " +
                        "INNER JOIN task on  timers.taskId = task.id " +
                        "ORDER BY startTime DESC";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ALL_TIMERS_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    long startTime = cursor.getLong(0);
                    int duration = cursor.getInt(1);
                    String location = cursor.getString(2);
                    String task = cursor.getString(3);
                    TimerClass timer = new TimerClass(startTime, duration, location, task);
                    timers.add(timer);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get timers from database in getAllTimers()");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return timers;
    }

    public int[] getAverageTimeSpentWorkingEachDay() {
            int[] timeSpentWorkingEachDay = new int[7];


            String EACH_DATE_QUERY =
                    " SELECT startTime, duration FROM timers " +
                            "ORDER BY startTime DESC";

            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(EACH_DATE_QUERY, null);

            try {
                if (cursor.moveToFirst()) {
                    do {
                        long startTime = cursor.getLong(0);
                        int durationInMinutes = cursor.getInt(1) / 60;

                        Calendar cal = new GregorianCalendar();
                        cal.setTimeInMillis(startTime);
                        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                        timeSpentWorkingEachDay[dayOfWeek-1] += durationInMinutes;
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.d(TAG, "Error while trying to get info from database in getAverageTimeSpentWorkingEachDay()");
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            return timeSpentWorkingEachDay;

    }

    public List<String> getAllTasks() {
        List<String> tasks = new ArrayList<>();
        String ALL_TASKS_QUERY = "SELECT " + TASK_NAME + " FROM " + TABLE_TASKS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ALL_TASKS_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String task = cursor.getString(0);
                    if(!task.equals("")) tasks.add(task);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get tasks from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return tasks;
    }


    public HashMap<String, Integer> getTimeAtEachLocation() {
        HashMap<String, Integer> map = new HashMap<>();

        String EACH_DATE_QUERY =
                " SELECT duration, location FROM timers " +
                        "INNER JOIN locations on timers.locaitonId = locations.id ";


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(EACH_DATE_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {

                    int durationInMinutes = cursor.getInt(0) / 60;
                    String location = cursor.getString(1);
                    if(!location.equals("")){
                        int count = map.containsKey(location) ? map.get(location) : 0;
                        map.put(location, count + durationInMinutes);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get info from database in getAverageTimeSpentWorkingEachDay()");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return map;

    }

    public List<String> getAllLocations() {
        List<String> locations = new ArrayList<>();
        String ALL_LOCATIONS_QUERY = "SELECT " + LOCATION_NAME + " FROM " + TABLE_LOCATIONS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ALL_LOCATIONS_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String location = cursor.getString(0);
                    if(!location.equals(""))  locations.add(location);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get locations from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return locations;
    }

    public boolean deleteLocation(String location) {
        SQLiteDatabase db = getReadableDatabase();
        String IS_LOCATION_IN_TIMER_QUERY = "SELECT count(*) FROM "+ TABLE_TIMER+
                " LEFT OUTER JOIN locations ON timers.locaitonId = locations.id WHERE location = '" + location + "'";
        Cursor c = db.rawQuery(IS_LOCATION_IN_TIMER_QUERY, null);
        c.moveToFirst();
        boolean locationIsInMeeting = (c.getInt(0)>0)? true: false;
        if(locationIsInMeeting) return false;  // Dont Delete the location since their is already a existing meeting

        //No meetings use the location so it is safe to delete the location
        String whereArgs[] ={location};
        db.delete(DataHelper.TABLE_LOCATIONS, "location=?" , whereArgs);
        db.close();
        return true;
    }

    public boolean DeleteTask(String task) {
        SQLiteDatabase db = getReadableDatabase();
        String IS_TASK_IN_TIMER_QUERY = "SELECT count(*) FROM "+ TABLE_TIMER+
                " LEFT OUTER JOIN task ON timers.taskId = task.id WHERE task.name = '" + task + "'";
        Cursor c = db.rawQuery(IS_TASK_IN_TIMER_QUERY, null);
        c.moveToFirst();
        boolean taskIsInTimer = (c.getInt(0)>0)? true: false;
        if(taskIsInTimer) return false;  // Dont Delete the location since their is already a existing meeting

        //No meetings use the location so it is safe to delete the location
        String whereArgs[] ={task};
        db.delete(DataHelper.TABLE_LOCATIONS, "location=?" , whereArgs);
        db.close();
        return true;
    }

    public HashMap<String, Integer> getTimeCompletingEachTask() {
        HashMap<String, Integer> map = new HashMap<>();

        String EACH_DATE_QUERY =
                " SELECT duration, task.name FROM timers " +
                        "INNER JOIN task on timers.taskId = task.id ";


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(EACH_DATE_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {

                    int durationInMinutes = cursor.getInt(0) / 60;
                    String location = cursor.getString(1);
                    if(!location.equals("")){ // If the location is not empty than the count either the time completing the task gets added to the map or a new location is added.
                        int count = map.containsKey(location) ? map.get(location) : 0;
                        map.put(location, count + durationInMinutes);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get info from database in getAverageTimeSpentWorkingEachDay()");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return map;

    }

    public void DeleteTimer(long startingTime) {
        SQLiteDatabase db = getReadableDatabase();
        Log.w("BROSKI", startingTime + "");
        String whereArgs[] ={startingTime + ""};
        db.delete(DataHelper.TABLE_TIMER, "startTime=?" , whereArgs);
        db.close();
    }
}


