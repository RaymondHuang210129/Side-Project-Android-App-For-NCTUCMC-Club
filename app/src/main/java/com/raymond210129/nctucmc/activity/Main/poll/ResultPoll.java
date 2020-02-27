package com.raymond210129.nctucmc.activity.Main.poll;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.raymond210129.nctucmc.R;
import com.raymond210129.nctucmc.activity.Main.Main_poll;
import com.raymond210129.nctucmc.dataStructure.Poll.Form;
import com.raymond210129.nctucmc.dataStructure.Poll.Vote;

import java.util.ArrayList;

public class ResultPoll extends AppCompatActivity {
    BarChart barChart;
    ArrayList<Integer> yValue = new ArrayList<>();

    ArrayList<String> xValues = new ArrayList<>();
    private static final String TAG = ResultPoll.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        barChart = findViewById(R.id.chart);
        Intent intent = getIntent();
        String formKey = intent.getStringExtra("form_key");
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference selectionItems = firebaseDatabase.getReference()
                .child("Poll").child("form").child(formKey).child("choice");
        Log.d(TAG, formKey);

        selectionItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Log.d(TAG, snapshot.getValue(Form.class).getContent());
                    Form form = snapshot.getValue(Form.class);
                    xValues.add(form.getContent());
                    DatabaseReference voteNumber = firebaseDatabase.getReference().child("Poll").child("votes").child(form.getMemberKey()).child("members");
                    voteNumber.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            yValue.add((int)dataSnapshot.getChildrenCount());
                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            {
                                Log.d(TAG + dataSnapshot.getRef().getParent().getKey(), snapshot.getValue(Vote.class).getName());


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



    }

}
