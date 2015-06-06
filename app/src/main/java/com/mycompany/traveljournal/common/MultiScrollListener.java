package com.mycompany.traveljournal.common;

import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjayaram on 6/6/2015.
 */
public class MultiScrollListener implements AbsListView.OnScrollListener {
    List<AbsListView.OnScrollListener> mListeners = new ArrayList<AbsListView.OnScrollListener>();
    public void addScrollListener(AbsListView.OnScrollListener listener){
        mListeners.add(listener);
    }
    public void removeListener(AbsListView.OnScrollListener listener){
        mListeners.remove(listener);
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        for(AbsListView.OnScrollListener listener: mListeners){
            listener.onScrollStateChanged(view,scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        for(AbsListView.OnScrollListener listener: mListeners){
            listener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}
