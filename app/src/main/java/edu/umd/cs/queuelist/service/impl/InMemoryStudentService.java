package edu.umd.cs.queuelist.service.impl;


import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.umd.cs.queuelist.model.Student;
import edu.umd.cs.queuelist.service.StudentService;

public class InMemoryStudentService implements StudentService {
    private Context context;
    private List<Student> students;

    public InMemoryStudentService(Context context) {
        this.context = context;
        this.students = new ArrayList<Student>();
    }

    public void addStudentToQueue(Student student) {
        Student currStudent = getStudentById(student.getUserId());
        if (currStudent == null) {
            students.add(student);
        } else {
            currStudent.setName(student.getName());
            currStudent.setUserId(student.getUserId());
            currStudent.setProblem(student.getProblem());
            currStudent.setClassCode(student.getClassCodePosition());
            currStudent.setAssignment(student.getAssignmentPosition());
        }
    }

    public Student getStudentById(String id) {
        for (Student student : students) {
            if (student.getUserId().equals(id)) {
                return student;
            }
        }

        return null;
    }

    public List<Student> getAllStudents() {
        List<Student> prioritizedStudents = new ArrayList<Student>(students);

        Collections.sort(prioritizedStudents, new Comparator<Student>() {
            @Override
            public int compare(Student student1, Student student2) {
                return student1.getTimeCreated().compareTo(student2.getTimeCreated());
            }
        });

        return prioritizedStudents;
    }

}
