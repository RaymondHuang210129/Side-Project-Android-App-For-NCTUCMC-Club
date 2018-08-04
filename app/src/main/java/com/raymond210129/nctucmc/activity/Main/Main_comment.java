package com.raymond210129.nctucmc.activity.Main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.raymond210129.nctucmc.dataStructure.ChatMessage;
import com.raymond210129.nctucmc.helper.SQLiteHandler;

import java.util.HashMap;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view =  inflater.inflate(R.layout.main_comment, container, false);
        send = view.findViewById(R.id.send_message);
        messageInput = view.findViewById(R.id.message_input);
        db = new SQLiteHandler(getContext());



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

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessaageUser());

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
                    FirebaseDatabase.getInstance().getReference().child("Comment").push()
                            .setValue(new ChatMessage(messageInput.getText().toString().trim(), getCurrentUser()));
                    messageInput.setText("");
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
