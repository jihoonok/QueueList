package edu.umd.cs.queuelist.model;

/**
 * Created by khanhnguyen on 3/22/17.
 */

import android.util.Log;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Student implements Serializable {
    private String userid;
    private String name;
    private String problem;
    private ClassCode classCode = ClassCode.NONE;
    private String assignment = "Question";
    private String timeCreated;

    public Student() {

    }

    public String getUserId() {
        return userid;
    }

    public void setUserId(String id) {
        this.userid = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public ClassCode getClassCode() {
        return classCode;
    }

    public int getClassCodePosition() {
        switch (classCode) {
            case NONE:
                return 0;
            case CMSC131:
                return 1;
            case CMSC132:
                return 2;
            case CMSC216:
                return 3;
            default:
                return 0;
        }
    }

    public void setClassCode(int position) {
        switch (position) {
            case 0:
                this.classCode = ClassCode.NONE;
                break;
            case 1:
                this.classCode = ClassCode.CMSC131;
                break;
            case 2:
                this.classCode = ClassCode.CMSC132;
                break;
            case 3:
                this.classCode = ClassCode.CMSC216;
                break;
            default:
                this.classCode = ClassCode.NONE;
                break;
        }
    }

    public String getAssignment() {
        return assignment;
    }


    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public void setTimeCreated(String date) {

        /*String[] dateTime;
        String year,month,day,hour,minute,seconds;
        String[] date2;
        String[] time;
        dateTime = date.split(" ");
        date2 = dateTime[0].split("-");
        time = dateTime[1].split(":");

        Timestamp tm = new Timestamp(Integer.parseInt(date2[0]), Integer.parseInt(date2[1]), Integer.parseInt(date2[2]),
                Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]), 00);*/

        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date23=null;

        try {
            date23 = utcFormat.parse(date);
        }catch(Exception e){
            Log.d("Debug", "Time error");
        }
        DateFormat estFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        estFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));

        Log.d("Debug", estFormat.format(date23));

        this.timeCreated = estFormat.format(date23) + " EST";


    }

    @Override
    public String toString() {
        int displayLength = 10;
        return "Assignment: " + assignment + "\n"
                + "Inserted in Queue: " + timeCreated + "\n"
                + "Directory ID: " + userid;
    }

    public enum ClassCode {
        NONE, CMSC131, CMSC132, CMSC216;
    }

    public enum Assignment {
        NONE, PROJECT1, PROJECT2, PROJECT3;
    }
}