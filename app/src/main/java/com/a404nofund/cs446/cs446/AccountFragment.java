package com.a404nofund.cs446.cs446;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.a404nofund.cs446.cs446.utils.FireBaseUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment
implements GoogleApiClient.OnConnectionFailedListener {
    protected GoogleApiClient mGoogleApiClient;
    protected FirebaseAuth mAuth;
    protected FirebaseUser mFirebaseUser;
    private ProgressDialog progressDialog;
    private GoogleSignInOptions mSignIn;
    private ArrayList<Bar> points;
    private int throwAway;
    private int used;
    private int expir;
    private AccountFragment.OnFragmentInteractionListener mListener;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public AccountFragment() {
    }

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        points = new ArrayList<Bar>();
        throwAway = 0;
        used = 0;
        expir = 0;
        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null) {
            mFirebaseUser = mAuth.getCurrentUser();
        }
        mSignIn = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                .enableAutoManage(this.getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mSignIn)
                .build();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivityForResult(new Intent(getContext(), RegisterActivity.class), 1);
                }
            }
        };
    }

    public String getColor(){
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return String.format("#%02x%02x%02x", r, g, b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account2, container, false);



        Button upload = (Button) view.findViewById(R.id.upload_button);
        final Button analyse = (Button) view.findViewById(R.id.analyse_button);
        Button report = (Button) view.findViewById(R.id.report_button);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                String json = mPref.getString("data", null);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Product>>() {}.getType();
                ArrayList<Product> products = gson.fromJson(json, type);

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("productSection/" + FireBaseUtils.getCurrentUser().getEmail().replace(".", ","));
                    ref.setValue(products);

                points.clear();

                Toast.makeText(getContext(),"UPDATE SUCCESS",Toast.LENGTH_LONG).show();

            }
        });

        analyse.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("productSection");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int account = 0;
                        for (DataSnapshot usersnapshot : dataSnapshot.getChildren()) {//create Bar for each user

                            Bar expire = new Bar();
                            Bar usedU = new Bar();
                            Bar throwA = new Bar();
                            String colorString = getColor();
                            expire.setColor(Color.parseColor(colorString));
                            expire.setName("User " + String.valueOf(account/3) + " E" );
                            expire.setValue(0);

                            usedU.setColor(Color.parseColor(colorString));
                            usedU.setName("User " + String.valueOf(account/3) + " U" );
                            usedU.setValue(0);

                            throwA.setColor(Color.parseColor(colorString));
                            throwA.setName("User " + String.valueOf(account/3) + " T" );
                            throwA.setValue(0);


                            points.add(expire);
                            points.add(usedU);
                            points.add(throwA);
                            for(DataSnapshot itemsnapshot : usersnapshot.getChildren()){
                                Object products = itemsnapshot.getValue();
                                Gson gson= new Gson();
                                String json = gson.toJson(products);
                                analyseData(json,account);
                            }
                            //Clean up for next user
                            account += 3;
                            throwAway = 0;
                            used = 0;
                            expir = 0;
                        }

                        Toast.makeText(getContext(),"DATABASE DOWNLOADED",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

//                signOut.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mAuth.signOut();
//
//                        Auth.GoogleSignInApi.signOut(mGoogleApiClient)
//                                .setResultCallback(new ResultCallback<Status>() {
//                                    @Override
//                                    public void onResult(@NonNull Status status) {
//
//                                    }
//                                });
//
//
//                    }
//                });




            }
        });

        report.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                BarGraph g = (BarGraph) getView().findViewById(R.id.graph);
                g.setBars(points);
                Toast.makeText(getContext(),"REPORT GENERATED",Toast.LENGTH_LONG).show();


            }
        });

        return view;
    }

    public void analyseData(String json,int account){
        if(json == null){
            Toast.makeText(getContext(),"DATABASE EMPTY",Toast.LENGTH_LONG).show();

        } else {
            try{
                final JSONObject obj = new JSONObject(json);
                int itemID = obj.getInt("itemID"); // 1 json object == 1 Product object
                //Use loops to count how many thrown away, or make graphs
                int finishStatus = obj.getInt("finish");

                if(finishStatus == 2){ //ThrowAway
                    Bar tmp = points.get(account+2);
                    throwAway++;
                    tmp.setValue(throwAway);
                    points.set(account+2,tmp);

                } else if (finishStatus == 1){ //used
                    Bar tmp = points.get(account+1);
                    used++;
                    tmp.setValue(used);
                    points.set(account+1,tmp);

                } else { // Expire
                    Bar tmp = points.get(account);
                    expir++;
                    tmp.setValue(expir);
                    points.set(account,tmp);
                }
            } catch (Exception e){

            }



        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Toast.makeText(getContext(), "Register Success", Toast.LENGTH_LONG).show();

        }

        if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getContext(), "User Cancel", Toast.LENGTH_LONG).show();

        }

        if (resultCode == Activity.RESULT_FIRST_USER) {
            Toast.makeText(getContext(), "Register Fail", Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

}
