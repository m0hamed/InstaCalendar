package fi.aalto.moble.instacalendar;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import retrofit.Response;
import retrofit.http.Query;

public class FilterActivity extends AppCompatActivity {

    private String calId;

    private EditText day;
    private EditText month;
    private EditText year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Filter");

        calId = getIntent().getStringExtra(ListCalendarsActivity.EXTRA_CAL_ID);

        Button dayView = (Button) findViewById(R.id.day_view);
        Button monthView = (Button) findViewById(R.id.month_view);

        day = (EditText) findViewById(R.id.day);
        month = (EditText) findViewById(R.id.month);
        year = (EditText) findViewById(R.id.year);

        dayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setResult(true);
            }
        });

        monthView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(false);
            }
        });


    }

    private void setResult(boolean getDay) {
        String d = day.getText().toString();
        String m = month.getText().toString();
        String y = year.getText().toString();
        Intent result = new Intent("RESULT_ACTION", Uri.parse("content://result_uri"));
        result.putExtra("DAY", d);
        result.putExtra("MONTH", m);
        result.putExtra("YEAR", y);
        result.putExtra("GETDAY", getDay);
        setResult(RESULT_OK, result);
        finish();
    }

}
