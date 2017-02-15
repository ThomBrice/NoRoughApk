package com.example.isen.noroughapk.Interfaces;

/**
 * Created by isen on 25/10/2016.
 */

public interface ClickListenerFragment {
    public void ClickListener(String name);

    public void ClickListener(String name, int id);
    public void ClickListener(String name ,int from , int position,int score);

    public void ClickListener(String name, String data);

    public void BluetoothDeviceSet(String name, String address);
}

