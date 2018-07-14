package com.raymond210129.nctucmc.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
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
    private BottomNavigationView bottomNavigationView;

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
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottomview);
        viewPager = findViewById(R.id.viewpager);
        toolbar = findViewById(R.id.toolbar);

        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView drawerUserName = headerView.findViewById(R.id.drawer_username);

        FirebaseApp.initializeApp(this);

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






        viewPager.addOnPageChangeListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch(position)
                {
                    case 0:
                        return main_comment;
                    case 1:
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


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_SHORT).show();
                switch(item.getItemId())
                {
                    case R.id.menu_comment_board:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.menu_booking_system:
                        viewPager.setCurrentItem(1);
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

    }

    @Override
    public void onPageSelected(int position)
    {

        bottomNavigationView.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {

    }
}
