package com.example.fileupload;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static com.example.fileupload.FileUtils.bitmapToFile;
import static com.example.fileupload.FileUtils.getFileFromUri;

public class MainActivity extends AppCompatActivity {

    private Button selectFileBtn, selectImgBtn;
    private static final int SELECT_FILE = 111;
    private static final int CAPTURE_IMAGE = 222;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectFileBtn = findViewById(R.id.select_file_btn);
        selectImgBtn = findViewById(R.id.select_img_btn);


        selectFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });

        selectImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForPermission();
            }
        });

    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, SELECT_FILE);

    }


    private void checkForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Explanation", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "request permisssion", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                }
            }
            
            else {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                Intent cameraIntent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAPTURE_IMAGE);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 456 && resultCode == Activity.RESULT_OK) {


        }

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CAPTURE_IMAGE:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                    File file = bitmapToFile(bitmap, this);

                    if (file != null) {
                        if (file.exists()) {
                            Toast.makeText(this, "file exist", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "file doesn't exist", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "File is null", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case SELECT_FILE:
                    Uri returnUri = data.getData();


                        try {
                            File file1 = getFileFromUri(this, returnUri);


                            if (file1!= null) {
                                if (file1.exists()) {
                                    Toast.makeText(this, "file exist", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(this, "file doesn't exist", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "File is null", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    break;
            }
        }
    }



}