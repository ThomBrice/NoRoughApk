package com.example.isen.noroughapk.fragment_partie_lancée;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.isen.noroughapk.R;
import com.example.isen.noroughapk.GPSTracker;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.R.attr.bitmap;
import static android.R.attr.duration;


public class MapsFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    private Location mLocation;
    private double latitude; // latitude
    private double longitude; // longitude

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();


        mMapView.getMapAsync(new OnMapReadyCallback() {
            GPSTracker gpsTracker = new GPSTracker(getContext());
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if (gpsTracker.canGetLocation()) {
                    latitude = gpsTracker.getLatitude();
                    longitude = gpsTracker.getLongitude();

                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude)));

                    Toast.makeText(getContext(), "Your Location is - \nLat: " + gpsTracker.getLatitude()
                                    + "\nLong: " + gpsTracker.getLongitude()
                            , Toast.LENGTH_LONG).show();
                }else {
                    gpsTracker.showSettingsAlert();
                }
            }
        });


    }


    @Override
    public void onPause() {
        super.onPause();
        //locationManager.removeUpdates(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
