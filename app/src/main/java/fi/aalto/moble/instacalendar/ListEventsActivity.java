package fi.aalto.moble.instacalendar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListEventsActivity extends AppCompatActivity {

    private String calId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        calId = getIntent().getStringExtra(ListCalendarsActivity.EXTRA_CAL_ID);

        Parcelable[] events = getIntent().getParcelableArrayExtra(ListCalendarsActivity.EXTRA_EVENTS_LIST);
        ArrayList<String> eventStrings = new ArrayList<>();
        for (Parcelable event : events) {
            eventStrings.add(((Event)event).toString());
        }

        ListAdapter eventsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, eventStrings.toArray(new String[0]));
        ListView eventsView = (ListView) findViewById(R.id.events_list);
        eventsView.setAdapter(eventsAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListEventsActivity.this, CreateEvent.class);
                intent.putExtra(ListCalendarsActivity.EXTRA_CAL_ID, calId);
                startActivity(intent);
            }
        });
    }

}
