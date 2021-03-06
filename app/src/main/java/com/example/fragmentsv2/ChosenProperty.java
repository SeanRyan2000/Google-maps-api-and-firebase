package com.example.fragmentsv2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ChosenProperty extends AppCompatActivity {

    private static final String TAG = "ChosenProperty";
    private TextView textViewChosenPropertyAddress, textViewChosenPropertyContact, textViewChosenPropertyPrice, textViewChosenPropertyHouseType, textViewChosenPropertyTotalBeds, textViewChosenPropertyAvailableBeds, textViewSellerName, textViewSellerEmail;
    private ImageView imageView;
    private Button bookHouse;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_property);
        Log.d(TAG, "onCreate: ");

        textViewChosenPropertyAddress = findViewById(R.id.textViewChosenPropertyAddress);
        textViewChosenPropertyContact = findViewById(R.id.textViewChosenPropertyContact);
        textViewChosenPropertyPrice = findViewById(R.id.textViewChosenPropertyPrice);
        textViewChosenPropertyHouseType = findViewById(R.id.textViewChosenPropertyHouseType);
        textViewChosenPropertyTotalBeds = findViewById(R.id.textViewChosenPropertyTotalBeds);
        textViewChosenPropertyAvailableBeds = findViewById(R.id.textViewChosenPropertyAvailableBeds);
        textViewChosenPropertyAvailableBeds = findViewById(R.id.textViewChosenPropertyAvailableBeds);
        textViewChosenPropertyAvailableBeds = findViewById(R.id.textViewChosenPropertyAvailableBeds);
        textViewSellerName = findViewById(R.id.textViewSellerName);
        textViewSellerEmail = findViewById(R.id.textViewSellerEmail);
        imageView = findViewById(R.id.imageView7);
        bookHouse = findViewById(R.id.bookButton);


//
        if (getIntent().hasExtra("selected_accomodation")) {
            Accommodation accommodation = getIntent().getParcelableExtra("selected_accomodation");

            textViewChosenPropertyAddress.setText("Address: " + accommodation.address);
            textViewChosenPropertyContact.setText("Contact: " + accommodation.contact);
            textViewChosenPropertyPrice.setText("Price per Month: ???" + String.valueOf(accommodation.price));
            textViewChosenPropertyHouseType.setText("House Type: " + accommodation.houseType);
            textViewChosenPropertyTotalBeds.setText("Total number of bedrooms: " + String.valueOf(accommodation.numOfBedrooms));
            textViewChosenPropertyAvailableBeds.setText("Bedroom(s) available: " + String.valueOf(accommodation.spacesAvailable));
            textViewSellerName.setText("Contact Name: " + accommodation.sellerName);
            textViewSellerEmail.setText("Contact email: " + accommodation.sellerEmail);
            try {
                if (accommodation.imageUrl.contains("images")) {
                    storageReference = FirebaseStorage.getInstance().getReference().child(accommodation.imageUrl);


                    final File localFile = File.createTempFile("house", "png");
                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            imageView.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChosenProperty.this, "error downloading file", Toast.LENGTH_SHORT).show();


                        }
                    });



                } else {

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            bookHouse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (accommodation.spacesAvailable > 0) {
                        accommodation.spacesAvailable--;
                        Log.e(TAG, "onClick: ");
                        FirebaseDatabase.getInstance("https://safeaccomodation-58b6c-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Properties").child(accommodation.parentReference)
                                .setValue(accommodation).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ChosenProperty.this, "Booked House!", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(ChosenProperty.this, "Error, try again", Toast.LENGTH_LONG).show();
                                }
                            }

                        });

                    }else{
                        Toast.makeText(ChosenProperty.this, "House fully booked", Toast.LENGTH_LONG).show();
                        finish();

                    }
                }
            });

        } else {

        }

    }
}
