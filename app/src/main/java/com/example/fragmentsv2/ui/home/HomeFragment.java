package com.example.fragmentsv2.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fragmentsv2.MainActivity;
import com.example.fragmentsv2.R;
import com.example.fragmentsv2.SignIn;
import com.example.fragmentsv2.User;
import com.example.fragmentsv2.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentHomeBinding binding;
    private RadioButton radioMale, radioFemale;
    private Button logOutButton, applyChanges;
    private Spinner spinner;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private String fullName, email, yearOfStudy, gender;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logOutButton = view.findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                getActivity().recreate();
            }
        });

        applyChanges = (Button) view.findViewById(R.id.applyChangesBtn);
        applyChanges.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                yearOfStudy = String.valueOf(spinner.getSelectedItem());

                gender = "";
                if (radioFemale.isChecked()) {
                    gender = "Female";
                } else if (radioMale.isChecked()) {
                    gender = "Male";
                }

                Log.d("Apply Changes", " Reached here successfully " + yearOfStudy);

                User user = new User(fullName, email, gender, yearOfStudy);

                FirebaseDatabase.getInstance("https://safeaccomodation-58b6c-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "User successfully updated!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Error, try again", Toast.LENGTH_LONG).show();
                        }
                    }

                });
            }
        });


        radioMale = view.findViewById(R.id.radio_male);
        radioFemale = view.findViewById(R.id.radio_female);
        //onClick in main activity

        spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.year_of_study_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://safeaccomodation-58b6c-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");
        if(user==null){
            Intent intent = new Intent(getContext(), SignIn.class);
            startActivity(intent);
        }
        userID = user.getUid();




        /**
         * populate all the buttons textviews etc with the users information
         *
         */

        final TextView name = (TextView) view.findViewById(R.id.emptyFullNameTextView);
        final TextView emailTextView = (TextView) view.findViewById(R.id.emptyEmailTextView);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null){
                    fullName = userProfile.fullName;
                    email = userProfile.email;
                    yearOfStudy = userProfile.yearOfStudy;
                    gender = userProfile.gender;
                    name.setText(fullName);
                    emailTextView.setText(email);

                    // GENDER CHECKED
                    if (gender.equals("Male")) {
                        radioMale.setChecked(true);
                    } else if (gender.equals("Female")) {
                        radioFemale.setChecked(true);
                    }
                    // Spinner year selected.
                    // Not ideal way but with so few options hardcoding works better than spending the resources figuring out a solution
                    if (yearOfStudy.equals("First year")) {
                        spinner.setSelection(0,true);
                    } else if(yearOfStudy.equals("Second year")) {
                        spinner.setSelection(1,true);
                    } else if (yearOfStudy.equals("Third year")){
                        spinner.setSelection(2,true);
                    } else if (yearOfStudy.equals("Fourth year")){
                        spinner.setSelection(3,true);
                    } else if (yearOfStudy.equals("Post graduate")){
                        spinner.setSelection(4,true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        String selected = (String) parent.getItemAtPosition(pos);
        Log.d("spinner", "item selected is: " + selected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



}