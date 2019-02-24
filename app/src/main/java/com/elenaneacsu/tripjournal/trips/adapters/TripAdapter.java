package com.elenaneacsu.tripjournal.trips.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elenaneacsu.tripjournal.R;
import com.elenaneacsu.tripjournal.trips.entities.Trip;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private List<Trip> mTrips;

    public TripAdapter(List<Trip> tripList) {
        this.mTrips = tripList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View studentView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trip_item2, viewGroup, false);
        return new TripViewHolder(studentView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder tripViewHolder, int i) {
        Trip trip = mTrips.get(i);
        tripViewHolder.mTextviewTripName.setText(trip.getName());
        tripViewHolder.mTextViewTripDestination.setText(trip.getDestination());
        tripViewHolder.mTextViewTripRating.setText(String.valueOf(trip.getRating()));
        tripViewHolder.mTextViewTripPrice.setText(String.valueOf(trip.getPrice()));
        Picasso.get().load(trip.getImage()).into(tripViewHolder.mImageViewTrip);
    }

    @Override
    public int getItemCount() {
        return mTrips == null ? 0 : mTrips.size();
    }

    public class TripViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageViewTrip;
        public TextView mTextviewTripName;
        public TextView mTextViewTripDestination;
        public TextView mTextViewTripPrice;
        public TextView mTextViewTripRating;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageViewTrip = itemView.findViewById(R.id.imageview_trip);
            mTextviewTripName = itemView.findViewById(R.id.textview_tripname);
            mTextViewTripDestination = itemView.findViewById(R.id.textview_tripdestination);
            mTextViewTripPrice = itemView.findViewById(R.id.textview_price);
            mTextViewTripRating = itemView.findViewById(R.id.textview_rating);
        }
    }
}
