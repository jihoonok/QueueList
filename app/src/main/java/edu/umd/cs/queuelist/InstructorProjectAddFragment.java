package edu.umd.cs.queuelist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

/**
 * Created by jihoonok on 4/15/17.
 */

public class InstructorProjectAddFragment extends android.support.v4.app.Fragment {
    private View view;
    private EditText name;
    private Button enter;

    public static InstructorProjectAddFragment newInstance() {
        InstructorProjectAddFragment fragment = new InstructorProjectAddFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = inflater.inflate(R.layout.fragment_instructorproject, container, false);
        name = (EditText) view.findViewById(R.id.projectName);
        enter = (Button) view.findViewById(R.id.submit_project);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getActivity().getIntent();
                String projectname = name.getText().toString();
                new insertProject().execute(projectname);
                getActivity().finish();
            }
        });
        return view;
    }

    private class insertProject extends AsyncTask<String, String, String> {
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
            postDataParams.put("Project", params[0]);

            try {
                url = new URL("http://www.taterpqueue.xyz/insertProject.php");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
