// ISAAC WALDRON - IWALDR200 - S1715300
package org.me.gcu.iwaldr200;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    private TextView LocationView;
    private TextView MagnitudeView;
    private TextView PubDateView;
    private TextView GeoCoordsView;
    private TextView DepthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        LocationView = (TextView) findViewById(R.id.LocationView);
        MagnitudeView = (TextView) findViewById(R.id.MagnitudeView);
        PubDateView = (TextView) findViewById(R.id.PubDateView);
        GeoCoordsView = (TextView) findViewById(R.id.GeoCoordsView);
        DepthView = (TextView) findViewById(R.id.DepthView);
        Bundle bundle = getIntent().getExtras();

        LocationView.setText(bundle.getString("location"));
        Float magnitude = Float.valueOf(bundle.getString("magnitude")); // Magnitude to strng
        MagnitudeView.setText(String.valueOf(magnitude));
        if (magnitude < 1) {
            MagnitudeView.setTextColor(Color.parseColor("green"));
        } else if (magnitude >= 1 && magnitude <= 2) {
            MagnitudeView.setTextColor(Color.parseColor("yellow"));
        } else if (magnitude > 2) {
            MagnitudeView.setTextColor(Color.parseColor("red"));
        }
        PubDateView.setText(bundle.getString("pubDate"));
        GeoCoordsView.setText(bundle.getString("geoLat") + ", " + bundle.getString("geoLong"));
        DepthView.setText(bundle.getString("depth") + "KM");
    }

}
