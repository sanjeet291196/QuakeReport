package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanjit on 5/9/16.
 * Project: QuakeReport
 * <p/>
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {


    private static String JSON_RESPONSE = "";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }


    public static List<EarthquakeItem> fetchEarthquakeData(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JSON_RESPONSE = "";

        try {
            JSON_RESPONSE = makeHTTPRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<EarthquakeItem> earthquakes = new ArrayList<>();

        if (JSON_RESPONSE != null) {
            try {

                JSONObject root = new JSONObject(JSON_RESPONSE);

                JSONArray feature = root.getJSONArray("features");

                for (int i = 0; i < feature.length(); i++) {
                    JSONObject quake = feature.getJSONObject(i);
                    JSONObject properties = quake.getJSONObject("properties");
                    double magnitude = properties.getDouble("mag");
                    String place = properties.getString("place");
                    long time = properties.getLong("time");
                    String page_url = properties.getString("url");
                    earthquakes.add(new EarthquakeItem(magnitude, place, time, page_url));
                }

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
            }
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    private static String makeHTTPRequest(URL url) throws IOException {
        String response = null;
        if (url == null) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                response = readFromStream(inputStream);
            } else {
                Log.e("HTTP Request", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }

        return response;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}