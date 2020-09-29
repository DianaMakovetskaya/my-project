package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Main4Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
DrawerLayout drawerLayout;

ActionBarDrawerToggle actionBarDrawerToggle;
Toolbar toolbar;
NavigationView navigationView;
TextView username;
ImageView userphoto;
View hview;
    StorageReference storageReference;
    FirebaseStorage storage;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

//FragmentManager fragmentManager;
//FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        drawerLayout=findViewById(R.id.drawer);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView=findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        final TextView textTittle;

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        storage=FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    username.setText(documentSnapshot.getString("fName"));

                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });
        final StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(Main4Activity.this).load(uri).into(userphoto);
            }
        });


        //load default fragment
//        fragmentManager=getSupportFragmentManager();
//        fragmentTransaction=fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.container_fragment,new MainFragment());
//        fragmentTransaction.commit();
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new MainFragment()).commit();
            navigationView.setCheckedItem(R.id.Home);
        }
        hview=navigationView.getHeaderView(0);
        username=hview.findViewById(R.id.HeaderUserName);
        userphoto=hview.findViewById(R.id.HeaderImage);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.Home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new MainFragment()).commit();
                break;
            case R.id.Advert:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new Fragment_Advert()).commit();
                break;
            case R.id.Chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new ChatFragment()).commit();
                break;
            case R.id.Charity:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new CharityFragment()).commit();
                break;
            case R.id.Profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new ProfileFragment()).commit();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

}
