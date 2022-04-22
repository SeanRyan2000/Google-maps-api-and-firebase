package com.example.fragmentsv2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;

public class CreatePropertyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    PlacesClient placesClient;
    private Spinner spinner;
    private ImageView housePics;
//    private ImageButton uploadPics;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private EditText editTextNumberOfBeds, editTextBedsAvailable, editTextPricePerMonth, editTextContact;
    private Button submitButton, uploadPics;
    private String address, randomKey = "";
    private double latitude, longitude = 9999999.99999;

    /**
     * Used this tutorial to help with the code
     * https://www.youtube.com/watch?v=CQ5qcJetYAI
     *
     *
     * @param savedInstanceState
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_property);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //initialiaze edittexts
        editTextNumberOfBeds = (EditText) findViewById(R.id.editTextNumberOfBeds);
        editTextBedsAvailable = (EditText) findViewById(R.id.editTextBedsAvailable);
        editTextPricePerMonth = (EditText) findViewById(R.id.editTextPricePerMonth);
        editTextContact = (EditText) findViewById(R.id.editTextContact);

        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);

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
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                try {
                    address = place.getName();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed to get address. Try again", Toast.LENGTH_LONG).show();
                }
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


        housePics = findViewById(R.id.housePicture);
        uploadPics = findViewById(R.id.uploadPicsButton);
        uploadPics.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null &&  data.getData()!=null){
            imageUri = data.getData();
            housePics.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading image...");
        pd.show();

        randomKey = UUID.randomUUID().toString();
        StorageReference pictureRef = storageReference.child("images/" + randomKey);

        pictureRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "imageUploaded", Snackbar.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_LONG).show();
            }
        })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    pd.setMessage("Progress: " + (int) progressPercent + "%");
                }
            });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void uploadHouse() {
        String numBeds = editTextNumberOfBeds.getText().toString().trim();
        String bedsAvailable = editTextBedsAvailable.getText().toString().trim();
        String price = editTextPricePerMonth.getText().toString().trim();
        String contact = editTextContact.getText().toString().trim();
        if (validation(numBeds, bedsAvailable, price, contact, address, latitude, longitude) == false) {
            numBeds = "";
            bedsAvailable = "";
            price = "";
            contact = "";
            return;
        }
        double priceDouble = Double.parseDouble(price);
        int numBedsInt = Integer.parseInt(numBeds);
        int bedsAvailableInt = Integer.parseInt(bedsAvailable);
        String imageUrl;

        String houseType =  spinner.getSelectedItem().toString();

        if (randomKey != "") {
            imageUrl = "images/" + randomKey;
        }else {
            imageUrl = "";
        }
        Accommodation accomodation = new Accommodation(address, houseType, contact, imageUrl, priceDouble, numBedsInt, bedsAvailableInt, longitude, latitude);
        Log.e("Accomodation", accomodation.toString());
        Log.e("Accomodation", accomodation.toString());
        Log.e("Accomodation", accomodation.toString());
        Log.e("Accomodation", accomodation.toString());
        Log.e("Accomodation", accomodation.toString());
        Log.e("Accomodation", accomodation.toString());
        Log.e("Accomodation", accomodation.toString());

        String storageKey = UUID.randomUUID().toString();



        FirebaseDatabase.getInstance("https://safeaccomodation-58b6c-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Properties")
                .child(storageKey)
                .setValue(accomodation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CreatePropertyActivity.this, "Property successfully registered", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(CreatePropertyActivity.this, "Registration failed, try again", Toast.LENGTH_LONG).show();
                }
            }

        });

    }

    private boolean validation(String numBeds, String bedsAvailable, String price, String contact, String address, double latitude, double longitude) {
        if(numBeds.isEmpty()){
            editTextNumberOfBeds.setError("Can't be empty");
            editTextNumberOfBeds.requestFocus();
            return false;
        }
        if(bedsAvailable.isEmpty()){
            editTextBedsAvailable.setError("Can't be empty");
            editTextBedsAvailable.requestFocus();
            return false;
        }
        if(price.isEmpty()){
            editTextPricePerMonth.setError("Can't be empty");
            editTextPricePerMonth.requestFocus();
            return false;
        }
        if(contact.isEmpty()){
            editTextContact.setError("Can't be empty");
            editTextContact.requestFocus();
            return false;
        }
        if(address.isEmpty()){
            Log.e("address empty", address);
//            autocompleteSupportFragment.setError("Address cant be empty");
//            autocompleteSupportFragment.requestFocus();
            Toast.makeText(getApplicationContext(), "Failed to get address. Try again", Toast.LENGTH_LONG).show();

            return false;
        }
        if(latitude == 9999999.99999) {
            Toast.makeText(getApplicationContext(), "Failed to get address. Try again", Toast.LENGTH_LONG).show();

            return false;
        }
        if(longitude == 9999999.99999) {
            Toast.makeText(getApplicationContext(), "Failed to get address. Try again", Toast.LENGTH_LONG).show();

            return false;
        }


        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitButton:
                uploadHouse();
                uploadProperty();
                Toast.makeText(getApplicationContext(), "SUCCESS!!!!", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void uploadProperty() {

    }
}