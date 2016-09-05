/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    ArrayList<EarthquakeItem> earthquakes = null;
    ListView earthquakeListView;

    // Create a new {@link ArrayAdapter} of earthquakes
    EarthquakeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        earthquakeListView = (ListView) findViewById(R.id.list);

        earthquakes = new ArrayList<>();

        adapter = new EarthquakeAdapter(this, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthquakeItem selectedItem = adapter.getItem(position);

                Uri websiteURI = Uri.parse(selectedItem.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, websiteURI);

                startActivity(websiteIntent);
            }
        });

        EarthQuakeAsync async = new EarthQuakeAsync();
        async.execute();
    }

    private void getData() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String url = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=" +
                sdf.format(Calendar.getInstance().getTimeInMillis() - 999999999 - 999999999 - 799999999) +
                "&endtime=" +
                sdf.format(Calendar.getInstance().getTimeInMillis() + 100000000) +
                "&limit=1000&minfelt=50";
        Log.e(LOG_TAG, url);
        QueryUtils.fetchEarthquakeData(url);
    }

    private class EarthQuakeAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            getData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            earthquakes.clear();
            earthquakes.addAll(QueryUtils.extractEarthquakes());
            adapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }
    }
}
