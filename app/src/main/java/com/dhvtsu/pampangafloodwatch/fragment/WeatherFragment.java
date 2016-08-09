package com.dhvtsu.pampangafloodwatch.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dhvtsu.pampangafloodwatch.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {


    @Bind(R.id.webview)
    WebView wv1;

    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, view);

        initWeb();
        return view;
    }

    private void initWeb() {

        wv1.setWebViewClient(new MyBrowser());

        String url = "https://earth.nullschool.net/#current/wind/surface/level/orthographic=-236.36,11.87,3000/loc=121.022,15.198";
        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.loadUrl(url);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
