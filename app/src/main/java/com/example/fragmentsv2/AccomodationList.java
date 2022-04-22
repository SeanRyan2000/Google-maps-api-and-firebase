package com.example.fragmentsv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AccomodationList extends AppCompatActivity {

    RecyclerView recyclerView;

    DatabaseReference mDatabase;
    MyAdapter myAdapter;
    ArrayList<Accommodation> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accomodation_list);

        recyclerView = findViewById(R.id.recyclerView2);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new MyAdapter(this, list);
        recyclerView.setAdapter(myAdapter);

        mDatabase = FirebaseDatabase.getInstance("https://safeaccomodation-58b6c-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Properties");
        Log.e(" Reached", "before data value event listener");

        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e(" Reached", "inside data change class");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Accommodation accommodation = postSnapshot.getValue(Accommodation.class);

                    String name = accommodation.address;
                    Double latitude = accommodation.latitude;
                    Double longitude = accommodation.longitude;
                    Double price = accommodation.price;

                    Log.e(" Accomodation", "values of object: " + name + " " + latitude + " " + longitude);
                    Log.e(" Accomodation", "values of object: " + name + " " + latitude + " " + longitude);
                    Log.e(" Accomodation", "values of object: " + name + " " + latitude + " " + longitude);

                    list.add(accommodation);


                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}