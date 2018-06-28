package com.blockchain.store.playmarket.ui.ico_screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class IcoFragment extends Fragment implements IcoFragmentContracts.View {
    private static final String TAG = "IcoFragment";

    IcoFragmentPresenter presenter;

    public IcoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ico, container, false);
        bindViews();
        attachPresenter();
        getIcoApps();
        return view;
    }

    private void attachPresenter() {
        presenter = new IcoFragmentPresenter();
        presenter.init(this);
    }

    private void getIcoApps() {
        presenter.getIcoApps();
    }

    private void bindViews() {

    }

    @Override
    public void onIcoAppsReady(ArrayList<App> apps) {
        Log.d(TAG, "onIcoAppsReady: ");
    }

    @Override
    public void onIcoAppsFailed(Throwable throwable) {
        Log.d(TAG, "onIcoAppsFailed: ");
    }
}
