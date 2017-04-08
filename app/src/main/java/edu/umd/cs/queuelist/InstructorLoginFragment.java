package edu.umd.cs.queuelist;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.app.Activity.RESULT_CANCELED;

/**
 * Created by jihoonok on 3/21/17.
 */

public class InstructorLoginFragment extends android.support.v4.app.Fragment {
    private View view;
    private Button loginButton, cancelButton;
    private EditText password, instructorid;


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
        loginButton = (Button)view.findViewById(R.id.login_button);
        cancelButton = (Button)view.findViewById(R.id.cancel_button);
        password = (EditText)view.findViewById(R.id.password);
        instructorid = (EditText)view.findViewById(R.id.instructor_id);


        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view1) {
                Intent intent1 = getActivity().getIntent();
                String instructorid_string = instructorid.getText().toString();
                String password_string = password.getText().toString();
                if (instructorid_string.equals("CMSC131") && password_string.equals("dennis")) {
                    getActivity().setResult(RESULT_CANCELED, intent1);
                    getActivity().finish();
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "Wrong id and password combination", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view1) {
                Intent intent1 = getActivity().getIntent();
                getActivity().setResult(RESULT_CANCELED, intent1);
                getActivity().finish();
            }
        });

        return view;
    }
}
