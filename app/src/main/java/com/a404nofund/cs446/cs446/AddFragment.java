package com.a404nofund.cs446.cs446;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.support.v4.content.ContextCompat.checkSelfPermission;


/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {
    private Button cameraButton;
    private Button scanButton;
    private Button batchButton;
    private ImageView photoImageView;
    private static final int REQUEST_CODE = 0;
    private String strImgPath = "";
    private File imageFile = null;
    private Uri uri = null;

    private final int IMAGE_MAX_WIDTH = 540;
    private final int IMAGE_MAX_HEIGHT = 960;

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    static private int index = 0;
    private int mode = 0; // 0:camera 1:scan 2:batch

    private ProgressDialog mProgresss;
    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance() {
        AddFragment fragment = new AddFragment();
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
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        cameraButton = (Button) view.findViewById(R.id.take_photo);
        scanButton = (Button) view.findViewById(R.id.scan);
        batchButton = (Button) view.findViewById(R.id.batch);

        cameraButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Intent getPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"fname_00" + index++ + ".jpg"));
                getPhoto.putExtra(MediaStore.EXTRA_OUTPUT, uri);//根据uri保存照片
                getPhoto.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);//保存照片的质量
                if (checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_REQUEST_CODE);
                }
                mode = 0;
                startActivityForResult(getPhoto, REQUEST_CODE);
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mode = 1;
                new IntentIntegrator(getActivity()).initiateScan();
                Toast.makeText(getActivity(), "Searching...", Toast.LENGTH_SHORT).show();
                String result = "coke";
//                Uri uri = Uri.parse("android.resource://"+ getContext().getPackageName()+"/drawable/" + result);
                Uri path = Uri.parse("android.resource://com.a404nofund.cs446/" + R.drawable.coke);
                openPage(NewProductDetailFragment.newInstance(path, result));
            }
        });

        batchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mode = 2;
                Intent intent = new Intent(getActivity(), batchCodeActivity.class);
                startActivityForResult(intent, 2);
            }
        });



        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mode == 0) {
            if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
                openPage(NewProductDetailFragment.newInstance(uri));
            }else {
                Toast.makeText(getActivity(), "Picture is not taken", Toast.LENGTH_SHORT).show();
            }
        } else if (mode == 1){
            if (resultCode == RESULT_OK) {

                String code = data.getStringExtra("SCAN_RESULT");

            } else {
                Toast.makeText(getActivity(), "Scanning failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            cameraButton.callOnClick();
        }
    }

    private int getZoomScale(File imageFile) {
        int scale = 1;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(strImgPath, options);
        while (options.outWidth / scale >= IMAGE_MAX_WIDTH
                || options.outHeight / scale >= IMAGE_MAX_HEIGHT) {
            scale *= 2;
        }
        return scale;
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getActivity(), "camera permission granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(getActivity(), "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }

    protected boolean openPage(Fragment selectedFragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        return true;
    }

}
