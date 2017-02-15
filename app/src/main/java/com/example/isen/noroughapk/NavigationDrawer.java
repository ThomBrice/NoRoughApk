package com.example.isen.noroughapk;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isen.noroughapk.Bluetooth.BluetoothLeService;
import com.example.isen.noroughapk.Bluetooth.SampleGattAttributes;
import com.example.isen.noroughapk.Interfaces.ClickListenerFragment;
import com.example.isen.noroughapk.Interfaces.LocationChangeCalcul;
import com.example.isen.noroughapk.Interfaces.TrouChangeListener;
import com.example.isen.noroughapk.fragment_partie_lancée.ActivityFragment;
import com.example.isen.noroughapk.fragment_partie_lancée.MapsFragment;
import com.example.isen.noroughapk.fragment_partie_lancée.ScoreFragment;
import com.example.isen.noroughapk.json_helper.JsonReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static android.bluetooth.BluetoothGattCharacteristic.PERMISSION_READ;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_NOTIFY;
import static com.example.isen.noroughapk.Bluetooth.BluetoothLeService.ACTION_DATA_VERIFIED;

/**
 * Created by Thomas B on 18/01/2017.
 */

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ClickListenerFragment,LocationChangeCalcul,TrouChangeListener{

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private NavigationView navigationView;
    private Bundle bundle = new Bundle();
    private ActivityFragment activityFragment;
    private ScoreFragment scoreFragment;
    private MapsFragment mapsFragment;
    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 1;
    private boolean doubleBackToExitPressedOnce = false;
    private static int PARTY_LAUNCH = 0;

    private JsonReader jsonReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.getMenu().performIdentifierAction(R.id.nav_start, 0);

        jsonReader = new JsonReader(getApplicationContext());
        jsonReader.execute();

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBlueTooth, REQUEST_CODE_ENABLE_BLUETOOTH);
        }

        scoreFragment = new ScoreFragment();
    }

    @Override
    public void onResume(){
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
        }
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            onDestroy();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_LONG).show();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView.getMenu().performIdentifierAction(R.id.nav_start,0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000); //wait 2 seconds
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        int id= android.os.Process.myPid();
        android.os.Process.killProcess(id);
        mBluetoothLeService.disconnect();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_color) {

        } else if (id == R.id.action_languages) {

        } else if (id == R.id.action_size) {

        } else if (id == R.id.action_connexion) {

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_start) {
            StartFragment startFragment = new StartFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main, startFragment)
                    .commit();
        } else if (id == R.id.nav_historique) {
            HistoryFragment historyFragment = new HistoryFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main, historyFragment)
                    .commit();
        } else if (id == R.id.nav_statistique) {
            StatisticFragment statisticFragment = new StatisticFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main, statisticFragment)
                    .commit();
        } else if (id == R.id.nav_swing) {
            AnalyseFragment analyseFragment = new AnalyseFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main, analyseFragment)
                    .commit();
        } else if (id == R.id.nav_mon_compte) {
            AccountFragment accountFragment = new AccountFragment();
            bundle.putParcelable("nomsGolf",jsonReader);
            accountFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main, accountFragment)
                    .commit();
        } else if (id == R.id.nav_appareillage) {
            // Check if the BLE is available on the device
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_CODE_ENABLE_BLUETOOTH);
                } else {
                    AppareillageFragment appareillageFragment = new AppareillageFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_main, appareillageFragment)
                            .commit();
                }
            }
            else {
                Toast.makeText(this, "pas de BLE dans ce portable \n impossible d'utiliser le NoRough", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void ClickListener(String name ) {

        switch (name) {
            case "play":
                this.activityFragment = new ActivityFragment();
                this.scoreFragment = new ScoreFragment();
                bundle.putParcelable("jsonReader",jsonReader);
                activityFragment.setArguments(bundle);
                this.mapsFragment = new MapsFragment();
                mapsFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.content_main, activityFragment)
                        .commit();
                break;
            case "goToHistory":
                HistoryFragment historyFragment = new HistoryFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_main, historyFragment)
                        .commit();
                break;

            case "goToAccount":
                AccountFragment accountFragment = new AccountFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_main, accountFragment)
                        .commit();
                break;

            case "goToGetGolf":
                GetGolf getGolf = new GetGolf();
                bundle.putParcelable("jsonReader",jsonReader);
                getGolf.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.content_main, getGolf)
                        .commit();
                break;

            case"chooseGolf":
                ChooseGolfFragment chooseGolfFragment = new ChooseGolfFragment();
                bundle.putParcelable("jsonReader",jsonReader);
                bundle.putParcelable("nomsGolf",jsonReader);
                chooseGolfFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.content_main, chooseGolfFragment)
                        .commit();
                break;

            case "goToHistoryScoreFragment":

                HistoryScoreFragment historyScoreFragment = new HistoryScoreFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("ID",1868940442);
                historyScoreFragment.setArguments(bundle);

                fragmentManager.beginTransaction()
                        .replace(R.id.content_main,historyScoreFragment)
                        .commit();
                break;





            default:
                break;

        }
    }

    @Override
    public  void ClickListener(String name ,int from , int score,int position){
        switch (name){
            case "sharePartie":
                AmisFragment amisFragment = new AmisFragment();
                bundle.putInt("from", from);
                bundle.putInt("position", position);
                bundle.putInt("score", score);


                amisFragment.setArguments(bundle);

                fragmentManager.beginTransaction()
                        .replace(R.id.content_main,amisFragment)
                        .commit();
                break;
        }
    }

    @Override
    public void ClickListener(String name, int id){
        switch (name) {
            case "goToHistoryScoreFragment":

                HistoryScoreFragment historyScoreFragment = new HistoryScoreFragment();
                bundle.putInt("ID",id);
                historyScoreFragment.setArguments(bundle);

                fragmentManager.beginTransaction()
                        .replace(R.id.content_main,historyScoreFragment)
                        .commit();
                break;


            default:
                break;
        }
    }

    @Override
    public void ClickListener(String name, String data){
        switch (name) {
            case "calibration":
                if(mConnected) {
                    characteristicTX.setValue(data);
                    mBluetoothLeService.writeCharacteristic(characteristicTX);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void BluetoothDeviceSet(String name, String address) {
        //bind Service
        mDeviceAddress=address;
        mDeviceName=name;
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void LocationChangeCalcul(double latitude, double longitude) {
        activityFragment.setTextStart(latitude,longitude); // This calls the method setTextStart in the Control fragment (ActivityFragment).
        mapsFragment.setLatLong(latitude,longitude);   // This calls the method setLatLong in Maps Fragment;
    }

    @Override
    public void TrouChangeListener(int num){
        activityFragment.setNum(num);
        //mapsFragment.setNum(num);
    }

    // set the fragment to launch if the bluetooth is enable at runtime
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == -1) {
            AppareillageFragment appareillageFragment = new AppareillageFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main, appareillageFragment)
                    .commit();
        } else {
            Toast.makeText(getApplicationContext(),"La Bluetooth n'a pas été allumé. Veuillez réessayer.", Toast.LENGTH_LONG).show();
        }
    }





    // Début de la connection au Bluetooth

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private String mDeviceName;
    private String mDeviceAddress;
    private String data;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private BluetoothGattCharacteristic characteristicTX;
    private BluetoothGattCharacteristic characteristicRX;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    public final static UUID HM_11_RX_TX = UUID.fromString(SampleGattAttributes.HM_11_RX_TX);
    public final static UUID HM_11_CONF = UUID.fromString(SampleGattAttributes.HM_11_CONF);


    //Management of the Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize())
                finish();
            // Automatically connects to the device upon successful start-up initialization
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                characteristicTX.setValue("5:"+data);
                mBluetoothLeService.writeCharacteristic(characteristicTX);
                characteristicRX.setValue(data);
                mBluetoothLeService.broadcastUpdate(ACTION_DATA_VERIFIED, characteristicRX);
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_VERIFIED);
        return intentFilter;
    }

    //Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));

            //if the service exists for HM 11 Serial
            if (SampleGattAttributes.lookup(uuid, unknownServiceString) == "HM-11 Serial") {
                currentServiceData.put(LIST_UUID, uuid);
                gattServiceData.add(currentServiceData);

                //get characteristic when UUID matches RX/TX UUID
                characteristicTX = gattService.getCharacteristic(BluetoothLeService.UUID_HM_11_RX_TX);
                characteristicRX = gattService.getCharacteristic(BluetoothLeService.UUID_HM_11_RX_TX);
            }
        }
    }

}
