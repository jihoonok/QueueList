package edu.umd.cs.queuelist;

import android.support.v4.app.Fragment;

/**
 * Created by jihoonok on 4/15/17.
 */

public class InstructorProjectAddActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return InstructorProjectAddFragment.newInstance();
    }


}
