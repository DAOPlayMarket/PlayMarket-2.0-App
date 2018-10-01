package com.blockchain.store.playmarket.ui.add_token_screen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.Token;
import com.blockchain.store.playmarket.repositories.TransactionRepository;
import com.blockchain.store.playmarket.utilities.AccountManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTokenActivity extends AppCompatActivity {
    private static final String TAG = "AddTokenActivity";

    @BindView(R.id.address_text) EditText addressText;
    @BindView(R.id.sent_action) Button sendAction;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.tokenTitle) TextView tokenTitle;
    @BindView(R.id.tokenSymbol) TextView tokenSymbol;
    @BindView(R.id.tokenDecimals) TextView tokenDecimals;
    @BindView(R.id.tokenBalanceof) TextView tokenBalanceof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_token);
        ButterKnife.bind(this);
        addressText.setText("0x538106e553f5ba3298199c1998ba061922815a6c");


    }

    private void onError(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void onOk(Token token) {
        tokenTitle.setText(token.name);
        tokenSymbol.setText(token.symbol);
        tokenDecimals.setText(token.decimals);
        tokenBalanceof.setText(token.balanceOf);
    }

    @OnClick(R.id.sent_action)
    void OnSentClicked() {
        TransactionRepository.test(addressText.getText().toString(), AccountManager.getAddress().getHex()).subscribe(this::onOk, this::onError);
    }
}
