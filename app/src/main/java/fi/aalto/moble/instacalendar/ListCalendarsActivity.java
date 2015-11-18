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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.Response;

public class ListCalendarsActivity extends AppCompatActivity {

    public static String EXTRA_EVENTS_LIST = "instacalendar.EVENTS";
    public static String EXTRA_CAL_ID = "instacalendar.CAL_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_calendars);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Parcelable[] calendars = getIntent().getParcelableArrayExtra(LoginActivity.EXTRA_CALENDARS_LIST);
        ArrayList<String> calendarNames = new ArrayList<>();
        for (Parcelable calendar : calendars) {
            calendarNames.add(((Calendar)calendar).name);
        }

        ListAdapter calendarsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, calendarNames.toArray(new String[0]));
        ListView calendarsView = (ListView) findViewById(R.id.calendars_list);
        calendarsView.setAdapter(calendarsAdapter);
        calendarsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Calendar c = (Calendar) calendars[position];
                new GetEventsTask(c).execute((Void)null);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public class GetEventsTask extends AsyncTask<Void, Void, Boolean> {
        private final Calendar calendar;


        GetEventsTask(Calendar calendar) {
            this.calendar = calendar;
        }

        public Boolean doInBackground(Void...params) {
            // should be moved to wrapper class
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.135:3001/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            InstaCalAPI api = retrofit.create(InstaCalAPI.class);
            try {
                Response<List<Event>> events =
                        api.events(calendar._id, LoginActivity.getLoginToken()
                                ).execute();
                if (events.isSuccess()) {
                    Intent intent = new Intent(ListCalendarsActivity.this, ListEventsActivity.class);
                    ArrayList<Parcelable> eventsList = new ArrayList<>();
                    eventsList.addAll(events.body());
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArray(EXTRA_EVENTS_LIST, eventsList.toArray(new Parcelable[0]));
                    intent.putExtra(EXTRA_CAL_ID, calendar._id);
                    intent.putExtras(bundle);
                    startActivity(intent);

                    Log.w("Events Task: " + events.body().get(0).toString(), "Info");
                }
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Network Error, please try again!", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }

    }

}
