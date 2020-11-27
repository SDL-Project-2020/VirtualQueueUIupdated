package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    DatabaseReference reference;
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fAuth = FirebaseAuth.getInstance();

        /*Check  Whether Current User logged in through Customer or vendor*/
        if (fAuth.getCurrentUser() != null) {
            reference = FirebaseDatabase.getInstance().getReference().child("Customers");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.getKey().equals(fAuth.getUid())) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), ShopCategories.class));
                            flag = false;
                        }
                    }
                    if (flag) {
                        finish();
                        startActivity(new Intent(getApplicationContext(), AfterLoginActivity.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    /* Onclick for new vendors*/
    public void Vendor(View view) {
        Intent intent = new Intent(this, VendorLoginActivity.class);
        startActivity(intent);
    }

    /*OnClick for new Customers*/

    public void Customer(View view) {
        Intent intent = new Intent(this, CustomerLoginActivity.class);
        startActivity(intent);
    }

}