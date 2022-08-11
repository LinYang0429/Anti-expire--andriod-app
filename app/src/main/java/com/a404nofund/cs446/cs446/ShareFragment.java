package com.a404nofund.cs446.cs446;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.a404nofund.cs446.cs446.dataType.ProductDetail;
import com.a404nofund.cs446.cs446.dataType.ShareDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class ShareFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdapterShare adapter;
    ProgressDialog progressDialog;
    ArrayList<Product> products;

    private OnFragmentInteractionListener mListener;

    public ShareFragment() {
    }

    public static ShareFragment newInstance() {
        ShareFragment fragment = new ShareFragment();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerviewshare, container, false);
        products = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewshare);
        layoutManager = new GridLayoutManager(view.getContext(),2);
        progressDialog = new ProgressDialog(getContext());

        extractFromDatabase();//gson.fromJson(json,type);

        recyclerView.setLayoutManager(layoutManager);
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String json = mPref.getString("share",null);
        Gson gson = new Gson();
        Type type  = new TypeToken<ArrayList<ShareDetail> >(){}.getType();
        ArrayList<ShareDetail> shares =  gson.fromJson(json,type);

        json = mPref.getString("data",null);
        gson = new Gson();
        type  = new TypeToken<ArrayList<Product> >(){}.getType();
        ArrayList<Product> p = gson.fromJson(json,type);

        adapter = new RecyclerViewAdapterShare(shares, p);
        recyclerView.setAdapter(adapter);
        return view;
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

    public void extractFromDatabase(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("productSection");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Product> tmp = new ArrayList<>();
                progressDialog.setMessage("LOADING FROM DATABASE");
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                for (DataSnapshot usersnapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot itemsnapshot : usersnapshot.getChildren()) {
                        Object productss = itemsnapshot.getValue();
                        Gson gson = new Gson();
                        String json = gson.toJson(productss);
                        try{
                            JSONObject obj = new JSONObject(json);
                            int itemID = obj.getInt("itemID");
                            String brandName = obj.getString("brandName");
                            String productName = obj.getString("productName");
                            int daysLeft = obj.getInt("daysLeft");
                            String tags = obj.getString("tags");
                            JSONObject edate = obj.getJSONObject("expireDate");
                            JSONObject odate = obj.getJSONObject("openDate");
                            JSONObject nt = obj.getJSONObject("notificationTime");
                            int PAO = obj.getInt("PAO");
                            int like = obj.getInt("like");
                            String img = obj.getString("image");
                            int finish = obj.getInt("finish");


                            int date1 = edate.getInt("date");
                            int day1 = edate.getInt("day");
                            int hour1 = edate.getInt("hours");
                            int minute1 = edate.getInt("minutes");
                            int month1 = edate.getInt("month");
                            int second1 = edate.getInt("seconds");
                            int time1 = edate.getInt("time");
                            int timez1 = edate.getInt("timezoneOffset");
                            int year = edate.getInt("year");

                            int date2 = odate.getInt("date");
                            int day2 = odate.getInt("day");
                            int hour2 = odate.getInt("hours");
                            int minute2 = odate.getInt("minutes");
                            int month2 = odate.getInt("month");
                            int second2 = odate.getInt("seconds");
                            int time2 = odate.getInt("time");
                            int timez2 = odate.getInt("timezoneOffset");
                            int year2 = odate.getInt("year");

                            int date3 = nt.getInt("date");
                            int day3 = nt.getInt("day");
                            int hour3 = nt.getInt("hours");
                            int minute3 = nt.getInt("minutes");
                            int month3 = nt.getInt("month");
                            int second3 = nt.getInt("seconds");
                            int time3 = nt.getInt("time");
                            int timez3 = nt.getInt("timezoneOffset");
                            int year3 = nt.getInt("year");

                            Date open = new Date();
                            open.setDate(date1);
                            open.setHours(hour1);
                            open.setMinutes(minute1);
                            open.setMonth(month1);
                            open.setSeconds(second1);
                            open.setTime(time1);
                            open.setYear(year);


                            Date expr = new Date();
                            expr.setDate(date2);
                            expr.setHours(hour2);
                            expr.setMinutes(minute2);
                            expr.setMonth(month2);
                            expr.setSeconds(second2);
                            expr.setTime(time2);
                            expr.setYear(year2);

                            Date expr2 = new Date();
                            expr2.setDate(date3);
                            expr2.setHours(hour3);
                            expr2.setMinutes(minute3);
                            expr2.setMonth(month3);
                            expr2.setSeconds(second3);
                            expr2.setTime(time3);
                            expr2.setYear(year3);

                            Product p = new Product(itemID,brandName, productName, daysLeft, tags, expr,
                                    open, img, expr2,finish,PAO);

                            products.add(p);


                        } catch (Exception e){

                        }

                    }
                }

                progressDialog.dismiss();
                //Toast.makeText(getContext(),String.valueOf(i),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
