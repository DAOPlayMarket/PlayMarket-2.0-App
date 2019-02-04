package com.blockchain.store.playmarket.ui.login_screen.welcome_screen;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.interfaces.LoginPromptCallback;
import com.blockchain.store.playmarket.ui.file_manager_screen.FileManagerActivity;
import com.blockchain.store.playmarket.ui.login_screen.LoginViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeFragment extends Fragment {
    @BindView(R.id.import_account_button) TextView importAccount;
    private LoginPromptCallback loginPromptCallback;
    private LoginViewModel loginViewModel;

    public static WelcomeFragment openAsCreateNewFragment() {
        Bundle args = new Bundle();
        WelcomeFragment fragment = new WelcomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        ButterKnife.bind(this, view);
        importAccount.setText(Html.fromHtml(getString(R.string.import_account_button)));
        loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        return view;
    }

    @OnClick(R.id.create_account_button)
    void createAccountButtonPressed() {
        loginPromptCallback.openPasswordPromptFragment();
    }

    @OnClick(R.id.import_account_button)
    void importAccountButtonPressed() {
        Intent intent = new Intent(getContext(), FileManagerActivity.class);
        intent.putExtra(FileManagerActivity.START_FILE_MANAGER_TAG, "all_files");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginPromptCallback) {
            loginPromptCallback = (LoginPromptCallback) context;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String jsonData = data.getStringExtra("json_data");
            loginViewModel.jsonData.setValue(jsonData);
            loginPromptCallback.openPasswordPromptFragment();
        }
    }
}
