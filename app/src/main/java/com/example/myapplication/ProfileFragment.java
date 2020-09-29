package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView editProfileImage;
    private Uri mImageUri;
    StorageReference storageReference;
    FirebaseStorage storage;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Button save;
    String userId;
    FirebaseUser user;
    EditText editUsername,editEmail,editPhone,oldPassword,newPassword,RepeatPassword;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile,container,false);
        getActivity().setTitle("Profile");
        editProfileImage=view.findViewById(R.id.editProfileImage);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        storage=FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        user=fAuth.getCurrentUser();
        editUsername=view.findViewById(R.id.editUsername);
        editEmail=view.findViewById(R.id.editEmail);
        editPhone=view.findViewById(R.id.editPhone);
        oldPassword=view.findViewById(R.id.OldPassword);
        newPassword=view.findViewById(R.id.NewPassword);
        RepeatPassword=view.findViewById(R.id.RepeatPassword);
        save=view.findViewById(R.id.saveButton);




        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    editUsername.setText(documentSnapshot.getString("fName"));
                    editEmail.setText(documentSnapshot.getString("email"));
                    editPhone.setText(documentSnapshot.getString("phone"));
                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });
        final StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getActivity()).load(uri).into(editProfileImage);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editUsername.getText().toString().isEmpty()||editEmail.getText().toString().isEmpty()||editPhone.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"please fill in the info",Toast.LENGTH_SHORT).show();
                    return;
                }
                final String email=editEmail.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docRef=fStore.collection("users").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("fName",editUsername.getText().toString());
                        edited.put("email",email);
                        edited.put("phone",editPhone.getText().toString());
                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(),"Profile is updated",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        editProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(openGalleryIntent,1000);
                openFileChooser();
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
//        if(requestCode == 1000){
//            if(resultCode == Activity.RESULT_OK){
//                Uri imageUri = data.getData();
//
//                //profileImage.setImageURI(imageUri);
//
//                uploadPicture(imageUri);
//
//            }
//        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.with(editProfileImage.getContext()).load(mImageUri).into(editProfileImage);
            uploadPicture(mImageUri);

        }


    }
//    private String getFileExtension(Uri uri) {
//        ContentResolver cR = getActivity().getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(cR.getType(uri));
//    }


    private void uploadPicture(Uri imageUri) {
        if (mImageUri != null) {
//            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
//                    + "." + getFileExtension(mImageUri));
            //UUID.randomUUID().toString()
            StorageReference fileReference = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        } else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
        }





    }

}
