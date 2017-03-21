package edu.umd.cs.queuelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jihoonok on 3/21/17.
 */

public class InstructorLoginFragment extends android.support.v4.app.Fragment {
    private View view;

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

        view = inflater.inflate(R.layout.fragment_instructorlogin, container, false);

        return view;
    }
}
