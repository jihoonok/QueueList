package edu.umd.cs.queuelist.service;


import java.util.List;

import edu.umd.cs.queuelist.model.Student;

public interface StudentService {
    public void addStudentToQueue(Student student);
    public Student getStudentById(String id);
    public List<Student> getAllStudents(String course);
}
