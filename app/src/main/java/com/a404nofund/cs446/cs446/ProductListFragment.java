package com.a404nofund.cs446.cs446;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.a404nofund.cs446.cs446.dataType.ProductDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ProductListFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdapterProduct adapter;

    private OnFragmentInteractionListener mListener;

    public ProductListFragment() {
    }

    public static ProductListFragment newInstance() {
        ProductListFragment fragment = new ProductListFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerviewproduct, container, false);
        if (view.hasFocus()){
            Log.d("W", "Wrong");
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String json = mPref.getString("data",null);
        Gson gson = new Gson();
        Type type  = new TypeToken<ArrayList<Product> >(){}.getType();
        ArrayList<Product> products =  gson.fromJson(json,type);
        adapter = new RecyclerViewAdapterProduct(view.getContext(), products);
        recyclerView.setAdapter(adapter);
        return view;
    }

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
}
