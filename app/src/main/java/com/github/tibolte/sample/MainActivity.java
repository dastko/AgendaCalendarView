package com.github.tibolte.sample;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;

import com.badoo.mobile.util.WeakHandler;
import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements CalendarPickerController,
NavigationView.OnNavigationItemSelectedListener{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.activity_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.agenda_calendar_view)
    AgendaCalendarView mAgendaCalendarView;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
    private int mNavItemId;
    private WeakHandler mHandler = new WeakHandler();
    private ImageView mImageView;
    private static final long DRAWER_CLOSE_DELAY = 250;
    private List<CalendarEvent> eventList;
    // region Lifecycle methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        Intent intent = getIntent();
        int position = intent.getIntExtra("CONFIRMED", -1);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mPlanetTitles = getResources().getStringArray(R.array.agenda_array);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();
//
//        // Set the drawer toggle as the DrawerListener
//
//
//
//        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // minimum and maximum date of our calendar
        // 2 month behind, one year ahead, example: March 2015 <-> May 2015 <-> May 2016
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        if(eventList == null) {
            eventList = new ArrayList<>();
            mockList(eventList);
        }
        if(position != -1) {
            changeIcon(position);
        }

        mAgendaCalendarView.init(eventList, minDate, maxDate, Locale.getDefault(), this);
        mAgendaCalendarView.addEventRenderer(new DrawableEventRenderer());
    }

    private void changeIcon(int position) {
        CalendarEvent event = eventList.get(position);
        event.setImageView(0);
        eventList.set(position, event);

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mDrawerLayout.closeDrawers();

                        return true;
                    }
                });
    }

    // endregion

    // region Interface - CalendarPickerController

    @Override
    public void onDaySelected(DayItem dayItem) {
        Log.d(LOG_TAG, String.format("Selected day: %s", dayItem));
    }

    @Override
    public void onEventSelected(CalendarEvent event) {
        Intent intent = new Intent(this, EventActivity.class);
        event.getSharedImage();
        String [] date = event.getTitle().split("-");
        intent.putExtra("START_TIME", date[0]);
        intent.putExtra("END_TIME", date [1]);
        intent.putExtra("COLOR", event.getColor());
        intent.putExtra("DATE", event.getDayReference());
        intent.putExtra("CARD", event.getId());
//        ActivityOptionsCompat options = ActivityOptionsCompat.
//                makeSceneTransitionAnimation(this, (View)event.getSharedImage(), "profile");

        startActivity(intent);
        Log.d(LOG_TAG, String.format("Selected event: %s", event));
    }

    @Override
    public void onScrollToDate(Calendar calendar) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        }
    }


    // endregion

    // region Private Methods

    private void mockList(List<CalendarEvent> eventList) {
        Calendar startTime1 = Calendar.getInstance();
        Calendar endTime1 = Calendar.getInstance();

        endTime1.add(Calendar.DAY_OF_WEEK, 1);
        BaseCalendarEvent event1 = new BaseCalendarEvent("08:00 - 08.30 AM", "Bezoek Mrs. Louwers", "Stadhouderskade 85, Amsterdam",
                ContextCompat.getColor(this, R.color.orange_dark), startTime1, endTime1, true, 1);
        eventList.add(event1);


        Calendar startTime2 = Calendar.getInstance();
        startTime2.add(Calendar.DAY_OF_WEEK, 1);
        Calendar endTime2 = Calendar.getInstance();
        endTime2.add(Calendar.DAY_OF_WEEK, 2);
        BaseCalendarEvent event2 = new BaseCalendarEvent("09:00 - 10.30 AM", "Medisch onderzoek, Mr. Jensen", "Keizersgracht 5, Zwolle",
                ContextCompat.getColor(this, R.color.blue_dark), startTime1, endTime1, true, 1);
        eventList.add(event2);


        Calendar startTime3 = Calendar.getInstance();
        Calendar endTime3 = Calendar.getInstance();
        startTime3.set(Calendar.HOUR_OF_DAY, 14);
        startTime3.set(Calendar.MINUTE, 0);
        endTime3.set(Calendar.HOUR_OF_DAY, 15);
        endTime3.set(Calendar.MINUTE, 0);
        BaseCalendarEvent event3 = new BaseCalendarEvent("11:00 - 13.30 PM", "Medische testen, Mrs. De Vries", "Mercuriusplein 1 5, Utrecht",
                ContextCompat.getColor(this, R.color.yellow), startTime1, endTime1, true, 1);
        eventList.add(event3);


        Calendar startTime4 = Calendar.getInstance();
        startTime2.add(Calendar.DAY_OF_WEEK, 4);
        Calendar endTime4 = Calendar.getInstance();
        endTime2.add(Calendar.DAY_OF_WEEK, 5);

        BaseCalendarEvent event4 = new BaseCalendarEvent("14:00 - 16.30 PM", "Medisch onderzoek, Mr. Van de Berg", "Zuiderzeelaan 43-51, Zwolle",
                ContextCompat.getColor(this, R.color.grey_dark), startTime4, endTime4, true, 1);
        eventList.add(event4);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        mNavItemId = item.getItemId();

        mDrawerLayout.closeDrawer(GravityCompat.START);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(mNavItemId);
            }
        }, DRAWER_CLOSE_DELAY);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.support.v7.appcompat.R.id.home){
            return mDrawerToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void navigate(final int itemId){

    }

    // endregion
}
