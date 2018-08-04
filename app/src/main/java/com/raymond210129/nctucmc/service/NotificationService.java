package com.raymond210129.nctucmc.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;

import org.json.JSONObject;

public class NotificationService extends FirebaseMessagingService {
    public static final String TAG = NotificationService.class.getSimpleName();


    @Override
    public void onNewToken(String token)
    {
        super.onNewToken(token);
        Log.d("NEW_TOKEN", token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Topic: " + remoteMessage.getNotification().getTitle());
        Log.d(TAG, "Notification Body:" + remoteMessage.getNotification().getBody());

        if(remoteMessage.getFrom().compareTo("/topics/Comment") == 0)
        {
            Intent intent = new Intent("com.raymond210129.nctucmc_COMMENT_MESSAGE");
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            intent.putExtra("Comment", 1);
            localBroadcastManager.sendBroadcast(intent);
        }



        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


}
