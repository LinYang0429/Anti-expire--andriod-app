package com.a404nofund.cs446.cs446;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ShareBaseActivity extends AppCompatActivity
        implements
        GoogleApiClient.OnConnectionFailedListener{


    private ProgressDialog mProgressDialog;
    protected FirebaseUser mFirebaseUser;
    protected GoogleApiClient mGoogleApiClient;
    protected FirebaseAuth mAuth;
    private GoogleSignInOptions mSignIn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth != null){
            mFirebaseUser = mAuth.getCurrentUser();
        }
        mSignIn = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,mSignIn)
                .build();

    }

    protected void showProgressDialog(){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    protected void dissmissProgressDialog(){
        if(mProgressDialog != null){
            mProgressDialog.dismiss();
        }
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }


    protected void signOut(){
        mAuth.signOut();

        Auth.GoogleSignInApi.signOut(mGoogleApiClient)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                    }
                });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dissmissProgressDialog();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
