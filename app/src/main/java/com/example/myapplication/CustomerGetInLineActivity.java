/*
MIT License

        Copyright (c) 2020 SDL-Project-2020 (Sarish Nigade, Onkar Litake, Parth Patil)

        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:

        The above copyright notice and this permission notice shall be included in all
        copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
        SOFTWARE.
*/
package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class CustomerGetInLineActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
    TextView t1, t2;

    //Onkar is NOOb Noob Noob Noob

boolean flag=true;
    Button b1, b2, b3;
    FirebaseAuth fAuth;
    private String category;
    private String shopname;
    private String address;
    DatabaseReference reference;
    int countof_participants;
    int lastCoupon, Coupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_get_in_line);
        t1 = findViewById(R.id.textViewCurrentCouponNumber);
        t2 = findViewById(R.id.currentparticipaipants);
        b1 = findViewById(R.id.buttonGetInLine);
        b2 = findViewById(R.id.mapsgoogle);
        b3=findViewById(R.id.buttonCurrentLine);

      //  RecyclerView programminglist=(RecyclerView)findViewById(R.id.queue_data);


        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        shopname = intent.getStringExtra("shopname");
        address = intent.getStringExtra("address");
        // b3=findViewById(R.id.)
        fAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Queues").child(category).child(shopname);

        setLastCoupon();
        setCoupon();
        setCurrentParticipants();


       // DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Shops").child(category).child(shopname);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        Intent intent2=new Intent(getApplicationContext(),QueueDataDisplay.class);
                        intent2.putExtra("category_1",category);
                        intent2.putExtra("shopname_1",shopname);
                        startActivity(intent2);
            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference().child("Queues").child(category).child(shopname);
                Random rand = new Random();


                final boolean[] exists = {false};
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        t2.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getValue().equals(fAuth.getUid())) {
                                Toast.makeText(CustomerGetInLineActivity.this, "You Are Already Added", Toast.LENGTH_SHORT).show();
                                exists[0] = true;
                            }
                        }
                        if (!exists[0]) {
                            if (Coupon<=0)
                            {
                                Coupon = 1;
                            }
                            reference.child(String.valueOf(Coupon)).setValue(fAuth.getUid());
                            Toast.makeText(CustomerGetInLineActivity.this, "Coupon Number " + Coupon, Toast.LENGTH_SHORT).show();
                            t1.setText(String.valueOf(Coupon));
                            setCurrentParticipants();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //     Toast.makeText(CustomerGetInLineActivity.this,reference.toString(),Toast.LENGTH_SHORT).show();
                //     Toast.makeText(CustomerGetInLineActivity.this,reference.orderByKey().limitToLast(1).toString(),Toast.LENGTH_SHORT).show();
                //    reference.child(String.valueOf(reference.orderByKey().limitToLast(1))).setValue(fAuth.getUid());
             //   Toast.makeText(CustomerGetInLineActivity.this, "Added", Toast.LENGTH_SHORT).show();
                // String.valueOf(Integer.parseInt(String.valueOf(reference.orderByKey().limitToLast(1)))+1)
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerGetInLineActivity.this, MapsActivity.class);
            //    startActivity(intent);
                try{
                  //  Uri uri=Uri.parse("https://www.google.co.in/maps/dir/"+ "my location" +"/"+shopname+","+address);
                    Uri uri=Uri.parse("google.navigation:q="+shopname.trim()+"("+address.trim()+")");
                    intent=new Intent(Intent.ACTION_VIEW,uri);
                    intent.setPackage("com.google.android.apps.maps");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                catch (ActivityNotFoundException e){
                    Uri uri=Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                    intent=new Intent(Intent.ACTION_VIEW,uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }
        });


    }

    private void setCurrentParticipants() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() { //ref will be your desired path where you want to count children
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {   // Check for data snapshot has some value
                      // check for counts of data snapshot children
                    t2.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setCoupon() {
        final int[] coupon = {-1};
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue().equals(fAuth.getUid())) {
                        coupon[0] = Integer.parseInt(snapshot.getKey());
                        t1.setText(String.valueOf(coupon[0]));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setLastCoupon() {
        final boolean[] flag = {false};
        reference.orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                if (!flag[0]) {
                    lastCoupon = Integer.parseInt(dataSnapshot.getKey());
                    Coupon = lastCoupon + 1;
                    if (Coupon == 0)
                        Coupon = 1;
                    flag[0] = true;
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

            // ...
        });
    }

    private boolean addedToLine() {
        reference = FirebaseDatabase.getInstance().getReference().child("Queues").child(category).child(shopname);
        final boolean[] flag = {false};
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue().equals(fAuth.getUid())) {
                        Toast.makeText(CustomerGetInLineActivity.this, snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                        flag[0] = true;
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return flag[0];


    }


    public void CurrentLine(View view) {
        Intent intent = new Intent(this, LineActivity.class);
        startActivity(intent);
    }

    public void ExitLine(View view) {
        t1.setText(String.valueOf(0));
        FirebaseAuth fAuth;
        FirebaseUser user;
        fAuth= FirebaseAuth.getInstance();
        user=fAuth.getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("Queues").child(category).child(shopname).orderByValue().equalTo(user.getUid());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    Toast.makeText(CustomerGetInLineActivity.this, "You Have Exited The Queue", Toast.LENGTH_SHORT).show();
                    setCurrentParticipants();
                }
            }

            else
            {
                Toast.makeText(getApplicationContext(), "Please Get in Queue First", Toast.LENGTH_LONG).show();

            }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void Back(View view) {
        finish();
    }
}