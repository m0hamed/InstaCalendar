package fi.aalto.moble.instacalendar;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;


import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.CalendarDayEvent;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit.Response;

public class EventList extends AppCompatActivity {
    CalendarView calendar;

    String calendar_id;

    private java.util.Calendar currentCalender = java.util.Calendar.getInstance(Locale.getDefault());
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    private Map<Date, List<Event>> bookings = new HashMap<>();

    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Bundle extras = getIntent().getExtras();
        calendar_id = extras.getString("CALENDAR_ID");
        new EventListTask().execute(calendar_id);
        //initializes the calendarview
        initializeCalendar();
    }

    public void initializeCalendar() {
        final CompactCalendarView compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.drawSmallIndicatorForEvents(true);

        // below allows you to configure color for the current day in the month
        //compactCalendarView.setCurrentDayBackgroundColor(getResources().getColor(R.color.black));
        // below allows you to configure colors for the current day the user has selected
        //compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.dark_red));

        addEvents(compactCalendarView, -1);
        addEvents(compactCalendarView, java.util.Calendar.DECEMBER);
        addEvents(compactCalendarView, java.util.Calendar.AUGUST);
        compactCalendarView.invalidate();

    }

    private void addEvents(CompactCalendarView compactCalendarView, int month) {
        currentCalender.setTime(new Date());
        currentCalender.set(java.util.Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = currentCalender.getTime();
        for(int i = 0; i < 6; i++){
            currentCalender.setTime(firstDayOfMonth);
            if(month > -1){
                currentCalender.set(java.util.Calendar.MONTH, month);
            }
            currentCalender.add(java.util.Calendar.DATE, i);
            setToMidnight(currentCalender);
            compactCalendarView.addEvent(new CalendarDayEvent(currentCalender.getTimeInMillis(),  Color.argb(255, 169, 68, 65)), false);
            bookings.put(currentCalender.getTime(), eventList);
        }
    }

    private void setToMidnight(java.util.Calendar calendar) {
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
    }

    public class EventListTask extends AsyncTask<String, Void, List<Event>> {


        @Override
        protected List<Event> doInBackground(String... params) {

            InstaCalAPI api = ApiWrapper.getApi();
            Log.w("CalendarListTask", "test");
            try {
                Response<List<Event>> events = api.events(params[0], ApiWrapper.getToken(getApplicationContext())).execute();
                if(events.isSuccess()) {
                    return events.body();
                } else {
                    Log.w("CalendarListTask", events.raw().toString());
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final List<Event> events) {

            if (events != null) {
                Log.w("EventListTask", events.toString());
                eventList = events;
            } else {
            }
        }

        @Override
        protected void onCancelled() {
        }
    }
}

