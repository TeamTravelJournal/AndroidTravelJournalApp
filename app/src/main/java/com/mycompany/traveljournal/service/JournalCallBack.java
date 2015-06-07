package com.mycompany.traveljournal.service;

import java.util.List;

/**
 * Created by ekucukog on 6/7/2015.
 */
public interface JournalCallBack<T> {

    public void onSuccess(T t);
    public void onFailure(Exception e);
}
