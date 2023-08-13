package com.ch1ck3n.salmonac.utils;

import java.util.LinkedList;
import java.util.Queue;

public class SampleList {
    long d = 10;
    public SampleList(){}
    public SampleList(long d){
        this.d = d;
    }
    private Queue<Long> ticks = new LinkedList<>();

    public float add(Long l){
        this.ticks.add(l);
        return getAverage();
    }

    public float getAverage() {
        long time = System.currentTimeMillis();
        while (!this.ticks.isEmpty() && this.ticks.size() > d)
            this.ticks.poll();
        float average = 0;
        for(long l : this.ticks)
            average += l;
        average /= this.d;
        return average;
    }

    public boolean isEnough() {
        return this.ticks.size() >= d;
    }

    public void clear() {
        this.ticks.clear();
    }
}
