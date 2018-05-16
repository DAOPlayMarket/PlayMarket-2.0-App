package com.blockchain.store.playmarket.ui.login_screen.fingerprint_configuring_screen;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.ui.login_screen.LoginViewModel;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.ui.new_user_welcome_activity.NewUserWelcomeActivity;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.mattprecious.swirl.SwirlView;
import com.mtramin.rxfingerprint.RxFingerprint;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

public class FingerprintConfiguringFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private String accountPassword;
    private Disposable fingerprintDisposable = Disposables.empty();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fingerprint_configuring, container, false);
        ButterKnife.bind(this, view);

        loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);

        loginViewModel.accountPassword.observe(getActivity(), s -> {
            if(s == null || s.isEmpty()){
                return;
            }
            accountPassword = s;
            encryptData();
        });

        return view;
    }

    @OnClick(R.id.dont_activate_button) void doNotActivateButtonClicked(){
        fingerprintDisposable.dispose();
        loginingFinish();
    }

    void encryptData() {
        fingerprintDisposable = RxFingerprint.encrypt(getContext(), accountPassword)
                .subscribe(fingerprintEncryptionResult -> {
                    switch (fingerprintEncryptionResult.getResult()) {
                        case FAILED:
                            Toast.makeText(getContext(), "Fingerprint not recognized, try again!", Toast.LENGTH_LONG).show();
                            break;
                        case HELP:
                            Toast.makeText(getContext(), fingerprintEncryptionResult.getMessage(), Toast.LENGTH_LONG).show();
                            break;
                        case AUTHENTICATED:
                            String encrypted = fingerprintEncryptionResult.getEncrypted();
                            Hawk.put(Constants.ENCRYPTED_PASSWORD, encrypted);
                            Toast.makeText(getContext(), "encryption successful:" + encrypted, Toast.LENGTH_LONG).show();

                            loginingFinish();
                            break;
                    }
                }, throwable -> {
                    //noinspection StatementWithEmptyBody
                    if (RxFingerprint.keyInvalidated(throwable)) {
                        // The keys you wanted to use are invalidated because the user has turned off his
                        // secure lock screen or changed the fingerprints stored on the device
                        // You have to re-encrypt the data to access it
                    }
                    Log.e("ERROR", "encrypt", throwable);
                    Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                    ToastUtil.showToast(throwable.getMessage());
                });
    }

    public void openWelcomeActivity(String address) {
        Intent intent = new Intent(getContext(), NewUserWelcomeActivity.class);
        intent.putExtra(Constants.WELCOME_ACTIVITY_ADDRESS_EXTRA, address);
        startActivity(intent);
    }

    private void openMainActivity() {
        startActivity(new Intent(getContext(), MainMenuActivity.class));
        getActivity().finish();
    }

    private void loginingFinish(){
        if (loginViewModel.jsonData == null){
            String account = AccountManager.getAddress().getHex();
            openWelcomeActivity(account);
        }
        else openMainActivity();
    }
}
