package com.unipr.bookblog.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.unipr.bookblog.R;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CameraFragment extends Fragment {
    private ImageView myImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 100);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_camera, container, false);
        Button btn = (Button) fragmentView.findViewById(R.id.buttoncamera);
        Button btn2 = (Button) fragmentView.findViewById(R.id.buttonback);
        myImage = (ImageView) fragmentView.findViewById(R.id.imageView);

        btn.setOnClickListener(v -> {
            //Open Camera
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 100);
        });

        btn2.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), HomeActivity.class);
            startActivity(intent);
        });

        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Bitmap captureImage = null;
            if (data != null) {
                captureImage = (Bitmap) data.getExtras().get("data");
            }
            myImage.setImageBitmap(captureImage);
        } else {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }
    }
}