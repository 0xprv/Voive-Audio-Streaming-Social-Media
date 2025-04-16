package com.voive.android;

public class TIMING_MODAL {

    String DAY;
    int TIME_LISTENED;

    public TIMING_MODAL(String DAY, int TIME_LISTENED) {
        this.DAY = DAY;
        this.TIME_LISTENED = TIME_LISTENED;
    }

    public void setDAY(String DAY) {
        this.DAY = DAY;
    }

    public void setTIME_LISTENED(int TIME_LISTENED) {
        this.TIME_LISTENED = TIME_LISTENED;
    }
}
