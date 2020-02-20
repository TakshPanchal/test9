package com.example.test9;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;
    private Button choose;
    private Button upload;
    private EditText edt1;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Uri imageuri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt1 = (EditText) findViewById(R.id.editText);
        choose = (Button) findViewById(R.id.button);
        upload = (Button) findViewById(R.id.button2);
//        edt1 = (EditText) findViewById(R.id.editText);
//        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        imageView = (ImageView) findViewById(R.id.image);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");


        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            chooseIMG();

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadIMG();

            }
        });
   }

    private void chooseIMG() {

        Intent intent = new Intent();
        intent.setType("Image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!= null && data.getData()!= null)
        {
            imageuri = data.getData();
            Picasso.with(this).load(imageuri).into(imageView);

        }

    }




    private void uploadIMG() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("uploading");
        progressDialog.show();

        if (imageuri != null)
        {
            StorageReference reference = storageReference.child(System.currentTimeMillis() + "" + getFileExtension(imageuri));
            reference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    upload mupload = new upload(edt1.getText().toString().trim(),taskSnapshot.toString());
                    String uploadId = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(uploadId).setValue(upload);


                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("uploaded "+(int)progress+"%");

                }
            });
        }

    }


    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));


    }

}
