package edu.umd.cs.queuelist.service.impl;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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
        students = new ArrayList<Student>();
        new getStudent().execute();
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return students;
    }

    private class getStudent extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("Debug", "About to begin");
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            String response = "";

            try {
                url = new URL("http://www.taterpqueue.xyz/StudentList.php");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setDoInput(true);

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
                else {
                    response="No way";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d("Debug", "List");
            if (!response.equalsIgnoreCase("0 results")) {
                putStudent(response);
            }
            return response;

        }


        @Override
        protected void onPostExecute(String result) {
            if(result.equalsIgnoreCase("Query Error")){
                Log.d("Debug", "onPostExecute: U");
            }else{
                Log.d("Debug", result);
                Log.d("Debug", "onPostExecute: S");
            }

        }

        protected void putStudent(String result){
            String[] words = result.split("&");
            Log.d("Debug", words.length+"");
            int track = 0;
            List<Student> tempList = new ArrayList<Student>();
            String studentName = "", assignment = "", problem = "";
            for (String word : words) {
                Student student = new Student();
                String[] stuff = word.split(",");

                studentName = stuff[0];
                assignment = stuff[1];
                problem = stuff[2];

                if (assignment.equalsIgnoreCase("Project 1"))
                    student.setAssignment(1);
                else if (assignment.equalsIgnoreCase("Project 2"))
                    student.setAssignment(2);
                else if (assignment.equalsIgnoreCase("Project 3"))
                    student.setAssignment(3);
                student.setName(studentName);
                student.setProblem(problem);
                student.setUserId("dennis");
                tempList.add(student);
            }

            students = tempList;
            Log.d("Debug", students.toString());
        }


    }

}
