package com.example.isen.noroughapk.fragment_partie_lancée;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.isen.noroughapk.Bluetooth.BluetoothLeService;
import com.example.isen.noroughapk.Interfaces.LocationChangeCalcul;
import com.example.isen.noroughapk.NavigationDrawer;
import com.example.isen.noroughapk.R;
import com.example.isen.noroughapk.GPSTracker;

import com.example.isen.noroughapk.json_helper.JsonReader;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
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
    private PlaceCircle placeCircle;
    private HashMap<String, String> trou;
    private JsonReader jsonReader;
    private Bundle bundle;
    private Circle circle;
    private static Double myLat;
    private static Double myLong;

    public MapsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        locationChangeCalcul = (NavigationDrawer) this.getActivity();

        bundle = this.getArguments();
        if(bundle !=null){
            jsonReader = bundle.getParcelable("jsonReader");
        }

        mMapView.onResume();

        setNum(0);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

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
        placeCircle = new PlaceCircle();
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

    public void setNum(int num){
        trou = jsonReader.getCoordonneesList().get(num);
    }

    public void setLatLong(Double latitude, Double longitude){
        myLat = latitude;
        myLong = longitude;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void displayData(String data){
        if(myLat!=null && myLong!=null) {
            placeCircle = new PlaceCircle(Double.parseDouble(data),myLat,myLong);
            placeCircle.execute();
        }
    }

    public class PlaceCircle extends AsyncTask<Double,Integer,Double>{

        Double mLat;
        Double mLong;
        Double latM;
        Double lonM;
        Double distanceTotale;
        Double distance;
        Double latC;
        Double lonC;

        public PlaceCircle(){}

        public PlaceCircle(double distance, double latitude, double longitude) {
            mLat = latitude;
            mLong = longitude;
            latM = Double.parseDouble(trou.get("LatM"));
            lonM = Double.parseDouble(trou.get("LonM"));
            this.distance = distance;
        }

        @Override
        protected Double doInBackground(Double... doubles) {
            Calcul();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);
            if(circle != null)
                circle.remove();

            circle = googleMap.addCircle(new CircleOptions()
                    .center(new LatLng(latC, lonC))
                    .radius(5) // radius in meter
                    .strokeWidth(2) //width of the line
                    .strokeColor(R.color.colorPrimaryDark) // border color
                    .fillColor(R.color.circle_map)); //fill color
        }

        public void Calcul() {
            distanceTotale = 2 * asin(sqrt((sin((convertRad(mLat) - convertRad(latM)) / 2)) * (sin((convertRad(mLat) - convertRad(latM)) / 2)) +
                    cos(convertRad(mLat)) * cos(convertRad(latM)) * (sin((convertRad(mLong) - convertRad(lonM)) / 2)) * (sin((convertRad(mLong) - convertRad(lonM)) / 2)))) * 6366 * 1000;
            latC = mLat + (((latM-mLat)*distance)/distanceTotale);
            lonC = mLong + (((lonM-mLong)*distance)/distanceTotale);
        }

        Double convertRad(Double var) {
            return (Math.PI * var) / 180;
        }
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_DATA_VERIFIED.equals(action)) {
                String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                String split[];
                split=data.split(":");
                if(split[0].equals("3")){
                    split=split[1].split("/");
                    displayData(split[0]);
                }
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_VERIFIED);
        return intentFilter;
    }
}
