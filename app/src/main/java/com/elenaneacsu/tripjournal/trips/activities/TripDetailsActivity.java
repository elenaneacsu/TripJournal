package com.elenaneacsu.tripjournal.trips.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elenaneacsu.tripjournal.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.Calendar;

import static com.elenaneacsu.tripjournal.utils.Constants.TRIP_DESTINATION;
import static com.elenaneacsu.tripjournal.utils.Constants.TRIP_END_DATE;
import static com.elenaneacsu.tripjournal.utils.Constants.TRIP_IMAGE_URL;
import static com.elenaneacsu.tripjournal.utils.Constants.TRIP_NAME;
import static com.elenaneacsu.tripjournal.utils.Constants.TRIP_PRICE;
import static com.elenaneacsu.tripjournal.utils.Constants.TRIP_RATING;
import static com.elenaneacsu.tripjournal.utils.Constants.TRIP_START_DATE;
import static com.elenaneacsu.tripjournal.utils.Constants.TRIP_TYPE;

public class TripDetailsActivity extends AppCompatActivity {

    private TextView mTextViewName;
    private TextView mTextViewDestination;
    private TextView mTextViewType;
    private TextView mTextViewStartDate;
    private TextView mTextViewEndDate;
    private TextView mTextViewPrice;
    private TextView mTextViewRating;
    private ImageView mImageView;

    private String imageUrl;
    private String name;
    private String destination;
    private String type;
    private long startDate;
    private long endDate;
    private long price;
    private long rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        initView();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            getTripFromBundle(bundle);
            fillTripFields();
        }
    }

    private void initView() {
        mImageView = findViewById(R.id.imageview_photodetail);
        mTextViewName = findViewById(R.id.textview_namefb);
        mTextViewDestination = findViewById(R.id.textview_destinationfb);
        mTextViewType = findViewById(R.id.textview_typefb);
        mTextViewStartDate = findViewById(R.id.textview_startdatefb);
        mTextViewEndDate = findViewById(R.id.textview_enddatefb);
        mTextViewPrice = findViewById(R.id.textview_pricefb);
        mTextViewRating = findViewById(R.id.textview_ratingfb);
    }

    private void getTripFromBundle(Bundle bundle) {
        name = bundle.getString(TRIP_NAME);
        destination = bundle.getString(TRIP_DESTINATION);
        type = bundle.getString(TRIP_TYPE);
        startDate = bundle.getLong(TRIP_START_DATE);
        endDate = bundle.getLong(TRIP_END_DATE);
        price = bundle.getLong(TRIP_PRICE);
        rating = bundle.getLong(TRIP_RATING);
        imageUrl = bundle.getString(TRIP_IMAGE_URL);
    }

    //todo BUG MARE LA DATA
    private void fillTripFields() {
        mTextViewName.setText(name);
        mTextViewDestination.setText(destination);
        mTextViewType.setText(type);
        mTextViewStartDate.setText(calendarDate(startDate));
        mTextViewEndDate.setText(calendarDate(endDate));
        mTextViewPrice.setText(String.valueOf(price));
        mTextViewRating.setText(String.valueOf(rating));
        Glide.with(TripDetailsActivity.this)
                .load(imageUrl)
                .into(mImageView);
    }

    private String calendarDate(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                (calendar.get(Calendar.MONTH)+1) + "/" +
                calendar.get(Calendar.YEAR);
    }
}
