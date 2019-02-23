package com.elenaneacsu.tripjournal.trips.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.elenaneacsu.tripjournal.R;
import com.elenaneacsu.tripjournal.trips.activities.ManageTripActivity;
import com.elenaneacsu.tripjournal.trips.adapters.RecyclerTouchListener;
import com.elenaneacsu.tripjournal.trips.adapters.TripAdapter;
import com.elenaneacsu.tripjournal.trips.adapters.TripClickListener;
import com.elenaneacsu.tripjournal.trips.entities.Trip;
import com.elenaneacsu.tripjournal.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TripListFragment extends Fragment {

    private RecyclerView mRecyclerViewTrips;
    private List<Trip> mTripList;
    private TripAdapter mTripAdapter;

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;

    private ValueEventListener mValueEventListener;
    private DatabaseReference mUserReference;
    private List<StorageReference> mStorageReferences;


    public TripListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseStorage = FirebaseStorage.getInstance();

        mTripList = new ArrayList<>();
        mStorageReferences = new ArrayList<>();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerViewTrips = view.findViewById(R.id.recyclerview_trips);

        getAllTrips();


        mRecyclerViewTrips.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerViewTrips, new TripClickListener() {
            @Override
            public void onClick(View view, int position) {
                //todo vizualizare FRD+FST
            }

            @Override
            public void onLongClick(View view, int position) {
                Trip selectedTrip = mTripList.get(position);
                Intent intent = new Intent(getActivity(), ManageTripActivity.class);
                intent.putExtra(Constants.TRIP_NAME, selectedTrip.getName());
                intent.putExtra(Constants.TRIP_DESTINATION, selectedTrip.getDestination());
                intent.putExtra(Constants.TRIP_TYPE, selectedTrip.getType());
                intent.putExtra(Constants.TRIP_PRICE, selectedTrip.getPrice());
                intent.putExtra(Constants.TRIP_RATING, selectedTrip.getRating());
                intent.putExtra(Constants.TRIP_START_DATE, selectedTrip.getStartDate());
                intent.putExtra(Constants.TRIP_END_DATE, selectedTrip.getEndDate());
                intent.putExtra(Constants.FLAG, "update");
                intent.putExtra(Constants.POSITION, position);
                Log.d("oaie", "onLongClick: position " + position);
                Log.d("oaie", "onLongClick: flag update");
                startActivityForResult(intent, 0);
            }
        }));
    }

    private void getAllTrips() {
        mUserReference = mDatabaseReference.child("USERS").child(mFirebaseAuth.getCurrentUser().getUid());
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long noTrips = (long) dataSnapshot.child("noTrips").getValue();
                if (noTrips == 0) {
                    Toast.makeText(getActivity(), "No trips", Toast.LENGTH_SHORT).show();
                } else {
                    DataSnapshot tripsDataSnapshot = dataSnapshot.child("trips");
                    for (DataSnapshot tripDataSnapshot : tripsDataSnapshot.getChildren()) {
                        Trip trip = new Trip();
                        trip.setName((String) tripDataSnapshot.child("name").getValue());
                        trip.setDestination((String) tripDataSnapshot.child("destination").getValue());
                        trip.setType(Trip.TripType.valueOf((String) tripDataSnapshot.child("type").getValue()));
                        trip.setStartDate((Long) tripDataSnapshot.child("startDate").getValue());
                        trip.setEndDate((Long) tripDataSnapshot.child("endDate").getValue());
                        trip.setRating((Long) tripDataSnapshot.child("rating").getValue());
                        trip.setPrice((Long) tripDataSnapshot.child("price").getValue());
                        trip.setImage((String) tripDataSnapshot.child("img").getValue());
                        mTripList.add(trip);
                    }
                }

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                mRecyclerViewTrips.setLayoutManager(layoutManager);
                mTripAdapter = new TripAdapter(mTripList);
                mRecyclerViewTrips.setAdapter(mTripAdapter);
                mTripAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mUserReference.addValueEventListener(mValueEventListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mUserReference.removeEventListener(mValueEventListener);
    }
}
