package fi.aalto.moble.instacalendar;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import fi.aalto.moble.instacalendar.CreateEvent.AppEvent;

public class ListPhoneEventsActivity extends AppCompatActivity {

    public static final String[] EVENTS_PROJECTION = new String[]{
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.EVENT_LOCATION,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_TITLE_INDEX = 1;
    private static final int PROJECTION_LOCATION_INDEX = 2;
    private static final int PROJECTION_START_INDEX = 3;
    private static final int PROJECTION_END_INDEX = 4;

    public String calendar_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_phone_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        setTitle("Phone Events");

        calendar_id = getIntent().getStringExtra(ListCalendarsActivity.EXTRA_CAL_ID);

        ContentResolver cr = getContentResolver();

        Cursor cur = cr.query(CalendarContract.Events.CONTENT_URI, EVENTS_PROJECTION, null, null, null);

        ListView listView = (ListView) findViewById(R.id.events_list);

        listView.setAdapter(new CursorAdapter(ListPhoneEventsActivity.this, cur, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.event_list_view, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {

                TextView tt1 = (TextView) view.findViewById(R.id.event_name);
                tt1.setText(cursor.getString(PROJECTION_TITLE_INDEX));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.w("test", "click");
                Cursor cur = (Cursor) parent.getItemAtPosition(position);
                Log.w("test", cur.toString());
                AppEvent e = new AppEvent(cur.getString(PROJECTION_TITLE_INDEX), cur.getString(PROJECTION_LOCATION_INDEX), cur.getLong(PROJECTION_START_INDEX), cur.getLong(PROJECTION_END_INDEX));
                new CreateEventAsync(e).execute();
            }
        });
    }

    public class CreateEventAsync extends AsyncTask<Void, Void, Boolean> {

        private final AppEvent event;
        public CreateEventAsync(AppEvent event) {
            this.event = event;
        }

        public Boolean doInBackground(Void...params) {
            InstaCalAPI api = ApiWrapper.getApi();
            try {
                api.createEvent(calendar_id, ApiWrapper.getToken(getApplicationContext()), event).execute();
            }catch(IOException e) {

                return false;
            }

            return true;
        }

        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(getApplicationContext(), "Event added to your calendar", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Network Error, please try again!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
