package edu.umd.cs.queuelist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by jihoonok on 3/21/17.
 */
public class MainFragment extends android.support.v4.app.Fragment {
    private Button studentButton;
    private Button instructorButton;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_main, container, false);

        studentButton = (Button) view.findViewById(R.id.student);
        studentButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v1) {
                Intent intent = new Intent(getActivity(), StudentNameActivity.class);
                startActivity(intent);
            }
        });

        instructorButton = (Button) view.findViewById(R.id.instructor);
        instructorButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v1) {
                Intent intent = new Intent(getActivity(), InstructorLoginActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
