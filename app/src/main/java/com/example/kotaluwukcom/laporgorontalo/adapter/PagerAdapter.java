package com.example.kotaluwukcom.laporgorontalo.adapter;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.kotaluwukcom.laporgorontalo.fragment.HomeFragment;
import com.example.kotaluwukcom.laporgorontalo.fragment.MapsFragment;
import com.example.kotaluwukcom.laporgorontalo.fragment.ProfileFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int number_tabs;

    public PagerAdapter(FragmentManager fm, int number_tabs) {
        super(fm);
        this.number_tabs = number_tabs;
    }

    //Mengembalikan Fragment yang terkait dengan posisi tertentu
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new MapsFragment();
            case 2 :
                return new ProfileFragment();
            default:
                return null;
        }
    }

    //Mengembalikan jumlah tampilan yang tersedia.
    @Override
    public int getCount() {
        return number_tabs;
    }
}