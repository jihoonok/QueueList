package edu.umd.cs.queuelist.service;


import java.util.List;

import edu.umd.cs.queuelist.model.Student;

public interface StudentService {
    void addStudentToQueue(Student student);
    Student getStudentById(String id);
    List<Student> getAllStudents(String course);
}
