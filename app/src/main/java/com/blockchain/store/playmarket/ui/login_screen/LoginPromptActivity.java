package com.blockchain.store.playmarket.ui.login_screen;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.UserListAdapter;
import com.blockchain.store.playmarket.interfaces.LoginPromptCallback;
import com.blockchain.store.playmarket.ui.login_screen.fingerprint_configuring_screen.FingerprintConfiguringFragment;
import com.blockchain.store.playmarket.ui.login_screen.welcome_screen.WelcomeFragment;
import com.blockchain.store.playmarket.ui.login_screen.password_prompt_screen.PasswordPromptFragment;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.NonSwipeableViewPager;
import com.blockchain.store.playmarket.utilities.ViewPagerAdapter;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginPromptActivity extends AppCompatActivity implements LoginPromptContract.View, LoginPromptCallback {

    private LoginPromptPresenter presenter;
    private AlertDialog importDialog;
    private UserListAdapter adapter;
    private LoginViewModel loginViewModel;

    @BindView(R.id.login_viewPager) NonSwipeableViewPager loginViewPager;
    private ViewPagerAdapter loginAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_prompt);
        ButterKnife.bind(this);

        presenter = new LoginPromptPresenter();
        presenter.init(this);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        loginAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        loginAdapter.addFragment(new WelcomeFragment());
        loginAdapter.addFragment(new PasswordPromptFragment());
        loginAdapter.addFragment(new FingerprintConfiguringFragment());
        loginViewPager.setAdapter(loginAdapter);

        if (AccountManager.isHasUsers()) {
            goToMainActivity(null);
        } else {
            if (presenter.checkJsonFileExists()) showImportUserDialog();
        }
    }

    public void goToMainActivity(View view) {
        startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
        finish();
    }

    @Override
    public void showImportUserDialog() {
        View view = getLayoutInflater().inflate(R.layout.import_user_dialog, null);

        importDialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create();

        RecyclerView userListRecyclerView = (RecyclerView) view.findViewById(R.id.json_keysore_files_recycler_view);
        Button importButton = (Button) view.findViewById(R.id.import_button);
        Button cancelButton = (Button) view.findViewById(R.id.close_manager_button);

        // Получаем список файлов "JsonKeystoreFileList" из каталога "/PlayMarket2.0/Accounts/".
        ArrayList<File> userList = presenter.getJsonKeystoreCollection();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        userListRecyclerView.setLayoutManager(layoutManager);
        // Объявляем и устанавливаем адаптер для "userListRecyclerView".
        adapter = new UserListAdapter(userList);
        userListRecyclerView.setAdapter(adapter);

        importButton.setOnClickListener(v -> {
            if (adapter.getSelectedItem() == -1) {
                Toast.makeText(this, "You need chose one of the accounts", Toast.LENGTH_SHORT).show();
            } else {
                File selectedUserJsonFile = userList.get(adapter.getSelectedItem());
                String jsonData = presenter.getDataFromJsonKeystoreFile(selectedUserJsonFile, "all_data");
                presenter.saveJsonDataOnViewModel(loginViewModel, jsonData);
                importDialog.dismiss();
                openPasswordPromptFragment();
            }
        });

        cancelButton.setOnClickListener(v -> importDialog.dismiss());

        importDialog.show();
    }

    @Override
    public void openWelcomeFragment() {
        loginViewPager.setCurrentItem(0, true);
    }

    @Override
    public void openPasswordPromptFragment() {
        loginViewPager.setCurrentItem(1, true);
    }

    @Override
    public void openFingerprintConfigFragment() {
        loginViewPager.setCurrentItem(2, true);
    }

    @Override
    public void onBackPressed() {
        if (loginViewPager.getCurrentItem() == 1) {
            loginViewModel.jsonData.setValue(null);
            loginViewModel.accountPassword.setValue(null);
            openWelcomeFragment();
        }
        else if (loginViewPager.getCurrentItem() == 0) super.onBackPressed();
    }
}
