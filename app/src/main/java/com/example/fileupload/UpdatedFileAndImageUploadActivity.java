package com.example.fileupload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdatedFileAndImageUploadActivity extends AppCompatActivity {


    private ImageFile imageFile = null;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int FILE_SELECT_CODE = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void openCamera() {
        dispatchTakenPictureIntent();
    }

    private void openFileUpload() {
        showFileChooser();
    }


    private void dispatchTakenPictureIntent() {

        //check for camera permissions
//        if (!isPermissionGranted(permissions)) {
//            return;
//        }

        Intent takenPictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takenPictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            imageFile = null;

            try {
                imageFile = createImageFile(this);
                photoFile = imageFile.getFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                Toast.makeText(this, "Unable to create an image file", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile
                );
                takenPictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takenPictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }




    public static ImageFile createImageFile(Context context) throws IOException {
        //create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName ,
                ".jpg" ,
                storageDir
        );

        String currentPath = image.getAbsolutePath();
        ImageFile imageFile = new ImageFile(image, currentPath);
        return imageFile;
    }



    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:

                    try {

                        if (isFileExist(imageFile.getFile())) {


//                            setImageUpload(BitmapFactory.decodeFile(imageFile.getFile().getPath()), imageFile.getFile().getName(), false);
//                            uploadAttachment(imageFile.getFile());

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;

                case FILE_SELECT_CODE:
                    Uri uri = data.getData();

                    try {

                        File source = getFile(this, uri);

                        if (isFileExist(source)) {
//                            setImageUpload(null, source.getName(), false);
//                            uploadAttachment(source);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }


    private boolean isFileExist(File file) {
        if (file != null) {
            if (file.exists()) {
                return true;
            } else {
                Toast.makeText(this, "file doesn't exist", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "File is null", Toast.LENGTH_SHORT).show();
        }

        return false;
    }



    public static File getFile(Context context, Uri uri) throws IOException {
        File destinationFilename = new File(context.getFilesDir().getPath() + File.separatorChar + queryName(context, uri));
        try (InputStream ins = context.getContentResolver().openInputStream(uri)) {
            createFileFromStream(ins, destinationFilename);
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
        return destinationFilename;
    }

    public static void createFileFromStream(InputStream ins, File destination) {
        try (OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String queryName(Context context, Uri uri) {
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }
}
