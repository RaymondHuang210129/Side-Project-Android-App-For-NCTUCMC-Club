package com.raymond210129.nctucmc.activity.Main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.raymond210129.nctucmc.R;
import com.raymond210129.nctucmc.activity.Main.notification.CreateNotification;
import com.raymond210129.nctucmc.dataStructure.Notification;

public class Main_notification extends Fragment
{


    private FirebaseListAdapter adapter;
    private ListView listView;
    private static final String TAG = Main_notification.class.getSimpleName();
    private Query query;
    private FloatingActionButton importantNotification;
    private FloatingActionButton defaultNotification;
    private FloatingActionMenu floatingActionMenu;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.main_notification, container, false);
        listView = view.findViewById(R.id.notification_view);
        query = FirebaseDatabase.getInstance().getReference().child("Notifications");
        importantNotification = view.findViewById(R.id.important_notification);
        defaultNotification = view.findViewById(R.id.default_notification);
        floatingActionMenu = view.findViewById(R.id.notification_menu);

        FirebaseListOptions options = new FirebaseListOptions.Builder<Notification>()
                .setLifecycleOwner(getActivity())
                .setQuery(query, Notification.class)
                .setLayout(R.layout.notification_item)
                .build();
        adapter = new FirebaseListAdapter<Notification>(options){

            @Override
            protected void populateView(View v, Notification model, int position) {
                TextView notificationTitle = v.findViewById(R.id.notification_title);
                TextView notificationContent = v.findViewById(R.id.notification_content);
                TextView notificationRank = v.findViewById(R.id.notification_rank);
                TextView notificationUser = v.findViewById(R.id.notification_user);
                TextView notificationTime = v.findViewById(R.id.notification_time);

                notificationTitle.setText(model.getTitle());
                notificationContent.setText(model.getContent());
                GradientDrawable gradientDrawable;

                if(model.getRank().equals("high"))
                {
                    notificationRank.setText("重要");
                    gradientDrawable = (GradientDrawable) notificationRank.getBackground().mutate();
                    gradientDrawable.setColor(Color.RED);
                }
                else if(model.getRank().equals("default"))
                {
                    notificationRank.setText("一般");
                    gradientDrawable = (GradientDrawable) notificationRank.getBackground().mutate();
                    gradientDrawable.setColor(Color.BLUE);
                }
                //notificationRank.setText(model.getRank());
                notificationUser.setText(model.getUser());
                notificationTime.setText(DateFormat.format("yyyy-MM-dd HH:mm", model.getTime()) + " 發送");
                //notificationTime.setText(String.valueOf(model.getTime()));
            }
        };
        listView.setAdapter(adapter);

        importantNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_notification.this.getActivity(), CreateNotification.class);
                intent.putExtra("Rank", "high");
                floatingActionMenu.close(true);

                startActivity(intent);
            }
        });

        defaultNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_notification.this.getActivity(), CreateNotification.class);
                intent.putExtra("Rank", "default");
                floatingActionMenu.close(true);
                startActivity(intent);
            }
        });




        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        listView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder
    {
        View view;

        public NotificationHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setTitle(String title)
        {
            TextView post_title = view.findViewById(R.id.notification_title);
            post_title.setText(title);
        }

        public void setContent(String content)
        {
            TextView post_content = view.findViewById(R.id.notification_content);
            post_content.setText(content);
        }

        public void setTime(String time)
        {
            TextView  post_time = view.findViewById(R.id.notification_time);
            post_time.setText(time);
        }

        public void setUser(String user)
        {
            TextView post_user = view.findViewById(R.id.notification_user);
            post_user.setText(user);
        }

        public void setRank(String rank)
        {
            TextView post_rank = view.findViewById(R.id.notification_rank);
            post_rank.setText(rank);
        }

    }



}
