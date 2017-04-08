package edu.umd.cs.queuelist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by jihoonok on 3/21/17.
 */

public class InstructorLoginFragment extends android.support.v4.app.Fragment {
    private Spinner classSpinner;
    private Button submitButton;
    private EditText username;
    private EditText password;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static InstructorLoginFragment newInstance() {
        InstructorLoginFragment fragment = new InstructorLoginFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = inflater.inflate(R.layout.fragment_instructorlogin, container, false);
        submitButton = (Button) view.findViewById(R.id.submit);
        classSpinner = (Spinner)view.findViewById(R.id.spinner_class);
        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);

        ArrayAdapter<CharSequence> classAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.class_array, android.R.layout.simple_spinner_item);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(classAdapter);

        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String classCode = classSpinner.getSelectedItem().toString();

                if (classCode.equals("CMSC131")) {
                    if (user.equals("CMSC131") && pass.equals("sprcoredump")) {
                        Intent intent = new Intent(getActivity(), InstructorViewActivity.class);
                        intent.putExtra("Course", "CMSC131");
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "Incorrect login information",
                                Toast.LENGTH_SHORT).show();
                    }
                } else if (classCode.equals("CMSC132")) {
                    if (user.equals("CMSC132") && pass.equals("sprcoredump")) {
                        Intent intent = new Intent(getActivity(), InstructorViewActivity.class);
                        intent.putExtra("Course", "CMSC132");
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "Incorrect login information",
                                Toast.LENGTH_SHORT).show();
                    }
                } else if (classCode.equals("CMSC216")) {
                    if (user.equals("CMSC216") && pass.equals("sprcoredump")) {
                        Intent intent = new Intent(getActivity(), InstructorViewActivity.class);
                        intent.putExtra("Course", "CMSC216");
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "Incorrect login information",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Incorrect login information",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
