package com.dhvtsu.pampangafloodwatch.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dhvtsu.pampangafloodwatch.fragment.HistoryViewFragment;

/**
 * Created by mleano on 7/22/2016.
 */
public class HistoryPagerAdapter extends FragmentStatePagerAdapter {


    CharSequence titles[];
    int count;

    public HistoryPagerAdapter(FragmentManager fm, CharSequence[] titles) {
        super(fm);
        this.titles = titles;
        this.count = titles.length;
    }


    @Override
    public Fragment getItem(int position) {
        return HistoryViewFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
