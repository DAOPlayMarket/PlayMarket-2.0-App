package com.blockchain.store.playmarket.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blockchain.store.playmarket.data.content.AppContent;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.utilities.data.ClipboardUtils;
import com.blockchain.store.playmarket.utilities.installer.ApkInstaller;
import com.blockchain.store.playmarket.utilities.net.APIUtils;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.blockchain.store.playmarket.utilities.data.ImageUtils;
import com.blockchain.store.playmarket.utilities.device.PermissionUtils;

import org.ethereum.geth.BigInt;
import org.ethereum.geth.Transaction;

import io.ethmobile.ethdroid.KeyManager;

/**
 * An activity representing a single App detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link }.
 */
public class AppDetailActivityOld extends AppCompatActivity {

    ImageView iconView;
    TextView appTitleHeader;
    TextView titleViewBody;
    TextView priceTextView;
    TextView developerTextView;
    Button buyButton;
    EthereumPrice price;
    boolean free;
    String idApp2 = "";
    int idApp = 0;
    int idCat = 0;
    String hashIPFS;
    String descriptionText;
    int imageCount;

    private KeyManager keyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail_old);

        setupKeyManager();

        appTitleHeader = (TextView) findViewById(R.id.AppNameTitle);
        developerTextView = (TextView) findViewById(R.id.developerName);
        priceTextView = (TextView) findViewById(R.id.AppPrice);
        buyButton = (Button) findViewById(R.id.buyBtn);
        titleViewBody = (TextView) findViewById(R.id.AppNameBody);
        iconView = (ImageView) findViewById(R.id.iconView);

        AppContent.AppItem item = (AppContent.AppItem) getIntent().getExtras().get("item");

        idApp = Integer.valueOf(item.id);
        idApp2 = item.appId;
        idCat = Integer.valueOf(item.category);
        price = new EthereumPrice(String.valueOf(item.price));
        free = item.free;
        hashIPFS = item.hashIPFS;
        descriptionText = item.description;
        imageCount = item.imageCount;

        appTitleHeader.setText(item.name);
        titleViewBody.setText(item.name);
        developerTextView.setText(item.developer);

        if (item.icon != null) {
            iconView.setImageBitmap(ImageUtils.getBitmapFromBase64(item.icon));
        }

        if (free) {
            priceTextView.setText("Free");
            buyButton.setText("Download");
        } else {
            String priceUnit = price.getUnits();
            if  (priceUnit.equals("ETH")) {
                priceTextView.setText("Price: " + price.getDisplayPrice(false));
                buyButton.setText("Buy: " + price.getDisplayPrice(false));
            } else if (priceUnit.equals("Gwei")) {
                priceTextView.setText("Price: " + price.getDisplayPrice(false));
                buyButton.setText("Buy: " + price.getDisplayPrice(false));
            } else {
                priceTextView.setText("Price: " + price.getDisplayPrice(false));
                buyButton.setText("Buy: " + price.getDisplayPrice(false));
            }

        }

//        MarkdownView markdownView = (MarkdownView) findViewById(R.id.markdownView);
//        markdownView.loadMarkdown(APIUtils.api.generateMarkdownForThumbnail(hashIPFS) + descriptionText + APIUtils.api.generateMarkdownForImages(hashIPFS, imageCount));
//        if (BuildUtils.shouldSetMarkdownBackground()) {
//            markdownView.setBackgroundColor(getColor(R.color.markdownViewBackground));
//        }


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                } catch (Exception e) {

                }
            }
        });

        thread.start();
    }

    protected void setupKeyManager() {
        keyManager = CryptoUtils.setupKeyManager(getFilesDir().getAbsolutePath());
    }

    public void buyApp(View view) {

        if (APIUtils.api.balance.isZero() && !free) {
            displayNotEnoughMoneyAlert();
            showAddFundsDialog();
            return;
        }

        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.purchase_confirm_dialog);

        TextView priceText = (TextView) d.findViewById(R.id.priceText);
        if (free) {
            priceText.setText("Free");
        } else {
            priceText.setText(buyButton.getText());
        }

        TextView appTitleText = (TextView) d.findViewById(R.id.appTitleText);
        appTitleText.setText(appTitleHeader.getText());

        TextView balanceText = (TextView) d.findViewById(R.id.balanceText);
        balanceText.setText(APIUtils.api.balance.getDisplayPrice(true));

        ImageView appIconView = (ImageView) d.findViewById(R.id.appIcon);
        appIconView.setImageDrawable(iconView.getDrawable());

        d.show();



        Button close_btn = (Button) d.findViewById(R.id.continue_button);
        close_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                promptForPassword();
                d.dismiss();
            }
        });
    }

    public void promptForPassword() {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.password_prompt_dialog);

        final EditText passwordText = (EditText) d.findViewById(R.id.passwordText);

        d.show();

        TextView addFundsBtn = (TextView) d.findViewById(R.id.continue_button);
        addFundsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                purchase(passwordText.getText().toString(), false, null);
                d.dismiss();
            }
        });


        Button close_btn = (Button) d.findViewById(R.id.continue_button);
        close_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                d.dismiss();
            }
        });
    }

    public void promptForAmountToInvest() {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.invest_amount_dialog);

        final EditText investmentAmountText = (EditText) d.findViewById(R.id.investmentAmountText);

        d.show();

        TextView addFundsBtn = (TextView) d.findViewById(R.id.continue_button);
        addFundsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                promptForPasswordInvest(investmentAmountText.getText().toString());
                d.dismiss();
            }
        });

        Button close_btn = (Button) d.findViewById(R.id.continue_button);
        close_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                d.dismiss();
            }
        });
    }

    public void promptForPasswordInvest(final String amountToInvest) {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.password_prompt_dialog);

        final EditText passwordText = (EditText) d.findViewById(R.id.passwordText);

        d.show();

        TextView addFundsBtn = (TextView) d.findViewById(R.id.continue_button);
        addFundsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                purchase(passwordText.getText().toString(), true, amountToInvest);
                d.dismiss();
            }
        });


        Button close_btn = (Button) d.findViewById(R.id.continue_button);
        close_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                d.dismiss();
            }
        });
    }


    public void showAddFundsDialog() {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.show_address_dialog);

        final TextView addressTextView = (TextView) d.findViewById(R.id.addressTextView);
        try {
            addressTextView.setText(keyManager.getAccounts().get(0).getAddress().getHex());
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView balanceTextView = (TextView) d.findViewById(R.id.balanceText);
        balanceTextView.setText(APIUtils.api.balance.getDisplayPrice(true));

        Button close_btn = (Button) d.findViewById(R.id.continue_button);
        close_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                d.dismiss();
            }
        });

        Button copyAddressButton = (Button) d.findViewById(R.id.copyAddressButton);
        copyAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtils.copyToClipboard(getApplicationContext(), addressTextView.getText().toString());
                showCopiedAlert();
            }
        });

        d.show();
    }

    private void showCopiedAlert() {
        Toast.makeText(getApplicationContext(), "Address Copied!",
                Toast.LENGTH_LONG).show();
    }

    public void purchase(final String password, final boolean invest, final String amountToInvest) {
        displayProccessingAlert();

        final Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    String gasPrice = APIUtils.api.getGasPrice();
                    int nonce = APIUtils.api.getNonce(keyManager.getAccounts().get(0).getAddress().getHex());

                    BigInt value = new BigInt(0);

                    if (invest && amountToInvest != null) {
                        value.setInt64((long) (new EthereumPrice("1000000000000000000").inWei().doubleValue() * Double.parseDouble(amountToInvest)));
                    } else {
                        value.setInt64(price.inWei().longValue());
                    }

                    Transaction tx;
//                    if (invest) {
//                        tx = new Transaction(
//                                nonce, new Address(CryptoUtils.ICO_CONTRACT_ADDRESS),
//                                value, new BigInt(300000), new BigInt(Long.valueOf(gasPrice)), null);
//                    } else {
//                        tx = new Transaction(
//                                nonce, new Address(CryptoUtils.CONTRACT_ADDRESS),
//                                value, new BigInt(200000), new BigInt(Long.valueOf(gasPrice)), CryptoUtils.getDataForBuyApp(idApp2, String.valueOf(idCat)));
//                    }

                    try {
                        Transaction transaction = keyManager.getKeystore().signTxPassphrase(keyManager.getAccounts().get(0), password, null, new BigInt(3));

                        if (invest) {
                            APIUtils.api.sendTX(CryptoUtils.getRawTransaction(transaction));
                        } else {
                            installApk(CryptoUtils.getRawTransaction(transaction));
                        }

                        Log.d("Ether", CryptoUtils.getRawTransaction(transaction));

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                             @Override
                             public void run() {
                                 if (invest) {
                                     displayInvestmentCompletedAlert();
                                 } else {
                                     displayDownloadingAlert();
                                 }
                             }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        APIUtils.api.updateBalance(keyManager);

    }

    public boolean installApk(String tx) {
        PermissionUtils.verifyStoragePermissions(this);

        ApkInstaller apkInstaller = new ApkInstaller();
        apkInstaller.setContext(getApplicationContext());
        try {
            apkInstaller.execute(APIUtils.getApkLink(keyManager.getAccounts().get(0).getAddress().getHex(), idApp2, String.valueOf(idCat), hashIPFS),
                    APIUtils.getSendTxLink("0x" + tx, idApp2, String.valueOf(idCat), hashIPFS));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apkInstaller.successful;
    }

    public void displayDownloadingAlert() {
        Toast.makeText(getApplicationContext(), "App Downloading!",
                Toast.LENGTH_LONG).show();
    }

    public void displayInvestmentCompletedAlert() {
        Toast.makeText(getApplicationContext(), "Tokens Received!",
                Toast.LENGTH_LONG).show();
    }


    public void displayInvestedAlert() {
        Toast.makeText(getApplicationContext(), "Investment Processing!",
                Toast.LENGTH_LONG).show();
    }


    public void displayProccessingAlert() {
        Toast.makeText(getApplicationContext(), "Purchase Processing!",
                Toast.LENGTH_LONG).show();
    }

    public void displayNotEnoughMoneyAlert() {
        Toast.makeText(getApplicationContext(), "Insufficient Funds!",
                Toast.LENGTH_LONG).show();
    }

    public void investApp(View view) {

        if (APIUtils.api.balance.isZero()) {
            displayNotEnoughMoneyAlert();
            showAddFundsDialog();
            return;
        }

        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.purchase_confirm_dialog);

        TextView priceText = (TextView) d.findViewById(R.id.priceText);
        priceText.setText("");

        TextView appTitleText = (TextView) d.findViewById(R.id.appTitleText);
        appTitleText.setText(appTitleHeader.getText());

        TextView balanceText = (TextView) d.findViewById(R.id.balanceText);
        balanceText.setText(APIUtils.api.balance.getDisplayPrice(true));

        ImageView appIconView = (ImageView) d.findViewById(R.id.appIcon);
        appIconView.setImageDrawable(iconView.getDrawable());

        d.show();


        Button close_btn = (Button) d.findViewById(R.id.continue_button);
        close_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                promptForAmountToInvest();
                d.dismiss();
            }
        });

    }

    public void goBackToList(View view) {
        finish();
    }
}
