package com.raymond210129.nctucmc.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.raymond210129.nctucmc.R;

import com.raymond210129.nctucmc.activity.Main.Main_booking;
import com.raymond210129.nctucmc.activity.Main.Main_comment;
import com.raymond210129.nctucmc.activity.Main.Main_notification;
import com.raymond210129.nctucmc.activity.Main.Main_poll;
import com.raymond210129.nctucmc.activity.Main.SettingActivity;
import com.raymond210129.nctucmc.helper.SQLiteHandler;
import com.raymond210129.nctucmc.helper.SessionManager;


import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private DrawerLayout mDrawerLayout;
    private AHBottomNavigation bottomNavigationView;
    private TabLayout tabLayout;


    private ViewPager viewPager; //= findViewById(R.id.viewpager);;
    private Toolbar toolbar;// = findViewById(R.id.toolbar);
    private NavigationView navigationView;//
    //private TextView drawerUserName;


    private Main_comment main_comment = new Main_comment();
    private Main_booking main_booking = new Main_booking();
    private Main_notification main_notification = new Main_notification();
    private Main_poll main_poll = new Main_poll();

    private SQLiteHandler db;
    private SessionManager session;

    private RecyclerView recyclerView;

    private ArrayList<String> mData = new ArrayList<>();




    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("com.raymond210129.nctucmc_COMMENT_MESSAGE"));

        GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            setContentView(R.layout.activity_main);
        }
        else
        {
            setContentView(R.layout.activity_main_api23);
        }

        mDrawerLayout = findViewById(R.id.drawer_layout);

        viewPager = findViewById(R.id.viewpager);
        toolbar = findViewById(R.id.toolbar);

        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView drawerUserName = headerView.findViewById(R.id.drawer_username);


        //drawerUserName = findViewById(R.id.drawer_username);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        if(!session.isLoggedIn())
        {
            logoutUser();
        }
        HashMap<String, String> user;
        user = db.getUserDetails();
        String name = user.get("name");

        drawerUserName.setText("哈囉，" +  name);

        FirebaseMessaging.getInstance().subscribeToTopic("Comment");
        FirebaseMessaging.getInstance().subscribeToTopic("groupAll");







        viewPager.addOnPageChangeListener(this);


        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                switch(position)
                {
                    case 0:
                        //changeStatusBarColor(0);
                        return main_comment;
                    case 1:
                        //changeStatusBarColor(1);
                        return main_booking;
                    case 2:
                        return main_notification;
                    case 3:
                        return main_poll;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 4;
            }
        });



        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24px);
        setSupportActionBar(toolbar);
       // ActionBar actionBar = getSupportActionBar();
        //actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);



        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        //Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });



        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch(item.getItemId())
                        {
                            case R.id.drawer_logout:
                                logoutUser();
                                break;

                            case R.id.drawer_account_settings:
                                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                                startActivity(intent);
                                break;

                        }


                        return true;
                    }
                }
        );


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            bottomNavigationView = findViewById(R.id.bottomview);

            bottomNavigationView.setInactiveColor(ContextCompat.getColor(getApplicationContext(), R.color.input_login_hint));
            bottomNavigationView.setAccentColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

            AHBottomNavigationItem itemComment = new AHBottomNavigationItem(R.string.comment_board, R.drawable.icons8_comments_24, R.color.colorPrimary);
            AHBottomNavigationItem itemBooking = new AHBottomNavigationItem(R.string.booking_system, R.drawable.icons8_booking_24, R.color.bookingPrimary);
            AHBottomNavigationItem itemNotification = new AHBottomNavigationItem(R.string.notifications, R.drawable.icons8_notification_24, R.color.notificationPrimary);
            AHBottomNavigationItem itemPoll = new AHBottomNavigationItem(R.string.poll, R.drawable.icons8_survey_24, R.color.pollPrimary);

            bottomNavigationView.addItem(itemComment);
            bottomNavigationView.addItem(itemBooking);
            bottomNavigationView.addItem(itemNotification);
            bottomNavigationView.addItem(itemPoll);

            //bottomNavigationView.setColored(true);
            //bottomNavigationView.setNotification("1", 3);

            bottomNavigationView.setDefaultBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

            bottomNavigationView.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
                @Override
                public boolean onTabSelected(int position, boolean wasSelected) {
                    switch (position)
                    {
                        case 0:
                            viewPager.setCurrentItem(0);
                            changeStatusBarColor(0);
                            Commentcount = 0;
                            updateNotification();
                            break;
                        case 1:
                            viewPager.setCurrentItem(1);
                            changeStatusBarColor(1);
                            break;
                        case 2:
                            viewPager.setCurrentItem(2);
                            changeStatusBarColor(2);
                            break;
                        case 3:
                            viewPager.setCurrentItem(3);
                            changeStatusBarColor(3);
                            break;
                    }
                    return true;
                }
            });
            //bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

            /*
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    //Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_SHORT).show();
                    switch(item.getItemId())
                    {
                        case R.id.menu_comment_board:
                            viewPager.setCurrentItem(0);
                            //changeStatusBarColor(0);
                            break;
                        case R.id.menu_booking_system:
                            viewPager.setCurrentItem(1);
                            //changeStatusBarColor(1);
                            break;
                        case R.id.menu_notifications:
                            viewPager.setCurrentItem(2);
                            break;
                        case R.id.menu_poll:
                            viewPager.setCurrentItem(3);
                            break;
                    }

                    return true;
                }
            });
            */
        }
        else
        {
            tabLayout = findViewById(R.id.tabs);

            tabLayout.addTab(tabLayout.newTab().setText("留言板"));
            tabLayout.addTab(tabLayout.newTab().setText("琴房預約"));
            tabLayout.addTab(tabLayout.newTab().setText("通知"));
            tabLayout.addTab(tabLayout.newTab().setText("調查"));

            tabLayout.setTabTextColors(ContextCompat.getColor(getApplicationContext(), R.color.input_login_hint), ContextCompat.getColor(getApplicationContext(), R.color.white));

            tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                    changeStatusBarColor(tab.getPosition());

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        }


    }

    public void changeStatusBarColor(int page)
    {
        Window window = getWindow();
        ActionBar actionBar = getSupportActionBar();
        View header = navigationView.getHeaderView(0);

        if(page == 0)
        {
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                bottomNavigationView.setDefaultBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

            }
            else
            {
                tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                tabLayout.setBackground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));
            }
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));
            header.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));

        }
        else if(page == 1)
        {
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.commentPrimaryDark));
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                bottomNavigationView.setDefaultBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.bookingPrimary));
            }
            else
            {
                tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getApplicationContext(), R.color.commentPrimaryDark));
                tabLayout.setBackground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.bookingPrimary)));
            }
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.bookingPrimary)));
            header.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.commentPrimaryDark));
        }
        else if(page == 2)
        {
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.noificationPrimaryDark));
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                bottomNavigationView.setDefaultBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.notificationPrimary));
                bottomNavigationView.setDefaultBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.notificationPrimary));
            }
            else
            {
                tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getApplicationContext(), R.color.noificationPrimaryDark));
                tabLayout.setBackground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.notificationPrimary)));
            }
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.notificationPrimary)));
            header.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.noificationPrimaryDark));
        }
        else
        {
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.pollPrimaryDark));
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                bottomNavigationView.setDefaultBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.pollPrimary));
            }
            else
            {
                tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getApplicationContext(), R.color.pollPrimaryDark));
                tabLayout.setBackground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.pollPrimary)));
            }
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.pollPrimary)));
            header.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.pollPrimaryDark));
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        if(!sessionManager.isLoggedIn())
        {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void logoutUser()
    {
        session.setLogin(false);

        db.deleteUsers();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }



    @Override
    public  boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch(menuItem.getItemId())
        {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {



            viewPager.setCurrentItem(item.getOrder());
            return true;

        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
        //changeStatusBarColor(position);
    }

    @Override
    public void onPageSelected(int position)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            //bottomNavigationView.getMenu().getItem(position).setChecked(true);
            bottomNavigationView.setCurrentItem(position);
        }
        else
        {
            tabLayout.getTabAt(position).select();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {

    }


    static int Commentcount = 0;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(viewPager.getCurrentItem() != 0)
            {
                int count = intent.getIntExtra("Comment", 0);
                Commentcount += count;
                updateNotification();
            }

        }
    };

    private void updateNotification()
    {
        if(Commentcount == 0)
        {
            bottomNavigationView.setNotification("", 0);
        }
        else
        {
            bottomNavigationView.setNotification(Integer.toString(Commentcount), 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}
