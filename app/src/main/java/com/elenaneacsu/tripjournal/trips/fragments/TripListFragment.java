package com.elenaneacsu.tripjournal.trips.fragments;


import android.app.Activity;
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

import com.elenaneacsu.tripjournal.R;
import com.elenaneacsu.tripjournal.trips.activities.ManageTripActivity;
import com.elenaneacsu.tripjournal.trips.adapters.RecyclerTouchListener;
import com.elenaneacsu.tripjournal.trips.adapters.TripAdapter;
import com.elenaneacsu.tripjournal.trips.adapters.TripClickListener;
import com.elenaneacsu.tripjournal.trips.entities.Trip;
import com.elenaneacsu.tripjournal.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TripListFragment extends Fragment {

    private RecyclerView mRecyclerViewTrips;
    private List<Trip> mTripList = new ArrayList<>();

    public TripListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_trip_list, container, false);
        mRecyclerViewTrips = rootView.findViewById(R.id.recyclerview_trips);



//        Bundle bundle = getActivity().getIntent().getExtras();
//        if(bundle != null) {
//            Trip trip = bundle.getParcelable(Constants.TRIP_OBJECT);
//            String flag = bundle.getString(Constants.FLAG);
//            if(flag!=null) {
//                if(flag.equals("update")) {
//                    int position = bundle.getInt(Constants.POSITION);
//                    mTripList.remove(position);
//                    mTripList.add(position, trip);
//                }
//            } else {
//                mTripList.add(trip);
//            }
//        }

        if(getArguments()!=null) {
            Trip trip = getArguments().getParcelable(Constants.TRIP_OBJECT);
            String flag = getArguments().getString(Constants.FLAG);
            if(flag.equalsIgnoreCase("add")) {
                mTripList.add(trip);
            } else if(flag.equalsIgnoreCase("update")) {
                int position = getArguments().getInt(Constants.POSITION);
                mTripList.remove(position);
                mTripList.add(position, trip);
            }
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewTrips.setLayoutManager(layoutManager);
        TripAdapter adapter = new TripAdapter(mTripList);
        mRecyclerViewTrips.setAdapter(adapter);

        mRecyclerViewTrips.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerViewTrips, new TripClickListener() {
            @Override
            public void onClick(View view, int position) {

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
                Log.d("oaie", "onLongClick: position "+position);
                Log.d("oaie", "onLongClick: flag update");
                startActivityForResult(intent, 0);
            }
        }));

        return rootView;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode == 0) {
//            if(resultCode == Activity.RESULT_OK) {
//                Trip trip = data.getParcelableExtra(Constants.TRIP_OBJECT);
//                String flag = data.getStringExtra(Constants.FLAG);
//                int position = data.getIntExtra(Constants.POSITION, -1);
//                if(flag.equalsIgnoreCase("update")) {
//                    if(position>-1) {
//                        mTripList.remove(position);
//                        mTripList.add(position, trip);
//                    }
//                } else if(flag.equalsIgnoreCase("add")) {
//                    mTripList.add(trip);
//                }
//            }
//        }
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

//    private List<Trip> getTrips() {
//        List<Trip> trips = new ArrayList<>();
//        trips.add(new Trip("Holiday 2017", "Islands", 0.0, 0,"https://upload.wikimedia.org/wikipedia/commons/5/58/Fernando_noronha.jpg"));
//        trips.add(new Trip("Fall 2017", "Rome",0.0, 0, "https://cdn.fodors.com/wp-content/uploads/2018/10/HERO_UltimateRome_Hero_shutterstock789412159.jpg"));
//        trips.add(new Trip("Summer 2017", "London",0.0, 0, "https://cdn.londonandpartners.com/visit/general-london/areas/river/76709-640x360-houses-of-parliament-and-london-eye-on-thames-from-above-640.jpg"));
//        trips.add(new Trip("Winter 2017", "Paris", 0.0, 0, "http://www.parisclassictour.com/wcms/img/-size-16957-900-600.jpg"));
//
//        return trips;
//    }

}
