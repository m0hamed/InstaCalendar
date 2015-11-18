package fi.aalto.moble.instacalendar;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

       calId = getIntent().getStringExtra(ListCalendarsActivity.EXTRA_CAL_ID);

        name = (EditText) findViewById(R.id.name);
        place = (EditText) findViewById(R.id.place);
        from = (EditText) findViewById(R.id.from);
        to = (EditText) findViewById(R.id.to);

        Button done = (Button)findViewById(R.id.submit);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
                new CreateEventAsync(new Event(name.getText().toString(),
                        place.getText().toString(), "", "")).execute((Void) null);

            }
        });

    }

    public class CreateEventAsync extends AsyncTask<Void, Void, Boolean> {

        private final Event event;
        public CreateEventAsync(Event event) {
            this.event = event;
        }

        public Boolean doInBackground(Void...params) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.135:3001/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            InstaCalAPI api = retrofit.create(InstaCalAPI.class);
            try {
               api.createEvent(calId, LoginActivity.getLoginToken(), event
                        ).execute();
            }catch(IOException e) {
                Toast.makeText(getApplicationContext(), "Network Error, please try again!", Toast.LENGTH_LONG).show();
                return false;
            }

            return true;
        }
    }
}
