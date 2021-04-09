// ISAAC WALDRON - IWALDR200 - S1715300
package org.me.gcu.iwaldr200;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity {

    private ArrayList<PullParser> earthquakeList;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            /*
            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            */

            // Set Map Default Position (GCU Building)
            LatLng GCU = new LatLng(55.86680, -4.25000);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GCU, 7f));

            for(int i = 0; i < earthquakeList.size(); i++) {
                BitmapDescriptor bmd = null;

                if(Float.parseFloat(earthquakeList.get(i).getMagnitude()) < 1)
                {
                    bmd = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                }
                else if(Float.parseFloat(earthquakeList.get(i).getMagnitude()) >= 1 && Float.parseFloat(earthquakeList.get(i).getMagnitude()) <= 2)
                {
                    bmd = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                }
                else if(Float.parseFloat(earthquakeList.get(i).getMagnitude()) > 2)
                {
                    bmd = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                }
                googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(earthquakeList.get(i).getGeolat()) , Double.parseDouble(earthquakeList.get(i).getGeolong()))).icon(bmd).title(earthquakeList.get(i).getLocation()));
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_maps_activity);
        earthquakeList = (ArrayList<PullParser>) getIntent().getExtras().getSerializable("Items");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}