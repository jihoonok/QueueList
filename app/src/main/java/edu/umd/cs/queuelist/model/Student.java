package edu.umd.cs.queuelist.model;

/**
 * Created by khanhnguyen on 3/22/17.
 */

import java.io.Serializable;
import java.util.Date;

public class Student implements Serializable {
    private String userid;
    private String name;
    private String problem;
    private ClassCode classCode = ClassCode.NONE;
    private Assignment assignment = Assignment.NONE;
    private Date timeCreated;

    public Student() {
        timeCreated = new Date();
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

    public Assignment getAssignment() {
        return assignment;
    }

    public int getAssignmentPosition() {
        switch (assignment) {
            case NONE:
                return 0;
            case PROJECT1:
                return 1;
            case PROJECT2:
                return 2;
            case PROJECT3:
                return 3;
            default:
                return 0;
        }
    }

    public void setAssignment(int position) {
        switch (position) {
            case 0:
                this.assignment = Assignment.NONE;
                break;
            case 1:
                this.assignment = Assignment.PROJECT1;
                break;
            case 2:
                this.assignment = Assignment.PROJECT2;
                break;
            case 3:
                this.assignment = Assignment.PROJECT3;
                break;
            default:
                this.assignment = Assignment.NONE;
                break;
        }
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    @Override
    public String toString() {
        int displayLength = 10;
        return name.substring(0, name.length() >= displayLength ? displayLength : name.length()) +
                " : " + userid + " : " + classCode + " : " + assignment;
    }

    public enum ClassCode {
        NONE, CMSC131, CMSC132, CMSC216;
    }

    public enum Assignment {
        NONE, PROJECT1, PROJECT2, PROJECT3;
    }
}