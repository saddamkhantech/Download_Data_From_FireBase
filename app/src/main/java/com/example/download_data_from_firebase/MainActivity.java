package com.example.download_data_from_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Trace;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
        private static final int STORAGE_PERMISSION_CODE=1000;
        private static Boolean mGranted=false;
        private StorageReference mRef;
        private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.imageView);
        mRef= FirebaseStorage.getInstance().getReference("docs/");
        getPermission();
    }

    private void getPermission()
    {   String externalReadPermission= Manifest.permission.READ_EXTERNAL_STORAGE.toString();
            String externalWritePermission=Manifest.permission.WRITE_EXTERNAL_STORAGE.toString();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if(ContextCompat.checkSelfPermission(this,externalReadPermission) != PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(this,externalWritePermission) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{externalReadPermission,externalWritePermission},STORAGE_PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==STORAGE_PERMISSION_CODE && grantResults.length>0)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_LONG).show();
                mGranted=true;
            }
            else {
                Toast.makeText(this,"Permission Not Granted",Toast.LENGTH_LONG).show();
            }
        }
    }
    //download the image and files
    public void readData(View view)
    {
//        final File file=new File(Environment.getExternalStorageDirectory(),"paras vipro.PNG");
//        long ONE_MEGABYTE=1024*1024;
//        mRef.child("images/paras vipro.PNG").getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
//                Toast.makeText(MainActivity.this,"Download Success",Toast.LENGTH_LONG).show();
//                //now store image to external storage
//                try {
//                    FileOutputStream fileOutputStream=new FileOutputStream(file);
//                    fileOutputStream.write(bytes);
//                    fileOutputStream.close();
//                    Log.i("check","File saved to device storage"+file);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(MainActivity.this,"Download Failed"+e.getMessage().toString(),Toast.LENGTH_LONG).show();
//            }
//        });


        //now using getFile
        //here we create temp file that hold the doenloaded file but it is temp when system need storage it will delete it
        File outputFile=null;
        try {
             outputFile=File.createTempFile("bill","pdf");
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        //now download the file
       // File file=outputFile;
        final File finalOutputFile = outputFile;
        mRef.child("images/pending bill.pdf").getFile(outputFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(MainActivity.this,"File downloaded",Toast.LENGTH_LONG).show();
                        Log.i("check2","File Downloaded"+ finalOutputFile);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"File download failed",Toast.LENGTH_LONG).show();
            }
        });


    }
}
