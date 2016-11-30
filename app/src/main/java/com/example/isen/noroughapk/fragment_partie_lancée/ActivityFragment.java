package com.example.isen.noroughapk.fragment_partie_lancée;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.isen.noroughapk.R;
import com.example.isen.noroughapk.json_helper.JsonReader;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private JsonReader jsonReader;

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

        jsonReader = new JsonReader(getContext());
        jsonReader.execute();

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
}
