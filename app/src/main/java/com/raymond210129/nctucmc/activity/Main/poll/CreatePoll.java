package com.raymond210129.nctucmc.activity.Main.poll;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.raymond210129.nctucmc.R;
import com.raymond210129.nctucmc.dataStructure.Poll.Event;
import com.raymond210129.nctucmc.dataStructure.Poll.Form;
import com.raymond210129.nctucmc.dataStructure.Poll.Number;
import com.raymond210129.nctucmc.helper.SQLiteHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CreatePoll extends AppCompatActivity{
    EditText pollTitle;
    EditText pollContent;
    EditText addSelection;
    DatePicker datePicker;
    TextView activityTitle;
    ImageButton add;
    RadioGroup radioGroup;
    RecyclerView selectionRecyclerView;
    Button submit;
    ArrayList<String> mdata;
    PollAdapter adapter;
    private SQLiteHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_poll);
        pollTitle = findViewById(R.id.poll_new_title);
        pollContent = findViewById(R.id.poll_new_content);
        addSelection = findViewById(R.id.choice_name);
        add = findViewById(R.id.add_choice);
        selectionRecyclerView = findViewById(R.id.poll_recyclerView);
        submit = findViewById(R.id.poll_submit);
        activityTitle = findViewById(R.id.poll_form_title);
        datePicker = findViewById(R.id.poll_new_deadline);
        radioGroup = findViewById(R.id.poll_group);
        mdata = new ArrayList<String>();

        selectionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectionRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new PollAdapter(mdata, CreatePoll.this);
        selectionRecyclerView.setAdapter(adapter);

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user;
        user = db.getUserDetails();
        final String name = user.get("name");

        Intent intent = getIntent();
        final String type = intent.getStringExtra("type");
        if(type.equals("others"))
        {
            activityTitle.setText("新增調查(其他)");
        }
        else if(type.equals("attendance"))
        {
            activityTitle.setText("新增調查(出席調查)");
        }
        else
        {
            activityTitle.setText("新增調查(日期選擇)");
        }

        Window window = getWindow();
        ActionBar actionBar = getSupportActionBar();
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.pollPrimaryDark));
        actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.pollPrimary)));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addSelection.getText().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "請輸入項目", Toast.LENGTH_SHORT);
                }
                else
                {
                adapter.addItem(addSelection.getText().toString(), adapter.getItemCount());
                addSelection.setText("");
                selectionRecyclerView.getLayoutManager().scrollToPosition(mdata.size() - 1);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pollTitle.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "請輸入標題", Toast.LENGTH_SHORT);
                }
                else if(pollContent.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "請輸入內容", Toast.LENGTH_SHORT);
                }
                else if(mdata.size() == 0)
                {
                    Toast.makeText(getApplicationContext(), "請添加至少一個選項", Toast.LENGTH_SHORT);
                }
                else
                {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),0, 0, 0);
                    long deadline = calendar.getTimeInMillis();

                    Long createTime = new Date().getTime();

                    int checked = radioGroup.getCheckedRadioButtonId();
                    String topic;
                    switch (checked)
                    {
                        case R.id.poll_group_all:
                            topic = "groupAll";
                            break;
                        case R.id.poll_group_hu:
                            topic = "groupHu";
                            break;
                        case R.id.poll_group_bass:
                            topic = "groupBass";
                            break;
                        case R.id.poll_group_flute:
                            topic = "groupFlute";
                            break;
                        case R.id.poll_group_lieu:
                            topic = "groupLieu";
                            break;
                        case R.id.poll_group_hit:
                            topic = "groupHit";
                            break;
                        default:
                            topic = "groupNone";
                            break;
                    }

                   DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Poll");
                   DatabaseReference databaseAction;
                   databaseAction = databaseReference.child("Form").push();
                   databaseAction.setValue(new Number(mdata.size()));
                   String formKey = databaseAction.getKey();
                   databaseAction = databaseReference.child("events").push();
                   databaseAction.setValue(new Event(pollContent.getText().toString(), createTime, formKey, pollTitle.getText().toString(), type, deadline, topic, name));
                    for(int i = 0; i < mdata.size(); i++)
                    {
                        //ArrayList<String> voteKeyRef = new ArrayList<>();
                        databaseAction = databaseReference.child("votes").push();
                        databaseAction.setValue(new Number(0));
                        String voteKey = databaseAction.getKey();
                        databaseAction = databaseReference.child("Form").child(formKey).child("choice").push();
                        databaseAction.setValue(new Form(mdata.get(i), voteKey));
                    }

                    Toast.makeText(getApplicationContext(), "建立成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });





    }
}
