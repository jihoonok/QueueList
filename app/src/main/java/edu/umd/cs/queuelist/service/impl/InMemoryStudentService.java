package edu.umd.cs.queuelist.service.impl;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            currStudent.setAssignment(student.getAssignment());
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

    public List<Student> getAllStudents(String course) {
        students = new ArrayList<Student>();
        new getStudent().execute(course);
        try {
            Thread.sleep(1500);
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
            HashMap<String,String> postDataParams = new HashMap<String,String>();
            postDataParams.put("course", params[0]);

            try {
                url = new URL("http://www.taterpqueue.xyz/StudentList.php");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Log.d("Debug", ""+ conn);
                conn.setReadTimeout(25000);
                conn.setConnectTimeout(25000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                Log.d("Debug", ""+responseCode);
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
                Log.d("Debug", e.getMessage());
                e.printStackTrace();
            }

            Log.d("Debug", "List");
            if (!response.equalsIgnoreCase("0 results")) {
                putStudent(response);
            }
            return response;

        }

        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
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

                student.setAssignment(assignment);
                student.setName(studentName);
                student.setProblem(problem);
                student.setUserId("N\\A");
                tempList.add(student);
            }

            students = tempList;
            Log.d("Debug", students.toString());
        }


    }

}
