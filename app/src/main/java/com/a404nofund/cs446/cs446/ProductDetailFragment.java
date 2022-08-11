package com.a404nofund.cs446.cs446;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.a404nofund.cs446.cs446.dataType.FakeDatabase;
import com.a404nofund.cs446.cs446.dataType.ProductDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


public class ProductDetailFragment extends Fragment  {

    int id;
    ImageView image;
    FloatingActionButton saveBottom;
    EditText brandName;
    EditText productName;
    EditText expirDate;
    Switch opend;
    EditText openDate;
    EditText period;
    Button usedUp;
    Button throwAway;

    private File imageFile = null;
    private String img;
    Calendar myCalendar;

    private final int IMAGE_MAX_WIDTH = 540;
    private final int IMAGE_MAX_HEIGHT = 960;


    public ProductDetailFragment() {
    }

    public static ProductDetailFragment newInstance() {
        ProductDetailFragment fragment = new ProductDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_item_detail, container, false);

        image  = (ImageView) rootView.findViewById(R.id.product_picture);
        saveBottom = (FloatingActionButton) rootView.findViewById(R.id.save_bottom);
        brandName = (EditText) rootView.findViewById(R.id.bradName);
        productName = (EditText) rootView.findViewById(R.id.productName);
        expirDate = (EditText) rootView.findViewById(R.id.expireDate);
        opend = (Switch) rootView.findViewById(R.id.open_switch);
        openDate = (EditText) rootView.findViewById(R.id.openDate);
        period = (EditText) rootView.findViewById(R.id.period);
        usedUp = (Button) rootView.findViewById(R.id.used_button);
        throwAway = (Button) rootView.findViewById(R.id.throw_button);

        String name = this.getArguments().getString("NAME_KEY");
        String tag = this.getArguments().getString("TAG_KEY");
        final String open = this.getArguments().getString("OPEN_KEY");
        String start = this.getArguments().getString("START_KEY");
        String end = this.getArguments().getString("END_KEY");
        final String left = this.getArguments().getString("LEFT_KEY");
        id = this.getArguments().getInt("ID_KEY");
        int openState = this.getArguments().getInt("OPENED_KEY");
        img = this.getArguments().getString("IMAGE_KEY");

        productName.setHint(name);
        brandName.setHint(tag);
        expirDate.setHint(end);
        if (openState == 1) {
            opend.setChecked(true);
        } else {
            opend.setChecked(false);
        }
        if (opend.isChecked()) {
            openDate.setHint(open);
        } else {
            openDate.setHint("Not opened");
        }
        period.setHint(left);

        if(img != Uri.EMPTY.getPath()) {
        imageFile = new File(img);
        int scale = 0;
        scale = getZoomScale(imageFile);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = scale;
        image.setImageBitmap(BitmapFactory.decodeFile(img,options));
        }

        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        expirDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }

        });

        period.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String value;
                if(period.getText().toString().equals("")) {
                    value = period.getHint().toString();
                } else {
                    value = period.getText().toString();
                }
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_YEAR, Integer.valueOf(value));
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                expirDate.setHint(df.format(c.getTime()));
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

        opend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Date d = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(d);
                    openDate.setText(formattedDate);
                } else {
                    openDate.setText("not opened");
                }
            }
        });

        usedUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                String json = mPref.getString("data", null);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Product>>() {}.getType();
                ArrayList<Product> products = gson.fromJson(json, type);
                for (Product p: products) {
                    if(p.getItemID() == id) {
                        products.remove(p);
                        break;
                    }
                }
                json = new Gson().toJson(products);
                SharedPreferences.Editor editor = mPref.edit();
                editor.putString("data", json);
                editor.apply();
                getActivity().onBackPressed();
            }
        });

        throwAway.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                String json = mPref.getString("data", null);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Product>>() {}.getType();
                ArrayList<Product> products = gson.fromJson(json, type);
                products.remove(id);
                json = new Gson().toJson(products);
                SharedPreferences.Editor editor = mPref.edit();
                editor.putString("data", json);
                editor.apply();
                getActivity().onBackPressed();
            }
        });

        saveBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity a = (AppCompatActivity) v.getContext();
                brandName = (EditText) a.findViewById(R.id.bradName);
                productName = (EditText) a.findViewById(R.id.productName);
                expirDate = (EditText) a.findViewById(R.id.expireDate);
                opend = (Switch) a.findViewById(R.id.open_switch);
                openDate = (EditText) a.findViewById(R.id.openDate);
                period = (EditText) a.findViewById(R.id.period);

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

                SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                String json = mPref.getString("data", null);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Product>>() {}.getType();
                ArrayList<Product> products = gson.fromJson(json, type);
                Product p = products.get(id);

                if (brandName.getText().length() != 0) {
                    String value = brandName.getText().toString();
                    p.setTags(value);
                    brandName.setText("");
                    brandName.setHint(value);
                }

                if (productName.getText().length() != 0) {
                    String value = productName.getText().toString();
                    p.setProductName(value);
                    productName.setText("");
                    productName.setHint(value);
                }

                if (openDate.getText().length() != 0) {
                    String value = openDate.getText().toString();
                    try {
                        if (opend.isChecked()) {
                            p.setOpenDate(df.parse(value));
                            openDate.setText("");
                            openDate.setHint(value);
                        } else {
                            Calendar c = Calendar.getInstance();
                            c.add(Calendar.DAY_OF_YEAR, 1000000);
                            openDate.setText("");
                            openDate.setHint(df.format(c.getTime()));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (expirDate.getText().length() != 0) {
                    String value = expirDate.getText().toString();
                    try {
                        p.setOpenDate(df.parse(value));
                        if (period.getHint().toString().equals("")) {
                            p.setDaysLeft(Integer.valueOf(period.getText().toString()));
                        } else {
                            p.setDaysLeft(Integer.valueOf(period.getHint().toString()));
                        }
                        expirDate.setText("");
                        expirDate.setHint(value);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (period.getText().length() != 0) {
                    String value = period.getText().toString();
                    p.setDaysLeft(Integer.valueOf(value));
                    if(period.getText().toString().equals("")) {
                        value = period.getHint().toString();
                    } else {
                        value = period.getText().toString();
                    }
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_YEAR, Integer.valueOf(value));
                    p.setExpireDate(c.getTime());
                    period.setText("");
                    period.setHint(value);
                    expirDate.setHint(df.format(c.getTime()));
                }

                if(opend.isChecked()) {
                    p.setPAO(1);
                } else {
                    p.setPAO(0);
                }

                json = new Gson().toJson(products);
                SharedPreferences.Editor editor = mPref.edit();
                editor.putString("data", json);
                editor.apply();

                Toast.makeText(getActivity(), "Infomation updated!", Toast.LENGTH_SHORT).show();
            }
        });

        return  rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private int getZoomScale(File imageFile) {
        int scale = 1;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(img, options);
        while (options.outWidth / scale >= IMAGE_MAX_WIDTH
                || options.outHeight / scale >= IMAGE_MAX_HEIGHT) {
            scale *= 2;
        }
        return scale;
    }

    public void updateLabel() {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        expirDate.setHint(df.format(myCalendar.getTime()));
        Date d = Calendar.getInstance().getTime();
        Date newExpirDate = myCalendar.getTime();
        long diff = newExpirDate.getTime() - d.getTime();
        int left = (int) diff / 1000 / 60 / 60 / 24;
        period.setHint(Integer.toString(left));

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String json = mPref.getString("data", null);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Product>>() {}.getType();
        ArrayList<Product> products = gson.fromJson(json, type);
        Product p = products.get(id);

        p.setExpireDate(myCalendar.getTime());
        p.setDaysLeft(left);
        json = new Gson().toJson(products);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("data", json);
        editor.apply();
    }
}
