package com.raymond210129.nctucmc.activity.Main.poll;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.raymond210129.nctucmc.R;
import com.raymond210129.nctucmc.dataStructure.Poll.Form;
import com.raymond210129.nctucmc.dataStructure.Poll.Vote;
import com.raymond210129.nctucmc.helper.SQLiteHandler;

import java.util.HashMap;

public class FillPoll extends AppCompatActivity {
    ListView choiceList;
    TextView pollTitle;
    TextView pollContent;
    Button submit;

    private SQLiteHandler db;
    private FirebaseListAdapter adapter;
    private static final String TAG = FillPoll.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        ActionBar actionBar = getSupportActionBar();
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.pollPrimaryDark));
        actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.pollPrimary)));

        setContentView(R.layout.activity_vote);
        choiceList = findViewById(R.id.choice_list);
        pollTitle = findViewById(R.id.poll_title);
        pollContent = findViewById(R.id.poll_content);
        submit = findViewById(R.id.submit);

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user;
        user = db.getUserDetails();
        final String name = user.get("name");
        Intent intent = getIntent();
        String formKey = intent.getStringExtra("form_key");
        pollTitle.setText(intent.getStringExtra("title"));
        pollContent.setText(intent.getStringExtra("content"));

        Query query = FirebaseDatabase.getInstance().getReference().child("Poll").child("form").child(formKey).child("choice").orderByChild("voteMembers");
        FirebaseListOptions options = new FirebaseListOptions.Builder<Form>()
                .setQuery(query, Form.class)
                .setLifecycleOwner(this)
                .setLayout(R.layout.poll_choice_list)
                .build();
        Log.d(TAG, formKey + "測試");
        adapter = new FirebaseListAdapter<Form>(options) {
            @Override
            protected void populateView(View v, final Form model, final int position) {
                final String correctness = model.getContent() + " " + position;
                final TextView choiceContent = v.findViewById(R.id.choice_content);
                final ToggleButton toggleButton = v.findViewById(R.id.select_button);
                choiceContent.setText(model.getContent());
                final String voteKey = model.getMemberKey();

                Query queryCheck = FirebaseDatabase.getInstance().getReference()
                        .child("Poll").child("votes").child(voteKey).child("members").orderByChild("name").equalTo(name);

                queryCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String itemName = new String();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            itemName = choiceContent.getText().toString();
                            if(correctness.equals(itemName + " " + position))
                            {
                                toggleButton.setChecked(true);
                                Log.d(TAG, itemName + " " + position);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                        .child("Poll").child("votes").child(voteKey).child("members");
                toggleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(toggleButton.isChecked())
                        {
                            DatabaseReference databaseAction = databaseReference.push();
                            databaseAction.setValue(new Vote(name));
                        }
                        else
                        {
                            Query query = databaseReference.orderByChild("name").equalTo(name);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                        snapshot.getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
            }
        };
        choiceList.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
