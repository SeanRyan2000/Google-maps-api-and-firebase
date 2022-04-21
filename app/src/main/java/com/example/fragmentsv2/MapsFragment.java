package com.example.fragmentsv2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

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
           // googleMap.addMarker(new MarkerOptions().position(ulCSIS).title("Marker in ulCsis"));

            LatLng oaklawns = new LatLng(52.66993860396053, -8.55934684390429);
            googleMap.addMarker(new MarkerOptions().position(oaklawns).title("Marker in 45 oaklawns"));

            LatLng elm = new LatLng(52.66736823100392, -8.567624155703376);
            googleMap.addMarker(new MarkerOptions().position(elm).title("Marker in 12A Hazelewood"));

            LatLng troy = new LatLng(52.66390497300699, -8.57720826535008);
            googleMap.addMarker(new MarkerOptions().position(troy).title("Marker in troy"));

            LatLng groody = new LatLng(52.663091599772706, -8.577476486233783);
            googleMap.addMarker(new MarkerOptions().position(groody).title("Marker in groody"));

            googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(ulCSIS));

            //need to set it up to ask
            googleMap.setMyLocationEnabled(true);
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