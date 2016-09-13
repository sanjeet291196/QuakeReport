package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by sanjit on 12/9/16.
 * Project: QuakeReport
 */
public class EarthquakeLoader extends AsyncTaskLoader<List<EarthquakeItem>> {

    private String mUrl;

    public EarthquakeLoader(Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<EarthquakeItem> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<EarthquakeItem> earthquakeItems = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakeItems;
    }
}
