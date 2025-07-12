package com.s22010695.limitly.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.s22010695.limitly.db_helpers.FocusModeTableHandler;
import com.s22010695.limitly.R;
import com.s22010695.limitly.db_helpers.SleepModeTableHandler;
import com.s22010695.limitly.db_helpers.StudyModeTableHandler;
import com.s22010695.limitly.db_helpers.TimerModeTableHandler;
import com.s22010695.limitly.mode_helpers.ActivatedModeViewModel;
import com.s22010695.limitly.mode_helpers.MainFunction;

public class HomeFragment extends Fragment {
    //declare objects
    private SwitchMaterial timerSwitch, focusSwitch, studySwitch, sleepSwitch;
    private TextView activatedMode, activatedModeInfo, blockCount, motivateTxt;

    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    private TimerModeTableHandler timerModeHelper;
    private FocusModeTableHandler focusModeHelper;
    private StudyModeTableHandler studyModeHelper;
    private SleepModeTableHandler sleepModeHelper;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //get elements using id
        timerSwitch = view.findViewById(R.id.timerSwitch);
        focusSwitch = view.findViewById(R.id.focusSwitch);
        studySwitch = view.findViewById(R.id.studySwitch);
        sleepSwitch = view.findViewById(R.id.sleepSwitch);

        //display block count
        blockCount = view.findViewById(R.id.blockCount);
        motivateTxt = view.findViewById(R.id.motivateTxt);

        //display the activated mode according current mode
        activatedMode = view.findViewById(R.id.activatedMode);
        activatedModeInfo = view.findViewById(R.id.activatedModeInfo);

        //connect db
        timerModeHelper = new TimerModeTableHandler(requireContext());
        focusModeHelper = new FocusModeTableHandler(requireContext());
        studyModeHelper = new StudyModeTableHandler(requireContext());
        sleepModeHelper = new SleepModeTableHandler(requireContext());

        //get each mode state from db
        timerSwitch.setChecked(timerModeHelper.getIsEnable());
        focusSwitch.setChecked(focusModeHelper.getIsEnable());
        studySwitch.setChecked(studyModeHelper.getIsEnable());
        sleepSwitch.setChecked(sleepModeHelper.getIsEnable());

        //get each mode states from user
        timerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            timerModeHelper.updateIsEnable(isChecked);
        });

        focusSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            focusModeHelper.updateIsEnable(isChecked);
        });

        studySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            studyModeHelper.updateIsEnable(isChecked);
        });

        sleepSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sleepModeHelper.updateIsEnable(isChecked);
        });

        return view;
    }

    //listen to SharedPreferences changes when fragment is visible
    @Override
    public void onResume() {
        super.onResume();

        prefs = requireContext().getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE);

        //set current mode and block count
        updateActivatedMode(prefs.getString("activatedMode", "none"));
        updateBlockCount(prefs.getInt("blockCount", 0));

        //register listener
        listener = (sharedPreferences, key) -> {
            if ("activatedMode".equals(key)) {
                updateActivatedMode(sharedPreferences.getString("activatedMode", "none"));
            } else if ("blockCount".equals(key)) {
                int count = sharedPreferences.getInt("blockCount", 0);
                updateBlockCount(count);
                Log.d("HomeFragment", "blockCount updated: " + count);
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(listener);
    }

    //clean up listener to avoid memory leaks
    @Override
    public void onPause() {
        super.onPause();
        if (prefs != null && listener != null) {
            prefs.unregisterOnSharedPreferenceChangeListener(listener);
        }
    }

    //create method for update current mode
    @SuppressLint("SetTextI18n")
    private void updateActivatedMode(String mode) {
        activatedMode.setText(mode);
        switch (mode) {
            case "focus":
                activatedMode.setText("FocusMode Activated");
                activatedModeInfo.setText("All selected apps are blocked until leaving the focus zone");
                break;
            case "study":
                activatedMode.setText("StudyMode Activated");
                activatedModeInfo.setText("All selected apps are blocked until device face up");
                break;
            case "sleep":
                activatedMode.setText("SleepMode Activated");
                activatedModeInfo.setText("All selected apps are blocked during sleep time");
                break;
            case "timer":
                activatedMode.setText("TimerMode Activated");
                activatedModeInfo.setText("All selected apps are blocked until blocked time end");
                break;
            default:
                activatedMode.setText("No Mode has been activated");
                activatedModeInfo.setText("Enable modes and reduce your screen time");
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateBlockCount(int count){
        blockCount.setText(String.valueOf(count));
        if (count < 3){
            motivateTxt.setText("Excellent control today! Keep up and focus.");
        } else if (count <= 6) {
            motivateTxt.setText("You are doing good. A few distractions, stay focused.");
        } else {
            motivateTxt.setText("So many distractions. Stay strong and refocus!");
        }
    }
}