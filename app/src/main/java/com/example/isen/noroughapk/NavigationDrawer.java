package com.example.isen.noroughapk;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.isen.noroughapk.Interfaces.ClickListenerFragment;
import com.example.isen.noroughapk.Interfaces.LocationChangeCalcul;
import com.example.isen.noroughapk.fragment_partie_lancée.ActivityFragment;
import com.example.isen.noroughapk.fragment_partie_lancée.MapsFragment;
import com.example.isen.noroughapk.json_helper.JsonReader;
import com.example.isen.noroughapk.Class.Player;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ClickListenerFragment,LocationChangeCalcul{

    FragmentManager fragmentManager = getSupportFragmentManager();
    NavigationView navigationView;
    Bundle bundle = new Bundle();
    Player player;
    ActivityFragment activityFragment;

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
    }

    @Override
    public void onResume(){
        super.onResume();

        player = new Player();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            player = bundle.getParcelable("PlayerFromFrag");}
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView.getMenu().performIdentifierAction(R.id.nav_start,0);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        } else if (id == R.id.nav_amis) {

        } else if (id == R.id.nav_mon_compte) {
            AccountFragment accountFragment = new AccountFragment();
            bundle.putParcelable("nomsGolf",jsonReader);
            bundle.putParcelable("Player",player);
            accountFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main, accountFragment)
                    .commit();
        } else if (id == R.id.nav_appareillage) {

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
                bundle.putParcelable("jsonReader",jsonReader);
                activityFragment.setArguments(bundle);
                MapsFragment mapsFragment = new MapsFragment();
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
    public void LocationChangeCalcul(double latitude, double longitude) {
        activityFragment.setTextStart(latitude,longitude); // This calls the method setTextStart in the Control fragment (ActivityFragment).

    }
}
