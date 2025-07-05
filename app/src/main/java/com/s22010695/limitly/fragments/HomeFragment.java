package com.s22010695.limitly.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.s22010695.limitly.db_helpers.FocusModeTableHandler;
import com.s22010695.limitly.R;
import com.s22010695.limitly.db_helpers.SleepModeTableHandler;
import com.s22010695.limitly.db_helpers.StudyModeTableHandler;
import com.s22010695.limitly.db_helpers.TimerModeTableHandler;

public class HomeFragment extends Fragment {
    //declare objects
    private SwitchMaterial timerSwitch, focusSwitch, studySwitch, sleepSwitch;
    private TimerModeTableHandler timerModeHelper;
    private FocusModeTableHandler focusModeHelper;
    private StudyModeTableHandler studyModeHelper;
    private SleepModeTableHandler sleepModeHelper;

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
}