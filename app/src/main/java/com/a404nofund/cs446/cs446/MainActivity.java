package com.a404nofund.cs446.cs446;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;
import android.app.Activity;
import com.a404nofund.cs446.cs446.dataType.FakeDatabase;
import com.a404nofund.cs446.cs446.dataType.ProductDetail;
import com.a404nofund.cs446.cs446.dataType.ShareDetail;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        {
    private SharedPreferences mPreference;
    private SharedPreferences.Editor mEditor;

    private static final int RC_SIGN_IN = 9001;


    protected BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    return openPage(TimelineFragment.newInstance());
                case R.id.navigation_dashboard:
                    return openPage(ProductListFragment.newInstance());
                case R.id.navigation_account:
                    return openPage(AccountFragment.newInstance());
                case R.id.navigation_add:
                    return openPage(AddFragment.newInstance());
                case R.id.navigation_share:
                    return openPage(ShareFragment.newInstance());
            }
            return openPage(TimelineFragment.newInstance());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mPreference = PreferenceManager.getDefaultSharedPreferences(this);
        ArrayList<Product> alist = new ArrayList<>();
        ArrayList<ShareDetail> slist = FakeDatabase.getShares();
        Gson gson = new Gson();
        String json = gson.toJson(alist);
        String json2 = gson.toJson(slist);
        mEditor = mPreference.edit();
        mEditor.putString("data",json);
        mEditor.putString("share",json2);
        mEditor.apply();

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        openPage(TimelineFragment.newInstance());
    }


    public boolean openPage(Fragment selectedFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        return true;
    }

    Boolean doubleBackToExitPressedOnce = false;




    @Override
    public void onBackPressed(){
        if (doubleBackToExitPressedOnce) {
            this.finish();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        super.onBackPressed();
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }





}


