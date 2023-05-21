package com.jedesign.productivitytimer;

import java.util.Calendar;
import androidx.fragment.app.FragmentActivity;

class GenerateData{

   public GenerateData(FragmentActivity activty){

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -1);


      DataHelper dh = new DataHelper(activty);

      long cosc3p97 = dh.addOrFindTask("COSC 3P97");
      long cosc2p95 = dh.addOrFindTask("COSC 2p95");
      long cosc3p03 = dh.addOrFindTask("COSC 3P03");
      long cosc3p71 = dh.addOrFindTask("COSC 3P71");

      long work = dh.addOrFindLocation("Work");
      long school = dh.addOrFindLocation("School");
      long home = dh.addOrFindLocation("Home");

      cal.add(Calendar.DATE, -1);
      dh.addTimer(new TimerClass(cal.getTimeInMillis(), 7654, "", ""),cosc3p97, work);
      dh.addTimer(new TimerClass(cal.getTimeInMillis(), 4562, "", ""),cosc2p95, home);

      cal.add(Calendar.DATE, -1);
      dh.addTimer(new TimerClass(cal.getTimeInMillis(), 8435, "", ""),cosc2p95, home);
      dh.addTimer(new TimerClass(cal.getTimeInMillis(), 9728, "", ""),cosc3p97, home);
      dh.addTimer(new TimerClass(cal.getTimeInMillis(), 4562, "", ""),cosc3p03, school);

      cal.add(Calendar.DATE, -1);
      dh.addTimer(new TimerClass(cal.getTimeInMillis(), 1535, "", ""),cosc2p95, home);
      dh.addTimer(new TimerClass(cal.getTimeInMillis(), 2728, "", ""), cosc3p71, work);
      dh.addTimer(new TimerClass(cal.getTimeInMillis(), 4562, "", ""),cosc3p03, school);

      cal.add(Calendar.DATE, -1);
      dh.addTimer(new TimerClass(cal.getTimeInMillis(), 600, "", ""),cosc3p71, school);
      dh.addTimer(new TimerClass(cal.getTimeInMillis(), 7800, "", ""),cosc3p03, home);
      dh.addTimer(new TimerClass(cal.getTimeInMillis(), 1425, "", ""),cosc2p95, work);

      cal.add(Calendar.DATE, -1);
      dh.addTimer(new TimerClass(cal.getTimeInMillis(), 2312, "", ""),cosc2p95, home);
      dh.addTimer(new TimerClass(cal.getTimeInMillis(), 2355, "", ""), cosc3p71, work);
      dh.addTimer(new TimerClass(cal.getTimeInMillis(), 8241, "", ""),cosc3p03, school);

      cal.add(Calendar.DATE, -1);
      dh.addTimer(new TimerClass(cal.getTimeInMillis(), 2335, "", ""),cosc2p95, home);
      dh.addTimer(new TimerClass(cal.getTimeInMillis(), 4528, "", ""), cosc3p71, work);
      dh.addTimer(new TimerClass(cal.getTimeInMillis(), 12762, "", ""),cosc3p97, school);

      cal.add(Calendar.DATE, -1);
      dh.addTimer(new TimerClass(cal.getTimeInMillis(), 2335, "", ""),cosc2p95, home);
      dh.addTimer(new TimerClass(cal.getTimeInMillis(), 2528, "", ""), cosc3p71, work);
      dh.addTimer(new TimerClass(cal.getTimeInMillis(), 3762, "", ""),cosc3p03, school);

      dh.close();


   }

}
