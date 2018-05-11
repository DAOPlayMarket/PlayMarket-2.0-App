package com.blockchain.store.playmarket.ui.login_screen.password_prompt_screen;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.interfaces.LoginPromptCallback;
import com.blockchain.store.playmarket.ui.login_screen.LoginViewModel;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.ui.new_user_welcome_activity.NewUserWelcomeActivity;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PasswordPromptFragment extends Fragment implements PasswordPromptContract.View {

    private LoginPromptCallback loginPromptCallback;
    private PasswordPromptPresenter presenter;
    private LoginViewModel loginViewModel;
    private String jsonData;

    @BindView(R.id.password_editText)
    EditText passwordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_prompt, container, false);
        ButterKnife.bind(this, view);

        presenter = new PasswordPromptPresenter();
        presenter.init(this);

        loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        loginViewModel.jsonData.observe(getActivity(), s -> jsonData = s);

        return view;
    }

    @OnClick(R.id.cancel_login_button)
    void cancelLoginButtonClicked() {
        loginViewModel.jsonData.setValue(null);
        loginViewModel.accountPassword.setValue(null);
        loginPromptCallback.openWelcomeFragment();
    }

    @OnClick(R.id.continue_login_button)
    void continueLoginButtonClicked() {
        if (jsonData == null) {
            if (passwordEditText.getText().toString().isEmpty()) {
                passwordEditText.setError(getResources().getString(R.string.empty_password));
            } else if (passwordEditText.getText().length() < 7) {
                passwordEditText.setError(getResources().getString(R.string.short_password));
            } else {
                if (presenter.checkSensorState(getContext()).equals(PasswordPromptContract.sensorState.NOT_SUPPORTED)) {
                    String address = presenter.createNewAccount(passwordEditText.getText().toString());
                    openWelcomeActivity(address);
                }
                else {
                    loginViewModel.accountPassword.setValue(passwordEditText.getText().toString());
                    loginPromptCallback.openFingerprintConfigFragment();
                }
            }

        } else {
            if (presenter.importAccount(jsonData, passwordEditText.getText().toString())) {
                if (presenter.checkSensorState(getContext()).equals(PasswordPromptContract.sensorState.NOT_SUPPORTED)) {
                    goToMainActivity();
                    ToastUtil.showToast(R.string.import_successful);
                }
                else{
                    loginViewModel.accountPassword.setValue(passwordEditText.getText().toString());
                    loginPromptCallback.openFingerprintConfigFragment();
                }
            } else {
                passwordEditText.setError(getResources().getString(R.string.wrong_password));
            }
        }
    }

    public void goToMainActivity() {
        startActivity(new Intent(getContext(), MainMenuActivity.class));
        getActivity().finish();
    }

    public void openWelcomeActivity(String address) {
        Intent intent = new Intent(getContext(), NewUserWelcomeActivity.class);
        intent.putExtra(Constants.WELCOME_ACTIVITY_ADDRESS_EXTRA, address);
        startActivity(intent);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginPromptCallback) {
            loginPromptCallback = (LoginPromptCallback) context;
        }
    }

    @Override
    public void showToast(Boolean success) {
        if (success) ToastUtil.showToast(R.string.success_autosave_message);
        else ToastUtil.showToast(R.string.failed_autosave_message);
    }
}
