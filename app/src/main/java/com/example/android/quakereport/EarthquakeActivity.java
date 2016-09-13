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

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<EarthquakeItem>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    ArrayList<EarthquakeItem> earthquakes = null;
    ListView earthquakeListView;
    TextView emptyView;

    // Create a new {@link ArrayAdapter} of earthquakes
    EarthquakeAdapter adapter;

    String url = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=4&limit=100";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        earthquakeListView = (ListView) findViewById(R.id.list);
        emptyView = (TextView) findViewById(R.id.empty_view);

        earthquakes = new ArrayList<>();

        adapter = new EarthquakeAdapter(this, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
        earthquakeListView.setEmptyView(emptyView);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthquakeItem selectedItem = adapter.getItem(position);

                Uri websiteURI = Uri.parse(selectedItem.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, websiteURI);

                startActivity(websiteIntent);
            }
        });

        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(1, null, this);
    }


    @Override
    public Loader<List<EarthquakeItem>> onCreateLoader(int i, Bundle bundle) {
        emptyView.setText(R.string.loading);
        return new EarthquakeLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<EarthquakeItem>> loader, List<EarthquakeItem> earthquakeItems) {
        adapter.clear();
        if (earthquakeItems != null && !earthquakeItems.isEmpty()) {
            adapter.addAll(earthquakeItems);
        }
        emptyView.setText(R.string.no_earthquake_data);
    }

    @Override
    public void onLoaderReset(Loader<List<EarthquakeItem>> loader) {
        adapter.clear();
    }
}
