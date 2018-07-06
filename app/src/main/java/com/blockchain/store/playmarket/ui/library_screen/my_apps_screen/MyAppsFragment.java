package com.blockchain.store.playmarket.ui.library_screen.my_apps_screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.SearchListAdapter;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.utilities.Constants;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAppsFragment extends Fragment {
    private static final String TAG = "MyAppsFragment";

    private SearchListAdapter adapter;
    private ArrayList<App> app = new ArrayList<>();
    private boolean isShowOnlyUpdatedApps = false;

    public MyAppsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_apps, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void loadSavedApps() {
        if (Hawk.contains(Constants.DOWNLOADED_APPS_LIST)) {
            app = Hawk.get(Constants.DOWNLOADED_APPS_LIST);
        }
        populateRecyclerView(app);
    }

    private void populateRecyclerView(ArrayList<App> apps) {
        if (apps.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            adapter = new SearchListAdapter(apps, this);
            recyclerView.setAdapter(adapter);
        }

    }

}
