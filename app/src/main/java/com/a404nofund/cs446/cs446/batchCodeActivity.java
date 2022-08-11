package com.a404nofund.cs446.cs446;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class batchCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_code);

        WebView webview = (WebView) findViewById(R.id.webview);
        webview.loadUrl("http://m.checkcosmetic.net");
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.create_bottom);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Uri uri = Uri.parse("android.resource://com.segf4ult.test/drawable/icon/f04");
//
//                openPage(NewProductDetailFragment.newInstance(uri));
                onBackPressed();
            }
        });
    }

    protected boolean openPage(Fragment selectedFragment) {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        return true;
    }


}
