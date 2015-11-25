package fi.aalto.moble.instacalendar;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class CreateEvent extends AppCompatActivity {

    private EditText name;
    private EditText place;
    private EditText from;
    private EditText to;
    private String calId;

    private String event_id;

    private boolean isUpdate;
    private InstaCalAPI api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        setTitle("Event");

        calId = getIntent().getStringExtra(ListCalendarsActivity.EXTRA_CAL_ID);
        isUpdate = getIntent().getBooleanExtra(ListEventsActivity.IsUpdate, false);

        name = (EditText) findViewById(R.id.name);
        place = (EditText) findViewById(R.id.place);
        from = (EditText) findViewById(R.id.from);
        to = (EditText) findViewById(R.id.to);
        api = ApiWrapper.getApi();
        Button done = (Button)findViewById(R.id.submit);
        Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteEventAsync().execute((Void)null);
            }
        });

        if (isUpdate) {
            Event e = getIntent().getParcelableExtra(ListEventsActivity.EVENT_EXTRA);
            event_id = e._id;
            name.setText(e.name);
            place.setText(e.place);
            from.setText(Event.ISOToString(e.starts_at));
            to.setText(Event.ISOToString(e.ends_at));
            delete.setVisibility(Button.VISIBLE);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new UpdateEventAsync(new AppEvent(name.getText().toString(),
                            place.getText().toString(), Event.stringToISO(from.getText().toString()),
                            Event.stringToISO(to.getText().toString()))).execute((Void) null);

                }
            });
        } else {
            delete.setVisibility(Button.INVISIBLE);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CreateEventAsync(new AppEvent(name.getText().toString(),
                            place.getText().toString(), Event.stringToISO(from.getText().toString()),
                            Event.stringToISO(to.getText().toString()))).execute((Void) null);

                }
            });
        }





    }

    public class CreateEventAsync extends AsyncTask<Void, Void, Boolean> {

        private final AppEvent event;
        public CreateEventAsync(AppEvent event) {
            this.event = event;
        }

        public Boolean doInBackground(Void...params) {

            try {
               api.createEvent(calId, ApiWrapper.getToken(getApplicationContext()), event
                        ).execute();
            }catch(IOException e) {
                Toast.makeText(getApplicationContext(), "Network Error, please try again!", Toast.LENGTH_LONG).show();
                return false;
            }

            return true;
        }

        protected void onPostExecute(Boolean success) {
            if (success) {
                finish();
            }
        }
    }

    public class UpdateEventAsync extends AsyncTask<Void, Void, Boolean> {

        public final AppEvent event;

        public UpdateEventAsync(AppEvent event) {
            this.event = event;
        }

        public Boolean doInBackground(Void...params) {
            try {
                api.updateEvent(calId, event_id, ApiWrapper.getToken(getApplicationContext()), event).execute();
            } catch(IOException e) {
                Toast.makeText(getApplicationContext(), "Network Error, please try again!", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean success) {
            if (success)
                finish();
        }

    }

    public class DeleteEventAsync extends AsyncTask<Void, Void, Boolean> {
        public Boolean doInBackground(Void...params) {
            try {
                api.deleteEvent(calId, event_id, ApiWrapper.getToken(getApplicationContext())).execute();
            } catch(IOException e) {
                Toast.makeText(getApplicationContext(), "Network Error, please try again!", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }

        public void onPostExecute(Boolean success) {
            if (success)
                finish();
        }
    }

    public static class AppEvent {
        public final String name;
        public final String place;
        public final String starts_at;
        public final String ends_at;

        public AppEvent(String name, String place, String starts_at, String ends_at) {
            this.name = name;
            this.place = place;
            this.ends_at = ends_at;
            this.starts_at = starts_at;

        }
    }
}
