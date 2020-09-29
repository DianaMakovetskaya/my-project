package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class Fragment_Advert extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    FirebaseStorage storage;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;


    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ImageView advert;
    EditText info;
    Button publish;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.advert_fragment,container,false);
        getActivity().setTitle("Publish your advertisement");
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();

        advert=view.findViewById(R.id.imageAdvert1);
        info=view.findViewById(R.id.textView7);
        publish=view.findViewById(R.id.publish);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        advert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getActivity(), "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();

                }
            }
        });
        return view;
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            //Picasso.with(advert.getContext()).load(mImageUri).into(advert);
            advert.setImageURI(mImageUri);

        }
    }


    private void uploadFile() {
        final String imageName=info.getText().toString();
        if (mImageUri != null) {
            final String key = mDatabaseRef.push().getKey();
            //fAuth.getCurrentUser().getUid();
            mStorageRef.child(key + ".jpg").putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mStorageRef.child(key + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            HashMap hashMap = new HashMap();
                            hashMap.put("Info", imageName);
                            hashMap.put("ImageUrl", uri.toString());
                            mDatabaseRef.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            });
                        }


                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        uploadFileCurrentUser();
    }

    private void uploadFileCurrentUser() {
        final StorageReference NewStoregeRef;
        final DatabaseReference NewDatabaseRef;
        NewStoregeRef=FirebaseStorage.getInstance().getReference("UsersUploads");
        NewDatabaseRef=FirebaseDatabase.getInstance().getReference("UsersUploads");
        final String imageName=info.getText().toString();
        if (mImageUri != null) {
            final String key2 = NewDatabaseRef.child(fAuth.getCurrentUser().getUid()).push().getKey();
            //fAuth.getCurrentUser().getUid();
            NewStoregeRef.child(fAuth.getCurrentUser().getUid()).child(key2 + ".jpg").putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    NewStoregeRef.child(fAuth.getCurrentUser().getUid()).child(key2 + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            HashMap hashMap = new HashMap();
                            hashMap.put("Info", imageName);
                            hashMap.put("ImageUrl", uri.toString());
                            NewDatabaseRef.child(fAuth.getCurrentUser().getUid()).child(key2).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            });
                        }


                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }
}
