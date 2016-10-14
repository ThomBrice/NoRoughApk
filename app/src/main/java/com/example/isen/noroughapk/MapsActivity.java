package com.example.isen.noroughapk;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Thomas B on 12/10/2016.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map).getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(GoogleMap map){
        map.addMarker(new MarkerOptions()
        .position(new LatLng(50.657561, 3.129439)) //coordonnées de la citadelle
        .title("Marker"));
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

}
