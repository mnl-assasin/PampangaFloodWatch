package com.dhvtsu.pampangafloodwatch.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhvtsu.pampangafloodwatch.R;
import com.dhvtsu.pampangafloodwatch.adapter.HistoryPagerAdapter;
import com.dhvtsu.pampangafloodwatch.sliding.SlidingTabLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {


    @Bind(R.id.tabs)
    SlidingTabLayout tabs;
    @Bind(R.id.pager)
    ViewPager pager;

    CharSequence titles[] = {"Bacolor", "Florida Blanca", "Lubao"};
    HistoryPagerAdapter adapter;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, v);

        initPager();
        getActivity().setTitle("HELLO WORLD");
        return v;
    }

    private void initPager() {
        adapter = new HistoryPagerAdapter(getChildFragmentManager(), titles);
        pager.setAdapter(adapter);

        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.textColorPrimaryDark);
            }
        });
        tabs.setViewPager(pager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
