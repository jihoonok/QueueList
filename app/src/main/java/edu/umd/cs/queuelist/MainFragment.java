package edu.umd.cs.queuelist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;

/**
 * Created by jihoonok on 3/21/17.
 */
public class MainFragment extends android.support.v4.app.Fragment {
    private ImageButton studentButton;
    private ImageButton instructorButton;
    private View view;
    private final String TAG = getClass().getSimpleName();
    private PopupWindow pw;
    public static ProgressDialog dialog;

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
        StudentListActivity.activityVisible = false;

        studentButton = (ImageButton) view.findViewById(R.id.student);
        studentButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v1) {
                initiatePopupWindow(view);
            }
        });

        instructorButton = (ImageButton) view.findViewById(R.id.instructor);
        instructorButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v1) {
                Intent intent = new Intent(getActivity(), InstructorLoginActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initiatePopupWindow(View v) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popup,
                    (ViewGroup) v.findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, 750, 500, true);
            pw.setElevation(20);
            // display the popup in the center
            pw.showAtLocation(v, Gravity.CENTER, 0, 0);


            Button enterButton = (Button) layout.findViewById(R.id.enterList);
            final Spinner classSpinner = (Spinner) layout.findViewById(R.id.courseSpinner);

            ArrayAdapter<CharSequence> classAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                    R.array.class_array, android.R.layout.simple_spinner_item);
            classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            classSpinner.setAdapter(classAdapter);

            enterButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v1) {
                    Intent intent = new Intent(getActivity(), StudentListActivity.class);
                    intent.putExtra("course",classSpinner.getSelectedItem().toString().toLowerCase());
                    Log.d(TAG, "Student Button");
                    startActivity(intent);
                    if(pw.isShowing()){
                        pw.dismiss();
                    }

                    dialog = ProgressDialog.show(getActivity(),"Please Wait..","Loading Student List ... ",true);

                }
            });

            pw.setOutsideTouchable(true);
            pw.setFocusable(true);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
