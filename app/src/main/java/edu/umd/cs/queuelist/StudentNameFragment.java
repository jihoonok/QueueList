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
                //Log.d(TAG, aStory.getId());
                intent2.putExtra(EXTRA_STUDENT_CREATED, student);
                getActivity().setResult(RESULT_OK, intent2);
                getActivity().finish();
            }
        });

        return view;
    }

}