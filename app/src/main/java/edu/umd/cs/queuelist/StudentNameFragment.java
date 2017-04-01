package edu.umd.cs.queuelist;

import android.content.Intent;
import android.net.Uri;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

                switch (classSpinner.getSelectedItemPosition()) {
                    case 0:
                        course = "None";
                        break;
                    case 1:
                        course = "CMSC131";
                        break;
                    case 2:
                        course = "CMSC132";
                        break;
                    case 3:
                        course = "CMSC216";
                        break;
                    default:
                        break;
                }

                new insertStudent().execute(name_string, course);
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
            try {

                // Enter URL address where your php file resides
                url = new URL("http://10.0.2.2/QueueList/insertStudent.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exceptionURL";
            }

            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("name", params[0])
                        .appendQueryParameter("course", params[1]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exceptionConnection";
            }

            try {

                int response_code = conn.getResponseCode();
                Log.d("debug", ""+response_code);

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    Log.d("Debug", result.toString());
                    return(result.toString());


                }else{
                    Log.d("Debug", "onPost Not Success: ");
                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exceptionResponse";
            } finally {
                Log.d("Debug", "disconnect: ");
                conn.disconnect();
            }


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

    }

}