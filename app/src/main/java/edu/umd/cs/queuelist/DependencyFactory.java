package edu.umd.cs.queuelist;


import android.content.Context;

import edu.umd.cs.queuelist.service.StudentService;
import edu.umd.cs.queuelist.service.impl.InMemoryStudentService;

public class DependencyFactory {
    private static StudentService studentService;

    public static StudentService getStudentService(Context context) {
        if (studentService == null) {
            studentService = new InMemoryStudentService(context);
        }
        return studentService;
    }
}
