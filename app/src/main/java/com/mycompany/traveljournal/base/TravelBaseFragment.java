package com.mycompany.traveljournal.base;


import android.support.v4.app.Fragment;
import android.widget.ProgressBar;

public class TravelBaseFragment extends Fragment {

    protected ProgressBar pb;

    protected void showProgress() {
        if (pb != null) {
            pb.setVisibility(ProgressBar.VISIBLE);
        }
    }

    protected void hideProgress() {
        if (pb != null) {
            pb.setVisibility(ProgressBar.INVISIBLE);
        }
    }
}
