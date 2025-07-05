package com.s22010695.limitly.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.s22010695.limitly.R;
import com.s22010695.limitly.db_helpers.SleepModeTableHandler;
import com.s22010695.limitly.db_helpers.TimerModeTableHandler;
import com.s22010695.limitly.activities.AddZoneActivity;
import com.s22010695.limitly.activities.LoginActivity;
import com.s22010695.limitly.activities.SelectedZonesActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SettingsFragment extends Fragment {

    //declare objects
    private ImageView blockTimeIcon, unblockTimeIcon, setSleepTimeIcon, setWakeTimeIcon, addZoneIcon;
    private TextView selectedBlockTime, selectedUnblockTime, selectedSleepTime, selectedWakeTime, navToZones;
    private Button logoutBtn;
    private TimerModeTableHandler timerModeHelper;
    private SleepModeTableHandler sleepModeHelper;

    //declare variables
    private int blockTime, unblockTime, sleepTime, wakeTime;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
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

        //connect timer mode table
        timerModeHelper = new TimerModeTableHandler(requireContext());

        //get block time from db
        int blockedTime = timerModeHelper.getBlockTime();
        selectedBlockTime.setText(blockedTime + " min");

        //get unblock time from db
        int unblockedTime = timerModeHelper.getUnblockTime();
        selectedUnblockTime.setText(unblockedTime + " min");

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

        //connect timer mode table
        sleepModeHelper = new SleepModeTableHandler(requireContext());

        //get sleep time from db
        int sleepMin = sleepModeHelper.getSleepTime();
        String crntSleepTime = formatTime(sleepMin);
        selectedSleepTime.setText(crntSleepTime);

        //get wake time from db
        int wakeMin = sleepModeHelper.getWakeTime();
        String crntWakeTime = formatTime(wakeMin);
        selectedWakeTime.setText(crntWakeTime);

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

        //navigate to add zone activity using intent
        addZoneIcon = view.findViewById(R.id.addZoneIcon);
        addZoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddZoneActivity.class);
                startActivity(intent);
            }
        });

        //navigate to selected zones activity using intent
        navToZones = view.findViewById(R.id.navToZones);
        navToZones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SelectedZonesActivity.class);
                startActivity(intent);
            }
        });

        //logout method
        logoutBtn = view.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
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

                //save block time
                timerModeHelper.updateBlockTime(blockTime);
                selectedBlockTime.setText(blockTime + " min");
                Toast.makeText(getContext(), "Min block time saved", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", null);

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

                //save unblock time
                timerModeHelper.updateUnblockTime(unblockTime);
                selectedUnblockTime.setText(unblockTime + " min");
                Toast.makeText(getContext(),"Min unblock time saved", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", null);

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

                sleepTime = hour * 60 + minute;

                //save sleep time
                sleepModeHelper.updateSleepTime(sleepTime);
                selectedSleepTime.setText(formatTime(sleepTime));
                Toast.makeText(getContext(),"Sleep time saved", Toast.LENGTH_SHORT).show();
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

                wakeTime = hour * 60 + minute;

                //save wake time
                sleepModeHelper.updateWakeTime(wakeTime);
                selectedWakeTime.setText(formatTime(wakeTime));
                Toast.makeText(getContext(),"Sleep time saved", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //create method for format time from minutes
    public String formatTime(int minutes){
        int hour = minutes / 60;
        int min = minutes % 60;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

}