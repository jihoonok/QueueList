package edu.umd.cs.queuelist;

import android.support.v4.app.Fragment;

/**
 * Created by jihoonok on 3/21/17.
 */

public class StudentNameActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return StudentNameFragment.newInstance();
    }
}
