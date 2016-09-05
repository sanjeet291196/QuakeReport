package com.example.android.quakereport;

/**
 * Created by sanjit on 5/9/16.
 * Project: QuakeReport
 */

public class EarthquakeItem {
    private double magnitude;
    private String place;
    private long time;
    private String url;

    public EarthquakeItem(double magnitude, String place, long time, String url) {
        this.magnitude = magnitude;
        this.place = place;
        this.time = time;
        this.url = url;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getPlace() {
        return place;
    }

    public long getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }
}
