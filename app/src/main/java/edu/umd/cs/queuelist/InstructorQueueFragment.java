package edu.umd.cs.queuelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.umd.cs.queuelist.model.Student;
import edu.umd.cs.queuelist.service.StudentService;

public class InstructorQueueFragment extends Fragment {


    private String course;
    private StudentService stuser;
    private View view;
    private RecyclerView recycle;
    private StudentAdapter studentA;
    private final String TAG = getClass().getSimpleName();
    private String majorCourse = "cmsc131";
    private SwipeRefreshLayout mSwipeRefreshLayout;

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
        getActivity().setTitle(majorCourse.toUpperCase());

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent getIntent = getActivity().getIntent();
        course = getIntent.getStringExtra("Class");
        view = inflater.inflate(R.layout.fragment_studentqueue, container, false);
        recycle = (RecyclerView)view.findViewById(R.id.student_recycler_view);
        recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateUI(majorCourse);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        updateUI(course);

        return view;
    }

    public static InstructorQueueFragment newInstance() {
        return new InstructorQueueFragment();
    }

    private void updateUI(String course) {
        List<Student> studentL = stuser.getAllStudents(course);
        getActivity().setTitle(course.toUpperCase());
        if (studentA == null) {
            studentA = new StudentAdapter(studentL);
            recycle.setAdapter(studentA);
        } else {
            studentA.setStudents(studentL);
            studentA.notifyDataSetChanged();
        }
    }

}
