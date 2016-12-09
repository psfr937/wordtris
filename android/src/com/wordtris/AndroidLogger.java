package com.wordtris;

import android.util.Log;

/**
 * Created by raymond on 11/22/16.
 */

public class AndroidLogger implements Logger {
    public void debug(String tag, String message) {
        Log.d(tag, message);
    }
}