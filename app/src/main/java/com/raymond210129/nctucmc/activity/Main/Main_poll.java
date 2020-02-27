package com.raymond210129.nctucmc.activity.Main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.raymond210129.nctucmc.R;
import com.raymond210129.nctucmc.activity.Main.poll.CreatePoll;
import com.raymond210129.nctucmc.activity.Main.poll.FillPoll;
import com.raymond210129.nctucmc.activity.Main.poll.ResultPoll;
import com.raymond210129.nctucmc.dataStructure.Poll.Event;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class Main_poll extends Fragment {
    private View view;
    private FloatingActionButton buttonDateSelection;
    private FloatingActionButton buttonAttendance;
    private FloatingActionButton buttonCustom;
    private Query query;
    private FirebaseListAdapter adapter;
    private ListView listView;
    private ProgressDialog pDialog;
    private static final String TAG = Main_poll.class.getSimpleName();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.main_poll, container, false);
        buttonCustom = view.findViewById(R.id.poll_custom);
        buttonAttendance = view.findViewById(R.id.poll_attendance);
        buttonDateSelection = view.findViewById(R.id.poll_date);
        listView = view.findViewById(R.id.poll_list);
        pDialog = new ProgressDialog(getContext());

        query = FirebaseDatabase.getInstance().getReference().child("Poll").child("events").orderByChild("createTime");
        FirebaseListOptions options = new FirebaseListOptions.Builder<Event>()
                .setLifecycleOwner(getActivity())
                .setQuery(query, Event.class)
                .setLayout(R.layout.poll_item)
                .build();
        adapter = new FirebaseListAdapter<Event>(options) {
            @NonNull

            @Override
            protected void populateView(View v, final Event model, int position) {
                TextView pollType = v.findViewById(R.id.poll_type);
                TextView pollGroup = v.findViewById(R.id.poll_group);
                TextView pollTitle = v.findViewById(R.id.poll_title);
                TextView pollContent = v.findViewById(R.id.poll_content);
                Button result = v.findViewById(R.id.poll_result);
                Button vote = v.findViewById(R.id.poll_vote);
                TextView pollUser = v.findViewById(R.id.poll_user);
                TextView pollTime = v.findViewById(R.id.poll_time);
                TextView pollDeadLine = v.findViewById(R.id.poll_deadline);

                //pollType.setText(model.getType());
                //pollGroup.setText(model.getGroup());

                GradientDrawable gradientDrawable;
                gradientDrawable = (GradientDrawable) pollGroup.getBackground().mutate();
                gradientDrawable.setColor(Color.BLUE);

                switch (model.getGroup())
                {
                    case "groupHu":
                        pollGroup.setText("擦弦");
                        break;
                    case "groupAll":
                        pollGroup.setText("社團");
                        break;
                    case "groupBass":
                        pollGroup.setText("低音");
                        break;
                    case "groupFlute":
                        pollGroup.setText("吹管");
                        break;
                    case "groupLieu":
                        pollGroup.setText("彈撥");
                        break;
                    case "groupHit":
                        pollGroup.setText("打擊");
                        break;
                }

                gradientDrawable = (GradientDrawable) pollType.getBackground().mutate();
                gradientDrawable.setColor(ContextCompat.getColor(getActivity(), R.color.notificationPrimary));

                switch (model.getType())
                {
                    case "date":
                        pollType.setText("擇日");
                        break;
                    case "attendance":
                        pollType.setText("出席");
                        break;
                    case "others":
                        pollType.setText("其他");
                        break;
                }

                pollTitle.setText(model.getTitle());
                pollContent.setText(model.getContent());
                pollUser.setText(model.getUser());
                //Log.d("ABC", model.getCreateTime() + "");
                pollTime.setText(DateFormat.format("yyyy-MM-dd HH:mm", model.getCreateTime()) + " 發送");
                pollDeadLine.setText("期限：" + DateFormat.format("yyyy-MM-dd HH:mm", model.getDeadline()));

                final String key = this.getRef(position).getKey();


                vote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pDialog.setMessage("查詢中");
                        //showDialog();

                        Intent intent = new Intent(Main_poll.this.getActivity(), FillPoll.class);
                        intent.putExtra("event_key", key);
                        intent.putExtra("form_key", model.getFormKey());
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("content", model.getContent());
                        startActivity(intent);
                    }
                });

                result.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Main_poll.this.getActivity(), ResultPoll.class);
                        intent.putExtra("form_key", model.getFormKey());
                        startActivity(intent);


                    }
                });



            }
        };
        listView.setStackFromBottom(true);
        listView.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
        listView.setAdapter(adapter);


        buttonCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreatePoll.class);
                intent.putExtra("type", "others");
                startActivity(intent);
            }
        });

        buttonAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreatePoll.class);
                intent.putExtra("type", "attendance");
                startActivity(intent);
            }
        });

        buttonDateSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreatePoll.class);
                intent.putExtra("type", "date");
                startActivity(intent);
            }
        });





        return view;
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
