package com.example.fragmentsv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class CreatePropertyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    PlacesClient placesClient;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_property);
        String apikey = "AIzaSyAlc_SUKl0mIn0pUfa3pUiHkNu5TEQucSI";
        if(!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apikey);

        }
        placesClient = Places.createClient(this);
        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        String placeId = "INSERT_PLACE_ID_HERE";
        autocompleteSupportFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(51.3996870247157, -10.900648847337855),
                new LatLng(55.353097528361296, -5.560874845758649)
        )
        );

        autocompleteSupportFragment.setPlaceFields((Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME)));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final LatLng latLng = place.getLatLng();
                Log.i("PlacesApi", "onPlaceSelected" + latLng.latitude + "\n " + latLng.longitude);
                autocompleteSupportFragment.setText(place.getAddress());
            }
            @Override
            public void onError(@NonNull Status status) {
            }
        });

        spinner = (Spinner) findViewById(R.id.spinnerHouseType);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.house_type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}