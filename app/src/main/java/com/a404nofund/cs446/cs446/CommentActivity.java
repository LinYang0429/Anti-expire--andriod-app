package com.a404nofund.cs446.cs446;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.a404nofund.cs446.cs446.utils.FireBaseUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*This is main activity that shows comment list of an item */
public class CommentActivity extends ShareBaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private ImageView imageView;
    private TextView tvName;
    private TextView tvEmail;
    String pos;

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    startActivityForResult(new Intent(CommentActivity.this,RegisterActivity.class).putExtra("Position",pos),1);
                }
            }
        };



        /*Nagivation bar may not work*/
        int position = getIntent().getIntExtra("Position",0);
        Bundle bundle = new Bundle();
        pos = Integer.toString(position);
        bundle.putString("Position",Integer.toString(position));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CommentListFragment fragment = new CommentListFragment();
        fragment.setArguments(bundle);
        transaction.replace(R.id.frame_layout2, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
       // BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
       // navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
       // openPage(CommentListFragment.newInstance());
    }


    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);



    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == Activity.RESULT_OK) {
            Toast.makeText(getBaseContext(),"Register Success",Toast.LENGTH_LONG).show();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();

            bundle.putString("Position",pos);
            CommentListFragment fragment = CommentListFragment.newInstance();
            fragment.setArguments(bundle);

            transaction.replace(R.id.frame_layout2, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        if(resultCode ==Activity.RESULT_CANCELED){
            Toast.makeText(getBaseContext(),"User Cancel",Toast.LENGTH_LONG).show();
            finish();

        }

        if(resultCode == Activity.RESULT_FIRST_USER){
            Toast.makeText(getBaseContext(),"Register Fail",Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        signOut();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

    }

    @Override
    protected  void onStop(){
        super.onStop();
        if(mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }

    }

}
