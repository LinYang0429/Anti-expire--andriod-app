package com.a404nofund.cs446.cs446;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import pub.devrel.easypermissions.EasyPermissions;

import static android.content.ContentValues.TAG;

public class NewProductDetailFragment extends ProductDetailFragment {

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

    int leftDate = 7;
    static int num = 0;
    private int isOpened = 0;
    Calendar myCalendar;

    private SharedPreferences mPreference;
    private SharedPreferences.Editor mEditor;

    private File imageFile = null;
    private Uri uri = null;

    private String name = "";
    private String[] galleryPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private final int IMAGE_MAX_WIDTH = 540;
    private final int IMAGE_MAX_HEIGHT = 960;

    public NewProductDetailFragment() {
    }

    public static NewProductDetailFragment newInstance(Uri uri) {
        NewProductDetailFragment fragment = new NewProductDetailFragment();
        fragment.setUri(uri);
        return fragment;
    }

    public static NewProductDetailFragment newInstance(Uri uri, String name) {
        NewProductDetailFragment fragment = new NewProductDetailFragment();
        fragment.setUri(uri);
        fragment.setName(name);
        return fragment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
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

        mPreference = PreferenceManager.getDefaultSharedPreferences(getContext());

        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(d);
        String name = this.name;
        String tag = "";
        String open = formattedDate;
        String start = formattedDate;
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 7);
        String end = df.format(c.getTime());

        productName.setText(tag);
        productName.setText(name);
        expirDate.setHint(end);
        if (opend.isChecked()) {
            openDate.setHint(open);
            isOpened = 1;
        } else {
            openDate.setHint("Not opened");
            isOpened = 0;
        }
        period.setHint(Integer.toString(leftDate));

        if (!EasyPermissions.hasPermissions(getContext(), galleryPermissions)) {
            EasyPermissions.requestPermissions(getActivity(), "Access for storage",
                    101, galleryPermissions);
        }

        imageFile = new File(uri.getPath());
        int scale = 0;
        scale = getZoomScale(imageFile);
        Log.i(TAG, "scale = "+scale);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = scale;
        image.setImageBitmap(BitmapFactory.decodeFile(uri.getPath(),options));

        try {
            double rand = Math.random();
            int finish = 0;
            if(rand >= 0 && rand < 0.3){
                finish = 0;
            } else if (rand < 0.6) {
                finish = 1;
            } else {
                finish = 2;
            }

            Product newPruduct = new Product(num, "", name, leftDate, tag, df.parse(end),
                     df.parse(open), uri.getPath(), df.parse(start), finish,isOpened);

            String json = mPreference.getString("data", null);
            Gson gson = new Gson();
            Type type  = new TypeToken<ArrayList<Product>>(){}.getType();
            ArrayList<Product> productArrayList =  gson.fromJson(json,type);
            productArrayList.add(newPruduct);

            json = gson.toJson(productArrayList);
            mEditor = mPreference.edit();
            mEditor.putString("data",json);
            mEditor.apply();
        } catch (ParseException e) {
            e.printStackTrace();
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
                    if(p.getItemID() == num) {
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
                products.remove(num);
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
                Product p = products.get(num);

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
                num++;
            }
        });

        return  rootView;
    }

    private int getZoomScale(File imageFile) {
        int scale = 1;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri.getPath(), options);
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
        Product p = products.get(num);

        p.setExpireDate(myCalendar.getTime());
        p.setDaysLeft(left);
        json = new Gson().toJson(products);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("data", json);
        editor.apply();
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
