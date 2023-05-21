package com.jedesign.productivitytimer;

import java.util.Calendar;

public class TimerClass {
   private long startingTime;
   private int duration;
   private String location;
   private String task;

   public TimerClass(long startingTime, int duration, String location, String task){
      this.duration = duration;
      this.location = location;
      this.task = task;
      this.startingTime = startingTime;
   }


   public int getDuration() {
      return duration;
   }

   public long getStartingTime() {
      return startingTime;
   }

   public String getLocation() {
      return location;
   }

   public String getTask() {
      return task;
   }
}
