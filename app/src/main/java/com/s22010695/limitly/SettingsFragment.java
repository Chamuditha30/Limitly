package com.s22010695.limitly;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

public class SettingsFragment extends Fragment {

    //declare objects
    ImageView blockTimeIcon, unblockTimeIcon, setSleepTimeIcon, setWakeTimeIcon;
    TextView selectedBlockTime, selectedUnblockTime, selectedSleepTime, selectedWakeTime;

    //declare variables
    private int blockTime, unblockTime, sleepTime, wakeTime;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        //get elements by id
        blockTimeIcon = view.findViewById(R.id.blockTimeIcon);
        unblockTimeIcon = view.findViewById(R.id.unblockTimeIcon);
        selectedBlockTime = view.findViewById(R.id.selectedBlockTime);
        selectedUnblockTime = view.findViewById(R.id.selectedUnblockTime);
        setSleepTimeIcon = view.findViewById(R.id.setSleepTimeIcon);
        setWakeTimeIcon = view.findViewById(R.id.setWakeTimeIcon);
        selectedSleepTime = view.findViewById(R.id.selectedSleepTime);
        selectedWakeTime = view.findViewById(R.id.selectedWakeTime);

        //set block time entered by user
        blockTimeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBlockTime();
            }
        });

        //set unblock time entered by user
        unblockTimeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUnblockTime();
            }
        });

        //set sleep time entered by user
        setSleepTimeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSleepTime();
            }
        });

        //set wake time entered by user
        setWakeTimeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWakeTime();
            }
        });


        return view;
    }

    //create dialog box to get block time from user
    private void setBlockTime(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Set block time (Min)");

        final NumberPicker numberPicker = new NumberPicker(getContext());
        numberPicker.setMinValue(5);
        numberPicker.setMaxValue(15);
        numberPicker.setValue(15);

        builder.setView(numberPicker);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                blockTime = numberPicker.getValue();
                selectedBlockTime.setText(blockTime + " min");
                Toast.makeText(getContext(),blockTime + " min block time saved", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#6A6A6A"));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#6A6A6A"));
    }

    //create dialog box to get unblock time from user
    private void setUnblockTime(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Set unblock time (Min)");

        final NumberPicker numberPicker = new NumberPicker(getContext());
        numberPicker.setMinValue(30);
        numberPicker.setMaxValue(60);
        numberPicker.setValue(30);

        builder.setView(numberPicker);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                unblockTime = numberPicker.getValue();
                selectedUnblockTime.setText(unblockTime + " min");
                Toast.makeText(getContext(),unblockTime + " min unblock time saved", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#6A6A6A"));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#6A6A6A"));
    }

    //create dialog box get sleep time from user
    private void setSleepTime(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Set sleep time");

        final TimePicker timePicker = new TimePicker(getContext());
        timePicker.setIs24HourView(false);

        builder.setView(timePicker);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                String amPm = (hour >= 12) ? " PM" : " AM";
                int hour12 = (hour == 0 || hour == 12) ? 12 : hour % 12;
                String formatedTime = hour12 + " : " + minute + amPm;
                sleepTime = hour + minute;
                selectedSleepTime.setText(formatedTime);
                Toast.makeText(getContext(),formatedTime + " sleep time saved", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //create dialog box get wake time from user
    private void setWakeTime(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Set wake time");

        final TimePicker timePicker = new TimePicker(getContext());
        timePicker.setIs24HourView(false);

        builder.setView(timePicker);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                String amPm = (hour >= 12) ? " PM" : " AM";
                int hour12 = (hour == 0 || hour == 12) ? 12 : hour % 12;
                String formatedTime = hour12 + " : " + minute + amPm;
                wakeTime = hour + minute;
                selectedWakeTime.setText(formatedTime);
                Toast.makeText(getContext(),formatedTime + " sleep time saved", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}