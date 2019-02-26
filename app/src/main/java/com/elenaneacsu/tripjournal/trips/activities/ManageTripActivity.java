package com.elenaneacsu.tripjournal.trips.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.elenaneacsu.tripjournal.R;
import com.elenaneacsu.tripjournal.trips.entities.Trip.TripType;
import com.elenaneacsu.tripjournal.trips.fragments.DatePickerFragment;
import com.elenaneacsu.tripjournal.trips.fragments.TripListFragment;
import com.elenaneacsu.tripjournal.utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.elenaneacsu.tripjournal.utils.Constants.*;
import static com.elenaneacsu.tripjournal.utils.Constants.CAPTURE_IMAGE_REQUEST;

public class ManageTripActivity extends FragmentActivity implements DatePickerFragment.OnDataPass {

    private EditText mEditTextTripName;
    private EditText mEditTextDestination;
    private RadioGroup mRadioGroupTripType;
    private RadioButton mRadioButtonMountains;
    private RadioButton mRadioButtonSeaside;
    private RadioButton mRadioButtonCityBreak;
    private SeekBar mSeekBarPrice;
    private RatingBar mRatingBar;
    private Button mButtonStartDate;
    private Button mButtonEndDate;
    private Button mButtonCaptureImage;
    private Button mButtonSelectImage;
    private ImageView mImageViewSelectedImage;

    private String name;
    private String destination;
    private TripType type;
    private long price;
    private long rating;
    private long startDate;
    private long endDate;
    private int position;
    private String flag;
    private File photoFile = null;
    String mCurrentPhotoPath;
    private byte[] photo;

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;

    private long noTrips;
    private boolean updateTrip;

//    private int fromYear, fromMonth, fromDay, toYear, toMonth, toDay;
//    private DatePickerDialog.OnDateSetListener fromDateListener, toDateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_trip);

        initView();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseStorage = FirebaseStorage.getInstance();

        mButtonCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    captureImage();
                } else {
                    captureImage2();
                }
            }
        });

        mButtonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL);
                    } else {
                        pickImageFromGallery();
                    }
                } else {
                    pickImageFromGallery();
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString(TRIP_NAME);
            destination = bundle.getString(TRIP_DESTINATION);
            price = bundle.getLong(TRIP_PRICE);
            rating = bundle.getLong(TRIP_RATING);
            startDate = bundle.getLong(TRIP_START_DATE);
            endDate = bundle.getLong(TRIP_END_DATE);
            type = TripType.valueOf(bundle.getString(TRIP_TYPE));
            updateTrip = bundle.getBoolean(UPDATE);
            position = bundle.getInt(POSITION);
            Log.d("onCreate", "onCreate: position "+position);

            setValuesFromBundle();
        }

    }


    private void initView() {
        mEditTextTripName = findViewById(R.id.edittext_tripname);
        mEditTextDestination = findViewById(R.id.edittext_destination);
        mRadioGroupTripType = findViewById(R.id.radiogroup_triptype);
        mRadioButtonMountains = findViewById(R.id.radiobutton_mountains);
        mRadioButtonCityBreak = findViewById(R.id.radiobutton_citybreak);
        mRadioButtonSeaside = findViewById(R.id.radiobutton_seaside);
        mRatingBar = findViewById(R.id.ratingbar_trip);
        mSeekBarPrice = findViewById(R.id.seekbar_price);
        mButtonCaptureImage = findViewById(R.id.btn_fromcamera);
        mButtonSelectImage = findViewById(R.id.btn_fromgallery);
        mImageViewSelectedImage = findViewById(R.id.imageview_selectedpicture);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
            } else {
                displayMessage(this, getString(R.string.permission_denied));
            }
        }
        if (requestCode == REQUEST_EXTERNAL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            } else {
                displayMessage(this, getString(R.string.permission_denied));
            }
        }

    }

    private void captureImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile();
                    displayMessage(getBaseContext(), photoFile.getAbsolutePath());

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.elenaneacsu.tripjournal.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
                    }
                } catch (Exception ex) {
                    // Error occurred while creating the File
                    displayMessage(getBaseContext(), ex.getMessage());
                }
            } else {
                displayMessage(getBaseContext(), "Null");
            }
        }
    }

    private void captureImage2() {
        try {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            photoFile = createImageFile4();
            if (photoFile != null) {
                displayMessage(getBaseContext(), photoFile.getAbsolutePath());
                Uri photoURI = Uri.fromFile(photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST);
            }
        } catch (Exception e) {
            displayMessage(getBaseContext(), "Camera is not available." + e.toString());
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File createImageFile4() {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                displayMessage(getBaseContext(), "Unable to create directory.");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;

    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 0, outputStream);
            photo = outputStream.toByteArray();
            mImageViewSelectedImage.setImageBitmap(myBitmap);
        } else if (requestCode == SELECT_IMAGE_REQUEST && resultCode == RESULT_OK) {
            mImageViewSelectedImage.setImageURI(data.getData());
            Bitmap myBitmap = BitmapFactory.decodeFile(data.getData().toString());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 0, outputStream);
            photo = outputStream.toByteArray();
        } else {
            displayMessage(getBaseContext(), "Request cancelled or something went wrong.");
        }
    }

    private void getValuesFromFields() {
        name = mEditTextTripName.getText().toString();
        destination = mEditTextDestination.getText().toString();
        if (mRadioButtonMountains.isChecked()) {
            type = TripType.Mountains;
        } else if (mRadioButtonCityBreak.isChecked()) {
            type = TripType.City_Break;
        } else {
            type = TripType.Seaside;
        }
        rating = (long) mRatingBar.getRating();
        price = mSeekBarPrice.getProgress();

    }

    private void setValuesFromBundle() {
        mEditTextTripName.setText(name);
        mEditTextDestination.setText(destination);
        if (type == TripType.City_Break) {
            mRadioButtonCityBreak.setChecked(true);
        } else if (type == TripType.Mountains) {
            mRadioButtonMountains.setChecked(true);
        } else {
            mRadioButtonSeaside.setChecked(true);
        }
        mRatingBar.setRating(rating);
        mSeekBarPrice.setProgress((int) price);
    }

    public void btnSaveTripOnClick(View view) {
        if (isValidDestination() && isValidTripName() && isValidTripType()) {
            getValuesFromFields();
            final DatabaseReference userReference = mDatabaseReference.child("USERS")
                    .child(mFirebaseAuth.getCurrentUser().getUid());
            if (updateTrip) {
                final DatabaseReference tripReference = userReference.child("trips").child("trip_" + (position+1));
                tripReference.child("name").setValue(name);
                tripReference.child("destination").setValue(destination);
                tripReference.child("price").setValue(price);
                tripReference.child("rating").setValue(rating);
                tripReference.child("startDate").setValue(startDate);
                tripReference.child("endDate").setValue(endDate);
                tripReference.child("type").setValue(type);

                Intent intent = new Intent(ManageTripActivity.this, TripListFragment.class);
                setResult(Activity.RESULT_OK, intent);
                finish();

            } else {
                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        noTrips = dataSnapshot.child("noTrips").getValue(Long.class);
                        noTrips++;

                        final DatabaseReference tripReference = userReference.child("trips").child("trip_" + noTrips);

                        final StorageReference imgStorageReference = mFirebaseStorage.getReference().child("USERS")
                                .child(mFirebaseAuth.getCurrentUser().getUid()).child("img_" + noTrips);

                        UploadTask uploadTask = imgStorageReference.putBytes(photo);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imgStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        tripReference.child("img").setValue(uri.toString());
                                    }
                                });
                            }
                        });
                        tripReference.child("name").setValue(name);
                        tripReference.child("destination").setValue(destination);
                        tripReference.child("price").setValue(price);
                        tripReference.child("rating").setValue(rating);
                        tripReference.child("startDate").setValue(startDate);
                        tripReference.child("endDate").setValue(endDate);
                        tripReference.child("type").setValue(type);
                        userReference.child("noTrips").setValue(noTrips);

                        Intent intent = new Intent(ManageTripActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    private boolean isValidTripName() {
        String tripName = mEditTextTripName.getText().toString();
        if (tripName == null || tripName.isEmpty()) {
            mEditTextDestination.setError("Please insert a name for your trip");
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidDestination() {
        String destination = mEditTextDestination.getText().toString();
        if (destination == null || destination.isEmpty()) {
            mEditTextDestination.setError("Please insert a destination for your trip");
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidTripType() {
        if (mRadioGroupTripType.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a type", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }


    public void selectStartDate(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), getString(R.string.select_start_date));
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(((DatePickerFragment) newFragment).selectedYear, ((DatePickerFragment) newFragment).selectedMonth,
//                ((DatePickerFragment) newFragment).selectedDay);
//        Log.d("startDate", "selectStartDate: day "+((DatePickerFragment) newFragment).selectedDay);
//
//       // startDate = ((DatePickerFragment)newFragment).getTimeInMillis();
//        startDate = calendar.getTimeInMillis();
////        Log.d("startDate", "selectStartDate: "+startDate);
    }


    public void selectEndDate(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), getString(R.string.select_end_date));
    }


    private void displayMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDataPass(long data) {
        startDate = data;
        Log.d("onData", "onDataPass: "+startDate);
    }
}
