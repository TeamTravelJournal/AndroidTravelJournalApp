package com.mycompany.traveljournal.base;


import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;

import com.mycompany.traveljournal.R;

public class TravelBaseFragment extends Fragment {

    protected ProgressBar pb;

    public void setUpViews(View v) {
        pb = (ProgressBar) v.findViewById(R.id.pbLoading);
    }

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
