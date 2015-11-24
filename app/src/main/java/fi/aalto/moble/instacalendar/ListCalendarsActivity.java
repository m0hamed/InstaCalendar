package fi.aalto.moble.instacalendar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.List;

import retrofit.Response;

public class ListCalendarsActivity extends AppCompatActivity {

    public static String EXTRA_CAL_ID = "instacalendar.CAL_ID";

    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_calendars);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Calendars");

        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        listView = (ListView)findViewById(R.id.calendars_list);
        listView.setEmptyView(progressBar);

        new CalendarListTask().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Calendar c = (Calendar)parent.getItemAtPosition(position);
                Intent intent = new Intent(getBaseContext(), ListEventsActivity.class);
                intent.putExtra(EXTRA_CAL_ID, c._id);
                startActivity(intent);
                Log.w("Click Listner", c._id);
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

    public class CalendarListTask extends AsyncTask<Void, Void, List<Calendar>> {


        @Override
        protected List<Calendar> doInBackground(Void... params) {

            InstaCalAPI api = ApiWrapper.getApi();
            Log.w("CalendarListTask", "test");
            try {
                Response<List<Calendar>> calendars = api.calendars(ApiWrapper.getToken(getApplicationContext())).execute();
                if(calendars.isSuccess()) {
                    return calendars.body();
                } else {
                    Log.w("CalendarListTask",calendars.raw().toString());
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final List<Calendar> calendars) {

            if (calendars != null) {
                Log.w("CalenderListTask", calendars.toString());
                listView.setAdapter(new ArrayAdapter<Calendar>(getApplicationContext(), R.layout.list_view, calendars) {
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = convertView;

                        if (v == null) {
                            LayoutInflater vi;
                            vi = LayoutInflater.from(getContext());
                            v = vi.inflate(R.layout.list_view, null);
                        }
                        Calendar p = getItem(position);
                        if(p != null) {
                            TextView tt1 = (TextView) v.findViewById(R.id.calendar_name);
                            tt1.setText(p.name);
                        }
                        return v;
                    }
                });
            }
        }

        @Override
        protected void onCancelled() {
        }
    }

}
