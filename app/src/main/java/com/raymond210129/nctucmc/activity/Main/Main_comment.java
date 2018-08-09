package com.raymond210129.nctucmc.activity.Main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.raymond210129.nctucmc.R;
import com.raymond210129.nctucmc.activity.Main.booking.Booking_Search;
import com.raymond210129.nctucmc.activity.MainActivity;
import com.raymond210129.nctucmc.activity.PasswordSettingActivity;
import com.raymond210129.nctucmc.activity.SubscriptionSettingActivity;
import com.raymond210129.nctucmc.dataStructure.ChatMessage;
import com.raymond210129.nctucmc.helper.SQLiteHandler;

import java.util.HashMap;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class Main_comment extends Fragment
{
    private ImageButton send;
    private EditText messageInput;
    private SQLiteHandler db;
    private DatabaseReference databaseReference;
    public FirebaseListAdapter<ChatMessage> adapter;
    private static final String TAG = Main_comment.class.getSimpleName();
    public Query query;
    private ListView listview;
    View view;
    static String name = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view =  inflater.inflate(R.layout.main_comment, container, false);
        send = view.findViewById(R.id.send_message);
        messageInput = view.findViewById(R.id.message_input);
        db = new SQLiteHandler(getContext());
        if(name.equals(""))
        {
            name = getCurrentUser();
        }




        //ListView listOfMessages = view.findViewById(R.id.messages_view);



        query = FirebaseDatabase.getInstance().getReference().child("Comment");
        FirebaseListOptions options = new FirebaseListOptions.Builder<ChatMessage>().setLifecycleOwner(getActivity())
                .setQuery(query, ChatMessage.class).setLayout(R.layout.others_message).build();
        listview = view.findViewById(R.id.messages_view);
        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                //Log.d(TAG, "test");
                //Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();


                TextView messageText = v.findViewById(R.id.others_message_body);
                TextView messageUser = v.findViewById(R.id.others_message_name);
                TextView messageTime = v.findViewById(R.id.others_message_time);
                View view1 = v.findViewById(R.id.others_message);
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessaageUser());


                Drawable drawable;

                if(name.equals(model.getMessaageUser()))
                {
                    view1.setScaleX(-1.0f);
                    messageText.setScaleX(-1.0f);
                    messageUser.setScaleX(-1.0f);
                    messageTime.setScaleX(-1.0f);
                    messageText.setBackground(getResources().getDrawable(R.drawable.my_message));
                    messageText.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                }
                else
                {
                    view1.setScaleX(1.0f);
                    messageText.setScaleX(1.0f);
                    messageUser.setScaleX(1.0f);
                    messageTime.setScaleX(1.0f);
                    messageText.setBackground(getResources().getDrawable(R.drawable.others_message));
                    messageText.setTextColor(ContextCompat.getColor(getActivity(), R.color.input_register_hint));
                }


                // Format the date before showing it
                messageTime.setText(DateFormat.format("yyyy-MM-dd HH:mm", model.getMessageTime()) + " 發送");
            }
        };


        listview.setAdapter(adapter);






        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!messageInput.getText().toString().trim().equals(""))
                {
                    if(messageInput.getText().toString().trim().equals(":change password"))
                    {
                        messageInput.setText("");
                        Intent intent = new Intent(Main_comment.this.getActivity(), PasswordSettingActivity.class );
                        startActivity(intent);
                    }
                    else if (messageInput.getText().toString().trim().equals(":log out"))
                    {
                        MainActivity activity = (MainActivity) getActivity();
                        activity.logoutUser();
                    }
                    else if (messageInput.getText().toString().trim().equals(":group setting"))
                    {
                        messageInput.setText("");
                        Intent intent = new Intent(Main_comment.this.getActivity(), SubscriptionSettingActivity.class );
                        startActivity(intent);
                    }
                    else
                    {
                        FirebaseDatabase.getInstance().getReference().child("Comment").push()
                                .setValue(new ChatMessage(messageInput.getText().toString().trim(), getCurrentUser()));
                        messageInput.setText("");
                    }

                }
                else
                {
                    Toast.makeText(getActivity(), "請輸入訊息" , Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        listview.setAdapter(adapter);

    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    public void onPause()
    {
        super.onPause();
        messageInput.clearFocus();
    }

    public String getCurrentUser()
    {
        HashMap<String, String> user;
        user = db.getUserDetails();
        String name = user.get("name");
        return name;
    }




}
