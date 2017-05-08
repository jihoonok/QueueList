package edu.umd.cs.queuelist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.umd.cs.queuelist.model.Student;
import edu.umd.cs.queuelist.service.StudentService;

import static edu.umd.cs.queuelist.MainFragment.dialog;

/**
 * An activity representing a list of Students. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StudentDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StudentListActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CREATE_STORY = 1;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private int resultCode;
    private StudentService stuser;
    private StudentAdapter studentA;
    private RecyclerView recyclerView;
    private static String majorCourse = "cmsc131";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CoordinatorLayout coordLayout;
    public static boolean activityVisible = false;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Debug", "onResume: UpdateUI");
        updateUI(majorCourse);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateUI(majorCourse);
                Snackbar.make(mSwipeRefreshLayout, "Student Queue List Updated", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        Intent in = getIntent();
        if(in.getStringExtra("course") != null) {
            majorCourse = in.getStringExtra("course");
        }
        setTitle(majorCourse.toUpperCase());
        coordLayout = (CoordinatorLayout) findViewById(R.id.coordLayout);

        stuser = DependencyFactory.getStudentService(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(majorCourse.toUpperCase());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(getApplicationContext(), StudentNameActivity.class);
                startActivityForResult(intent3, REQUEST_CODE_CREATE_STORY);
                onActivityResult(REQUEST_CODE_CREATE_STORY, resultCode, intent3);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.student_list);
        assert recyclerView != null;
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        //setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.student_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    /*private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new StudentAdapter(DummyContent.ITEMS));
    }*/

    public class StudentAdapter
            extends RecyclerView.Adapter<StudentAdapter.StudentHolder> {

        private List<Student> mValues;

        public StudentAdapter(List<Student> items) {
            mValues = items;
        }

        public void setStudents(List<Student> list) {
            mValues = list;
        }

        @Override
        public StudentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.student_list_content, parent, false);
            return new StudentHolder(view);
        }

        @Override
        public void onBindViewHolder(final StudentHolder holder, final int position) {
            final Student student = mValues.get(position);
            holder.bindStudent(student,position+1);
            if(position % 2 == 0) {
                holder.mView.setBackgroundColor(getColor(R.color.lightGray));
            }else {
                holder.mView.setBackgroundColor(getColor(R.color.darkWhite));
            }


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(StudentDetailFragment.ARG_ITEM_ID, student.getName());
                        arguments.putString(StudentDetailFragment.INFO, student.toString());
                        StudentDetailFragment fragment = new StudentDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.student_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, StudentDetailActivity.class);
                        intent.putExtra(StudentDetailFragment.ARG_ITEM_ID, student.getName());
                        intent.putExtra(StudentDetailFragment.INFO, student.toString());
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class StudentHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            private Student student;

            public StudentHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            public void bindStudent(Student s, int order) {
                student = s;
                mContentView.setText(s.getName());
                mIdView.setText(order+"");

            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CREATE_STORY) {
                Student aStudent = (Student)data.getSerializableExtra(StudentNameFragment.EXTRA_STUDENT_CREATED);
                stuser.addStudentToQueue(aStudent);
                int course = aStudent.getClassCodePosition();
                switch (course) {
                    case 0:
                        majorCourse = "none";
                        updateUI("none");
                        break;
                    case 1:
                        majorCourse = "cmsc131";
                        updateUI("cmsc131");
                        break;
                    case 2:
                        majorCourse = "cmsc132";
                        updateUI("cmsc132");
                        break;
                    case 3:
                        majorCourse = "cmsc216";
                        updateUI("cmsc216");
                        break;
                    default:
                        majorCourse = "none";
                        updateUI("none");
                }

                Snackbar.make(coordLayout, aStudent.getName() + " Inserted to Queue List", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        }
    }
    private void updateUI(String course) {
        List<Student> studentL = stuser.getAllStudents(course);
        setTitle("Student Queue - " + course.toUpperCase());
        if (studentA == null) {
            studentA = new StudentAdapter(studentL);
            recyclerView.setAdapter(studentA);
        } else {
            studentA.setStudents(studentL);
            studentA.notifyDataSetChanged();
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
