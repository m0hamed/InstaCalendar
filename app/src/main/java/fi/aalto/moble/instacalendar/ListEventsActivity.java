package fi.aalto.moble.instacalendar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Response;

public class ListEventsActivity extends AppCompatActivity {

    public static final String IsUpdate = "instacalendar.IS_UPDATE";
    public static final String EVENT_EXTRA = "instacalendar.EVENT_EXTRA";

    private String calId;
    public ListView listView;
    public List<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Events");

        calId = getIntent().getStringExtra(ListCalendarsActivity.EXTRA_CAL_ID);


        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        listView = (ListView)findViewById(R.id.events_list);
        listView.setEmptyView(progressBar);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event p = events.get(position);
                Intent intent = new Intent(ListEventsActivity.this, CreateEvent.class);
                intent.putExtra(IsUpdate, true);
                intent.putExtra(ListCalendarsActivity.EXTRA_CAL_ID, calId);
                intent.putExtra(EVENT_EXTRA, p);
                startActivity(intent);
            }
        });

       // new ListEventsTask().execute((Void) null);


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

    @Override
    public void onResume() {
        super.onResume();
        new ListEventsTask().execute((Void) null);
    }

    public class ListEventsTask extends AsyncTask<Void, Void, List<Event>> {


        @Override
        protected List<Event> doInBackground(Void... params) {

            InstaCalAPI api = ApiWrapper.getApi();
            Log.w("EventListTask", "test");
            try {
                Response<List<Event>> events = api.events(calId, ApiWrapper.getToken(getApplicationContext())).execute();
                if(events.isSuccess()) {
                    return events.body();
                } else {
                    Log.w("EventsListTask",events.raw().toString());
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
                Log.w("Events.ListTask", events.toString());
                ListEventsActivity.this.events = events;
                listView.setAdapter(new ArrayAdapter<Event>(getApplicationContext(), R.layout.list_view, events) {
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = convertView;

                        if (v == null) {
                            LayoutInflater vi;
                            vi = LayoutInflater.from(getContext());
                            v = vi.inflate(R.layout.event_list_view, null);
                        }
                        Event p = getItem(position);
                        if(p != null) {
                            TextView tt1 = (TextView) v.findViewById(R.id.event_name);
                            tt1.setText(p.name);
                        }
                        return v;
                    }
                });
            } else {
            }
        }

        @Override
        protected void onCancelled() {
        }
    }

}
