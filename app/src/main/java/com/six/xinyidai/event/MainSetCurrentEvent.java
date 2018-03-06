package com.six.xinyidai.event;

/**
 * Created by lihuabin on 2016/11/25.
 */
public class MainSetCurrentEvent {
    int old = 0;
    int Current;

    public MainSetCurrentEvent(int current) {
        Current = current;
    }

    public int getCurrent() {
        return Current;
    }

    public void setCurrent(int current) {
        Current = current;
    }

    public MainSetCurrentEvent(int current, int old) {
        Current = current;
        this.old = old;
    }

    public int getOld() {
        return old;
    }

    public void setOld(int old) {
        this.old = old;
    }
}
