package com.elenaneacsu.tripjournal.trips.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    public int year, month, day;
    OnDataPass mDataPass;
    private String flag;
//
//    public int getSelectedYear() {
//        Log.d("onDateSet", "getSelectedYear: "+selectedYear);
//        return selectedYear;
//    }
//
//    public int getSelectedMonth() {
//        return selectedMonth;
//    }
//
//    public int getSelectedDay() {
//        return selectedDay;
//    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            flag = bundle.getString("TYPE_OF_DATE");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {
//                    year = view.getYear();
//                    Log.d("onDateSet", "onDateSet: year " + view.getYear());
//                    //todo eroare
//                    month = view.getMonth();
//                    day = view.getDayOfMonth();
                    Toast.makeText(getActivity(), "The selected date is " + view.getYear() +
                            " / " + (view.getMonth() + 1) +
                            " / " + view.getDayOfMonth(), Toast.LENGTH_SHORT).show();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(view.getYear(), view.getMonth(), view.getDayOfMonth());
                    mDataPass.onDataPass(calendar.getTimeInMillis(), flag);
                }
            };

    public interface OnDataPass {
        void onDataPass(long data, String flag);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDataPass = (OnDataPass) context;
    }
}
