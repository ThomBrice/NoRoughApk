package com.example.isen.noroughapk.fragment_partie_lancée;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isen.noroughapk.R;
import com.example.isen.noroughapk.json_helper.JsonReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.*;

/**
 * Created by Thomas B on 02/11/2016.
 */

public class ActivityFragment extends Fragment{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private JsonReader jsonReader;
    private Bundle bundle;
    private TextView startGreen;
    private TextView midGreen;
    private TextView endGreen;
    private HashMap<String, String> trou;
    private CalculDistances calculDistances;

    public ActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_activity, container, false);

        viewPager = (ViewPager) result.findViewById(R.id.viewpager_activité);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) result.findViewById(R.id.tabs_activité);
        tabLayout.setupWithViewPager(viewPager); // Assigns the ViewPager to TabLayout

        startGreen = (TextView) result.findViewById(R.id.start_green);
        midGreen = (TextView) result.findViewById(R.id.mid_green);
        endGreen = (TextView) result.findViewById(R.id.end_green);

        bundle = this.getArguments();
        if(bundle !=null){
            jsonReader = bundle.getParcelable("jsonReader");
        }

        trou = jsonReader.getCoordonneesList().get(0);

        calculDistances = new CalculDistances();

        return result;
    }

    public void setupViewPager(ViewPager upViewPager) {
        // Defines the number of tabs by setting appropriate fragment and tab name.
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MapsFragment(), "Maps");
        adapter.addFragment(new ScoreFragment(), "Score");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        // Custom adapter class provides fragments required for the view pager
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position){
        return mFragmentTitleList.get(position);
        }

    }

    public void setTextStart(Double latitude, Double longitude){
/*
        Double RadLatitude = convertRad(latitude);
        Double RadLongitude = convertRad(longitude);
        Double RadLatS = convertRad(Double.parseDouble(trou.get("LatS")));
        Double RadLongS = convertRad(Double.parseDouble(trou.get("LonS")));

        Double distanceS = 2 * asin(sqrt((sin((RadLatitude - RadLatS) / 2))*(sin((RadLatitude - RadLatS) / 2)) +
                cos(RadLatitude) * cos(RadLatS)*(sin((RadLongitude - RadLongS) / 2))*(sin((RadLongitude - RadLongS) / 2))))* 6366;
        RadLatS = convertRad(Double.parseDouble(trou.get("LatM")));
        RadLongS = convertRad(Double.parseDouble(trou.get("LonM")));
        Double distanceM = 2 * asin(sqrt((sin((RadLatitude - RadLatS) / 2))*(sin((RadLatitude - RadLatS) / 2)) +
                cos(RadLatitude) * cos(RadLatS)*(sin((RadLongitude - RadLongS) / 2))*(sin((RadLongitude - RadLongS) / 2))))* 6366;
        RadLatS = convertRad(Double.parseDouble(trou.get("LatE")));
        RadLongS = convertRad(Double.parseDouble(trou.get("LonE")));
        Double distanceE = 2 * asin(sqrt((sin((RadLatitude - RadLatS) / 2))*(sin((RadLatitude - RadLatS) / 2)) +
                cos(RadLatitude) * cos(RadLatS)*(sin((RadLongitude - RadLongS) / 2))*(sin((RadLongitude - RadLongS) / 2))))* 6366;

        startGreen.setText(String.format(String.valueOf(distanceS.intValue()),"#,##"));
        midGreen.setText(String.valueOf(distanceM.intValue()));
        endGreen.setText(String.valueOf(distanceE.intValue()));
*/
        calculDistances = new CalculDistances(latitude,longitude,Double.parseDouble(trou.get("LatS")),Double.parseDouble(trou.get("LonS")),
                                           Double.parseDouble(trou.get("LatM")),Double.parseDouble(trou.get("LonM")),
                                           Double.parseDouble(trou.get("LatE")),Double.parseDouble(trou.get("LonE")));

        calculDistances.execute();
        /*
        valeurs = calculDistances.getValeurs();
        startGreen.setText(String.format("%1$.3f",valeurs[0]));
        midGreen.setText(String.format("%1$.3f",valeurs[1]));
        endGreen.setText(String.format("%1$.3f",valeurs[2]));
        */
    }

    public class CalculDistances extends AsyncTask<Double,Integer,Double> {

        Double mLat;
        Double mLong;
        Double latS;
        Double lonS;
        Double latM;
        Double lonM;
        Double latE;
        Double lonE;
        Double[] valeurs = new Double[3];

        public CalculDistances() {

        }

        public CalculDistances(Double mLat, Double mLong, Double latS, Double lonS, Double latM, Double lonM, Double latE, Double lonE) {
            this.latS = latS;
            this.mLong = mLong;
            this.mLat = mLat;
            this.lonS = lonS;
            this.latM = latM;
            this.lonM = lonM;
            this.latE = latE;
            this.lonE = lonE;
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
            startGreen.setText(String.format("%1$.2f",valeurs[0]));
            midGreen.setText(String.format("%1$.2f",valeurs[1]));
            endGreen.setText(String.format("%1$.2f",valeurs[2]));
            Toast.makeText(getContext(),"refresh",Toast.LENGTH_SHORT);
        }

        public void Calcul(){
            Double RadLatitude = convertRad(mLat);
            Double RadLongitude = convertRad(mLong);

            Double RadLat = convertRad(latS);
            Double RadLong = convertRad(lonS);
            Double distanceS = 2 * asin(sqrt((sin((RadLatitude - RadLat) / 2))*(sin((RadLatitude - RadLat) / 2)) +
                    cos(RadLatitude) * cos(RadLat)*(sin((RadLongitude - RadLong) / 2))*(sin((RadLongitude - RadLong) / 2))))* 6366;

            RadLat = convertRad(latM);
            RadLong = convertRad(lonM);
            Double distanceM = 2 * asin(sqrt((sin((RadLatitude - RadLat) / 2))*(sin((RadLatitude - RadLat) / 2)) +
                    cos(RadLatitude) * cos(RadLat)*(sin((RadLongitude - RadLong) / 2))*(sin((RadLongitude - RadLong) / 2))))* 6366;

            RadLat = convertRad(latE);
            RadLong = convertRad(lonE);
            Double distanceE = 2 * asin(sqrt((sin((RadLatitude - RadLat) / 2))*(sin((RadLatitude - RadLat) / 2)) +
                    cos(RadLatitude) * cos(RadLat)*(sin((RadLongitude - RadLong) / 2))*(sin((RadLongitude - RadLong) / 2))))* 6366;

            valeurs[0] = distanceS * 1000; //résultat en mètres
            valeurs[1] = distanceM * 1000;
            valeurs[2] = distanceE * 1000;
        }

        Double convertRad(Double var) {
            return (Math.PI * var) / 180.;
        }
    }
}
