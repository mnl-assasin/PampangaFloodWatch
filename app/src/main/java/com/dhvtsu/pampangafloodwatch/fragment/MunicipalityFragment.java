package com.dhvtsu.pampangafloodwatch.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhvtsu.pampangafloodwatch.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MunicipalityFragment extends Fragment {


    public MunicipalityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_municipality, container, false);
        return v;
    }

}
