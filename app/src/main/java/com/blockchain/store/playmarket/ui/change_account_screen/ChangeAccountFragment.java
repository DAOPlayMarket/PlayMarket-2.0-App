package com.blockchain.store.playmarket.ui.change_account_screen;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.ChangeAccountAdapter;
import com.blockchain.store.playmarket.ui.file_manager_screen.FileManagerActivity;
import com.blockchain.store.playmarket.ui.login_screen.LoginPromptActivity;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.DialogManager;
import com.blockchain.store.playmarket.views.CustomAlertDialog;

import org.ethereum.geth.Account;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity.IMPORT_ACCOUNT_REQUEST_CODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeAccountFragment extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private ChangeAccountAdapter adapter;

    public ChangeAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_account, container, false);
        ButterKnife.bind(this, view);
        initAdapter();
        return view;
    }

    private void initAdapter() {
        adapter = new ChangeAccountAdapter(new ChangeAccountAdapter.adapterCallback() {
            @Override
            public void onAddressClicked(int position) {
                AccountManager.setCurrentUserPosition(position);
            }

            @Override
            public void onDeleteAccountClicked(Account account) {
                CustomAlertDialog customAlertDialog = new CustomAlertDialog(getActivity()).setAccount(account);
                customAlertDialog.setOnDismissListener(dialogInterface -> initAdapter());
                customAlertDialog.show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.close_button)
    void onCloseClicked() {
        getActivity().onBackPressed();
    }


    @OnClick(R.id.fab)
    void onFabClicked() {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.change_account_bottom_sheet, null);
        mBottomSheetDialog.setContentView(view);

        mBottomSheetDialog.findViewById(R.id.new_wallet_holder).setOnClickListener
                (v -> {
                    LoginPromptActivity.startAsAddNewAccount((AppCompatActivity) getActivity(), MainMenuActivity.CHANGE_ACCOUNT_REQUEST_CODE);
                    mBottomSheetDialog.dismiss();
                });
        mBottomSheetDialog.findViewById(R.id.import_wallet_holder).setOnClickListener
                (v -> {
                    Intent intent = new Intent(getActivity(), FileManagerActivity.class);
                    intent.putExtra(FileManagerActivity.START_FILE_MANAGER_TAG, "all_files");
                    getActivity().startActivityForResult(intent, IMPORT_ACCOUNT_REQUEST_CODE);
                    mBottomSheetDialog.dismiss();
                });
        mBottomSheetDialog.show();
    }

    @OnClick(R.id.testUnlockAccount)
    void onTestUnlockAccountClicked() {
        new DialogManager().showDividendsDialog(getActivity(), () -> Toast.makeText(getActivity(), AccountManager.getAddress().getHex() + " :Account unlocked!", Toast.LENGTH_SHORT).show());
    }

    public void updateAdapter() {
        initAdapter();
    }
}
