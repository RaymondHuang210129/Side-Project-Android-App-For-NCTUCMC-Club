package com.raymond210129.nctucmc.activity.Main;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;

import com.raymond210129.nctucmc.R;
import com.raymond210129.nctucmc.activity.Main.Booking.Booking_Search;

import java.time.format.DateTimeFormatter;


public class Main_booking extends Fragment
{
    private RecyclerView recyclerView;
    private ProgressDialog pDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.main_booking, container, false);
        Button confirmDate = view.findViewById(R.id.confirm_date);
        final DatePicker datePicker = view.findViewById(R.id.booking_calender);
        pDialog = new ProgressDialog(getContext());
        hideDialog();



        //CalendarView calendarView = view.findViewById(R.id.booking_calender);

        confirmDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String year = Integer.toString(datePicker.getYear());
                String month = Integer.toString((datePicker.getMonth() + 1));
                String day = Integer.toString(datePicker.getDayOfMonth());
                pDialog.setMessage("查詢中");
                showDialog();
                //Log.e("month", month);
                if(Integer.valueOf(month) < 10)
                {
                    month = "0" + month;
                }
                if(Integer.valueOf(day) < 10)
                {
                    day  = "0" + day ;
                }
                String searchText = (year + "-" + month + "-" + day);
                Intent intent = new Intent(Main_booking.this.getActivity(), Booking_Search.class );
                //intent.putExtra("SELECTED_YEAR", year);
                //intent.putExtra("SELECTED_MONTH", month);
                //intent.putExtra("SELECTED_DAY", day);
                intent.putExtra("SELECTED_DATE", searchText);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        hideDialog();

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}