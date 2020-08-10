package HealthCare;

//Each fognode can process 5 request at a time and will take 3 secs for each request to process

import java.util.Calendar;
import java.util.GregorianCalendar;

public class FOGNODE {
    int fogid;
    int latitude;
    int longitude;
    Calendar startTime[];
    Calendar endTime[];
    String conditions[];
    boolean flag;
    int reqCounter;
    int totalRequest=0;


    public FOGNODE(int fogid, int latitude, int longitude){
        this.fogid = fogid;
        this.latitude = latitude;
        this.longitude = longitude;
        startTime = new GregorianCalendar[5];
        endTime = new GregorianCalendar[5];
        conditions = new String[5];
        flag = false;
        reqCounter = -1;
    }

    public boolean setReq(String condition){
        if(reqCounter == 4){
            return false;
        }
        else {
            reqCounter++;
            totalRequest++;
            startTime[reqCounter] = new GregorianCalendar();
            endTime[reqCounter] = startTime[reqCounter];
            endTime[reqCounter].set(Calendar.SECOND, endTime[reqCounter].get(Calendar.SECOND) + 3);
            conditions[reqCounter] = condition;
            return true;
        }
    }

    public Calendar getEndTime(int reqCounter){
        return endTime[reqCounter];
    }

    public Calendar getStartTime(int reqCounter){
        return startTime[reqCounter];
    }

    public boolean status(){
        return this.flag;
    }
}
