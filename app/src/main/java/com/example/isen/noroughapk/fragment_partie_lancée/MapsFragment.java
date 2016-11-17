package com.example.isen.noroughapk.fragment_partie_lancée;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.isen.noroughapk.R;
import com.example.isen.noroughapk.GPSTracker;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {

    private MapView mMapView;
    private GoogleMap googleMap;
    private Location mLocation;
    private double latitude; // latitude
    private double longitude; // longitude
    private GPSTracker gpsTracker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        // enléve la toolbar (en bas à droite) qui permet de trouver un chemin jusqu'au marker

        //googleMap.getUiSettings().setZoomControlsEnabled(true);

        gpsTracker = new GPSTracker(getContext());

        mMapView.onResume();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mMapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                googleMap.getUiSettings().setMapToolbarEnabled(false); // disable help from Api maps when a marker's click appear

                if (gpsTracker.canGetLocation()) {
                    latitude = gpsTracker.getLatitude(); // loading my latitude
                    longitude = gpsTracker.getLongitude(); // loading my longitude

                    //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17)); // zoom on my position

                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    googleMap.setMyLocationEnabled(true);

                    Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude,longitude))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                    gpsTracker.animateMarker(marker,new LatLng(1,3),false,googleMap);
                } else {
                    gpsTracker.showSettingsAlert();  // Alert if GPS is not enabled
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        gpsTracker.stopUsingGPS();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
