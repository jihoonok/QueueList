package edu.umd.cs.queuelist;

import android.support.v4.app.Fragment;

/**
 * Created by jihoonok on 3/30/17.
 */

public class InstructorViewActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return InstructorViewFragment.newInstance();
    }
}
