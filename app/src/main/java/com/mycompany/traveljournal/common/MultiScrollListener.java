package com.mycompany.traveljournal.common;

import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjayaram on 6/6/2015.
 */
public class MultiScrollListener extends RecyclerView.OnScrollListener {
    List<RecyclerView.OnScrollListener> mListeners = new ArrayList<RecyclerView.OnScrollListener>();
    public void addScrollListener(RecyclerView.OnScrollListener listener){
        mListeners.add(listener);
    }
    public void removeListener(RecyclerView.OnScrollListener listener){
        mListeners.remove(listener);
    }

    public void removeListener(){
        mListeners.clear();
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int newState){
        for(RecyclerView.OnScrollListener listener: mListeners){
            listener.onScrollStateChanged(recyclerView,newState);
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        for(RecyclerView.OnScrollListener listener: mListeners){
            listener.onScrolled(recyclerView, dx, dy);
        }
    }

    /*@Override
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
    }*/
}
