package edu.umd.cs.queuelist;


import android.support.v4.app.Fragment;

public class MainActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        int fuck = 0;
        return MainFragment.newInstance();
    }
}
