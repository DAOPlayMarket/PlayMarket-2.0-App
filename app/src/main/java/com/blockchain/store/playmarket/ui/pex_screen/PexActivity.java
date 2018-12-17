package com.blockchain.store.playmarket.ui.pex_screen;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.PexHistory;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.device.PermissionUtils;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PexActivity extends AppCompatActivity implements PexContract.View {

    private static final String TAG = "PexActivity";

    //    @BindView(R.id.candleStickChart) CandleStickChart candleStickChart;
    @BindView(R.id.combinedChart) CombinedChart combinedChart;

    @BindView(R.id.web_view) WebView webView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;


    private PexPresenter presenter;
    private PexHistory pexHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pex_activity);
        ButterKnife.bind(this);
//        attachPresenter();
        setWebView();


    }

    private void setWebView() {
        webView.getSettings().setAppCachePath(getCacheDir().getAbsolutePath());
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.grant(PermissionUtils.getPermissionsLocation());
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.loadUrl(Constants.PAX_URL);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void attachPresenter() {
        presenter = new PexPresenter();
        presenter.init(this);
        presenter.loadHistory();
    }

    @Override
    public void onHistoryReady(PexHistory pexHistory) {
        this.pexHistory = pexHistory;

        combinedChart.setBackgroundColor(Color.WHITE);
        combinedChart.getDescription().setEnabled(false);
        combinedChart.setDrawGridBackground(false);

        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        xAxis.setValueFormatter((value, axis) -> {
            Float timeUnix = pexHistory.timeArray.get((int) value);
            Date date = new Date((long) (timeUnix * 1000));
            return new SimpleDateFormat("dd MMM").format(date);
        });
        xAxis.setGranularity(1f);

        YAxis leftAxis = combinedChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);

        combinedChart.resetTracking();

        ArrayList<CandleEntry> candleValues = new ArrayList<>();
        ArrayList<BarEntry> barValues = new ArrayList<>();
        for (int i = 0; i < pexHistory.closeArray.size(); i++) {
            Float close = pexHistory.closeArray.get(i);
            Float high = pexHistory.highArray.get(i);
            Float low = pexHistory.lowArray.get(i);
            Float open = pexHistory.openArray.get(i);
            Float time = pexHistory.timeArray.get(i);
            Float chartData = pexHistory.valueArray.get(i);


            candleValues.add(new CandleEntry(i, high, low, open, close, time));
            barValues.add(new BarEntry(i, chartData / 533232));
        }

        CandleDataSet set1 = new CandleDataSet(candleValues, "Data Set");

        set1.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> "");
        set1.setColor(Color.rgb(80, 80, 80));
        set1.setDrawIcons(false);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setShadowColor(Color.DKGRAY);
        set1.setShadowWidth(0.7f);
        set1.setNeutralColor(Color.BLUE);
        set1.setIncreasingColor(Color.GREEN);
        set1.setIncreasingPaintStyle(Paint.Style.FILL);
        set1.setDecreasingColor(Color.RED);
        set1.setDecreasingPaintStyle(Paint.Style.FILL);
        set1.setBarSpace(0.2f);
        set1.setShowCandleBar(true);

        CandleData data = new CandleData(set1);
        BarDataSet barDataSet = new BarDataSet(barValues, "bar data set");
        barDataSet.setBarBorderColor(Color.DKGRAY);
        BarData barData = new BarData(barDataSet);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(data);
        combinedData.setData(barData);

        combinedChart.setData(combinedData);
        combinedChart.getLegend().setEnabled(false);
        combinedChart.invalidate();

    }

}
