package edu.umd.cs.queuelist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import edu.umd.cs.queuelist.model.Student;
import edu.umd.cs.queuelist.service.StudentService;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by jihoonok on 3/21/17.
 */

public class StudentNameFragment extends android.support.v4.app.Fragment {

    static final String EXTRA_STUDENT_CREATED = "EXTRA_STUDENT_CREATED";
    private Button checkInButton, cancelButton;
    private View view;
    private Student student;
    private Spinner classSpinner, assignmentSpinner;
    private EditText name, userid, problem;
    private StudentService stuser;
    private String course;
    private String assignment;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static StudentNameFragment newInstance() {
        StudentNameFragment fragment = new StudentNameFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = inflater.inflate(R.layout.fragment_studentname, container, false);

        stuser = DependencyFactory.getStudentService(getActivity().getApplicationContext());
        classSpinner = (Spinner)view.findViewById(R.id.spinner_class);
        assignmentSpinner = (Spinner)view.findViewById(R.id.spinner_assignment);
        name = (EditText)view.findViewById(R.id.student_name);
        userid = (EditText)view.findViewById(R.id.user_id);
        problem = (EditText)view.findViewById(R.id.problem);
        checkInButton = (Button)view.findViewById(R.id.check_in_button);
        cancelButton = (Button)view.findViewById(R.id.cancel_button);
        ArrayAdapter<CharSequence> classAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.class_array, android.R.layout.simple_spinner_item);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(classAdapter);
        ArrayAdapter<CharSequence> assignmentAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.assignment_array, android.R.layout.simple_spinner_item);
        assignmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assignmentSpinner.setAdapter(assignmentAdapter);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view1) {
                Intent intent1 = getActivity().getIntent();
                getActivity().setResult(RESULT_CANCELED, intent1);
                getActivity().finish();
            }
        });

        checkInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view1) {
                Intent intent2 = getActivity().getIntent();
                String name_string = name.getText().toString();
                String userid_string = userid.getText().toString();
                String problem_string = problem.getText().toString();
                student = new Student();
                student.setName(name_string);
                student.setUserId(userid_string);
                student.setProblem(problem_string);
                student.setClassCode(classSpinner.getSelectedItemPosition());
                student.setAssignment(assignmentSpinner.getSelectedItemPosition());

                switch (classSpinner.getSelectedItemPosition()) {
                    case 0:
                        course = "None";
                        break;
                    case 1:
                        course = "cmsc131";
                        break;
                    case 2:
                        course = "cmsc132";
                        break;
                    case 3:
                        course = "cmsc216";
                        break;
                    default:
                        break;
                }

                switch (assignmentSpinner.getSelectedItemPosition()) {
                    case 0:
                        assignment = "None";
                        break;
                    case 1:
                        assignment = "Project 1";
                        break;
                    case 2:
                        assignment = "Project 2";
                        break;
                    case 3:
                        assignment = "Project 3";
                        break;
                    default:
                        break;
                }


                new insertStudent().execute(name_string, course, assignment, problem_string);
                //Log.d(TAG, aStory.getId());
                intent2.putExtra(EXTRA_STUDENT_CREATED, student);
                getActivity().setResult(RESULT_OK, intent2);
                getActivity().finish();
            }
        });

        return view;
    }

    private class insertStudent extends AsyncTask<String, String, String> {
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
            postDataParams.put("course", params[1]);
            postDataParams.put("name",params[0]);
            postDataParams.put("assignment", params[2]);
            postDataParams.put("problem",params[3]);

            try {
                url = new URL("http://www.taterpqueue.xyz/insertStudent.php");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
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
            if(result.equalsIgnoreCase("Inserted successfully")){
                Log.d("Debug", "onPostExecute: S");
            }else{
                Log.d("Debug", result);
                Log.d("Debug", "onPostExecute: U");
            }

        }

        private String readStream(InputStream out){
            String output = "Hello world";
            try {
                out.read();
                out.close();
                return "Read success";
            }catch (IOException e) {
                Log.d("Debug", "readStream");
                return "Read unsuccess";
            }

        }

    }

}