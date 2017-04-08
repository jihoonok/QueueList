package edu.umd.cs.queuelist;

import android.support.v4.app.Fragment;

/**
 * Created by  on 4/8/17.
 */

public class InstructorQueueActivity extends SingleFragmentActivity {

        @Override
        protected Fragment createFragment() {
            return InstructorQueueFragment.newInstance();
        }
}
