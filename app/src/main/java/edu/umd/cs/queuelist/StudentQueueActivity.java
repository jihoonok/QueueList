package edu.umd.cs.queuelist;

/**
 * Created by khanhnguyen on 3/24/17.
 */

import android.support.v4.app.Fragment;
import android.util.Log;

public class StudentQueueActivity extends SingleFragmentActivity {

    private final String TAG = getClass().getSimpleName();

    @Override
    protected Fragment createFragment() {
        Log.d(TAG, "call 2");
        return StudentQueueFragment.newInstance();
    }


}