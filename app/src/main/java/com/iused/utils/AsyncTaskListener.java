package com.iused.utils;

/**
 * Created by Sujith on 11-07-2015.
 */
public interface AsyncTaskListener {

    /**
     * Method onTaskCancelled.
     * @param data String
     */
    void onTaskCancelled(String data);
    /**
     * Method onTaskComplete.
     * @param result String
     * @param tag String
     */
    void onTaskComplete(String result, String tag);
}
