package com.raymond210129.nctucmc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.raymond210129.nctucmc.R;
import com.raymond210129.nctucmc.dataStructure.UserSubscription;
import com.raymond210129.nctucmc.helper.SQLiteHandler;

import java.util.HashMap;

public class SubscriptionSettingActivity extends AppCompatActivity {
    Switch switchHu;
    Switch switchBass;
    Switch switchLieu;
    Switch switchFlute;
    Switch switchHit;
    Button submit;

    private SQLiteHandler db;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_setting);
        switchHu = findViewById(R.id.switch_hu);
        switchBass = findViewById(R.id.switch_bass);
        switchFlute = findViewById(R.id.switch_flute);
        switchLieu = findViewById(R.id.switch_lieu);
        switchHit = findViewById(R.id.switch_hit);
        submit = findViewById(R.id.subscription_submit);

        db = new SQLiteHandler(getApplicationContext());
        final HashMap<String, String> user;
        user = db.getUserDetails();
        final String name = user.get("name");
        String transactionKey;

        final Query query = FirebaseDatabase.getInstance().getReference().child("UserSubscription").orderByChild("name").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    String key = snapshot.getKey();
                    UserSubscription userSubscription = snapshot.getValue(UserSubscription.class);
                    if(userSubscription.getGroupBass()) switchBass.setChecked(true);
                    if(userSubscription.getGroupFlute()) switchFlute.setChecked(true);
                    if(userSubscription.getGroupHit()) switchHit.setChecked(true);
                    if(userSubscription.getGroupHu()) switchHu.setChecked(true);
                    if(userSubscription.getGroupLieu()) switchLieu.setChecked(true);
                    //Toast.makeText(getApplicationContext(), key, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            String key;
            @Override
            public void onClick(View view) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            key = snapshot.getKey();
                            FirebaseDatabase.getInstance().getReference().child("UserSubscription").child(key).removeValue();
                        }
                        FirebaseDatabase.getInstance().getReference().child("UserSubscription").push()
                                .setValue(new UserSubscription(name, true, switchBass.isChecked(),
                                        switchFlute.isChecked(), switchHit.isChecked(), switchHu.isChecked(), switchLieu.isChecked()
                                ));
                        finish();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });



    }
}
