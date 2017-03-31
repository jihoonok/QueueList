package edu.umd.cs.queuelist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by jihoonok on 3/30/17.
 */

public class InstructorViewFragment extends android.support.v4.app.Fragment {
    private Button nextStudent;
    private TextView studentInfo;

    public static InstructorViewFragment newInstance() {
        InstructorViewFragment fragment = new InstructorViewFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_instructorview, container, false);

        studentInfo = (TextView) view.findViewById(R.id.student);
        nextStudent = (Button) view.findViewById(R.id.next);

        nextStudent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                studentInfo.setText("Hello this is student 1");
                Fragment fragment = new Fragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_instructorview, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

}
