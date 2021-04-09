// ISAAC WALDRON - IWALDR200 - S1715300
package org.me.gcu.iwaldr200;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<PullParser> earthquakeList;
    private TextView StartDate;
    private TextView EndDate;
    private Button Search;
    private TextView LblMagnitudeView;
    private TextView MagnitudeView;
    private TextView LblDeepestView;
    private TextView DeepestView;
    private TextView LblShallowestView;
    private TextView ShallowestView;
    private TextView LblNorthView;
    private TextView NorthView;
    private TextView LblEastView;
    private TextView EastView;
    private TextView LblSouthView;
    private TextView SouthView;
    private TextView LblWestView;
    private TextView WestView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        earthquakeList = (ArrayList<PullParser>) getIntent().getExtras().getSerializable("Items");
        StartDate = (TextView) findViewById(R.id.StartDate);
        EndDate = (TextView) findViewById(R.id.EndDate);
        Search = (Button) findViewById(R.id.Search);
        Search.setOnClickListener(this);

        LblMagnitudeView = (TextView) findViewById(R.id.LblMagnitudeView);
        MagnitudeView = (TextView) findViewById(R.id.MagnitudeView);
        LblDeepestView = (TextView) findViewById(R.id.LblDeepestView);
        DeepestView = (TextView) findViewById(R.id.DeepestView);
        LblShallowestView = (TextView) findViewById(R.id.LblShallowestView);
        ShallowestView = (TextView) findViewById(R.id.ShallowestView);
        LblNorthView = (TextView) findViewById(R.id.LblNorthView);
        NorthView = (TextView) findViewById(R.id.NorthView);
        LblEastView = (TextView) findViewById(R.id.LblEastView);
        EastView = (TextView) findViewById(R.id.EastView);
        LblSouthView = (TextView) findViewById(R.id.LblSouthView);
        SouthView = (TextView) findViewById(R.id.SouthView);
        LblWestView = (TextView) findViewById(R.id.LblWestView);
        WestView = (TextView) findViewById(R.id.WestView);
    }

    @Override
    public void onClick(View v) {
        if (v == Search) {
            // Hide Keypad after button press
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            // Start Search
            searchFunction();
        }
    }

    private void searchFunction() {
        SimpleDateFormat enterDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat PullParserDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        ArrayList<PullParser> inPull = new ArrayList();

        try {
            Date startDateString = enterDateFormat.parse(StartDate.getText().toString());
            Date endDateDateString = enterDateFormat.parse(EndDate.getText().toString());

            for (int i = 0; i < earthquakeList.size(); i++) {
                try {
                    Date currDate = PullParserDateFormat.parse(earthquakeList.get(i).getPubDate());
                    if (currDate.after(startDateString) && currDate.before(endDateDateString)) {
                        inPull.add(earthquakeList.get(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Variables to store result
            PullParser east = null;
            PullParser west = null;
            PullParser north = null;
            PullParser south = null;
            PullParser magnitude = null;
            PullParser deepest = null;
            PullParser shallowest = null;

            for (int f = 0; f < inPull.size(); f++) {

                // Set Initial Values
                if (f == 0) {
                    north = inPull.get(0);
                    east = inPull.get(0);
                    south = inPull.get(0);
                    west = inPull.get(0);
                    deepest = inPull.get(0);
                    shallowest = inPull.get(0);
                } else { // Iterate through earthquakeList and keep a record of the highest/lowest values
                    if (Float.parseFloat(inPull.get(f).getGeolat()) > Float.parseFloat(north.getGeolat())) {
                        north = inPull.get(f);
                    } else if (Float.parseFloat(inPull.get(f).getGeolat()) < Float.parseFloat(south.getGeolat())) {
                        south = inPull.get(f);
                    } else if (Float.parseFloat(inPull.get(f).getGeolong()) < Float.parseFloat(west.getGeolong())) {
                        west = inPull.get(f);
                    } else if (Float.parseFloat(inPull.get(f).getGeolong()) > Float.parseFloat(east.getGeolong())) {
                        east = inPull.get(f);
                    }
                }
                // Perform Magnitude, Deepest & Shallowest Seperately due to bug (Doesn't work correctly if merged with above if statements
                if (f == 0) {
                    magnitude = inPull.get(0);
                } else if (Float.parseFloat(inPull.get(f).getMagnitude()) > Float.parseFloat(magnitude.getMagnitude())) {
                    magnitude = inPull.get(f);
                }
                if (f == 0) {
                    deepest = inPull.get(0);
                } else if (Float.parseFloat(inPull.get(f).getDepth()) > Float.parseFloat(deepest.getDepth())) {
                    deepest = inPull.get(f);
                } else if (Float.parseFloat(inPull.get(f).getDepth()) < Float.parseFloat(shallowest.getDepth())) {
                    shallowest = inPull.get(f);
                }
            }

            // Display Labels & Results
            LblMagnitudeView.setVisibility(View.VISIBLE);
            MagnitudeView.setText(magnitude.getMagnitude());

            LblDeepestView.setVisibility(View.VISIBLE);
            DeepestView.setText(deepest.getDepth() + "KM");

            LblShallowestView.setVisibility(View.VISIBLE);
            ShallowestView.setText(shallowest.getDepth() + "KM");

            LblNorthView.setVisibility(View.VISIBLE);
            NorthView.setText(north.getLocation());

            LblEastView.setVisibility(View.VISIBLE);
            EastView.setText(east.getLocation());

            LblSouthView.setVisibility(View.VISIBLE);
            SouthView.setText(south.getLocation());

            LblWestView.setVisibility(View.VISIBLE);
            WestView.setText(west.getLocation());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
