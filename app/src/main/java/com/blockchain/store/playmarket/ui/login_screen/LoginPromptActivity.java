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
import com.blockchain.store.playmarket.ui.login_screen.password_prompt_screen.PasswordPromptFragment;
import com.blockchain.store.playmarket.ui.login_screen.welcome_screen.WelcomeFragment;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.ui.new_user_welcome_activity.NewUserWelcomeActivity;
import com.blockchain.store.playmarket.utilities.BaseActivity;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.NonSwipeableViewPager;
import com.blockchain.store.playmarket.utilities.ViewPagerAdapter;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginPromptActivity extends BaseActivity implements LoginPromptContract.View, LoginPromptCallback {

    private LoginPromptPresenter presenter;
    private AlertDialog importDialog;
    private UserListAdapter adapter;
    private LoginViewModel loginViewModel;

    public enum START_OPTION {
        NEW_ACCOUNT, IMPORT_ACCOUNT
    }

    @BindView(R.id.login_viewPager) NonSwipeableViewPager loginViewPager;
    private ViewPagerAdapter loginAdapter;

    private START_OPTION startOption;
    private String jsonData;

    public static void startAsAddNewAccount(AppCompatActivity activity, int requestCode) {
        Intent intent = new Intent(activity, LoginPromptActivity.class);
        intent.putExtra("startOption", START_OPTION.NEW_ACCOUNT);
        activity.startActivityForResult(intent, requestCode);

    }

    public static void startAsImportccount(AppCompatActivity activity, int requestCode, String filepath) {
        Intent intent = new Intent(activity, LoginPromptActivity.class);
        intent.putExtra("startOption", START_OPTION.IMPORT_ACCOUNT);
        intent.putExtra("filepath", filepath);
        activity.startActivityForResult(intent, requestCode);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_prompt);
        ButterKnife.bind(this);


        presenter = new LoginPromptPresenter();
        presenter.init(this);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        if (getIntent() != null) {
            startOption = (START_OPTION) getIntent().getSerializableExtra("startOption");
            if (startOption == START_OPTION.IMPORT_ACCOUNT) {
                jsonData = getIntent().getStringExtra("filepath");
                if (jsonData == null) this.finish();
                loginViewModel.jsonData.setValue(jsonData);
            }
        }

        loginAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        if (startOption == null) {
            loginAdapter.addFragment(new WelcomeFragment());
        }
        loginAdapter.addFragment(new PasswordPromptFragment());
        loginViewPager.setAdapter(loginAdapter);

        if (presenter.checkJsonFileExists() && startOption == null) showImportUserDialog();

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

        cancelButton.setOnClickListener(v -> {
            importDialog.dismiss();
        });

        importDialog.show();
    }

    @Override
    public void openWelcomeFragment() {
        if(startOption != null){
            this.finish();
        }
        loginViewPager.setCurrentItem(0, true);
    }

    @Override
    public void openPasswordPromptFragment() {
        loginViewPager.setCurrentItem(1, true);
    }

    public void openNextActivity(String address) {
        if (startOption != null) {
            this.setResult(RESULT_OK);
            this.finish();
        } else {
            openWelcomeActivity(address);
        }
    }

    public void openMainActivity() {
        if (startOption == null) {
            startActivity(new Intent(this, MainMenuActivity.class));
            this.finish();
        }
        this.finish();
    }


    public void openWelcomeActivity(String address) {
        Intent intent = new Intent(this, NewUserWelcomeActivity.class);
        intent.putExtra(Constants.WELCOME_ACTIVITY_ADDRESS_EXTRA, address);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (loginViewPager.getCurrentItem() == 1) {

            loginViewModel.jsonData.setValue(null);
            openWelcomeFragment();
        } else if (loginViewPager.getCurrentItem() == 0) super.onBackPressed();
    }

}
