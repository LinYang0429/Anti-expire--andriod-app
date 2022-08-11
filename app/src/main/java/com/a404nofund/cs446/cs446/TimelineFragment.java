package com.a404nofund.cs446.cs446;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class TimelineFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TimelineAdapter adapter;

    public TimelineFragment() {
        // Required empty public constructor
    }

    public static TimelineFragment newInstance() {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        if (view.hasFocus()){
            Log.d("W", "Wrong");
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewTimeline);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String json = mPref.getString("data",null);
        Gson gson = new Gson();
        Type type  = new TypeToken<ArrayList<Product> >(){}.getType();
        ArrayList<Product> products =  gson.fromJson(json,type);
        adapter = new TimelineAdapter(view.getContext(), products);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
