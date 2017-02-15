package com.example.isen.noroughapk;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.isen.noroughapk.Bluetooth.BluetoothLeService;
import com.example.isen.noroughapk.Interfaces.ClickListenerFragment;

import java.util.ArrayList;

/**
 * Created by Thomas B on 08/12/2016.
 */

public class AppareillageFragment extends ListFragment {

    private BLEDeviceListAdapter bleDeviceListAdapter;
    private BluetoothAdapter bluetoothAdapter;
    private boolean scanning;
    private Handler handler;
    BluetoothLeScanner bluetoothLeScanner;
    private ScanCallback scanCallback;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;
    private ClickListenerFragment listenerFragment;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    // ensure to have permission coarse location
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    private Button scanButton;
    private Button stopButton;
    private ProgressBar progressBar;
    private Button calibrationButton;

    public AppareillageFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appareillage, container, false);

        listenerFragment=(NavigationDrawer) this.getActivity();

        scanButton = (Button) view.findViewById(R.id.scan_button);
        stopButton = (Button) view.findViewById(R.id.stop_button);
        calibrationButton = (Button) view.findViewById(R.id.calibration_button);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_appareillage);

        scanButton.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleDeviceListAdapter.clearDevices();
                stopButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                scanButton.setVisibility(View.INVISIBLE);
                calibrationButton.setVisibility(View.INVISIBLE);
                scanLeDevice(true);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.INVISIBLE);
                scanButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.INVISIBLE);
                scanLeDevice(false);
            }
        });

        calibrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerFragment.ClickListener("calibration","4:0/");
            }
        });

        // Device scan callback.
        if (Build.VERSION.SDK_INT >20)
            scanCallback = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, final ScanResult result) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (Build.VERSION.SDK_INT >20)
                                bleDeviceListAdapter.addDevice(result.getDevice());
                                bleDeviceListAdapter.notifyDataSetChanged();
                            }
                        });

                }
            };
        else
            mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bleDeviceListAdapter.addDevice(device);
                            bleDeviceListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };

        onResume();

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        final BluetoothDevice device = bleDeviceListAdapter.getDevice(position);
        if(device == null) return;
        if(scanning){
            scanning = false;
            if (Build.VERSION.SDK_INT > 20)
                bluetoothLeScanner.stopScan(scanCallback);
            else
                bluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        stopButton.performClick();
        listenerFragment.BluetoothDeviceSet(device.getName(),device.getAddress());
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        //initialization of the list view adapter
        bleDeviceListAdapter = new BLEDeviceListAdapter();
        setListAdapter(bleDeviceListAdapter);

        handler = new Handler();

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (Build.VERSION.SDK_INT >20) {
            bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
            // Make sure we have access coarse location enabled, if not, prompt the user to enable it
            if (Build.VERSION.SDK_INT > 22 && getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect peripherals.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }

        scanLeDevice(true);

    }

    @Override
    public void onPause() {
        super.onPause();
        scanLeDevice(false);
        bleDeviceListAdapter.clearDevices();
        getActivity().unregisterReceiver(mGattUpdateReceiver);
    }

    // scan device
    public void scanLeDevice(boolean enable) {
        if (enable) {
            //stop scanning after Scan_Period
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanning = false;
                    if (Build.VERSION.SDK_INT > 20)
                        bluetoothLeScanner.startScan(scanCallback);
                    else
                        bluetoothAdapter.stopLeScan(mLeScanCallback);
                    stopButton.performClick();
                }
            }, SCAN_PERIOD);

            scanning = true;
            if(Build.VERSION.SDK_INT > 20)
                bluetoothLeScanner.startScan(scanCallback);
            else
                bluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            scanning = false;
            if (Build.VERSION.SDK_INT > 20)
                bluetoothLeScanner.stopScan(scanCallback);
            else
                bluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    public class BLEDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> devices;
        private LayoutInflater inflator;

        public BLEDeviceListAdapter(){
            super();
            devices = new ArrayList<BluetoothDevice>();
            inflator = getActivity().getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device){
            if(!devices.contains(device)){
                devices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position){
            return devices.get(position);
        }

        public void clearDevices(){
            devices.clear();
        }

        @Override
        public int getCount(){
            return devices.size();
        }

        @Override
        public Object getItem(int i){
            return devices.get(i);
        }

        @Override
        public long getItemId(int i){
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;

            if(view == null){
                view = inflator.inflate(R.layout.item_bluetooth_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = devices.get(position);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() >0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText("Unknown device");
            viewHolder.deviceAddress.setText(device.getAddress());

            return view;
        }
    }

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                calibrationButton.setVisibility(View.VISIBLE);
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}
