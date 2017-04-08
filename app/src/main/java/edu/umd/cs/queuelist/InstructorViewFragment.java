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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jihoonok on 3/30/17.
 */

public class InstructorViewFragment extends android.support.v4.app.Fragment {
    private Button nextStudent;
    private TextView studentInfo;
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
        nextStudent = (Button) view.findViewById(R.id.next);

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
                url = new URL("http://10.0.2.2/QueueList/Deque.php");

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
                studentInfo.setText(result);
                Log.d("Debug", "onPostExecute: S");
            }

        }

    }

}
