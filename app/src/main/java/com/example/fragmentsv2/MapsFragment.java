package com.example.fragmentsv2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsFragment extends Fragment {

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        private DatabaseReference mDatabase;

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng ulCSIS = new LatLng(52.67399792339968, -8.57554856387995);
            googleMap.clear();

/**
 *
 * https://firebase.google.com/docs/database/android/read-and-write
 */
            mDatabase = FirebaseDatabase.getInstance("https://safeaccomodation-58b6c-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Properties");
            Log.e(" Reached", "before data value event listener");

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.e(" Reached", "inside data change class");
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        int i = 0;
                        String address = "House number " + i;
                        Accommodation accommodation = postSnapshot.getValue(Accommodation.class);

                        String name = accommodation.address;
                        Double latitude = accommodation.latitude;
                        Double longitude = accommodation.longitude;
                        Double price = accommodation.price;
                        Log.e(" Accomodation", "values of object: " + name + " " + latitude + " " + longitude);


                       // LatLng address = new LatLng(latitude, longitude);

                        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("House for rent " + name + " €" + price));
                        i++;
                    }



                   // Log.e("accomodation", "values of object: " + name);




                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w("Load", "loadPost:onCancelled", databaseError.toException());
                }
            });




            //adding adapter to recyclerview
//            mPostReference.addValueEventListener(postListener);

            googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(ulCSIS));

            //need to set it up to ask
//            googleMap.setMyLocationEnabled(true);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}