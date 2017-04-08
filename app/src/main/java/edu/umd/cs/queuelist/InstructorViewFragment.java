package edu.umd.cs.queuelist;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jihoonok on 3/30/17.
 */

public class InstructorViewFragment extends android.support.v4.app.Fragment {
    private Button nextStudent;
    private Button AddStudent;
    private Button viewQueue;
    private TextView studentInfo;
    private TextView studentAssignment;
    private TextView studentProblem;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private String course;


    public static InstructorViewFragment newInstance() {
        InstructorViewFragment fragment = new InstructorViewFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_instructorview, container, false);

        Intent getIntent = getActivity().getIntent();
        course = getIntent.getStringExtra("Course");
        Log.d("Debug", course);

        studentInfo = (TextView) view.findViewById(R.id.student);
        studentProblem = (TextView) view.findViewById(R.id.problem);
        studentAssignment = (TextView) view.findViewById(R.id.assignment);
        nextStudent = (Button) view.findViewById(R.id.next);
        AddStudent = (Button) view.findViewById(R.id.add_student);
        viewQueue = (Button) view.findViewById(R.id.viewQueue);

        nextStudent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new DequeueStudent().execute(course);
//                Fragment fragment = new Fragment();
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.fragment_instructorview, fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
            }
        });

        AddStudent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new insertStudent().execute(studentInfo.getText().toString(),course,studentAssignment.getText().toString(),studentProblem.getText().toString());
                new DequeueStudent().execute(course);
                Fragment fragment = new Fragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_instructorview, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        viewQueue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    private class DequeueStudent extends AsyncTask<String, String, String> {
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
                url = new URL("http://www.taterpqueue.xyz/Deque.php");

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
                        .appendQueryParameter("course", params[0]);
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
            if(result.equalsIgnoreCase("Failed to find student")){
                Log.d("Debug", result);
                Log.d("Debug", "onPostExecute: U");
            }else if(result.equalsIgnoreCase("Failed to delete student")){
                Log.d("Debug", result);
                Log.d("Debug", "onPostExecute: U");
            }else{

                String[] stuff = result.split(",");
                studentInfo.setText(stuff[0]);
                studentAssignment.setText(stuff[1]);
                studentProblem.setText(stuff[2]);
            }

            }

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




