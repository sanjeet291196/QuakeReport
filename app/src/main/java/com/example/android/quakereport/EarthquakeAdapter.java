package com.example.android.quakereport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by sanjit on 5/9/16.
 * Project: QuakeReport
 */
public class EarthquakeAdapter extends ArrayAdapter<EarthquakeItem> {
    public EarthquakeAdapter(Context context, List<EarthquakeItem> objects) {
        super(context, 0, objects);
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View currentView = convertView;
        if (currentView == null) {
            currentView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_item, null);
        }

        EarthquakeItem currentItem = getItem(position);

        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        TextView magnitude = (TextView) currentView.findViewById(R.id.magnitude_view);
        magnitude.setText(decimalFormat.format(currentItem.getMagnitude()));

        String placeText = currentItem.getPlace();
        int ofIndex = placeText.indexOf("of");

        TextView offset = (TextView) currentView.findViewById(R.id.place_offset_view);
        TextView place = (TextView) currentView.findViewById(R.id.place_view);

        if (ofIndex == -1) {
            offset.setText("Near the");
            place.setText(placeText);
        } else {
            offset.setText(placeText.substring(0, ofIndex + 2));
            place.setText(placeText.substring(ofIndex + 3));
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy\nhh:mm a");

        TextView time = (TextView) currentView.findViewById(R.id.time_view);
        time.setText(sdf.format(currentItem.getTime()));

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentItem.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


        return currentView;
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
