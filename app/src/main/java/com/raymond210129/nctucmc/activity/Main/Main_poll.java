package com.raymond210129.nctucmc.activity.Main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.raymond210129.nctucmc.R;

public class Main_poll extends Fragment {
    View view;
    FloatingActionButton buttonDateSelection;
    FloatingActionButton burronAttendance;
    FloatingActionButton CustomSelection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.main_poll, container, false);





        return view;
    }

}
