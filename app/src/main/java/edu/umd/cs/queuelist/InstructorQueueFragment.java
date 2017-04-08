package edu.umd.cs.queuelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.umd.cs.queuelist.model.Student;
import edu.umd.cs.queuelist.service.StudentService;

import static android.app.Activity.RESULT_OK;

/**
 * Created by khanhnguyen on 4/8/17.
 */

public class InstructorQueueFragment extends Fragment {

    private final int REQUEST_CODE_CREATE_STORY = 2;
    private int resultCode;
    private StudentService stuser;
    private View view;
    private RecyclerView recycle;
    private StudentAdapter studentA;
    private final String TAG = getClass().getSimpleName();

    private class StudentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Student student;
        private TextView name, userid, assignment, queueNumber;

        public StudentHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            name = (TextView)v.findViewById(R.id.list_item_student_name);
            userid = (TextView)v.findViewById(R.id.list_item_student_userid);
            assignment = (TextView)v.findViewById(R.id.list_item_student_assignment);
            queueNumber = (TextView)v.findViewById(R.id.list_item_queue_number);
        }

        public void bindStudent(Student s, int order) {
            student = s;
            name.setText(s.getName());
            userid.setText(s.getUserId());
            assignment.setText(s.getAssignment() + "");
            queueNumber.setText(order + "");
        }

        @Override
        public void onClick(View v) {
            //Log.d(TAG, aStory.toString() + "2");
            /*Intent intent = StoryActivity.newIntent(getActivity().getApplicationContext(),
                    aStory.getId());
            startActivityForResult(intent, REQUEST_CODE_CREATE_STORY);
            onActivityResult(REQUEST_CODE_CREATE_STORY, resultCode, intent);*/
        }
    }

    private class StudentAdapter extends RecyclerView.Adapter<StudentHolder> {

        private List<Student> studentList;
        private LayoutInflater inflater1;

        public StudentAdapter(List<Student> list) {
            studentList = list;
        }

        public void setStudents(List<Student> list) {
            studentList = list;
        }

        @Override
        public StudentHolder onCreateViewHolder(ViewGroup container, int num) {
            inflater1 = LayoutInflater.from(getActivity());
            View view1 = inflater1.inflate(R.layout.list_item_student,container, false);
            return new StudentHolder(view1);
        }

        @Override
        public void onBindViewHolder(StudentHolder s, int num) {
            Student aStudent = studentList.get(num);
            s.bindStudent(aStudent, num + 1);
        }

        @Override
        public int getItemCount() {
            return studentList.size();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stuser = DependencyFactory.getStudentService(getActivity().getApplicationContext());
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_studentqueue, container, false);
        recycle = (RecyclerView)view.findViewById(R.id.student_recycler_view);
        recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (recyclerView.SCROLL_STATE_DRAGGING == newState) {
                    if (newState == 1) {
                        Log.d("hello", "scrolling down");
                        updateUI();
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CREATE_STORY) {
                Student aStudent = (Student)data.getSerializableExtra(StudentNameFragment.EXTRA_STUDENT_CREATED);
                stuser.addStudentToQueue(aStudent);
                updateUI();
            }
        }
    }

    public static StudentQueueFragment newInstance() {
        return new StudentQueueFragment();
    }

    private void updateUI() {
        List<Student> studentL = stuser.getAllStudents();
        if (studentA == null) {
            studentA = new StudentAdapter(studentL);
            recycle.setAdapter(studentA);
        } else {
            studentA.setStudents(studentL);
            studentA.notifyDataSetChanged();
        }
    }
}