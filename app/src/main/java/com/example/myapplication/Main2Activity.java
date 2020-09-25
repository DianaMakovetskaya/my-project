package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {
    public static final String TAG = "TAG";
EditText etEmail,etPassword,etPhone,etFullname;
Button btnSignUp;
FirebaseAuth fAuth;
FirebaseFirestore fStore;
String userID;

//SQLiteOpenHelper openHelper;
//SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnSignUp=findViewById(R.id.buttonSignUp);
        etEmail= findViewById(R.id.email);
        etPassword=findViewById(R.id.password);
        etPhone= findViewById(R.id.phone);
        etFullname=findViewById(R.id.fullname);

        fAuth=FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
//        openHelper=new DBHelper(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=etEmail.getText().toString().trim();
                String password=etPassword.getText().toString().trim();
                final String fullName=etFullname.getText().toString();
                final String phone= etPhone.getText().toString();

                if(TextUtils.isEmpty(email)){
                    etEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    etPassword.setError("Password is required");
                    return;
                }
                if(password.length()<6){
                    etPassword.setError("Password muse be >= 6 Characters");
                }

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Main2Activity.this,"user created",Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fName",fullName);
                            user.put("email",email);
                            user.put("phone",phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });

                            Intent intent= new Intent(Main2Activity.this,Main3Activity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(Main2Activity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });

    }

//    public void InsertData(String email, String password, String phone){
//        ContentValues contentValues=new ContentValues();
//        contentValues.put(DBHelper.COL_3, email);
//        contentValues.put(DBHelper.COL_4, password);
//        contentValues.put(DBHelper.COL_2,phone);
//        long id=database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
//    }


}
