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
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CurrentLineActivity extends AppCompatActivity {

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
    String shopname,category,name;
TextView number;
    TextView currentnumber;
TextView phone_num;
    FirebaseAuth fAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_line);
        number = (TextView) findViewById(R.id.textViewCurrentCouponNumber);
        phone_num=(TextView)findViewById(R.id.phone_number);
        currentnumber=(TextView)findViewById(R.id.textViewCurrent);
        fAuth= FirebaseAuth.getInstance();
        user=fAuth.getCurrentUser();
        DatabaseReference reference1= FirebaseDatabase.getInstance().getReference().child("Vendor").child(user.getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                shopname=dataSnapshot.child("shopname").getValue().toString();
                category=dataSnapshot.child("category").getValue().toString();
                // name=dataSnapshot.child("name").getValue().toString();

               // Toast.makeText(getApplicationContext(), shopname+": -"+category, Toast.LENGTH_LONG).show();
                DatabaseReference reference3= FirebaseDatabase.getInstance().getReference().child("Queues").child(category).child(shopname);
                reference3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                            String temp1=snapshot.getValue().toString();
                            String temp2=snapshot.getKey().toString();
                            InformationQueue info1=new InformationQueue(temp2, temp1);



                            DatabaseReference namereference= FirebaseDatabase.getInstance().getReference().child("Customers");
                            Query query = namereference.getRef().child(info1.getUid()).child("name");
                //            Toast.makeText(getApplicationContext(),query.toString().toString(),Toast.LENGTH_SHORT).show();
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           //         Toast.makeText(getApplicationContext(),dataSnapshot.getValue().toString(),Toast.LENGTH_LONG).show();
                                    if(dataSnapshot.getValue()!=null) {
                                        String txt = info1.getNumber() + ": " + dataSnapshot.getValue();
                                        number.setText(txt);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            Query query1 = namereference.getRef().child(info1.getUid()).child("phone");
                            //            Toast.makeText(getApplicationContext(),query.toString().toString(),Toast.LENGTH_SHORT).show();
                            query1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    //         Toast.makeText(getApplicationContext(),dataSnapshot.getValue().toString(),Toast.LENGTH_LONG).show();
                                    if(dataSnapshot.getValue()!=null) {
                                        phone_num.setText("Phone:-" + dataSnapshot.getValue().toString());
                                        setCurrentParticipants();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
             /*               query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    Toast.makeText(getApplicationContext(),dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
                                UserQueueDisplay user = dataSnapshot.getValue(UserQueueDisplay.class);
                                //Toast.makeText(getApplicationContext(),user.getName(),Toast.LENGTH_SHORT).show();
                                   String txt=info1.getNumber()+": "+user.getName();
                               number.setText(txt);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

              */


                            break;

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void setCurrentParticipants() {
        DatabaseReference reference4= FirebaseDatabase.getInstance().getReference().child("Queues").child(category).child(shopname);
        reference4.addListenerForSingleValueEvent(new ValueEventListener() { //ref will be your desired path where you want to count children
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {   // Check for data snapshot has some value
                    // check for counts of data snapshot children
                    currentnumber.setText("Total Number of Participants: "+String.valueOf(dataSnapshot.getChildrenCount()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void NextNumber(View view)
    {




        DatabaseReference reference1= FirebaseDatabase.getInstance().getReference().child("Vendor").child(user.getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                shopname=dataSnapshot.child("shopname").getValue().toString();
                category=dataSnapshot.child("category").getValue().toString();
                // name=dataSnapshot.child("name").getValue().toString();

              //  Toast.makeText(getApplicationContext(), shopname+": -"+category, Toast.LENGTH_LONG).show();



                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        Query applesQuery = ref.child("Queues").child(category).child(shopname).orderByKey().limitToFirst(1);
             //   Toast.makeText(getApplicationContext(),applesQuery.toString(),Toast.LENGTH_LONG).show();
    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getValue() != null) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "The Queue is Empty", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });


}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }





    public void CurrentLine(View view)
    {
        Intent intent = new Intent(this, LineActivity.class);
        startActivity(intent);
    }

    public void StopLine(View view)
    {
        Toast.makeText(this,"Line Stopped",Toast.LENGTH_LONG).show();
    }
    public void Back(View view)
    {

        finish();
    }

}