package com.github.tibolte.sample;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.format.DateFormat;

import java.util.Calendar;

/**
 * Created by dastko on 8/25/16.
 */
public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    OnTimePickerSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnTimePickerSelectedListener {
        void onTimeSelected(int hour, int minute, int id);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnTimePickerSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(),android.R.style.Theme_DeviceDefault_Light_Dialog, this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        mCallback.onTimeSelected(hourOfDay, minute, view.getId());
    }
}
