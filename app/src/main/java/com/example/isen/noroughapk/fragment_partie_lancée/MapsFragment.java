package com.example.isen.noroughapk.fragment_partie_lancée;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isen.noroughapk.Interfaces.LocationChangeCalcul;
import com.example.isen.noroughapk.NavigationDrawer;
import com.example.isen.noroughapk.R;
import com.example.isen.noroughapk.GPSTracker;

import com.example.isen.noroughapk.StartFragment;
import com.example.isen.noroughapk.json_helper.JsonReader;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * Created by Thomas B on 02/11/2016.
 */

public class MapsFragment extends Fragment {

    private MapView mMapView;
    private GoogleMap googleMap;
    private GPSTracker gpsTracker;
    private LocationChangeCalcul locationChangeCalcul;

    public MapsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        locationChangeCalcul = (NavigationDrawer) this.getActivity();

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
                gpsTracker = new GPSTracker(getContext(),googleMap,locationChangeCalcul);

                googleMap.getUiSettings().setMapToolbarEnabled(false); // disable help from Api maps when a marker's click appear
                googleMap.getUiSettings().setZoomControlsEnabled(true);

                if (gpsTracker.canGetLocation()) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setCompassEnabled(false); // disable compass

                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            //calculDistances = new CalculDistances(new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude()),latLng);

                            // Creating a marker
                            MarkerOptions markerOptions = new MarkerOptions();

                            // Setting the position for the marker
                            markerOptions.position(latLng);

                            // Setting the title for the marker.
                            // This will be displayed on taping the marker
                            LatLng myLatLng = new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude());
                            markerOptions.title(String.format("%1$.2f",Calcul(myLatLng,latLng)));

                            // Clears the previously touched position
                            googleMap.clear();

                            // Placing a marker on the touched position
                            Marker marker =googleMap.addMarker(markerOptions);
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.target));

                            // always show the title
                            marker.showInfoWindow();

                        }
                    });

                } else {
                    gpsTracker.showSettingsAlert();  // Alert if GPS is not enabled
                }
            }
        });
    }

    public Double Calcul(LatLng myLatLng, LatLng markerLatLng) {
        Double distance;
        Double RadMyLatitude = convertRad(myLatLng.latitude);
        Double RadMyLongitude = convertRad(myLatLng.longitude);

        Double RadMarkerLat = convertRad(markerLatLng.latitude);
        Double RadMarkerLong = convertRad(markerLatLng.longitude);
        distance = 2 * asin(sqrt((sin((RadMyLatitude - RadMarkerLat) / 2)) * (sin((RadMyLatitude - RadMarkerLat) / 2)) +
                cos(RadMyLatitude) * cos(RadMarkerLat) * (sin((RadMyLongitude - RadMarkerLong) / 2)) * (sin((RadMyLongitude - RadMarkerLong) / 2)))) * 6366;
        distance*=1000;
        return distance;
    }

    Double convertRad(Double var) {
        return (Math.PI * var) / 180.;
    }

    @Override
    public void onPause() {
        super.onPause();
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
