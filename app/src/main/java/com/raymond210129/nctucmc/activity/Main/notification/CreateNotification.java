package com.raymond210129.nctucmc.activity.Main.notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.raymond210129.nctucmc.R;
import com.raymond210129.nctucmc.dataStructure.Notification;
import com.raymond210129.nctucmc.helper.SQLiteHandler;

import java.util.HashMap;

public class CreateNotification extends AppCompatActivity{
    RadioGroup radioGroup;
    EditText title;
    EditText content;
    Button summit;
    private SQLiteHandler db;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_notification);
        radioGroup = findViewById(R.id.notification_group);
        title = findViewById(R.id.notification_new_title);
        content = findViewById(R.id.notification_new_content);
        summit = findViewById(R.id.notification_summit);
        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user;
        user = db.getUserDetails();
        final String name = user.get("name");

        summit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.getText().toString().trim().equals("") || content.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "請輸入文字", Toast.LENGTH_LONG).show();
                }
                int checked = radioGroup.getCheckedRadioButtonId();
                String topic;
                switch (checked)
                {
                    case R.id.notification_group_all:
                        topic = "groupAll";
                        break;
                    case R.id.notification_group_hu:
                        topic = "groupHu";
                        break;
                    case R.id.notification_group_bass:
                        topic = "groupBass";
                        break;
                    case R.id.notification_group_flute:
                        topic = "groupFlute";
                        break;
                    case R.id.notification_group_lieu:
                        topic = "groupLieu";
                        break;
                    case R.id.notification_group_hit:
                        topic = "groupHit";
                        break;
                    default:
                        topic = "groupNone";
                        break;
                }
                Intent intent = getIntent();

                FirebaseDatabase.getInstance().getReference().child("Notifications").push()
                        .setValue(new Notification(title.getText().toString(), content.getText().toString(),
                                topic, intent.getStringExtra("Rank"), "none", 0, name));
                finish();
            }
        });
    }
}
