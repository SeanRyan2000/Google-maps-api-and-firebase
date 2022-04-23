package com.example.fragmentsv2.ui.feed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragmentsv2.Accommodation;
import com.example.fragmentsv2.ChosenProperty;
import com.example.fragmentsv2.CreatePropertyActivity;
import com.example.fragmentsv2.MyAdapter;
import com.example.fragmentsv2.R;
import com.example.fragmentsv2.databinding.FragmentDashboardBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FeedFragment extends Fragment  implements MyAdapter.OnNoteListener{

    private FragmentDashboardBinding binding;
    private FirebaseUser user;
    private FloatingActionButton addPropertyButton;
    private Button recyclerviewbtn;
    DatabaseReference mDatabase;
    MyAdapter myAdapter;
    ArrayList<Accommodation> list;
    RecyclerView recyclerView;
    Accommodation accommodation;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);



        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        addPropertyButton = view.findViewById(R.id.addPropertyFloatingButton);
        addPropertyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreatePropertyActivity.class);
                startActivity(intent);
                Log.d("Reached here", "entered on click listener");

            }
        });




        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        myAdapter = new MyAdapter(getContext(), list, this);
        recyclerView.setAdapter(myAdapter);

        mDatabase = FirebaseDatabase.getInstance("https://safeaccomodation-58b6c-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Properties");
        Log.e(" Reached", "before data value event listener");

        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    list.clear();
                } catch (Exception e) {

                }
                Log.e(" Reached", "inside data change class");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    accommodation = postSnapshot.getValue(Accommodation.class);

                    String name = accommodation.address;
                    Double latitude = accommodation.latitude;
                    Double longitude = accommodation.longitude;
                    Double price = accommodation.price;
                    //String email = accommodation.sellerEmail;

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(getContext(), ChosenProperty.class);




        //Parcelable
//        Log.e("TAG", "onNoteClick: ", accommodation);
        intent.putExtra("selected_accomodation", list.get(position));
        startActivity(intent);
    }
}